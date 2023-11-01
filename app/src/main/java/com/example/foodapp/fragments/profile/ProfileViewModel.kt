package com.example.foodapp.fragments.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airmovies.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _usernameState = MutableLiveData<String>()
    val usernameState: LiveData<String> = _usernameState

    private val _imageState = MutableLiveData<String>()
    val imageState: LiveData<String> = _imageState

    private val _weightState = MutableLiveData<Int>()
    val weightState: LiveData<Int> = _weightState

    private val _hightState = MutableLiveData<Int>()
    val hightState: LiveData<Int> = _hightState

    private val _progressState = MutableLiveData<Int>()
    val progressState: LiveData<Int> = _progressState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    val currentUid = firebaseAuth.currentUser?.uid.toString()
    fun getDataFromFirebase() {

        _loadingState.value = true

        firestore.collection("user").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, exeption ->

                if (exeption != null) {
                    _loadingState.value = false
                    _errorState.value = exeption.localizedMessage
                } else {
                    if (!snapshot!!.isEmpty) {
                        val documentList = snapshot.documents

                        for (document in documentList) {

                            val user = document.get("username") as String
                            val downloadUri = document.get("imgPath") as String
                            val weight = document.get("weight") as Int
                            val hight = document.get("hight") as Int
                            val progress = document.get("progress") as Int
                            _usernameState.value = user
                            _imageState.value = downloadUri
                            _weightState.value = weight
                            _hightState.value = hight
                            _progressState.value = progress

                        }
                    }
                }
            }
    }

    fun saveImage(pickedImage: Uri) {
        firestore.collection("user").document(currentUid)
            .update(
                hashMapOf<String, Any>(
                    "imgPath" to pickedImage
                )
            )
            .addOnSuccessListener {
                Log.d("imgUpdate", "Image successfully updated")
            }
            .addOnFailureListener {
                Log.e("imgUpdate", it.message.toString())
            }
    }

    fun logOut(){
        firebaseAuth.signOut()
    }

}