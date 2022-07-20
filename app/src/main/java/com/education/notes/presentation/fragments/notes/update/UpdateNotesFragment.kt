package com.education.notes.presentation.fragments.notes.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.education.notes.R
import com.education.notes.databinding.FragmentUpdateNotesBinding
import com.education.notes.presentation.model.Notes
import com.education.notes.presentation.viewmodel.NotesViewModel

class UpdateNotesFragment : Fragment() {
    private var _binding: FragmentUpdateNotesBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateNotesFragmentArgs>()
    private lateinit var mNotesViewModel: NotesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateNotesBinding.inflate(inflater, container, false)
        mNotesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        binding.updateNotesFragmentTitle.setText(args.currentNote.title)
        binding.updateNotesFragmentDescription.setText(args.currentNote.description)
        //binding.up(args.currentUser.age.toString())//HERE HAS TO BE IMAGEVIEW

        binding.updateNotesFragmentUpdateButton.setOnClickListener {
            updateItem()
        }

        return binding.root
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

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mNotesViewModel.deleteNote(args.currentNote)
            Toast.makeText(
                requireContext(),
                "Successfully removed ${args.currentNote.title}!",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.nav_graph_list_fragment)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete ${args.currentNote.title}?")
        builder.setMessage("Are you sure you want to delete ${args.currentNote.title}")
        builder.create().show()
    }

    private fun updateItem() {
        //val image = requireActivity().findViewById<ImageView>(R.drawable.just_for_example_icon)
        val title = binding.updateNotesFragmentTitle.text.toString()
        val description = binding.updateNotesFragmentDescription.text.toString()

        if (inputCheck(title, description)) {
            //Create User Object
            val updateNote = Notes(args.currentNote.id, title, description)
            //Update Current User
            mNotesViewModel.updateNote(updateNote)
            Toast.makeText(
                requireContext(),
                "Successfully updated!",
                Toast.LENGTH_LONG
            ).show()
            //NavigateBack
            findNavController().navigate(R.id.nav_graph_list_fragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please, fill out all the fields.",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun inputCheck(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }
}