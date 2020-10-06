package smu.miso

import android.widget.ImageView
import smu.miso.R

fun mappingDeptProfile(department: String, deptImage: ImageView) {
    when (department) {
        //융공대
        "컴퓨터과학전공" -> deptImage.setImageResource(R.drawable.computer_science)
        "생명공학전공" -> deptImage.setImageResource(R.drawable.bio_engineering)
        "휴먼지능정보공학전공" -> deptImage.setImageResource(R.drawable.human_intelligence_information_engineering)
        "전기공학" -> deptImage.setImageResource(R.drawable.electrical_engineering)
        "게임전공" -> deptImage.setImageResource(R.drawable.game)
        "화학에너지공학전공" -> deptImage.setImageResource(R.drawable.chemical_energy_engineering)

        //경영경제대
        "경영학부" -> deptImage.setImageResource(R.drawable.gyeongyoung)
        "경제금융학부" -> deptImage.setImageResource(R.drawable.gyeongje)
        "글로벌경영학과" -> deptImage.setImageResource(R.drawable.global_gyeongyoung)
        "융합경영학과" -> deptImage.setImageResource(R.drawable.yunghap)

        //문예대
        "무용예술전공" -> deptImage.setImageResource(R.drawable.muyong)
        "생활예술전공" -> deptImage.setImageResource(R.drawable.livingart)
        "스포츠건강관리전공" -> deptImage.setImageResource(R.drawable.sport)
        "식품영양학전공" -> deptImage.setImageResource(R.drawable.food)
        "음악학부" -> deptImage.setImageResource(R.drawable.music)
        "의류학전공" -> deptImage.setImageResource(R.drawable.cloth)
        "조형예술전공" -> deptImage.setImageResource(R.drawable.johyong)

        //사범대
        "교육학과" -> deptImage.setImageResource(R.drawable.education)
        "국어교육과" -> deptImage.setImageResource(R.drawable.korean)
        "수학교육과" -> deptImage.setImageResource(R.drawable.math)
        "영어교육과" -> deptImage.setImageResource(R.drawable.english)

        //인사대
        "가족복지학과" -> deptImage.setImageResource(R.drawable.family)
        "공공인재학부" -> deptImage.setImageResource(R.drawable.gonggong)
        "국가안보학과" -> deptImage.setImageResource(R.drawable.army)
        "문헌정보학전공" -> deptImage.setImageResource(R.drawable.book)
        "역사콘텐츠전공" -> deptImage.setImageResource(R.drawable.history)
        "지적재산권전공" -> deptImage.setImageResource(R.drawable.knowledge)

        else -> deptImage.setImageResource(R.drawable.ic_account_circle_black_36dp)
    }
}