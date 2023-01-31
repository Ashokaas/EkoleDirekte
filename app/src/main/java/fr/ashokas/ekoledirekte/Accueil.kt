package fr.ashokas.ekoledirekte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Accueil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val prenom = intent.getStringExtra("prenom")
        val nom = intent.getStringExtra("nom")
        val moy = intent.getStringExtra("moyenne_premier_trim")

        findViewById<TextView>(R.id.test_text).text = prenom + " " + nom + " " + moy
    }
}