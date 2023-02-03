package fr.ashokas.ekoledirekte

import android.annotation.SuppressLint
import android.content.Intent
import fr.ashokas.ekoledirekte.AccountData

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.*

import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        val buttonLogin = findViewById<Button>(R.id.login_button)
        val inputIdentifiant = findViewById<EditText>(R.id.user_id)
        val inputPassword = findViewById<EditText>(R.id.user_mdp)

        val exitButton = findViewById<ImageButton>(R.id.cross_button)

        exitButton.setOnClickListener {
            finish()
        }

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

                val datas = AccountData.getAccountData(
                    identifiant = identifiantValue,
                    password = passwordValue
                )

                // Identifiant et/ou mdp incorrect
                if (datas["code"] == 505) {
                    runOnUiThread {
                        findViewById<TextView>(R.id.text_view_error_login).text =
                            datas["message"].toString()
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

                    /*val notes = AccountData.getNotes(token = token, id = id.toString())
                    // La variable ci-dessous n'a pour seul objectif de tester la fonction getNotes()
                    val premierTrim = notes.get("premierTrim") as JSONObject
                    val moyennePremierTrim =
                        premierTrim.getJSONObject("ensembleMatieres").getString("moyenneGenerale")

                    findViewById<TextView>(R.id.text_view_error_login).text = moyennePremierTrim*/

                    val intent = Intent(this@MainActivity, Accueil::class.java)
                    intent.putExtra("prenom", prenom.toString())
                    intent.putExtra("nom", nom.toString())
                    intent.putExtra("email", email.toString())
                    intent.putExtra("token", token.toString())
                    intent.putExtra("id", id.toString())
                    intent.putExtra("message", message.toString())
                    startActivity(intent)



                }
            }
        }
    }
}



