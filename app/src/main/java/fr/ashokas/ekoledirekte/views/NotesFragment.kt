package fr.ashokas.ekoledirekte.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import fr.ashokas.ekoledirekte.R
import fr.ashokas.ekoledirekte.api.AccountData
import fr.ashokas.ekoledirekte.api.UserViewModel
import fr.ashokas.ekoledirekte.api.convertirDate
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

    @RequiresApi(Build.VERSION_CODES.O)
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

        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager)
        val fragmentList = listOf(
            TrimestreFragment.newInstance(1),
            TrimestreFragment.newInstance(2),
            TrimestreFragment.newInstance(3))

        val adapter = gradesPagerAdapter(this@NotesFragment, fragmentList)
        viewPager2.adapter = adapter
        viewPager2.isUserInputEnabled = true





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

                val trimestreNote = note["codePeriode"].toString()[3].digitToInt() - 1

                println(note)

                val matiereNote = note["libelleMatiere"]

                val devoirNote = note["devoir"]
                val valeurNote = note["valeur"]
                val surcmbNote = note["noteSur"]
                val coefNote = note["coef"]

                val dateNote = convertirDate(note["date"] as String)

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

            withContext(Dispatchers.Main) {
                val userViewModel = ViewModelProvider(this@NotesFragment)[UserViewModel::class.java]
                val matieres = notes["matieres"] as Array<Array<Any>>
                userViewModel.notes = MutableLiveData(grades)
                userViewModel.matieres = MutableLiveData(matieres)
                userViewModel.notesAndMatieres = MutableLiveData(Pair(grades, matieres))
            }

            /*
            var calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH) + 1

            var currentTrim = if (currentMonth in 9..11) {
                0
            } else if (currentMonth in 12..2) {
                1
            } else {
                2
            }*/

            val previousButton: ImageButton = view.findViewById<ImageButton>(R.id.previous_button)
            val nextButton: ImageButton = view.findViewById<ImageButton>(R.id.next_button)
            val titleText = view.findViewById<TextView>(R.id.title_text)

            previousButton.setOnClickListener {
                // Ajoutez ici la logique pour passer au trimestre précédent
                // et mettre à jour le titre en conséquence

                val page = viewPager2.currentItem
                viewPager2.setCurrentItem(page - 1, true)
                titleText.text = "Trimestre "

                /*val layoutAVider = view.findViewById<LinearLayout>(R.id.grades_table)
                layoutAVider.removeAllViews()

                currentTrim -= 1
                if (currentTrim < 0) {
                    currentTrim = 2
                }
                titleText.text = "Trimestre " + (currentTrim + 1).toString()

                for (mat in grades[currentTrim].keys) {
                    println(grades[currentTrim][mat])
                    afficher_matiere(view, savedInstanceState, mat)
                    for (note in grades[currentTrim][mat]!!) {
                        val noteAffiche = "${note[0]} : ${note[1]}/${note[2]}"
                        afficher_notes(view, savedInstanceState, noteAffiche)

                    }

                }*/
            }






            nextButton.setOnClickListener {
                // Ajoutez ici la logique pour passer au trimestre suivant
                // et mettre à jour le titre en conséquence

                val page = viewPager2.currentItem
                viewPager2.setCurrentItem(page + 1, true)
                titleText.text = "Trimestre "

                /*val layoutAVider = view.findViewById<LinearLayout>(R.id.grades_table)
                layoutAVider.removeAllViews()

                currentTrim += 1
                if (currentTrim > 2) {
                    currentTrim = 0
                }
                titleText.text = "Trimestre " + (currentTrim + 1).toString()
                for (mat in grades[currentTrim].keys) {
                    println(grades[currentTrim][mat])
                    afficher_matiere(view, savedInstanceState, mat)
                    for (note in grades[currentTrim][mat]!!) {
                        val noteAffiche = "${note[0]} : ${note[1]}/${note[2]}"
                        afficher_notes(view, savedInstanceState, noteAffiche)

                    }

                }*/
            }





            /*
            for (trim in 0 until 3) {
                val trim1 = Bundle()
                trim1.putSerializable("oui", oui)
                fragmentList[0].arguments = trim1
            }*/
        }







    }


    private fun pxToDp(px: Int): Int {
        val density = resources.displayMetrics.density
        return (px * density).toInt()
    }



    /*fun afficher_notes(view: View, savedInstanceState: Bundle?, text: String) {
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
    }*/
}
