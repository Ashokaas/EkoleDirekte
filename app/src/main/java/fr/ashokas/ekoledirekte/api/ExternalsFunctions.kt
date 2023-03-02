package fr.ashokas.ekoledirekte.api

import org.json.JSONArray
import org.json.JSONObject

class ExternalsFunctions {
    fun getAverage(notes: JSONArray, matieres: Array<String>) {
        val sumNotes = mutableMapOf<String, Int>()
        val sumCoeff = mutableMapOf<String, Int>()
        for (i in matieres.indices)
        for (i in 0 until notes.length()) {
            val note = notes.getJSONObject(i)
            val noteValue = (note.getString("valeur").toInt() * 20) /
                    note.getString("noteSur").toInt()

        }
    }
}