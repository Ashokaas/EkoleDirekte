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
        private suspend fun getNotes(token: String, id) {
            var notes = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = 'data={"token":"$token"}'
                val body = RequestBody.create(MediaType.parse("text/plain"), payload)
                val request = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/eleves/$id/notes.awp?verbe=get&v=4.26.2")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val response = client.newCall(request).execute()

                return@withContext response.body()?.string()
            }
            val notesJson = JSONObject(notes)
            if (notesJson.getInt("code") == 200) {
                notes = notesJson.getJSONArray("notes")
                val trimestres = notesJson.getJSONArray("periodes")
                val premierTrim = notesJson.getJSONObject(0)
                val deuxiemeTrim = notesJson.getJSONObject(1)
                val troisiemeTrim = notesJson.getJSONObject(2)
                val annee = notesJson.getJSONObject(3)
                if (premierTrim.getJSONObject("ensembleMatieres").getJSONArray("disciplines") == deuxiemeTrim.getJSONObject("ensembleMatieres").getJSONArray("disciplines") && 
                deuxiemeTrim.getJSONObject("ensembleMatieres").getJSONArray("disciplines") == troisiemeTrim.getJSONObject("ensembleMatieres").getJSONArray("disciplines")
                ){
                    val matieres: Array? = premierTrim.getJSONObject("ensembleMatieres").getJSONArray("disciplines")
                } else {
                    val matieres: Array? = null
                }
                return mapOf(
                    "notesJson" to notesJson,
                    "notes" to notes,
                    "premierTrim" to premierTrim,
                    "deuxiemeTrim" to deuxiemeTrim,
                    "troisiemeTrim" to troisiemeTrim,
                    "annee" to annee,
                    "matieres" to matieres
                )
            } else {return -1}
        }
        suspend fun getAccountData(identifiant: String, password: String): Map<String, Any> {
            return CoroutineScope(Dispatchers.IO).async {
                val account = loginEDaccount(identifiant, password)
                println(account)
                val accountJson = JSONObject(account)
                if (accountJson.getInt("code") == 200) {
                    val data = accountJson.getJSONObject("data")
                    val accounts = data.getJSONArray("accounts")
                    val token = accountJson.getString("token")
                    val firstAccount = accounts.getJSONObject(0)
                    val prenom: String = firstAccount.getString("prenom")
                    val nom: String = firstAccount.getString("nom")
                    val email: String = firstAccount.getString("email")
                    val id = firstAccount.getInt("id")
                    return@async mapOf(
                        "data" to data,
                        "token" to token,
                        "accounts" to accounts,
                        "firstAccount" to firstAccount,
                        "prenom" to prenom,
                        "nom" to nom,
                        "email" to email,
                        "id" to id,
                        "message" to "$prenom $nom $email"
                    )
                } else {
                    return@async mapOf(
                        "data" to "",
                        "token" to "",
                        "accounts" to "",
                        "firstAccount" to "",
                        "prenom" to "",
                        "nom" to "",
                        "email" to "",
                        "id" to -1,
                        "message" to accountJson.getString("message")
                    )
                }
            }.await()
        }
    }
}