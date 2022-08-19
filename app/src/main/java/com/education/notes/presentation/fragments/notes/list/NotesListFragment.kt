package com.education.notes.presentation.fragments.notes.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.education.notes.R
import com.education.notes.databinding.FragmentNotesListBinding
import com.education.notes.model.NotesModel
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.viewmodel.NotesViewModel

class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NotesViewModel
    private var notesList: List<NotesModel> = emptyList()
    private var adapter = NotesListAdapter(::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewInit()
        notesViewModelInit()
        binding.notesListFragmentFloatingAddButton.setOnClickListener {
            findNavController().navigate(R.id.addOrUploadNotesFragment)
        }
        showBottomNavigationMenu()
        menuHost()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun recyclerViewInit() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun notesViewModelInit() {
        notesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        notesViewModel.getAllNotes()
        notesViewModel.readAllData.observe(viewLifecycleOwner) { notes ->
            adapter.setData(notes)
            notesList = notes
        }
    }

    private fun onItemClick(position: Int) {
        val selectedItem = Bundle()
        selectedItem.putParcelable(NotesViewModel.BUNDLE_KEY, notesList[position])
        findNavController().navigate(
            R.id.action_notesListFragment_to_addOrUpdateNotesFragment,
            selectedItem
        )
    }

    private fun menuHost() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteAllUsers()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showBottomNavigationMenu() {
        val mainActivity = activity as MainActivity
        mainActivity.setBottomNavigationMenuVisibility(View.VISIBLE)
    }

    private fun deleteAllUsers() {
        if (notesList.isNotEmpty()) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                notesViewModel.deleteAllNotes()
                showToast(getString(R.string.notes_are_removed))
            }
            builder.setNegativeButton(getString(R.string.no)) { _, _ ->

            }
            builder.setTitle(getString(R.string.delete_all_question_title))
            builder.setMessage(getString(R.string.delete_all_question_message))
            builder.create().show()
        } else {
            showToast(getString(R.string.emptyDatabaseText))
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}