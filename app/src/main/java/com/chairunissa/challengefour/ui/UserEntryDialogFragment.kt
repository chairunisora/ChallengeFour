package com.chairunissa.challengefour.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chairunissa.challengefour.R
import com.chairunissa.challengefour.database.UserData
import com.chairunissa.challengefour.database.local.UserLocalData
import com.chairunissa.challengefour.database.local.room.UserDatabase
import com.chairunissa.challengefour.databinding.UserEntryDialogBinding
import com.chairunissa.challengefour.datauser.UserEntry
import com.chairunissa.challengefour.utils.AppExecutor


class UserEntryDialogFragment : DialogFragment() {
    private var _binding: UserEntryDialogBinding? = null
    private val binding get() = _binding!!

    private val args: UserEntryDialogFragmentArgs by navArgs()

    private enum class EditingState {
        NEW_NOTE,
        EXISTING_NOTE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserEntryDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteDatabase = UserDatabase.getInstance(requireContext())

        val appExecutors = AppExecutor()

        val localDataSource = UserLocalData.getInstance(noteDatabase.userDao())

        val notesRepository = UserData.getInstance(localDataSource, appExecutors)

        var id: Int? = null

        val editingState =
            if (args.itemId > 0) EditingState.EXISTING_NOTE
            else EditingState.NEW_NOTE

        if (editingState == EditingState.EXISTING_NOTE) {
            notesRepository.getNote(args.itemId) {
                binding.label.text = resources.getString(R.string.label_edit_note_dialog)
                binding.doneButton.text = resources.getString(R.string.title_update)
                binding.noteEdt.setText(it.note)
                binding.titleEdt.setText(it.title)
                id = it.id
            }
        }

        with(binding) {
            cancelButton.setOnClickListener {
                dismiss()
            }

            doneButton.setOnClickListener {
                val data = UserEntry(
                    id ?: 0,
                    titleEdt.text.toString().trim(),
                    noteEdt.text.toString().trim()
                )
                notesRepository.addNote(data)

//                setFragmentResult("entryNoteKey", bundleOf("resultKey" to true))
                val navController = findNavController()
                val savedStateHandle = navController.previousBackStackEntry!!.savedStateHandle
                savedStateHandle.set("resultKey", true)

                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

//    override fun getTheme(): Int {
//        return R.style.DialogTheme
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        UserDatabase.closeDb()
        UserDatabase.destroyInstance()
        _binding = null
    }

}