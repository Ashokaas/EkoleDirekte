package fr.ashokas.ekoledirekte.views

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.ashokas.ekoledirekte.R
import fr.ashokas.ekoledirekte.api.AccountData
import org.json.JSONArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.w3c.dom.Text

import android.graphics.Color
import android.view.WindowManager


class Accueil : AppCompatActivity() {

    private fun addRow(view: View, tableLayout: TableLayout) {
        val row: TableRow = TableRow(this)
        val lp: TableRow.LayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
        row.layoutParams = lp
        row.addView(view)
        tableLayout.addView(row)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        // Changement de la couleur de la barre de notification uniquement disponible après Lollipop (Android 5.0)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.green_secondary)
        }



        val prenom = intent.getStringExtra("prenom")
        val nom = intent.getStringExtra("nom")
        val moy = intent.getStringExtra("moyenne_premier_trim")
        val token = intent.getStringExtra("token")
        val id = intent.getStringExtra("id")
        val photo_url = intent.getStringExtra("photo_url")

        val username = findViewById<TextView>(R.id.username)
        username.text = "Bienvenue $prenom $nom".replace("c", "k")


        println(photo_url)
        val user_pdp = findViewById<ImageView>(R.id.user_pdp)

        Glide.with(this)
            .load("https://play-lh.googleusercontent.com/8ddL1kuoNUB5vUvgDVjYY3_6HwQcrg1K2fd_R8soD-e2QYj8fT9cfhfh3G0hnSruLKec")
            .into(user_pdp)


        user_pdp.setBackground(
            ShapeDrawable(OvalShape())
        )
        user_pdp.clipToOutline = true






        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    println("page 1")
                }

                R.id.page_2 -> {
                    CoroutineScope(Dispatchers.IO).launch {



                        val notes = AccountData.getNotes(
                            token = token.toString(),
                            id = id.toString()
                        )

                        runOnUiThread {
                            // findViewById<TextView>(R.id.test_text).text = notes.get("deuxiemeTrim").toString()
                            val tableau = findViewById<TableLayout>(R.id.notes_table)
                            findViewById<TableRow>(R.id.loading_row).visibility = View.GONE

                            val notesJArray = notes["notes"] as JSONArray
                            for (i in 0 until notesJArray.length()) {
                                Log.d("hehe", notesJArray.get(i)::class.java.typeName)

                                val note: JSONObject = notesJArray.get(i) as JSONObject
                                val textView: TextView = TextView(this@Accueil)
                                val text = note.getString("codePeriode") + " | " + note.getString("libelleMatiere") + " | " + note.getString("devoir") + "\nNote: " + note.getString("valeur") + "/" + note.getString("noteSur")
                                textView.text = text
                                addRow(textView, tableau)
                            }
                        }

                    }
                }
            }
            true
        }


    }
}