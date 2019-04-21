package tw.chikuo.linetvrecruitment.model

import android.app.Activity
import android.content.Context
import android.os.AsyncTask

class DramaModel (private val context: Context) {

    private val dramaDao = AppDatabase.getDatabase(context).dramaDao()

    fun updateDramaFromRemote(dramaList: MutableList<Drama>){
        AsyncTask.execute({
            dramaDao.deleteAll()
            dramaDao.insertAll(dramaList)
        })
    }

    interface DramaListCallback{
        fun callback(dramaList : MutableList<Drama>)
    }

    fun getDramaFromLocal(dramaListCallback: DramaListCallback){
        AsyncTask.execute({
            val localDramaList = dramaDao.all
            (context as Activity).runOnUiThread{
                dramaListCallback.callback(localDramaList)
            }
        })
    }

    fun getDramaFromLocal(keyword : String, dramaListCallback: DramaListCallback){
        AsyncTask.execute({
            val queryString = "%$keyword%"
            val localDramaList = dramaDao.getDramaByKeyword(queryString)
            (context as Activity).runOnUiThread{
                dramaListCallback.callback(localDramaList)
            }
        })
    }

    interface DramaCallback{
        fun callback(drama : Drama)
    }

    fun getDrama(id : String, dramaCallback: DramaCallback){
        AsyncTask.execute({
            val drama = dramaDao.findById(id)
            (context as Activity).runOnUiThread{
                dramaCallback.callback(drama)
            }
        })
    }

}