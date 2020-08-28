package smu.miso.ui.tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import smu.miso.R

class TagsFragment : Fragment() {
    //전역 변수 설정 공간
    private lateinit var tagsViewModel: TagsViewModel
    //기본 구성 함수(Don't touch)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {//여기까지
        tagsViewModel =
                ViewModelProviders.of(this).get(TagsViewModel::class.java)
        //어떤 화면을 container(프레그먼트가 보여질 화면)에 보여질 것인지 설정
        val root = inflater.inflate(R.layout.fragment_tags, container, false)
        val textView: TextView = root.findViewById(R.id.text_tags)
        tagsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    //버튼 등의 리스너를 달 때에는 onViewCreated안에 작성
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
    //사용자 설정 함수나 override함수들(onBackPressed같은거)은 여기에 작성
}