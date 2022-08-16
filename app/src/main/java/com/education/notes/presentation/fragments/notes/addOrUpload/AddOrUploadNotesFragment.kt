package com.education.notes.presentation.fragments.notes.addOrUpload

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.databinding.FragmentAddOrUploadNotesBinding
import com.education.notes.model.NotesModel
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.viewmodel.NotesViewModel

class AddOrUploadNotesFragment : Fragment() {
    private lateinit var mNotesViewModel: NotesViewModel
    private var _binding: FragmentAddOrUploadNotesBinding? = null
    private val binding get() = _binding!!
    private var imageURI: String = ""
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uriImage = result.data?.data
                Glide.with(requireContext()).load(uriImage).override(250, 250)
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
        mNotesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        uploadPicture()
        hideBottomNavigationMenu()
        val bundleNote = arguments?.getParcelable<NotesModel>("currentNote")
        if (bundleNote == null) {
            addNewNote()
        } else {
            (activity as AppCompatActivity).supportActionBar?.title =
                getString(R.string.upload_label_text)
            binding.addNotesFragmentAddButton.text = getString(R.string.upload_button_text)
            loadSelectedNote(bundleNote)
            menuHost(bundleNote)
            updateItem(bundleNote)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadSelectedNote(bundleNote: NotesModel) {
        binding.addNotesFragmentTitle.setText(bundleNote.title)
        binding.addNotesFragmentDescription.setText(bundleNote.description)
        imageURI = bundleNote.imageURL
        Glide.with(requireContext()).load(bundleNote.imageURL).override(250, 250)
            .into(binding.addNotesFragmentImageView)
    }

    private fun menuHost(bundleNote: NotesModel) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteNote(bundleNote)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun deleteNote(bundleNote: NotesModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            mNotesViewModel.deleteNote(bundleNote)
            Toast.makeText(
                requireContext(),
                "${getString(R.string.note_is_removed)} ${bundleNote.title}!",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.notesListFragment)
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
        }
        builder.setTitle("${getString(R.string.delete_note_question_title)} ${bundleNote.title}?")
        builder.setMessage("${getString(R.string.delete_note_question_message)} ${bundleNote.title}")
        builder.create().show()
    }

    private fun updateItem(bundleNote: NotesModel) {
        binding.addNotesFragmentAddButton.setOnClickListener {
            val title = binding.addNotesFragmentTitle.text.toString()
            val description = binding.addNotesFragmentDescription.text.toString()
            if (!(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))) {
                val updateNote = NotesModel(bundleNote.id, title, description, imageURI)
                mNotesViewModel.updateNote(updateNote)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.note_is_updated),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fill_out_note_fields),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    private fun uploadPicture() {
        binding.addNotesFragmentUploadPictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }
    }

    private fun hideBottomNavigationMenu() {
        val mainActivity = activity as MainActivity
        mainActivity.setBottomNavigationMenuVisibility(View.GONE)
    }

    private fun addNewNote() {
        binding.addNotesFragmentAddButton.setOnClickListener {
            insertDataToDataBase()
        }
    }

    private fun insertDataToDataBase() {
        val title = binding.addNotesFragmentTitle.text.toString()
        val description = binding.addNotesFragmentDescription.text.toString()
        if (!(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))) {
            if (imageURI.isEmpty()) {
                imageURI = getStandardURI()
            }
            val note = NotesModel(0, title, description, imageURI)
            mNotesViewModel.addNote(note)
            Toast.makeText(requireContext(), getString(R.string.note_is_added), Toast.LENGTH_LONG)
                .show()
            findNavController().navigateUp()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.note_is_not_added),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun getStandardURI(): String {
        return STANDARD_URI
    }

    companion object {
        private const val STANDARD_URI =
            "android.resource://com.education.notes/drawable/ic_empty_note"
    }
}