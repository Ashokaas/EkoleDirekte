package fr.ashokas.ekoledirekte.api

import android.content.Context
import android.graphics.Color
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
                    textView.text = note[1] + "/" + note[2] + " (" + note[3] + ")"
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
        if (prevSelect == null) {
            holder.layoutToExpand.layoutParams.height = LayoutParams.WRAP_CONTENT
            textView.setBackgroundColor(Color.parseColor("#DADADA"))
            holder.nomDevoir.text = note[0]
            holder.dateDevoir.text = note[4]
            holder.noteMini.text = "- " + note[6]
            holder.noteEleve.text = note[1]
            holder.noteMax.text = "+ " + note[7]
            prevSelect = Triple(pos, holder, textView)
        } else if (prevSelect!!.first == pos) {
            if (textView == prevSelect!!.third) {
                prevSelect!!.second.layoutToExpand.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0)
                prevSelect!!.third.setBackgroundColor(Color.parseColor("#FFFFFF"))
                prevSelect = null
            } else {
                prevSelect!!.third.setBackgroundColor(Color.parseColor("#FFFFFF"))
                textView.setBackgroundColor(Color.parseColor("#DADADA"))
                holder.nomDevoir.text = note[0]
                holder.dateDevoir.text = note[4]
                holder.noteMini.text = "- " + note[6]
                holder.noteEleve.text = note[1]
                holder.noteMax.text = "+ " + note[7]
                prevSelect = Triple(pos, holder, textView)
            }
        } else {
            prevSelect!!.second.layoutToExpand.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0)
            prevSelect!!.third.setBackgroundColor(Color.parseColor("#FFFFFF"))

            holder.layoutToExpand.layoutParams.height = LayoutParams.WRAP_CONTENT
            textView.setBackgroundColor(Color.parseColor("#DADADA"))
            holder.nomDevoir.text = note[0]
            holder.dateDevoir.text = note[4]
            holder.noteMini.text = "- " + note[6]
            holder.noteEleve.text = note[1]
            holder.noteMax.text = "+ " + note[7]
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