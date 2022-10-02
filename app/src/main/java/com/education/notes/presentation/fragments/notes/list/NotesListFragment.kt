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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.education.notes.R
import com.education.notes.databinding.FragmentNotesListBinding
import com.education.notes.model.NotesModel
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.utils.SwipeHelper
import com.education.notes.presentation.viewmodel.NotesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesListFragment : Fragment() {

    private val notesViewModel: NotesViewModel by viewModel()
    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!
    private var menuItemForVisibility: MenuItem? = null
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
        super.onViewCreated(view, savedInstanceState)
        recyclerViewInit()
        notesViewModelInit()
        binding.notesListFragmentFloatingAddButton.setOnClickListener {
            findNavController().navigate(R.id.addOrUploadNotesFragment)
        }
        showBottomNavigationMenu()
        menuHost()
    }

    private fun recyclerViewInit() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemTouchHelper =
            ItemTouchHelper(object : SwipeHelper(binding.recyclerView) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    val buttons: List<UnderlayButton>
                    val deleteButton = deleteButton(position)
                    buttons = listOf(deleteButton)
                    return buttons
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun notesViewModelInit() {
        notesViewModel.getAllNotes()
        notesViewModel.readAllData.observe(viewLifecycleOwner) { notes ->
            adapter.setData(notes)
            notesList = notes
            hideDeleteIconIfDatabaseIsEmpty()
        }
    }

    private fun hideDeleteIconIfDatabaseIsEmpty() {
        menuItemForVisibility?.isVisible = notesList.isNotEmpty()
    }

    private fun onItemClick(position: Int) {
        val selectedItem = Bundle()
        selectedItem.putParcelable(NotesViewModel.BUNDLE_KEY, notesList[position])
        findNavController().navigate(
            R.id.action_notesListFragment_to_addOrUpdateNotesFragment,
            selectedItem
        )
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setPositiveButton(R.string.yes) { _, _ ->
                        notesViewModel.deleteNote(notesList[position])
                        showToast("${getString(R.string.note_is_removed)} ${notesList[position].title}!")
                        notesViewModelInit()
                    }
                    builder.setNegativeButton(R.string.no) { _, _ ->
                    }
                    builder.setTitle("${getString(R.string.delete_note_question_title)} ${notesList[position].title}?")
                    builder.setMessage("${getString(R.string.delete_note_question_message)} ${notesList[position].title}?")
                    builder.create().show()
                }
            })
    }

    private fun menuHost() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                val itemMenu = menu.findItem(R.id.menu_delete)
                hideDeleteIconIfDatabaseIsEmpty()
                menuItemForVisibility = itemMenu
                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteAllNotes()
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

    private fun deleteAllNotes() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            notesViewModel.deleteAllNotes()
            menuItemForVisibility?.isVisible = false
            showToast(getString(R.string.everything_is_removed))
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->

        }
        builder.setTitle(getString(R.string.delete_all_question_title))
            builder.setMessage(getString(R.string.delete_all_question_message))
            builder.create().show()
    }

    private fun showToast(text: String) =
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
}