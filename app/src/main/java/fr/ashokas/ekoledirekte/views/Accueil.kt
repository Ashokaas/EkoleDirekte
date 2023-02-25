package fr.ashokas.ekoledirekte.views

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.ashokas.ekoledirekte.R


import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide


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


        // Récupération des données
        val prenom = intent.getStringExtra("prenom")
        val nom = intent.getStringExtra("nom")
        val moy = intent.getStringExtra("moyenne_premier_trim")
        val token = intent.getStringExtra("token")
        val id = intent.getStringExtra("id")
        var photo_url = intent.getStringExtra("photo_url")


        // Changement de la couleur de la barre de notification uniquement disponible après Lollipop (Android 5.0)
        /*val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.green_secondary)*/


        // Navbar
        val navigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment

        val navController: NavController = navHostFragment.navController
        // NavigationUI.setupWithNavController(navigationView, navController)

        // Ajouter les données à transmettre dans un Bundle
        val bundle = Bundle()
        bundle.putString("prenom", prenom)
        bundle.putString("nom", nom)
        bundle.putString("moy", moy)
        bundle.putString("token", token)
        bundle.putString("id", id)
        bundle.putString("photo_url", photo_url)

        // Créer une instance du fragment NotesFragment et lui transmettre le Bundle
        val notesFragment = NotesFragment()
        notesFragment.arguments = bundle



        // Affichage du nom et du prénom
        val username = findViewById<TextView>(R.id.username)
        username.text = "Bienvenue $prenom $nom"

        // Switch entre fragments avec bundle wooouaw
        navigationView.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.accueilFragment -> {
                    navController.navigate(R.id.accueilFragment, bundle)
                }

                R.id.notesFragment -> {
                    navController.navigate(R.id.notesFragment, bundle)
                }
            }
            true
        }


        // Afficher la PDP de l'utilisateur
        /*
        photo_url = photo_url
        println(photo_url)

        println(photo_url)
        val user_pdp = findViewById<ImageView>(R.id.user_pdp)

        Glide.with(this)
            .load(photo_url)
            .into(user_pdp)

        user_pdp.setBackground(
            ShapeDrawable(OvalShape())
        )
        user_pdp.clipToOutline = true
        */





        /*val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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

                        // findViewById<TextView>(R.id.test_text).text = notes.get("deuxiemeTrim").toString()
                        *//*
                        val tableau = findViewById<TableLayout>(R.id.notes_table)
                        findViewById<TableRow>(R.id.loading_row).visibility = View.GONE*//*

                        val notesJArray = notes["notes"] as JSONArray

                        val grades = listOf(
                            mutableMapOf<String, MutableList<List<String>>>(),
                            mutableMapOf<String, MutableList<List<String>>>(),
                            mutableMapOf<String, MutableList<List<String>>>()
                        )


                        for (i in 0 until notesJArray.length()) {
                            val note: JSONObject = notesJArray.get(i) as JSONObject

                            val trimestreNote = note["codePeriode"].toString().get(3).digitToInt() - 1

                            println(trimestreNote)

                            val matiereNote = note["libelleMatiere"]
                            val devoirNote = note["devoir"]
                            val valeurNote = note["valeur"]
                            val surcmbNote = note["noteSur"]
                            if (matiereNote !in grades[trimestreNote]) {
                                grades[trimestreNote][matiereNote as String] = mutableListOf()
                            }

                            grades[trimestreNote][matiereNote]!!.add(listOf(devoirNote, valeurNote, surcmbNote) as List<String>)
                        }

                        println(grades)




                    }

                    val matiere = TextView(this)
                    matiere.textSize = 18f
                    matiere.text = "Anglais"
                    matiere.typeface = ResourcesCompat.getFont(this, R.font.poppins_light)
                    matiere.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    matiere.setPadding(16, 8, 0, 8)

                    matiere.setTextColor(ContextCompat.getColor(this, R.color.black)) // définir la couleur du texte
                    matiere.setBackgroundColor(ContextCompat.getColor(this, R.color.grey)) // définir la couleur de fond



                    val note = TextView(this)
                    note.textSize = 12f
                    note.text = "EO Reportage 20/20"
                    note.typeface = ResourcesCompat.getFont(this, R.font.poppins_light)
                    note.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    note.setPadding(16, 8, 0, 8)

                    note.setTextColor(ContextCompat.getColor(this, R.color.black)) // définir la couleur du texte
                    note.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey)) // définir la couleur de fond



                    val parentView = findViewById<LinearLayout>(R.id.layout_notes)
                    parentView.addView(matiere)
                    parentView.addView(note)

                }
            }
            true
        }*/


    }


}


