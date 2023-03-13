package fr.ashokas.ekoledirekte.api

import android.util.Base64

/*
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

}*/

fun decodeB64(textB64: String): String {
    // Fonction permettant de décoder une chaine de caractère en base 64 et de la renvoyer en UTF-8
    val data = Base64.decode(textB64, Base64.DEFAULT)

    return String(data)
}


// La fonction prend une chaîne de caractères en entrée et retourne la même chaîne
// avec la première lettre de chaque mot en majuscule et le reste en minuscule.
fun formatage_nom_prenom(input: String): String {
    val words = input.split(" ", "-")
    val capitalizedWords = mutableListOf<String>()
    for (word in words) {
        val firstLetter = word.substring(0, 1).toUpperCase()
        val restOfWord = word.substring(1).toLowerCase()
        capitalizedWords.add("$firstLetter$restOfWord")
    }
    return capitalizedWords.joinToString(" ")
}
