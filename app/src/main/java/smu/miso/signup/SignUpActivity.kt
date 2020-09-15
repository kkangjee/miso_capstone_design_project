package smu.miso.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import smu.miso.CloudFunctions
import smu.miso.R


class SignUpActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        val dept_items = resources.getStringArray(R.array.department_spinner_entries)
        val myAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, dept_items)
        deptName_spinner.adapter = myAdapter
        deptName_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    this@SignUpActivity,
                    "학과를 선택해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //제출하기 버튼을 눌렀을 때 회원가입 액티비티로 전환
        bt_submit.setOnClickListener {
            val univName = univName.text.toString() //대학교 이름
            val deptName = deptName_spinner.selectedItem.toString()

            //대학교 이름, 학과명은 SignUpEmail 액티비티로 넘기기
            val intent = Intent(this, SignUpEmailActivity::class.java)
            intent.putExtra("univName", univName)
            intent.putExtra("department", deptName)

            startActivity(intent)
            finish()
        }
        sign_up.setOnClickListener {
            CloudFunctions.hideKeyboard(this)
        }
    }

}