package smu.miso.ui.randomchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import smu.miso.R

class RandomChatFragment : Fragment() {

    private lateinit var randomchatViewModel: RandomChatViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        randomchatViewModel =
                ViewModelProviders.of(this).get(RandomChatViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_randomchat, container, false)
        val textView: TextView = root.findViewById(R.id.text_randomchat)
        randomchatViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
}
}