package fr.ashokas.ekoledirekte

import android.annotation.SuppressLint
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

                val prenom = datas.get("prenom")
                val nom = datas.get("nom")
                val email = datas.get("email")
                val token: String = datas["token"] as String
                val id: Int = datas["id"] as Int
                val message = datas["message"]

                val notes = AccountData.getNotes(token=token, id=id)
                // La variable ci-dessous n'a pour seul objectif de tester la fonction getNotes()
                val premierTrim = notes.get("premierTrim") as JSONObject
                val moyennePremierTrim = premierTrim.getJSONObject("ensembleMatieres").getString("moyenneGenerale")


                findViewById<TextView>(R.id.text_view_error_login).text = "$message $moyennePremierTrim"
            }
        }
    }
}



