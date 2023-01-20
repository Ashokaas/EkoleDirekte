package fr.ashokas.ekoledirekte

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLogin = findViewById<Button>(R.id.login_button)
        val inputIdentifiant = findViewById<EditText>(R.id.user_id)
        val inputPassword = findViewById<EditText>(R.id.user_mdp)

        buttonLogin.setOnClickListener {
            val identifiantValue = inputIdentifiant.text.toString()
            val passwordValue = inputPassword.text.toString()
            println("Identifiant $identifiantValue")
            println("Mot de passe $passwordValue")

            CoroutineScope(Dispatchers.IO).launch {
                val account = loginEDaccount(identifiantValue, passwordValue)
                withContext(Dispatchers.Main) {
                    println(account)
                }
            }
        }
    }

    private suspend fun loginEDaccount(identifiant: String, password: String): String? {
        return withContext(Dispatchers.IO) {
            val version = "4.18.3"
            val url = "https://api.ecoledirecte.com/v3/login.awp?v=$version"
            val client = OkHttpClient()

            val json = """
                {
                    "uuid": "",
                    "identifiant": "$identifiant",
                    "motdepasse": "$password"
                }
            """

            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

            val request = Request.Builder()
                .url(url)
                .addHeader("authority", "api.ecoledirecte.com")
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader(
                    "user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36"
                )
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("sec-gpc", "1")
                .addHeader("origin", "https://www.ecoledirecte.com")
                .addHeader("sec-fetch-site", "same-site")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("referer", "https://www.ecoledirecte.com/")
                .addHeader("accept-language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
                .post(body)
                .build()

            val response = client.newCall(request).execute()

            return@withContext response.body()?.string()
        }
    }
}



