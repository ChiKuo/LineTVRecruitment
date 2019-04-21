package tw.chikuo.linetvrecruitment.model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl


class Client {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var sInstance: Client? = null
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        private var client = OkHttpClient()
        fun newInstance(context: Activity): Client {
            if (sInstance == null) {
                Companion.context = context
                sInstance = Client()
                client = OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build()
            }
            return sInstance as Client
        }
    }

    interface OnClientCallback {
        fun onFailure(errorMessage: String)
        fun onSuccess(jsonObject: JsonObject)
    }

    private fun clientGet(httpUrl: HttpUrl, onClientCallback: OnClientCallback?): Call {
        val request = Request.Builder()
                .url(httpUrl)
                .get()
                .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                (context as Activity).runOnUiThread {
                    onClientCallback?.onFailure("Response Failed")
                }
            }
            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful) {
                    try {
                        val jsonParser = JsonParser()
                        val responseMessage = response.body()!!.string()
                        val jsonObject = jsonParser.parse(responseMessage) as JsonObject
                        (context as Activity).runOnUiThread {
                            onClientCallback?.onSuccess(jsonObject)
                        }
                    } catch (e: Exception) {
                        (context as Activity).runOnUiThread {
                            onClientCallback?.onFailure(e.message!!)
                        }
                    }
                } else {
                    (context as Activity).runOnUiThread {
                        onClientCallback?.onFailure("Response Failed")
                    }
                }
            }
        })
        return call
    }

    interface OnDramaCallback {
        fun success(dramaList: MutableList<Drama>)
        fun failed(errorMessage: String?)
    }

    fun getDrama(OnDramaCallback: OnDramaCallback?) {

        val getUrl = "http://www.mocky.io/v2/5a97c59c30000047005c1ed2"
        val httpUrl = HttpUrl.parse(getUrl)!!.newBuilder().build()
        clientGet(httpUrl, object : OnClientCallback {
            override fun onFailure(errorMessage: String) {
                OnDramaCallback?.failed(errorMessage)
            }

            override fun onSuccess(jsonObject: JsonObject) {
                val gson = Gson()
                val dramaList = gson.fromJson<MutableList<Drama>>(jsonObject.get("data").toString(), object : TypeToken<MutableList<Drama>>() {}.type)
                OnDramaCallback?.success(dramaList)
            }
        })
    }

}