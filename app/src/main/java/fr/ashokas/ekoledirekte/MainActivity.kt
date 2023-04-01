package fr.ashokas.ekoledirekte

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import fr.ashokas.ekoledirekte.api.AccountData


import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import fr.ashokas.ekoledirekte.api.UserViewModel
import fr.ashokas.ekoledirekte.views.Accueil
import kotlinx.coroutines.*
import okhttp3.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Changement de la couleur de la barre de notification uniquement disponible après Lollipop (Android 5.0)
        /*if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.white)
        }*/


        val buttonLogin = findViewById<Button>(R.id.login_button)

        val inputIdentifiant = findViewById<EditText>(R.id.user_id)
        val inputPassword = findViewById<EditText>(R.id.user_mdp)

        val switchIsConnected: Switch = findViewById<Switch>(R.id.switch_remember_me)
        switchIsConnected.isChecked = false

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        val rememberSwitchCheck = sharedPref.getString("rememberSwitchCheck", "")



        if (rememberSwitchCheck == "true") {
            val rememberId = sharedPref.getString("rememberId", "")
            inputIdentifiant.setText(rememberId)
            val rememberPassword = sharedPref.getString("rememberPassword", "")
            inputPassword.setText(rememberPassword)

            switchIsConnected.isChecked = true
        }


        val progressBar = findViewById<RelativeLayout>(R.id.progressBar)

        var is_password_visile = false

        val editText = findViewById<EditText>(R.id.user_mdp)
        editText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (editText.right - editText.compoundDrawables[2].bounds.width())) {
                    // Gestion du clique sur le drawableEnd de l'EditText inputPassword (l'oeil)
                    if (is_password_visile == true) {
                        inputPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    } else {
                        inputPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                    println(is_password_visile)
                    is_password_visile = !is_password_visile
                    inputPassword.setSelection(inputPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }




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
                // Compte non élève
                if (datas["code"] == 200 && datas["typeCompte"] != "E") {
                    runOnUiThread {
                        findViewById<TextView>(R.id.text_view_error_login).text = "Ce compte n'est pas supporté par notre application il s'agit probablement d'un compte parent"
                        progressBar.visibility = View.GONE
                    }
                }
                // Utilisateur connecté
                else if (datas["code"] == 200) {

                    if (switchIsConnected.isChecked) {
                        with (sharedPref.edit()) {
                            putString("rememberSwitchCheck", "true")
                            putString("rememberId", inputIdentifiant.text.toString())
                            putString("rememberPassword", inputPassword.text.toString())
                            apply()
                        }
                    } else {
                        with (sharedPref.edit()) {
                            putString("rememberId", "")
                            putString("rememberPassword", "")
                            putString("rememberSwitchCheck", "false")
                            apply()
                        }
                    }



                    withContext(Dispatchers.Main){
                        val userViewModel = ViewModelProvider(this@MainActivity)[UserViewModel::class.java]
                        userViewModel.accountData = MutableLiveData(datas)
                    }


                    val prenom = datas["prenom"]
                    val nom = datas["nom"]
                    val email = datas["email"]
                    val token: String = datas["token"] as String
                    val id: Int = datas["id"] as Int
                    val message = datas["message"]
                    val photoURL = datas["photo"]
                    println(photoURL)


                    runOnUiThread { progressBar.visibility = View.GONE }

                    val intent = Intent(this@MainActivity, Accueil::class.java)
                    intent.putExtra("prenom", prenom.toString())
                    intent.putExtra("nom", nom.toString())
                    intent.putExtra("email", email.toString())
                    intent.putExtra("token", token.toString())
                    intent.putExtra("id", id.toString())
                    intent.putExtra("message", message.toString())
                    intent.putExtra("photoURL", photoURL.toString())
                    startActivity(intent)



                }
            }
        }
    }
}



