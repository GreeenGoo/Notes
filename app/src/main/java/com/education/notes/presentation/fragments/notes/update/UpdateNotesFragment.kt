package com.education.notes.presentation.fragments.notes.update

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.education.notes.R
import com.education.notes.databinding.FragmentAddNotesBinding
import com.education.notes.model.NotesModel
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.fragments.notes.add.AddNotesFragment
import com.education.notes.presentation.viewmodel.NotesViewModel

class UpdateNotesFragment : Fragment() {
    private var _binding: FragmentAddNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var args : NotesModel
    private lateinit var mNotesViewModel: NotesViewModel
    private var imageURI: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        args = arguments?.getParcelable<NotesModel>("currentNote")!!
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        binding.addNotesFragmentAddButton.text = "${getString(R.string.update_refresh_button_text)}"
        mNotesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        binding.addNotesFragmentTitle.setText(args.title)
        binding.addNotesFragmentDescription.setText(args.description)
        imageURI  = args.imageURL
        Glide.with(requireContext()).load(args.imageURL).override(250, 250)
            .into(binding.addNotesFragmentImageView)
        binding.addNotesFragmentUploadPictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }
        binding.addNotesFragmentAddButton.setOnClickListener {
            updateItem()
        }
        val mainActivity = activity as MainActivity
        mainActivity.setBottomNavigationMenuVisibility(View.GONE)
        return binding.root
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uriImage = result.data?.data
                binding.addNotesFragmentImageView.setImageURI(uriImage)
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

   /* private fun isURIEmpty(args: NotesModel): String {
        return if (args.imageURL.equals(STANDARD_URI)){
        }
    }*/

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            mNotesViewModel.deleteNote(args)
            Toast.makeText(
                requireContext(),
                "${getString(R.string.note_is_removed)} ${args.title}!",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.notesListFragment)
        }
        builder.setNegativeButton(R.string.no) { _, _ ->

        }
        builder.setTitle("${getString(R.string.delete_note_question_title)} ${args.title}?")
        builder.setMessage("${getString(R.string.delete_note_question_message)} ${args.title}")
        builder.create().show()
    }

    private fun updateItem() {
        val title = binding.addNotesFragmentTitle.text.toString()
        val description = binding.addNotesFragmentDescription.text.toString()
        if (inputCheck(title, description)) {
            //Create User Object
            val updateNote = NotesModel(args.id, title, description, imageURI)
            //Update Current User
            mNotesViewModel.updateNote(updateNote)
            Toast.makeText(
                requireContext(),
                getString(R.string.note_is_updated),
                Toast.LENGTH_LONG
            ).show()
            //NavigateBack
            findNavController().navigate(R.id.notesListFragment)
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
            "android.resource://com.education.notes/drawable/ic_empty_note"
    }
}