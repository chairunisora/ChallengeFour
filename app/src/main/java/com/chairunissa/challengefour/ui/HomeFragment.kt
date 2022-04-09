package com.chairunissa.challengefour.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chairunissa.challengefour.R
import com.chairunissa.challengefour.database.IUserData
import com.chairunissa.challengefour.database.UserData
import com.chairunissa.challengefour.database.local.AppLocalData
import com.chairunissa.challengefour.database.local.UserLocalData
import com.chairunissa.challengefour.database.local.room.UserDatabase
import com.chairunissa.challengefour.databinding.FragmentHomeBinding
import com.chairunissa.challengefour.datauser.UserEntry
import com.chairunissa.challengefour.utils.AppExecutor
import com.chairunissa.challengefour.utils.showMaterialAlertDialog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteDatabase = UserDatabase.getInstance(requireContext())

        val appExecutors = AppExecutor()

        val localDataSource = UserLocalData.getInstance(noteDatabase.userDao())

        val notesRepository = UserData.getInstance(localDataSource, appExecutors)

        val navController = findNavController()
        val navBackStackEntry = navController.getBackStackEntry(R.id.homeFragment)

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("resultKey")
            ) {
                val result = navBackStackEntry.savedStateHandle.get<Boolean>("resultKey")
                result?.let {
                    fetchData(notesRepository as IUserData)
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })

//        setFragmentResultListener("entryNoteKey") { _, bundle ->
//            val result = bundle.getBoolean("resultKey")
//            if (result) {
//                Log.d("HomeFragment", result.toString())
//                refreshFragment(notesRepository as NotesRepository)
//            }
//        }

//        binding.logoutTv.setOnClickListener {
//            AppLocalData.dropUserLoggedIn(requireContext())
//            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
//        }

        binding.fab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToUserEntryDialogFragment()
            findNavController().navigate(action)
        }

        val username = AppLocalData.getUserLoggedIn(requireContext())?.username
        binding.welcome.text = HtmlCompat
            .fromHtml(
                "Welcome, <strong>$username!</strong>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

        fetchData(notesRepository)
    }

    private fun fetchData(notesRepository: IUserData) {
        notesRepository.getNotes {
            setupAdapter(it, notesRepository)
        }
    }

    private fun setupAdapter(
        notes: List<UserEntry>,
        userData: IUserData
    ) {
        val adapter = UserAdapter (
            onEdit = { UserEntry ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToUserEntryDialogFragment(UserEntry.id)
                findNavController().navigate(action)
            },
            onDelete = { UserEntry ->
                requireActivity().showMaterialAlertDialog(
                    positiveButtonLable = "Ok",
                    negativeButtonLable = "Cancel",
                    title = "Confirm",
                    message = "Are you sure you want to delete this entry?",
                    actionOnPositiveButton = {
                        userData.deleteNote(UserEntry)
                        refreshFragment(userData)
                    }
                )
            }
        )

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.adapter = adapter
        adapter.submitList(notes)
    }

    private fun refreshFragment(userData: IUserData) {
        val fragmentManager = (requireContext() as? AppCompatActivity)?.supportFragmentManager
        val currentFragment = fragmentManager?.findFragmentById(R.id.nav_host_fragment)
        val transaction = fragmentManager?.beginTransaction()
        currentFragment?.let {
            transaction?.detach(it)?.attach(it)?.commit()
        }

        fetchData(userData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        UserDatabase.closeDb()
        UserDatabase.destroyInstance()
        _binding = null
    }
}