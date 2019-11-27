package com.example.coffee_test_ai

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class HttpUtil {
    //叩きたいREST APIのURLを引数とします
    fun httpGET(url : String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string()
        return body
    }
    fun httpPOST(url : String, coffee_taste : Int, richness : Int, sweetness : Int, acidity : Int, bitterness : Int, fragrance : Int, easy_to_drink : Int): Int? {
        val client_post = OkHttpClient()

        val json = JSONObject()
        json.put("coffee_taste", coffee_taste)
        json.put("richness", richness)
        json.put("sweetness", sweetness)
        json.put("acidity", acidity)
        json.put("bitterness", bitterness)
        json.put("fragrance", fragrance)
        json.put("easy_to_drink", easy_to_drink)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toString().toRequestBody(mediaType)
        val request_post : Request = Request.Builder().url(url).post(requestBody).build()
        val response_post = client_post.newCall(request_post).execute()
        var res_code = response_post.code
        response_post.close()
        return res_code
    }
}