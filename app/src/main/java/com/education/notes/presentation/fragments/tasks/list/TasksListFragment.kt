package com.education.notes.presentation.fragments.tasks.list

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
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
        _binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewInit()
        tasksViewModelInit()
        binding.addOrUploadTaskFloatingAddButton.setOnClickListener {
            binding.addOrUploadTasksAddPanel.visibility = View.VISIBLE
            addNewTask()
        }
        showBottomNavigationMenu()
        menuHost()
        super.onViewCreated(view, savedInstanceState)
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
        val task = TasksModel(0, taskText, false)
        tasksViewModel.addTask(task)
        showToast(getString(R.string.task_is_added))
        hideAddPanel()
        tasksViewModelInit()
        MainActivity.hideKeyboardFrom(requireContext(), requireView())
    }

    private fun cancelAddPanel() {
        hideAddPanel()
        MainActivity.hideKeyboardFrom(requireContext(), requireView())
    }

    private fun hideAddPanel() {
        binding.addOrUploadTasksAddPanel.visibility = View.GONE
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
        //val itemTouchHelper = ItemTouchHelper(SwipeToDelete(adapter))
        //itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getBitmapFromVectorDrawable(requireContext(), R.drawable.delete_icon),
            "Delete",
            14f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    tasksViewModel.deleteTask(tasksList[position])
                    showToast("Task is deleted!")
                    tasksViewModelInit()
                }
            })
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun tasksViewModelInit() {
        tasksViewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        tasksViewModel.getAllTasks()
        tasksViewModel.readAllData.observe(viewLifecycleOwner) { tasks ->
            adapter.setData(tasks)
            tasksList = tasks
        }
    }

    private fun onItemClick(position: Int, action: String) {
        if (action == CROSS_ACTION){
            if (tasksList[position].crossed) {
                tasksViewModel.updateTask(
                    TasksModel(tasksList[position].id, tasksList[position].text, false)
                )
            } else {
                tasksViewModel.updateTask(
                    TasksModel(tasksList[position].id, tasksList[position].text, true)
                )
            }
        }
        else if (action == SWIPE_ACTION){
            tasksViewModel.deleteTask(tasksList[position])
        }
        tasksViewModelInit()
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

    companion object{
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
        const val CROSS_ACTION = "cross"
        const val SWIPE_ACTION = "swipe_to_delete"
    }
}