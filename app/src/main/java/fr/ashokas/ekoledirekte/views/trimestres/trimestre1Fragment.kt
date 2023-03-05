package fr.ashokas.ekoledirekte.views.trimestres

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import fr.ashokas.ekoledirekte.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [trimestre1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class trimestre1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trimestre1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val frag = getView()


        val textView = view.findViewById<TextView>(R.id.matiere1note1)
        val layoutToExpand = view.findViewById<RelativeLayout>(R.id.LayouToExpand)

        textView.setOnClickListener {
            if (layoutToExpand.visibility == View.GONE) {
                // Si le layout est caché, on l'affiche en se déroulant
                layoutToExpand.visibility = View.VISIBLE
                val animate = TranslateAnimation(0F, 0F, -layoutToExpand.height.toFloat(), 0F)
                animate.duration = 500
                animate.fillAfter = true
                layoutToExpand.startAnimation(animate)
            } else {
                // Si le layout est affiché, on le cache en se repliant
                val animate = TranslateAnimation(0F, 0F, 0F, -layoutToExpand.height.toFloat())
                animate.duration = 500
                animate.fillAfter = true
                animate.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        layoutToExpand.visibility = View.GONE
                    }
                    override fun onAnimationRepeat(animation: Animation) {}
                })
                layoutToExpand.startAnimation(animate)
            }
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment trimestre1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            trimestre1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}