package smu.miso.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import smu.miso.R
import smu.miso.ui.randomchat.RandomChatViewModel

class ChatFragment: Fragment()  {
    //전역 변수 설정 공간


    //기본 구성 함수(Don't touch)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//여기까지
        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정
        val root = inflater.inflate(R.layout.fragment_chat, container, false)

        return root
    }
    //버튼 등의 리스너를 달 때에는 onViewCreated안에 작성
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
}