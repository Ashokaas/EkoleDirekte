package fr.ashokas.ekoledirekte.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.ashokas.ekoledirekte.R
import fr.ashokas.ekoledirekte.api.UserViewModel
import fr.ashokas.ekoledirekte.api.recycleAdapter
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TRIMESTRE = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [TrimestreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrimestreFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var trimestre: Int? = null
    lateinit var adapter: recycleAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trimestre = it.getInt(TRIMESTRE)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trimestre, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val frag = getView()

        val args = arguments
        val value = args?.getString("oui")

        println(value)
        println(value)
        println(value)
        println(value)
        println(value)
        println(value)

        val userViewModel = ViewModelProvider(requireParentFragment())[UserViewModel::class.java]

        userViewModel.notesAndMatieres.observe(viewLifecycleOwner) {
            val notesTrim = it.first[trimestre!! - 1] as Map<String, Any>
            val matieresTrim = it.second[trimestre!! - 1] as Array<JSONObject>

            val recyclerView: RecyclerView? = frag?.findViewById(R.id.testy)
            recyclerView?.layoutManager = LinearLayoutManager(context)
            adapter = recycleAdapter(context, Pair(notesTrim, matieresTrim))
            recyclerView?.adapter = adapter
        }




        /*val textView = view.findViewById<TextView>(R.id.matiere1note1)
        val layoutToExpand = view.findViewById<RelativeLayout>(R.id.LayouToExpand)

        textView.setOnClickListener {
            if (layoutToExpand.visibility == View.GONE) {
                // Si le layout est caché, on l'affiche en se déroulant
                layoutToExpand.visibility = View.VISIBLE
            } else {
                // Si le layout est affiché, on le cache en se repliant

                layoutToExpand.visibility = View.GONE

            }
        }*/

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param trimestre Trimestre pour ce fragment
         * @return A new instance of fragment trimestre1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(trimestre: Int) =
            TrimestreFragment().apply {
                arguments = Bundle().apply {
                    putInt(TRIMESTRE, trimestre)
                }
            }
    }
}