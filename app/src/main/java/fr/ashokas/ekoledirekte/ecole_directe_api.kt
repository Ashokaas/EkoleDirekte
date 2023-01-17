package fr.ashokas.ekoledirekte

import khttp.post

fun login(user: String, password: String): Map<String, String> {
    val version = "4.18.3"
    val header = mapOf(
        "authority" to "api.ecoledirecte.com",
        "accept" to "application/json, text/plain, */*",
        "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36",
        "content-type" to "application/x-www-form-urlencoded",
        "sec-gpc" to "1",
        "origin" to "https://www.ecoledirecte.com",
        "sec-fetch-site" to "same-site",
        "sec-fetch-mode" to "cors",
        "sec-fetch-dest" to "empty",
        "referer" to "https://www.ecoledirecte.com/",
        "accept-language" to "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7"
    )
    val data = "data={\n\t\"uuid\": \"\",\n\t\"identifiant\": \"$user\",\n\t\"motdepasse\": \"$password\"\n}"
    val url = "https://api.ecoledirecte.com/v3/login.awp?v=$version"
    val response = post(url, data = data, headers = header).jsonObject
    val account = mapOf(
        "token" to response["token"],
        "id" to response["data"]["accounts"][0]["id"],
        "email" to response["data"]["accounts"][0]["email"]
    )
    return account
}
