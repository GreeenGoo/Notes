package com.education.notes.presentation.fragments.notes.addOrUpload

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.data.entity.NotesEntity
import com.education.notes.databinding.FragmentAddOrUploadNotesBinding
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.viewmodel.NotesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddOrUploadNotesFragment : Fragment() {
    private val notesViewModel: NotesViewModel by viewModel()
    private var _binding: FragmentAddOrUploadNotesBinding? = null
    private var imageURI: String = ""
    private val binding get() = _binding!!
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uriImage = result.data?.data
                Glide.with(requireContext()).load(uriImage).override(PICTURE_WIDTH, PICTURE_HEIGHT)
                    .into(binding.addNotesFragmentImageView)
                if (uriImage != null)
                    imageURI = uriImage.toString()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrUploadNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadPicture()
        hideBottomNavigationMenu()
        val bundleNote = arguments?.getParcelable<NotesEntity>(NotesViewModel.BUNDLE_KEY)
        if (bundleNote == null) {
            addNewNote()
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                getString(R.string.upload_label_text)
            binding.addOrUploadNotesFragmentAddOrRefreshButton.text =
                getString(R.string.upload_button_text)
            loadSelectedNote(bundleNote)
            updateItem(bundleNote)
        }
    }

    private fun loadSelectedNote(bundleNote: NotesEntity) {
        binding.addNotesFragmentTitle.setText(bundleNote.title)
        binding.addNotesFragmentDescription.setText(bundleNote.description)
        imageURI = bundleNote.imageURL
        Glide.with(requireContext()).load(bundleNote.imageURL)
            .override(PICTURE_WIDTH, PICTURE_HEIGHT)
            .into(binding.addNotesFragmentImageView)
    }

    private fun updateItem(bundleNote: NotesEntity) {
        binding.addOrUploadNotesFragmentAddOrRefreshButton.setOnClickListener {
            val title = binding.addNotesFragmentTitle.text.toString()
            val description = binding.addNotesFragmentDescription.text.toString()
            if (title.isNotEmpty() && description.isNotEmpty()) {
                val updateNote = NotesEntity(bundleNote.id, title, description, imageURI)
                notesViewModel.updateNote(updateNote)
                showToast(getString(R.string.note_is_updated))
                findNavController().navigateUp()
            } else {
                showToast(getString(R.string.fill_out_note_fields))
            }
        }
    }

    private fun uploadPicture() {
        var intent: Intent
        binding.addNotesFragmentUploadPictureButton.setOnClickListener {
            intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }
    }

    private fun hideBottomNavigationMenu() {
        (requireActivity() as MainActivity).setBottomNavigationMenuVisibility(View.GONE)
    }

    private fun addNewNote() {
        binding.addOrUploadNotesFragmentAddOrRefreshButton.setOnClickListener {
            val title = binding.addNotesFragmentTitle.text.toString()
            val description = binding.addNotesFragmentDescription.text.toString()
            if (title.isNotEmpty() && description.isNotEmpty()) {
                if (imageURI.isEmpty()) {
                    imageURI = getStandardURI()
                }
                val note = NotesEntity(0, title, description, imageURI)
                notesViewModel.addNote(note)
                showToast(getString(R.string.note_is_added))
                findNavController().navigateUp()
            } else {
                showToast(getString(R.string.note_is_not_added))
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getStandardURI(): String = STANDARD_URI

    companion object {
        private const val STANDARD_URI =
            "android.resource://com.education.notes/drawable/ic_empty_note"
        private const val PICTURE_HEIGHT = 250
        private const val PICTURE_WIDTH = 250
    }
}