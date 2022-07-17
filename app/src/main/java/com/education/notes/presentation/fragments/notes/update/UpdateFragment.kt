package com.education.notes.presentation.fragments.notes.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.education.notes.R
import com.education.notes.databinding.FragmentUpdateBinding
import com.education.notes.presentation.model.User
import com.education.notes.presentation.viewmodel.UserViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.upgradeFragmentFirstName.setText(args.currentUser.firstName)
        binding.upgradeFragmentLastName.setText(args.currentUser.lastName)
        binding.upgradeFragmentAge.setText(args.currentUser.age.toString())

        binding.upgradeFragmentUpgradeButton.setOnClickListener {
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
                        deleteUser()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->

        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete ${args.currentUser.firstName}?")
        builder.setMessage("Are you sure you want to delete ${args.currentUser.firstName}")
        builder.create().show()
    }

    private fun updateItem() {
        val firstName = binding.upgradeFragmentFirstName.text.toString()
        val lastName = binding.upgradeFragmentLastName.text.toString()
        val age = Integer.parseInt(binding.upgradeFragmentAge.text.toString())

        if (inputCheck(firstName, lastName, binding.upgradeFragmentAge.text)) {
            //Create User Object
            val updateUser = User(args.currentUser.id, firstName, lastName, age)
            //Update Current User
            mUserViewModel.updateUser(updateUser)
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

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || age.isEmpty())
    }
}