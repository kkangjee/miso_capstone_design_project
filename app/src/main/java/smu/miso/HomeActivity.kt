package smu.miso

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
//MainActivity는 bottomnavigationview를 연결하기 위한 엑티비티입니다.
    var mBackWait: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //현재 프레그먼트로 보여질 수 있는 화면들 나열해 놓은 것, navController를 통해서 이동 가능
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_randomchat, R.id.navigation_tags, R.id.navigation_tagquestion, R.id.navigation_setting))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //뒤로가기 버튼 2번 터치 시 액티비티 종료
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(
                this@HomeActivity,
                "뒤로가기 버튼을 한번 더 누르면 종료됩니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            finish()
        }
    }
}
