package fr.ashokas.ekoledirekte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var button = findViewById<Button>(R.id.login_button)
        var input1 = findViewById<EditText>(R.id.user_id)
        var input2 = findViewById<EditText>(R.id.user_mdp)

        button.setOnClickListener {

        val input1Value = input1.text.toString()
        val input2Value = input2.text.toString()
        performAction(input1Value, input2Value)
        }
    }
}