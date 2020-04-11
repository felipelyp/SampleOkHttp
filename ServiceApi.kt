package com.zzflow.statesview

import android.app.Activity
import android.os.StrictMode
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ServiceApi(private val activity: Activity) : Callback {

    private val client = OkHttpClient()
    private var resultado: Resultado? = null

    init {
        val policy = StrictMode.ThreadPolicy.Builder()
        StrictMode.setThreadPolicy(policy.permitAll().build())
    }

    fun onResultado(resultado: Resultado){
        this.resultado = resultado
    }

    fun get(url: String) {
        request(Request.Builder().url(url).build())
    }

    fun post(url: String, body: String) {
        val mediaType = checkMediaType(body)

        val requestBody: RequestBody = body.toRequestBody(mediaType)

        val build = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        request(build)
    }

    private fun request(request: Request) {
        client.newCall(request).enqueue(this)
    }

    private fun checkMediaType(body: String): MediaType {
        return try {
            JSONObject(body)
            "application/json".toMediaType()
        } catch (e: JSONException) {
            "application/x-www-form-urlencoded".toMediaType()
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        prepareResponse(null)
    }

    override fun onResponse(call: Call, response: Response) {
        prepareResponse(response)
    }

    private fun prepareResponse(response: Response?) {
        activity.runOnUiThread {
            try {
                val success = response?.isSuccessful
                val result = response?.body?.string()

                resultado?.onResponse(result, success!!)
            } catch (error: Exception) {
                resultado?.onResponse(null, false)
            }
        }
    }

    interface Resultado {

        fun onResponse(result: String?, success: Boolean)
    }
}