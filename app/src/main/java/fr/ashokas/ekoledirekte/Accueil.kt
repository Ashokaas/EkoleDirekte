package fr.ashokas.ekoledirekte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Accueil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val prenom = intent.getStringExtra("prenom")
        val nom = intent.getStringExtra("nom")
        val moy = intent.getStringExtra("moyenne_premier_trim")
        val token = intent.getStringExtra("token")
        val id = intent.getStringExtra("id")

        CoroutineScope(Dispatchers.IO).launch {
            val notes = AccountData.getNotes(
                token = token.toString(),
                id = id.toString()
            )

            runOnUiThread {
                findViewById<TextView>(R.id.test_text).text = notes.get("deuxiemeTrim").toString()
            }

        }

    }
}