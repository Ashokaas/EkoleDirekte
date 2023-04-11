package fr.ashokas.ekoledirekte.api

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import fr.ashokas.ekoledirekte.R
import org.json.JSONObject


class recycleAdapter(context: Context?, data: Pair<Map<String, Any>,Array<JSONObject>>) : RecyclerView.Adapter<ViewHolder>() {

    private var notes: Map<String, Any>
    private var matieres: Array<JSONObject>
    private var dataFinales: MutableList<List<Any>> = mutableListOf()
    private var prevSelect: Triple<Int, NotesViewHolder, TextView>? = null

    private var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
        notes = data.first
        matieres = data.second

        // Generation de la liste des trucs a afficher
        // On passe par chaque matiere
        for (mat: JSONObject in matieres) {
            // si c un groupe (Tronc Commun, etc) on l'affiche juste
            if (mat["groupeMatiere"] == true) {
                dataFinales.add(listOf(0, mat["discipline"] as String))
            } else {
                dataFinales.add(listOf(0, mat["discipline"] as String))
                // On prend les notes de cette matiere
                // Si c nul on met une liste vide
                val notesTempo: List<Any> = if (notes[mat["discipline"]] == null) {
                    listOf()
                } else {notes[mat["discipline"]] as List<Any>}
                // On divise tout en liste de 3 truc comme ca 3 notes par ligne.
                var temp = mutableListOf<Any>()
                for (i in notesTempo.indices) {
                    if (i % 3 == 0) {
                        temp = mutableListOf(1)
                    }
                    temp.add(notesTempo[i])
                    if (i % 3 == 2) {
                        dataFinales.add(temp.toList())
                        temp.clear()
                    }
                }
                if (temp.isNotEmpty()) {
                    dataFinales.add(temp.toList())
                }
            }
        }
        println(dataFinales)
    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteViews: List<TextView>
        val nomDevoir : TextView
        val dateDevoir: TextView
        val noteMini: TextView
        val noteEleve: TextView
        val noteMax: TextView
        val layoutToExpand: LinearLayout

        init {
            // Define click listener for the ViewHolder's View
            noteViews = listOf(view.findViewById(R.id.noteView1), view.findViewById(R.id.noteView2), view.findViewById(R.id.noteView3))
            nomDevoir = view.findViewById(R.id.nomDevoir)
            dateDevoir = view.findViewById(R.id.dateDevoir)
            noteMini = view.findViewById(R.id.noteMini)
            noteEleve = view.findViewById(R.id.noteEleve)
            noteMax = view.findViewById(R.id.noteMax)
            layoutToExpand = view.findViewById(R.id.layoutToExpand)
        }
    }

    class ErrorViewHolder(view: View): RecyclerView.ViewHolder(view)

    class MatiereViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.matiereView)
        }
    }

    // Cette fonction passe sur chaque item de la liste a afficher et lui dit "wesh va prendre se layout la et utilise cette classe"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            1 -> {
                view = mInflater.inflate(R.layout.notesrow, parent, false)
                NotesViewHolder(view)
            }
            0 -> {
                view = mInflater.inflate(R.layout.matieretitrelayout, parent, false)
                MatiereViewHolder(view)
            }
            else -> {
                view = mInflater.inflate(R.layout.errorlayout, parent, false)
                ErrorViewHolder(view)
            }

        }
    }

    // Cette fonction passe sur chaque item de la liste et change le layout a quoi il ressemble
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (this.getItemViewType(position)) {
            1 -> {
                val viewHolder = holder as NotesViewHolder
                val data = dataFinales[position]
                for (i in 1 until data.size) {
                    val note = data[i] as List<String>
                    val textView = viewHolder.noteViews[i-1]
                    val note_coef = note[1] + "/" + note[2] + " " + "(" + note[3] + ")"
                    val coeflen = note[3].toString().length
                    val notespan = SpannableString(note_coef)
                    /*
                    // subscript
                    notespan.setSpan(SubscriptSpan(), 0, note_coef.length - coeflen, 0);
                    // make the subscript text smaller
                    notespan.setSpan(RelativeSizeSpan(0.5f), note_coef.length - coeflen, note_coef.length, 0)
                    */
                    textView.text = note_coef
                    textView.setOnClickListener { changerDropdown(viewHolder, note, textView, position) }
                }
            }
            0 -> {
                val viewHolder = holder as MatiereViewHolder
                viewHolder.textView.text = dataFinales[position][1] as String
            }
        }
    }

    private fun changerDropdown(holder: NotesViewHolder, note: List<String>, textView: TextView, pos: Int) {
        // Vérifier si un élément a déjà été sélectionné précédemment
        val isPrevSelected = prevSelect != null

        // Vérifier si l'élément actuellement sélectionné est le même que l'élément précédemment sélectionné
        val isSameItemSelected = isPrevSelected && prevSelect!!.first == pos && textView == prevSelect!!.third

        if (isSameItemSelected) {
            // Si l'élément sélectionné est le même que l'élément précédemment sélectionné, réduire la vue et supprimer la sélection
            prevSelect!!.second.layoutToExpand.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0)
            prevSelect!!.third.setBackgroundColor(Color.parseColor("#2e3439"))
            prevSelect = null
        } else {
            // Si un autre élément est sélectionné, réduire la vue de l'élément précédemment sélectionné et mettre à jour la sélection en cours
            prevSelect?.let {
                it.second.layoutToExpand.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0)
                it.third.setBackgroundColor(Color.parseColor("#2e3439"))
            }

            // Étendre la vue de l'élément sélectionné et mettre à jour les informations d'affichage
            holder.layoutToExpand.layoutParams.height = LayoutParams.WRAP_CONTENT
            textView.setBackgroundColor(Color.parseColor("#3e4552"))
            holder.nomDevoir.text = note[0]
            holder.dateDevoir.text = note[4]
            holder.noteMini.text = "Note min : -" + note[6]
            holder.noteEleve.text = "Moyenne : " + note[5]
            holder.noteMax.text = "Note max : +" + note[7]
            prevSelect = Triple(pos, holder, textView)
        }
    }


    override fun getItemCount(): Int {
        return dataFinales.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataFinales[position][0] as Int
    }



}