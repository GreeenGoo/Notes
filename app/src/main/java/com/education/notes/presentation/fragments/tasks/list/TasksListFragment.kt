package com.education.notes.presentation.fragments.tasks.list

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.education.notes.R
import com.education.notes.data.entity.TasksEntity
import com.education.notes.databinding.FragmentTasksListBinding
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.utils.SwipeHelper
import com.education.notes.presentation.viewmodel.TasksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TasksListFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by viewModel()
    private var _binding: FragmentTasksListBinding? = null
    private val binding get() = _binding!!
    private var menuItemForVisibility: MenuItem? = null
    private var tasksList: List<TasksEntity> = emptyList()
    private var adapter = TasksListAdapter(::onItemClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewInit()
        tasksViewModelInit()
        binding.addOrUploadTaskFloatingAddButton.setOnClickListener {
            binding.groupForAdd.visibility = View.VISIBLE
            addNewTask()
        }
        showBottomNavigationMenu()
        menuHost()
    }

    private fun addNewTask() {
        binding.addOrUploadTasksAddPanelOkButton.setOnClickListener {
            if (binding.addOrUploadTasksAddPanelFieldForText.text.isNotEmpty()) {
                addTaskToDatabase()
            } else {
                showToast(getString(R.string.fill_out_add_task_field))
            }
        }
        binding.addOrUploadTasksAddPanelCancelButton.setOnClickListener {
            cancelAddPanel()
        }
    }

    private fun addTaskToDatabase() {
        val taskText = binding.addOrUploadTasksAddPanelFieldForText.text.toString()
        val task = TasksEntity(0, taskText, false)
        tasksViewModel.addTask(task)
        showToast(getString(R.string.task_is_added))
        hideAddPanel()
        tasksViewModelInit()
        hideKeyboardFrom(requireContext(), requireView())
    }

    private fun cancelAddPanel() {
        hideAddPanel()
        hideKeyboardFrom(requireContext(), requireView())
    }

    private fun hideKeyboardFrom(context: Context, view: View?) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun hideAddPanel() {
        binding.groupForAdd.visibility = View.GONE
        binding.addOrUploadTasksAddPanelFieldForText.text = null
    }


    private fun recyclerViewInit() {
        val recyclerView = binding.addOrUploadTasksRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        val itemTouchHelper =
            ItemTouchHelper(object : SwipeHelper(binding.addOrUploadTasksRecyclerView) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    val buttons: List<UnderlayButton>
                    val deleteButton = deleteButton(position)
                    buttons = listOf(deleteButton)
                    return buttons
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.addOrUploadTasksRecyclerView)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    tasksViewModel.deleteTask(tasksList[position])
                    showToast(getString(R.string.task_is_deleted))
                    tasksViewModelInit()
                }
            })
    }

    private fun tasksViewModelInit() {
        tasksViewModel.getAllTasks()
        tasksViewModel.readAllData.observe(viewLifecycleOwner) { tasks ->
            adapter.setData(tasks)
            tasksList = tasks
            hideDeleteIconIfDatabaseIsEmpty()
        }
    }

    private fun onItemClick(position: Int) {
        if (tasksList[position].crossed) {
            tasksViewModel.updateTask(
                TasksEntity(tasksList[position].id, tasksList[position].text, false)
            )
        } else {
            tasksViewModel.updateTask(
                TasksEntity(tasksList[position].id, tasksList[position].text, true)
            )
        }
            tasksViewModel.getAllTasks()
    }

    private fun hideDeleteIconIfDatabaseIsEmpty() {
        menuItemForVisibility?.isVisible = tasksList.isNotEmpty()
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
                        deleteAllTasks()
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

    private fun deleteAllTasks() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            tasksViewModel.deleteAllTasks()
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

    companion object {
        fun toCrossLine(text: String): SpannableString {
            val crossedText = SpannableString(text)
            crossedText.setSpan(
                StrikethroughSpan(),
                0,
                crossedText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return crossedText
        }
    }
}