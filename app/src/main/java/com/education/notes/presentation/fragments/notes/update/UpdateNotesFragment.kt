package com.education.notes.presentation.fragments.notes.update

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.databinding.FragmentUpdateNotesBinding
import com.education.notes.presentation.model.Notes
import com.education.notes.presentation.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.card_view_layout.view.*

class UpdateNotesFragment : Fragment() {
    private var _binding: FragmentUpdateNotesBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateNotesFragmentArgs>()
    private lateinit var mNotesViewModel: NotesViewModel
    private var imageURI: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateNotesBinding.inflate(inflater, container, false)
        mNotesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        binding.updateNotesFragmentTitle.setText(args.currentNote.title)
        binding.updateNotesFragmentDescription.setText(args.currentNote.description)
        Glide.with(requireContext()).load(args.currentNote.imageURL).override(250, 250)
            .into(binding.updateNotesFragmentImageView)
        binding.updateNotesFragmentUploadPictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }
        binding.updateNotesFragmentRefreshButton.setOnClickListener {
            updateItem()
        }

        return binding.root
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uriImage = result.data?.data
                binding.updateNotesFragmentImageView.setImageURI(uriImage)
                imageURI = uriImage.toString()
            }

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteNote()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun isURIEmpty(args: UpdateNotesFragmentArgs): String {
        return if (args.currentNote.imageURL.toString() == STANDARD_URI){
            "null"
        } else{
            args.currentNote.imageURL.toString()
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            mNotesViewModel.deleteNote(args.currentNote)
            Toast.makeText(
                requireContext(),
                "${R.string.note_is_removed}${args.currentNote.title}!",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.nav_graph_list_fragment)
        }
        builder.setNegativeButton(R.string.no) { _, _ ->

        }
        builder.setTitle("${R.string.delete_note_question_title} ${args.currentNote.title}?")
        builder.setMessage("${R.string.delete_note_question_message} ${args.currentNote.title}")
        builder.create().show()
    }

    private fun updateItem() {
        val title = binding.updateNotesFragmentTitle.text.toString()
        val description = binding.updateNotesFragmentDescription.text.toString()
        if (inputCheck(title, description)) {
            //Create User Object
            val updateNote = Notes(args.currentNote.id, title, description, imageURI)
            //Update Current User
            mNotesViewModel.updateNote(updateNote)
            Toast.makeText(
                requireContext(),
                getString(R.string.note_is_updaded),
                Toast.LENGTH_LONG
            ).show()
            //NavigateBack
            findNavController().navigate(R.id.nav_graph_list_fragment)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_out_note_fields),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    companion object {
        private const val STANDARD_URI =
            "android.resource://com.education.notes/drawable/empty_note"
    }
}