package fr.ashokas.ekoledirekte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import fr.ashokas.ekoledirekte.login



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupérer les infos
        var button_login = findViewById<Button>(R.id.login_button)
        var input_identifiant = findViewById<EditText>(R.id.user_id)
        var input_password = findViewById<EditText>(R.id.user_mdp)

        button_login.setOnClickListener {

        val identifiant_value = input_identifiant.text.toString()
        val password_value = input_password.text.toString()
        println("id $identifiant_value")
        println(login(identifiant_value, password_value))
        }
    }
}