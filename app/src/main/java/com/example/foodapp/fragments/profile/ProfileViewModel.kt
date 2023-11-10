package com.example.foodapp.fragments.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airmovies.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _usernameState = MutableLiveData<String>()
    val usernameState: LiveData<String> = _usernameState

    private val _imageState = MutableLiveData<String>()
    val imageState: LiveData<String> = _imageState

    private val _genderState = MutableLiveData<String>()
    val genderState: LiveData<String> = _genderState

    private val _ageState = MutableLiveData<String>()
    val ageState: LiveData<String> = _ageState

    private val _weightState = MutableLiveData<String>()
    val weightState: LiveData<String> = _weightState

    private val _hightState = MutableLiveData<String>()
    val hightState: LiveData<String> = _hightState

    private val _progressState = MutableLiveData<String>()
    val progressState: LiveData<String> = _progressState

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    private val _editAge = MutableLiveData<Resource<String>>(Resource.Unspecified())

    private val _editWeight = MutableLiveData<Resource<String>>(Resource.Unspecified())

    private val _editProgress = MutableLiveData<Resource<String>>(Resource.Unspecified())

    private val _loadingStateEdit = MutableLiveData<Boolean>()

    private val _errorStateEdit = MutableLiveData<String>()

    val currentUid = firebaseAuth.currentUser?.uid.toString()

    fun getData() {
        _loadingState.value = true
        firestore.collection("user").whereEqualTo("uid", currentUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    _loadingState.value = false
                    _errorState.value = exception.localizedMessage
                    Log.e("errorProfile", exception.message.toString())
                } else {
                    if (!snapshot!!.isEmpty) {
                        val documentList = snapshot.documents

                        for (document in documentList) {
                            val username = document.get("username") as String
                            val imagePath = document.get("imgPath") as String
                            val gender = document.get("gender") as String
                            val age = document.get("age") as String
                            val weight = document.get("weight") as String
                            val hight = document.get("hight") as String
                            val progress = document.get("progress") as String
                            _usernameState.value = username
                            _imageState.value = imagePath
                            _genderState.value = gender
                            _ageState.value = age
                            _weightState.value = weight
                            _hightState.value = hight
                            _progressState.value = progress
                        }
                    }
                }
            }
    }

    fun saveImage(pickedImage: Uri) {

        _loadingStateEdit.value = true

        firebaseStorage.reference.child("imgPath").child(currentUid!!).delete()

        val storage = firebaseStorage.reference.child("imgPath").child(currentUid)
        storage.putFile(pickedImage)
        storage.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()

            firestore.collection("user").document(currentUid)
                .update(
                    mapOf(
                        "imgPath" to imageUrl
                    )
                ).addOnCompleteListener { _loadingStateEdit.value = false }
                .addOnFailureListener { exception ->
                    _loadingStateEdit.value = false
                    _errorStateEdit.value = exception.localizedMessage
                }
        }
    }

    fun editAge(age: String) = viewModelScope.launch {

        _editAge.postValue(Resource.Loading())

        firestore.collection("user").document(currentUid)
            .update(
                mapOf("age" to age)
            )
            .addOnSuccessListener {
                _editAge.value = Resource.Success(age)
            }
            .addOnFailureListener {
                _editAge.value = Resource.Error(it.message.toString())
                Log.d("ProfileVM", _editAge.toString())
            }

    }

    fun editWeight(weight: String) = viewModelScope.launch {

        _editWeight.postValue(Resource.Loading())

        firestore.collection("user").document(currentUid)
            .update(
                mapOf("weight" to weight)
            )
            .addOnSuccessListener {
                _editWeight.value = Resource.Success(weight)
            }
            .addOnFailureListener {
                _editWeight.value = Resource.Error(it.message.toString())
                Log.d("ProfileVM", _editWeight.toString())
            }

    }

    fun editProgress(progress: String) = viewModelScope.launch {

        _editProgress.postValue(Resource.Loading())

        firestore.collection("user").document(currentUid)
            .update(
                mapOf("progress" to progress)
            )
            .addOnSuccessListener {
                _editProgress.value = Resource.Success(progress)
            }
            .addOnFailureListener {
                _editProgress.value = Resource.Error(it.message.toString())
                Log.d("ProfileVM", _editProgress.toString())
            }

    }

    fun logOut(){
        firebaseAuth.signOut()
    }
}