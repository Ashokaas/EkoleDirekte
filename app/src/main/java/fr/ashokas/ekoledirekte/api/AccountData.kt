package fr.ashokas.ekoledirekte.api

import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
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
        suspend fun getAccountData(identifiant: String, password: String): Map<String, Any> {
            return CoroutineScope(Dispatchers.IO).async {
                val account = loginEDaccount(identifiant, password)
                println(account)
                val accountJson = JSONObject(account)
                if (accountJson.getInt("code") == 200) {
                    val account: JSONObject = accountJson.getJSONObject("data").getJSONArray("accounts").getJSONObject(0)
                    val prenom = account.getString("prenom")
                    val nom = account.getString("nom")
                    val email = account.getString("email")
                    val typeCompte = account.getString("typeCompte")
                    return@async mapOf(
                        "data" to accountJson,
                        "code" to 200,
                        "typeCompte" to typeCompte,
                        "message" to "$prenom $nom $email"
                    )
                } else {
                    return@async mapOf(
                        "data" to JSONObject(),
                        "code" to 505,
                        "message" to accountJson.getString("message")
                    )
                }
            }.await()
        }
        suspend fun getNotes(token: String, id: String): Map<String, Any?> {
            val notesStr = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = "data={\"token\":\"$token\"}"
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
            } ?: return mapOf(
                "notesJson" to "",
                "notes" to "",
                "premierTrim" to "",
                "deuxiemeTrim" to "",
                "troisiemeTrim" to "",
                "annee" to "",
                "matieres" to "")

            val notesJson = JSONObject(notesStr)
            if (notesJson.getInt("code") == 200) {
                val data = notesJson.getJSONObject("data")
                val notes = data.getJSONArray("notes")
                val trimestres = data.getJSONArray("periodes")
                val premierTrim = trimestres.getJSONObject(0)
                val deuxiemeTrim = trimestres.getJSONObject(1)
                val troisiemeTrim = trimestres.getJSONObject(2)
                val annee = trimestres.getJSONObject(3)

                val matieres: Array<Array<JSONObject>> =
                    Array(3) {
                        Array(trimestres.getJSONObject(it).getJSONObject("ensembleMatieres").getJSONArray("disciplines").length()) {it2 ->
                            trimestres.getJSONObject(it).getJSONObject("ensembleMatieres")
                            .getJSONArray("disciplines").getJSONObject(it2)}}

                return mapOf(
                    "notesJson" to notesJson,
                    "notes" to notes,
                    "premierTrim" to premierTrim,
                    "deuxiemeTrim" to deuxiemeTrim,
                    "troisiemeTrim" to troisiemeTrim,
                    "annee" to annee,
                    "matieres" to matieres
                )
            } else {
                return mapOf(
                "notesJson" to "",
                "notes" to "",
                "premierTrim" to "",
                "deuxiemeTrim" to "",
                "troisiemeTrim" to "",
                "annee" to "",
                "matieres" to "")}
        }
        suspend fun getMessages(token: String, id: Int): Map<String, Any?> {
            val messages = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = "data={\"token\":\"$token\"}"
                val body = RequestBody.create(MediaType.parse("text/plain"), payload)
                val requestReceived = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/eleves/$id/messages.awp?force=false&typeRecuperation=received&idClasseur=0&orderBy=date&order=desc&query=&onlyRead=&page=0&itemsPerPage=100&getAll=0&verbe=get&v=4.26.2")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val requestSent = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/eleves/$id/messages.awp?force=false&typeRecuperation=sent&idClasseur=0&orderBy=date&order=desc&query=&onlyRead=&page=0&itemsPerPage=100&getAll=0&verbe=get&v=4.26.2")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val responseReceived = client.newCall(requestReceived).execute()
                val responseSent = client.newCall(requestSent).execute()

                return@withContext listOf(responseReceived.body()?.string(), responseSent.body()?.string())
            }
            val dataReceived = JSONObject(messages[0]).getJSONObject("data")
            val dataSent = JSONObject(messages[1]).getJSONObject("data")
            val messagesReceived = dataReceived.getJSONObject("messages").getJSONArray("received")
            val messagesSent = dataSent.getJSONObject("messages").getJSONArray("sent")
            return mapOf(
                "messagesSent" to messagesReceived,
                "messagesReceived" to messagesSent
            )
        }
        suspend fun getMessageContent(token: String, id:Int, idMessage: Int): JSONObject {
            val datas: JSONObject = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = "data={\"token\": $token}"
                val body = RequestBody.create(MediaType.parse("text/plain"), payload)
                val request = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/eleves/$id/messages/$idMessage.awp?verbe=get&mode=destinataire&v=4.27.5")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36"
                    )
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val response = client.newCall(request).execute()

                return@withContext JSONObject(response.body()?.string())
            }
            return datas.getJSONObject("data")
        }
        suspend fun getHomework(token: String, id: Int): JSONObject {
            val datas: String? = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = "data={\"token\":$token}"
                val body = RequestBody.create(MediaType.parse("text/plain"), payload)
                val request = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/Eleves/$id/cahierdetexte.awp?verbe=get&v=4.26.2")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36"
                    )
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val response = client.newCall(request).execute()

                return@withContext response.body()?.string()
            }
            return JSONObject(datas)
        }
        suspend fun getSchedule(token: String, id: Int, dateDebut: String, dateFin: String): JSONObject {
            val datas: JSONObject = withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val payload = "data={\"token\": $token," +
                        "\"dateDebut\": $dateDebut," +
                        "\"dateFin\": $dateFin," +
                        "\"avecTrous\": false}"
                val body = RequestBody.create(MediaType.parse("text/plain"), payload)
                val request = Request.Builder()
                    .url("https://api.ecoledirecte.com/v3/E/$id/emploidutemps.awp?verbe=get&v=4.27.5")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36"
                    )
                    .addHeader("Referer", "https://www.ecoledirecte.com/")
                    .build()
                val response = client.newCall(request).execute()

                return@withContext JSONObject(response.body()?.string())
            }
            return datas
        }
    }
}