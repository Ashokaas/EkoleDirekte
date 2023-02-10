package fr.ashokas.ekoledirekte

import android.annotation.SuppressLint
import android.content.Intent
import fr.ashokas.ekoledirekte.api.AccountData

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.ashokas.ekoledirekte.views.Accueil
import kotlinx.coroutines.*
import okhttp3.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonLogin = findViewById<Button>(R.id.login_button)

        val inputIdentifiant = findViewById<EditText>(R.id.user_id)
        val inputPassword = findViewById<EditText>(R.id.user_mdp)

        val progressBar = findViewById<RelativeLayout>(R.id.progressBar)



        buttonLogin.setOnClickListener {
            runOnUiThread { progressBar.visibility = View.VISIBLE }

            val identifiantValue = inputIdentifiant.text.toString()
            val passwordValue = inputPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {

                val datas = AccountData.getAccountData(
                    identifiant = identifiantValue,
                    password = passwordValue
                )

                // Identifiant et/ou mdp incorrect(s)
                if (datas["code"] == 505) {
                    runOnUiThread {
                        findViewById<TextView>(R.id.text_view_error_login).text =
                            datas["message"].toString()
                        progressBar.visibility = View.GONE
                    }
                }

                // Utilisateur connecté
                if (datas["code"] == 200) {
                    val prenom = datas.get("prenom")
                    val nom = datas.get("nom")
                    val email = datas.get("email")
                    val token: String = datas["token"] as String
                    val id: Int = datas["id"] as Int
                    val message = datas["message"]
                    val photo_url = datas.get("photo")
                    println(photo_url)


                    runOnUiThread { progressBar.visibility = View.GONE }

                    val intent = Intent(this@MainActivity, Accueil::class.java)
                    intent.putExtra("prenom", prenom.toString())
                    intent.putExtra("nom", nom.toString())
                    intent.putExtra("email", email.toString())
                    intent.putExtra("token", token.toString())
                    intent.putExtra("id", id.toString())
                    intent.putExtra("message", message.toString())
                    intent.putExtra("photo_url", photo_url.toString())
                    startActivity(intent)



                }
            }
        }
    }
}



