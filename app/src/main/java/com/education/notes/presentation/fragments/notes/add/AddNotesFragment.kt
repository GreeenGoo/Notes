package com.education.notes.presentation.fragments.notes.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.education.notes.R
import com.education.notes.databinding.FragmentAddNotesBinding
import com.education.notes.presentation.model.Notes
import com.education.notes.presentation.viewmodel.NotesViewModel

class AddNotesFragment : Fragment() {
    private lateinit var mNotesViewModel: NotesViewModel
    private var _binding: FragmentAddNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        mNotesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        binding.addNotesFragmentAddButton.setOnClickListener {
            insertDataToDataBase()
        }

        binding.addNotesFragmentUploadPictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }

        return binding.root
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == RESULT_OK){
            val uriImage = result.data?.data
            binding.addNotesFragmentImageView.setImageURI(uriImage)
        }

    }

    private fun insertDataToDataBase() {
        //val image = requireActivity().findViewById<ImageView>(R.drawable.just_for_example_icon)
        val title = binding.addNotesFragmentTitle.text.toString()
        val description = binding.addNotesFragmentDescription.text.toString()

        if (!isNotEmptyChecking(title, description)) {
            //Create Note Object
            val note = Notes(0, title, description)
            //Add Data to DataBase
            mNotesViewModel.addNote(note)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            //Navigate Back
            findNavController().navigate(R.id.nav_graph_list_fragment)
        } else {
            Toast.makeText(requireContext(), "Please, fill out all the fields.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun isNotEmptyChecking(title: String, description: String): Boolean {
        return TextUtils.isEmpty(title) || TextUtils.isEmpty(description)
    }
}