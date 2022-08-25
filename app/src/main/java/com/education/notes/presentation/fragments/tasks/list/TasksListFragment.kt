package com.education.notes.presentation.fragments.tasks.list

import android.app.AlertDialog
import android.content.Context
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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.education.notes.R
import com.education.notes.databinding.FragmentTasksListBinding
import com.education.notes.model.TasksModel
import com.education.notes.presentation.MainActivity
import com.education.notes.presentation.viewmodel.TasksViewModel
import kotlinx.android.synthetic.main.fragment_notes_list.recycler_view

class TasksListFragment : Fragment() {

    private var _binding: FragmentTasksListBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksViewModel: TasksViewModel
    private var tasksList: List<TasksModel> = emptyList()
    private var adapter = TasksListAdapter(::onItemClick)
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreshLayout
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
        setItemTouchHelper()
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

    private fun setItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(100f, requireContext())
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MIN_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    showToast("РАБОТАЕТ!!!")
                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }

                    if (isCurrentlyActive) {
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive = dX
                        }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo(
                                (currentScrollXWhenInActive * dX / initXWhenInActive).toInt(),
                                0
                            )
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }
        ).apply {
            attachToRecyclerView(recycler_view)
        }
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
    }

    private fun tasksViewModelInit() {
        tasksViewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        tasksViewModel.getAllTasks()
        tasksViewModel.readAllData.observe(viewLifecycleOwner) { tasks ->
            adapter.setData(tasks)
            tasksList = tasks
        }
    }

    private fun onItemClick(position: Int, text: String) {
        if (text == "crossing") {
            if (tasksList[position].crossed) {
                tasksViewModel.updateTask(
                    TasksModel(tasksList[position].id, tasksList[position].text, false)
                )
            } else {
                tasksViewModel.updateTask(
                    TasksModel(tasksList[position].id, tasksList[position].text, true)
                )
            }
            tasksViewModelInit()
        }
        else if (text == "removing") {
            tasksViewModel.deleteTask(tasksList[position])
            tasksViewModelInit()
        }
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
    }

    private fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }
}