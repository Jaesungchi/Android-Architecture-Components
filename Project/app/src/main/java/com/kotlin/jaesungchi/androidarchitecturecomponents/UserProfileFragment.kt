package com.kotlin.jaesungchi.androidarchitecturecomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.observe

class UserProfileFragment : Fragment(){
    private val viewModel : UserProfileViewModel by viewModels( factoryProducer = { SavedStateViewModelFactory(
        this.requireActivity().application,this) })

    override fun onCreateView(
        inflter : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View {
        return inflter.inflate(R.layout.main_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.user.observe(viewLifecycleOwner){
            //Update UI
        }
    }
}