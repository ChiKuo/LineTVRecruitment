package tw.chikuo.linetvrecruitment.controller

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import tw.chikuo.linetvrecruitment.model.Client
import tw.chikuo.linetvrecruitment.R
import tw.chikuo.linetvrecruitment.model.Drama
import tw.chikuo.linetvrecruitment.model.DramaModel

class MainActivity : AppCompatActivity() {

    private var dramaAdapter : DramaAdapter? = null
    var prefs : SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setTitle(R.string.app_main)
        prefs = getSharedPreferences("linetvrecruitment", Context.MODE_PRIVATE)

        // RecyclerView
        dramaAdapter = DramaAdapter(this)
        rv.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this@MainActivity, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (dramaAdapter!!.getItemViewType(position)) {
                    DramaAdapter.TYPE_ITEM -> 1
                    else -> 2
                }
            }
        }
        rv.layoutManager = layoutManager
        rv.adapter = dramaAdapter

        // Search listener
        dramaAdapter?.onKeywordInputListener = object : DramaAdapter.OnKeywordInputListener {
            override fun textChange(string: String) {
                DramaModel(this@MainActivity).getDramaFromLocal(string, object : DramaModel.DramaListCallback {
                    override fun callback(dramaList: MutableList<Drama>) {
                        dramaAdapter?.dramaList = dramaList
                        dramaAdapter?.isEmpty = dramaList.size == 0
                        dramaAdapter?.notifyDataSetChanged()
                    }
                })

                // Save last search
                val editor = prefs!!.edit()
                editor.putString("searchKeyword", string)
                editor.apply()
            }
        }

        // handle last search
        val keyword = prefs!!.getString("searchKeyword", "")
        if (keyword != ""){
            dramaAdapter?.searchKeyword = keyword
            dramaAdapter?.notifyDataSetChanged()
        } else {
            loadData()
        }
    }

    private fun loadData() {
        val client = Client.newInstance(this)
        client.getDrama(object : Client.OnDramaCallback {
            override fun success(dramaList: MutableList<Drama>) {
                dramaAdapter?.dramaList = dramaList
                dramaAdapter?.notifyDataSetChanged()
                DramaModel(this@MainActivity).updateDramaFromRemote(dramaList)
            }

            override fun failed(errorMessage: String?) {
                DramaModel(this@MainActivity).getDramaFromLocal(object : DramaModel.DramaListCallback {
                    override fun callback(dramaList: MutableList<Drama>) {
                        dramaAdapter?.dramaList = dramaList
                        dramaAdapter?.notifyDataSetChanged()
                    }
                })
            }
        })
    }
}
