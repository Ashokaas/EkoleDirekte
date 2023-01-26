package fr.ashokas.ekoledirekte

import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject


class AccountData {
    companion object Account {
        fun create(): AccountData = AccountData()
        private suspend fun loginEDaccount(identifiant: String, password: String): String? {
            return withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = "data={\"identifiant\":\"$identifiant\",\"motdepasse\":\"$password\"}"
                val body = RequestBody.create(MediaType.parse("text/plain"), payload)
                val request = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/login.awp")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val response = client.newCall(request).execute()

                return@withContext response.body()?.string()
            }
        }
        private suspend fun getNotes(identifiant: String, password: String) {
        }
        suspend fun getAccountData(identifiant: String, password: String): Map<String, Any> {
            return CoroutineScope(Dispatchers.IO).async {
                val account = loginEDaccount(identifiant, password)
                println(account)
                val accountJson = JSONObject(account)
                if (accountJson.getInt("code") == 200) {
                    val data = accountJson.getJSONObject("data")
                    val accounts = data.getJSONArray("accounts")
                    // val token = data.getString("token")
                    val firstAccount = accounts.getJSONObject(0)
                    val prenom: String = firstAccount.getString("prenom")
                    val nom: String = firstAccount.getString("nom")
                    val email: String = firstAccount.getString("email")
                    return@async mapOf(
                        "data" to data,
                        "accounts" to accounts,
                        "firstAccount" to firstAccount,
                        "prenom" to prenom,
                        "nom" to nom,
                        "email" to email,
                        "message" to "$prenom $nom $email"
                    )
                } else {
                    return@async mapOf(
                        "data" to "",
                        "accounts" to "",
                        "firstAccount" to "",
                        "prenom" to "",
                        "nom" to "",
                        "email" to "",
                        "message" to accountJson.getString("message")
                    )
                }
            }.await()
        }
    }
}