package com.education.notes.presentation.fragments.tasks.list

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
import com.education.notes.databinding.FragmentTasksListBinding
import com.education.notes.model.TasksModel
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.viewmodel.TasksViewModel

class TasksListFragment : Fragment() {

    private var _binding: FragmentTasksListBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksViewModel: TasksViewModel
    private var tasksList: List<TasksModel> = emptyList()
    private var adapter = TasksListAdapter(::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewInit()
        tasksViewModelInit()
        binding.addOrUploadTaskFloatingAddButton.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addOrUploadTasksFragment)
        }
        showBottomNavigationMenu()
        menuHost()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun recyclerViewInit() {
        val recyclerView = binding.addOrUploadTasksRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun tasksViewModelInit() {
        tasksViewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        tasksViewModel.getAllTasks()
        tasksViewModel.readAllData.observe(viewLifecycleOwner) { tasks ->
            adapter.setData(tasks)
            tasksList = tasks
        }
    }

    private fun onItemClick(position: Int) {
        val selectedItem = Bundle()
        selectedItem.putParcelable(TasksViewModel.BUNDLE_KEY, tasksList[position])
        findNavController().navigate(
            R.id.action_tasksFragment_to_addOrUploadTasksFragment,
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
        if (tasksList.isNotEmpty()) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                tasksViewModel.deleteAllTasks()
                showToast(getString(R.string.everything_is_removed))
            }
            builder.setNegativeButton(getString(R.string.no)) { _, _ ->

            }
            builder.setTitle(getString(R.string.delete_all_question_title))
            builder.setMessage(getString(R.string.delete_all_question_message))
            builder.create().show()
        } else{
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