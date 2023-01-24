package fr.ashokas.ekoledirekte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import kotlinx.coroutines.*
import okhttp3.*

import kotlinx.serialization.json.Json
import org.json.JSONObject

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
            var shownPassword = "*"
            for (i in 1 until passwordValue.length) {
                shownPassword += "*"
            }
            println("Identifiant $identifiantValue")
            println("Mot de passe : $shownPassword")

            CoroutineScope(Dispatchers.IO).launch {

                val account = loginEDaccount(identifiantValue, passwordValue)

                withContext(Dispatchers.Main) {
                    /*var intent = Intent(this@MainActivity, Accueil::class.java)
                    intent.putExtra("account", account)
                    startActivity(intent)*/
                    println(account)
                    var account_json = JSONObject(account)


                    if (account_json.getInt("code") == 200) {
                        val data = account_json.getJSONObject("data")
                        val accounts = data.getJSONArray("accounts")
                        val firstAccount = accounts.getJSONObject(0)
                        val modules = firstAccount.getJSONArray("modules")
                        val prenom = firstAccount.getString("prenom")
                        val nom = firstAccount.getString("nom")
                        val email = firstAccount.getString("email")
                        findViewById<TextView>(R.id.text_view_error_login).text = prenom + " " + nom + " " + email
                        println("Account: $accounts")
                        println("Module : ${modules.getJSONObject(3)}")
                    }
                    if (account_json.getInt("code") == 505) {
                        findViewById<TextView>(R.id.text_view_error_login).text = account_json.getString("message")
                    }





                }
            }
        }
    }

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

}



