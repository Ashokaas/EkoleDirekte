package fr.ashokas.ekoledirekte.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import fr.ashokas.ekoledirekte.R
import fr.ashokas.ekoledirekte.api.AccountData
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val frag = getView()

        // Récupérer le Bundle contenant les données transmises
        val bundle = arguments

        // Récupérer chaque élément individuellement en utilisant la clé correspondante
        val prenom = bundle?.getString("prenom")
        val nom = bundle?.getString("nom")
        val moy = bundle?.getString("moy")
        val token = bundle?.getString("token")
        val id = bundle?.getString("id")
        val photo_url = bundle?.getString("photo_url")


        var currentTrimester = 1

        val previousButton = view.findViewById<ImageButton>(R.id.btn_previous)
        previousButton.setOnClickListener {
            // Gérer le clic sur le bouton "précédent"
        }

        val nextButton = view.findViewById<ImageButton>(R.id.btn_next)
        nextButton.setOnClickListener {
            // Gérer le clic sur le bouton "suivant"
        }


        val trimesterSpinner = view.findViewById<Spinner>(R.id.trimester_spinner)

// Liste des trimestres disponibles
        val trimesters = listOf("Trimestre 1", "Trimestre 2", "Trimestre 3")

// Adapter pour le spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, trimesters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        trimesterSpinner.adapter = adapter

// Sélection du trimestre courant
        trimesterSpinner.setSelection(currentTrimester - 1) // -1 car les indices commencent à 0




        CoroutineScope(Dispatchers.IO).launch {
            val notes = CoroutineScope(Dispatchers.IO).async {
                return@async AccountData.getNotes(
                    token = token.toString(),
                    id = id.toString()
                )
            }.await()

            val notesJArray = notes["notes"] as JSONArray

            var grades = listOf(
                mutableMapOf<String, MutableList<List<String>>>(),
                mutableMapOf<String, MutableList<List<String>>>(),
                mutableMapOf<String, MutableList<List<String>>>()
            )


            for (i in 0 until notesJArray.length()) {
                val note: JSONObject = notesJArray.get(i) as JSONObject

                val trimestreNote = note["codePeriode"].toString().get(3).digitToInt() - 1

                println(note)

                val matiereNote = note["libelleMatiere"]

                val devoirNote = note["devoir"]
                val valeurNote = note["valeur"]
                val surcmbNote = note["noteSur"]
                val coefNote = note["coef"]

                val dateNote = note["date"]

                val moyenneClasse = note["moyenneClasse"]
                val minClasse = note["minClasse"]
                val maxClasse = note["maxClasse"]

                if (matiereNote !in grades[trimestreNote]) {
                    grades[trimestreNote][matiereNote as String] = mutableListOf()
                }

                grades[trimestreNote][matiereNote]!!.add(listOf(
                    devoirNote, valeurNote, surcmbNote, coefNote,
                    dateNote,
                    moyenneClasse, minClasse, maxClasse
                ) as List<String>)
            }

            println(grades)



            for (trim in 0 until 3) {
                for (mat in grades[trim].keys) {
                    println(grades[trim][mat])
                    afficher_matiere(view, savedInstanceState, mat)
                    for (note in grades[trim][mat]!!) {
                        val noteAffiche = "${note[0]} : ${note[1]}/${note[2]}"
                        afficher_notes(view, savedInstanceState, noteAffiche)

                    }

                }
            }
        }


    }


    private fun pxToDp(px: Int): Int {
        val density = resources.displayMetrics.density
        return (px * density).toInt()
    }



    fun afficher_notes(view: View, savedInstanceState: Bundle?, text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val matiere = TextView(requireContext())
            matiere.textSize = 18f
            matiere.text = text
            matiere.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_light)
            matiere.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            matiere.setPadding(pxToDp(16), pxToDp(8), pxToDp(0), pxToDp(8))
            matiere.text
            matiere.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            ) // définir la couleur du texte
            matiere.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey2
                )
            ) // définir la couleur de fond


            val parentView = view.findViewById<LinearLayout>(R.id.grades_table)
            parentView.addView(matiere)
        }
    }


    fun afficher_matiere(view: View, savedInstanceState: Bundle?, text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val matiere = TextView(requireContext())
            matiere.textSize = 18f
            matiere.text = text
            matiere.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_light)
            matiere.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            matiere.setPadding(pxToDp(8), pxToDp(8), pxToDp(0), pxToDp(8))
            matiere.text
            matiere.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            ) // définir la couleur du texte
            matiere.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green_secondary
                )
            ) // définir la couleur de fond


            val parentView = view.findViewById<LinearLayout>(R.id.grades_table)
            parentView.addView(matiere)
        }
    }
}
