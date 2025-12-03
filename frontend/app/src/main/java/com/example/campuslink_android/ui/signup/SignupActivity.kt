package com.example.campuslink_android.ui.signup

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dao.AuthApi
import com.example.campuslink_android.data.dto.EmailCheckResponse
import com.example.campuslink_android.data.dto.SignupRequest
import com.example.campuslink_android.data.dto.SignupResponse
import com.example.campuslink_android.core.network.RetrofitInstance

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SignupActivity : AppCompatActivity() {

    private lateinit var spinnerCampus: Spinner
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var checkPrivacy: CheckBox
    private lateinit var btnShowPrivacy: Button
    private lateinit var btnCheckEmail: Button
    private lateinit var btnSignup: Button

    private var emailChecked = false
    private var selectedCampusId: Int = -1

    // 캠퍼스 목록
    private val campusList = listOf(
        Campus(1, "배재대학교", "pcu.ac.kr"),
        Campus(2, "충남대학교", "cnu.ac.kr"),
        Campus(3, "한남대학교", "hnu.kr"),
        Campus(4, "목원대학교", "mokwon.ac.kr"),
        Campus(5, "우송대학교", "wsi.ac.kr")
    )

    private val api = RetrofitInstance.authApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signup)

        initViews()
        setupCampusSpinner()
        setupEmailCheck()
        setupPrivacyDialog()
        setupSignupButton()
    }

    private fun initViews() {
        spinnerCampus = findViewById(R.id.editSchool)
        etEmail = findViewById(R.id.editEmail)
        etPassword = findViewById(R.id.editPassword)
        etPasswordConfirm = findViewById(R.id.editPasswordCheck)
        etName = findViewById(R.id.editName)
        etPhone = findViewById(R.id.editPhone)
        checkPrivacy = findViewById(R.id.checkPrivacy)
        btnShowPrivacy = findViewById(R.id.btnShowPrivacy)
        btnCheckEmail = findViewById(R.id.btnCheckEmail)
        btnSignup = findViewById(R.id.btnSignup)
    }

    // 캠퍼스 Spinner 설정
    private fun setupCampusSpinner() {
        val campusNames = campusList.map { it.name }

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item,
            campusNames)

        spinnerCampus.adapter = adapter

        spinnerCampus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCampusId = campusList[position].id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // 이메일 중복 체크
    private fun setupEmailCheck() {
        btnCheckEmail.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val domain = campusList.find { it.id == selectedCampusId }?.domain ?: ""

            if (!email.endsWith("@$domain")) {
                toast("해당 학교 이메일(@$domain)만 사용 가능")
                return@setOnClickListener
            }

            api.checkEmail(email).enqueue(object : Callback<EmailCheckResponse> {
                override fun onResponse(call: Call<EmailCheckResponse>, response: Response<EmailCheckResponse>) {
                    if (response.isSuccessful && response.body()?.data == false) {
                        toast("사용 가능한 이메일입니다.")
                        emailChecked = true
                    } else {
                        toast("이미 사용 중인 이메일입니다.")
                        emailChecked = false
                    }
                }

                override fun onFailure(call: Call<EmailCheckResponse>, t: Throwable) {
                    toast("서버 오류 발생")
                }
            })
        }
    }

    // 개인정보 처리방침 Dialog
    private fun setupPrivacyDialog() {
        btnShowPrivacy.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_privacy)

            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.99).toInt(),   // 가로 90%
                (resources.displayMetrics.heightPixels * 0.47).toInt()   // 세로 70%
            )
            val scrollView = dialog.findViewById<ScrollView>(R.id.scrollViewPrivacy)
            val btnAgree = dialog.findViewById<Button>(R.id.btnPrivacyAgree)

            btnAgree.isEnabled = false

            scrollView.viewTreeObserver.addOnScrollChangedListener {
                if (!scrollView.canScrollVertically(1)) {
                    btnAgree.isEnabled = true
                }
            }

            btnAgree.setOnClickListener {
                checkPrivacy.isChecked = true
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    // 회원가입
    private fun setupSignupButton() {
        btnSignup.setOnClickListener {

            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            val pw2 = etPasswordConfirm.text.toString()
            val name = etName.text.toString()
            val phone = etPhone.text.toString()

            if (!emailChecked) {
                toast("이메일 중복 확인을 해주세요.")
                return@setOnClickListener
            }
            if (pw != pw2) {
                toast("비밀번호가 일치하지 않습니다.")
                return@setOnClickListener
            }
            if (!checkPrivacy.isChecked) {
                toast("개인정보 처리방침에 동의해야 합니다.")
                return@setOnClickListener
            }

            val req = SignupRequest(selectedCampusId, email, pw, name, phone)

            api.signup(req).enqueue(object : Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    toast("회원가입 완료!")
                    finish()
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    toast("서버 연결 실패")
                }
            })
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

data class Campus(val id: Int, val name: String, val domain: String)
