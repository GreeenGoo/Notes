package com.education.notes.presentation.fragments.notes.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.education.notes.R
import com.education.notes.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentUpdateBinding.inflate(inflater, container, false)

        binding.upgradeFragmentFirstName.setText(args.currentUser.firstName)
        binding.upgradeFragmentLastName.setText(args.currentUser.lastName)
        binding.upgradeFragmentAge.setText(args.currentUser.age.toString())

        return binding.root
    }
}