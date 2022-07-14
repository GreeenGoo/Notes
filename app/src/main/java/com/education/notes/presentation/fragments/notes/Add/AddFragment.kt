package com.education.notes.presentation.fragments.notes.Add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.education.notes.R
import com.education.notes.databinding.FragmentAddBinding
import com.education.notes.presentation.data.User
import com.education.notes.presentation.data.UserViewModel

class AddFragment : Fragment() {
    private lateinit var mUserViewModel: UserViewModel
    private var _binding: FragmentAddBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.addFragmentAddButton.setOnClickListener {
            insertDataToDataBase()
        }

        return binding.root
    }

    private fun insertDataToDataBase() {
        val firstName = binding.addFragmentFirstName.text.toString()
        val lastName = binding.addFragmentLastName.text.toString()
        val age = binding.addFragmentAge.text

        if (inputCheck(firstName, lastName, age)) {
            //Create User Object
            val user = User(0, firstName, lastName, Integer.parseInt(age.toString()))
            //Add Data to DataBase
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            //Navigate Back
            findNavController().navigate(R.id.nav_graph_list_fragment)
        } else {
            Toast.makeText(requireContext(), "Please, fill out all the fields.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty())
    }
}