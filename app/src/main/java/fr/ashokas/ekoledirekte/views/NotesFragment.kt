package fr.ashokas.ekoledirekte.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.ashokas.ekoledirekte.R
import fr.ashokas.ekoledirekte.api.AccountData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

        // Récupérer le Bundle contenant les données transmises
        val bundle = arguments

        // Récupérer chaque élément individuellement en utilisant la clé correspondante
        val prenom = bundle?.getString("prenom")
        val nom = bundle?.getString("nom")
        val moy = bundle?.getString("moy")
        val token = bundle?.getString("token")
        val id = bundle?.getString("id")
        val photo_url = bundle?.getString("photo_url")

        println(prenom)
        println(nom)
        println(id)
    }

}
