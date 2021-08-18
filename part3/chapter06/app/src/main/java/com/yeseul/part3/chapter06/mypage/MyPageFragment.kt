package com.yeseul.part3.chapter06.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeseul.part3.chapter06.R
import com.yeseul.part3.chapter06.databinding.FragmentMypageBinding

class MyPageFragment: Fragment(R.layout.fragment_mypage) {

    private lateinit var binding : FragmentMypageBinding
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        binding.signInOutButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (auth.currentUser == null) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()){ task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "$email 님 안녕하세요!", Toast.LENGTH_SHORT).show()
                            successSignIn()
                        } else {
                            Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                auth.signOut()
                successSignOut()
            }
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.emailEditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signInOutButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }

        binding.passwordEditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signInOutButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            successSignOut()
        } else {
            binding.emailEditText.setText(auth.currentUser?.email)
            binding.passwordEditText.setText("******")
            successSignIn()
        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        binding.emailEditText.isEnabled = false
        binding.passwordEditText.isEnabled = false

        binding.signInOutButton.text = "로그아웃"
        binding.signInOutButton.isEnabled = true
        binding.signUpButton.isEnabled = false
    }

    private fun successSignOut() {
        binding.emailEditText.text.clear()
        binding.emailEditText.isEnabled = true
        binding.passwordEditText.text.clear()
        binding.passwordEditText.isEnabled = true

        binding.signInOutButton.text = "로그인"
        binding.signInOutButton.isEnabled = false
        binding.signUpButton.isEnabled = false
    }
}