package fr.ashokas.ekoledirekte

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout.Tab
import org.json.JSONArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val prenom = intent.getStringExtra("prenom")
        val nom = intent.getStringExtra("nom")
        val moy = intent.getStringExtra("moyenne_premier_trim")
        val token = intent.getStringExtra("token")
        val id = intent.getStringExtra("id")

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