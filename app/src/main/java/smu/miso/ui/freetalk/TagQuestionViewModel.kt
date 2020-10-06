package smu.miso.ui.freetalk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TagQuestionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is TagQuestion Fragment"
    }
    val text: LiveData<String> = _text
}