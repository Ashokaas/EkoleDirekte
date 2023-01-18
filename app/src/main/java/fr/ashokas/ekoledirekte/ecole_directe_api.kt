package fr.ashokas.ekoledirekte

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject


public fun login(user: String, password: String): Map<String, String> {
    val version = "4.18.3"
    val client = OkHttpClient()
    val mediaType = MediaType.parse("application/json; charset=utf-8")
    val body = RequestBody.create(mediaType, "data={\"uuid\":\"\",\"identifiant\":\"$user\",\"motdepasse\":\"$password\"}")
    val request = Request.Builder()
        .url("https://api.ecoledirecte.com/v3/login.awp?v=$version")
        .post(body)
        .addHeader("authority", "api.ecoledirecte.com")
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
        .addHeader("content-type", "application/x-www-form-urlencoded")
        .addHeader("sec-gpc", "1")
        .addHeader("origin", "https://www.ecoledirecte.com")
        .addHeader("sec-fetch-site", "same-site")
        .addHeader("sec-fetch-mode", "cors")
        .addHeader("sec-fetch-dest", "empty")
        .addHeader("referer", "https://www.ecoledirecte.com/")
        .addHeader("accept-language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
        .build()
    val response = client.newCall(request).execute()
    val jsonString = response.body()?.string()
    val jsonObject = JSONObject(jsonString)
    val account = mapOf(
        "token" to jsonObject.getString("token"),
        "id" to jsonObject.getJSONObject("data").getJSONArray("accounts").getJSONObject(0).getString("id"),
        "email" to jsonObject.getJSONObject("data").getJSONArray("accounts").getJSONObject(0).getString("email")
    )
    return account
}

