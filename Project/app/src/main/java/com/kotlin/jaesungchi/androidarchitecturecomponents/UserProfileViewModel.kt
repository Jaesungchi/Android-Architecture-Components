package com.kotlin.jaesungchi.androidarchitecturecomponents

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserProfileViewModel(
    savedStateHandle: SavedStateHandle) : ViewModel() {
    val userId : String = savedStateHandle["uid"] ?:
    throw IllegalArgumentException("missing user id")
    val user : LiveData<User> = TODO()
}