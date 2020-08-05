package smu.miso.ui.tagquestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import smu.miso.R

class TagQuestionFragment : Fragment() {

    private lateinit var tagquestionViewModel: TagQuestionViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tagquestionViewModel =
                ViewModelProviders.of(this).get(TagQuestionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tagquestion, container, false)
        val textView: TextView = root.findViewById(R.id.text_tagquestion)
        tagquestionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}