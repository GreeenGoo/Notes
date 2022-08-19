package com.education.notes.presentation.fragments.tasks.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.education.notes.R
import com.education.notes.databinding.FragmentNotesListBinding

class TasksListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.notesListFragmentFloatingAddButton.setOnClickListener{
            findNavController().navigate(R.id.action_tasksFragment_to_addOrUploadTasksFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}