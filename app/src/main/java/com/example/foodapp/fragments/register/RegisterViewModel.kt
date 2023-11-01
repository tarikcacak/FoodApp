package com.example.foodapp.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.RegisterFieldState
import com.example.airmovies.util.RegisterValidation
import com.example.airmovies.util.Resource
import com.example.airmovies.util.validateEmail
import com.example.airmovies.util.validateHight
import com.example.airmovies.util.validatePassword
import com.example.airmovies.util.validateUser
import com.example.airmovies.util.validateWeight
import com.example.foodapp.models.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val registerLiveData = MutableLiveData<Resource<User>>(Resource.Unspecified())
    private val validationLiveData = MutableLiveData<RegisterFieldState>()

    fun createAccountWithEmailAndPassword(user: User) = viewModelScope.launch {

        if (checkValidation(user)) {

            registerLiveData.value = Resource.Loading()

            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }
                .addOnFailureListener {
                    registerLiveData.value = Resource.Error(it.message.toString())
                }
        } else {

            val registerFieldState = RegisterFieldState(
                validateUser(user.username),
                validateEmail(user.email),
                validatePassword(user.password),
                validateWeight(user.weight),
                validateHight(user.hight)
            )

            validationLiveData.value = registerFieldState

        }

    }

    private fun saveUserInfo(userUid: String, user: User) {

        val hashMap = hashMapOf<String, Any>()
        hashMap["username"] = user.username
        hashMap["email"] = user.email
        hashMap["password"] = user.password
        hashMap["weight"] = user.weight
        hashMap["hight"] = user.hight
        hashMap["imgPath"] = user.imgPath
        hashMap["progress"] = user.progress

        firestore.collection("user")
            .document(userUid)
            .set(hashMap)
            .addOnSuccessListener {
                registerLiveData.value = Resource.Success(user)
            }
            .addOnFailureListener {
                registerLiveData.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: User): Boolean {
        val emailValidaiton = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        val userValidation = validateUser(user.username)
        val weightValidation = validateWeight(user.weight)
        val hightValidation = validateHight(user.hight)
        val shouldRegister = emailValidaiton is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success && userValidation is RegisterValidation.Success &&
                weightValidation is RegisterValidation.Success && hightValidation is RegisterValidation.Success

        return shouldRegister
    }

    fun observeRegisterLiveData(): LiveData<Resource<User>> {
        return registerLiveData
    }

    fun observeValidationLiveData(): LiveData<RegisterFieldState> {
        return validationLiveData
    }

}