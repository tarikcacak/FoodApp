package com.example.foodapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.util.LoginFieldState
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.airmovies.util.validateEmail
import com.example.airmovies.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val loginLiveData = MutableLiveData<Resource<FirebaseUser>>()
    private val validationLiveData = MutableLiveData<LoginFieldState>()
    private val resetPasswordLiveData = MutableLiveData<Resource<String>>()

    fun login(email: String, password: String) {
        if (checkValidation(email, password)) {
            loginLiveData.value = Resource.Loading()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        loginLiveData.value = Resource.Success(it)
                    }
                }
                .addOnFailureListener {
                    loginLiveData.value = Resource.Error(it.message.toString())
                }
        } else {

            val loginFieldState = LoginFieldState(
                validateEmail(email),
                validatePassword(password)
            )

            validationLiveData.value = loginFieldState

        }
    }

    fun resetPassword(email: String) {
        resetPasswordLiveData.value = Resource.Loading()
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                resetPasswordLiveData.value = Resource.Success(email)
            }
            .addOnFailureListener {
                resetPasswordLiveData.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(email: String, password: String): Boolean {
        val emailValidaiton = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidaiton is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

    fun observeLoginLiveData(): LiveData<Resource<FirebaseUser>> {
        return loginLiveData
    }

    fun observeValidationLiveData(): LiveData<LoginFieldState> {
        return validationLiveData
    }

    fun observeResetPasswordLiveData(): LiveData<Resource<String>> {
        return resetPasswordLiveData
    }

}