package smu.miso.ui.randomchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RandomChatViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is randomchat Fragment"
    }
    val text: LiveData<String> = _text
}