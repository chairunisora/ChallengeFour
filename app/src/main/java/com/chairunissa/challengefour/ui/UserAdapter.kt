package com.chairunissa.challengefour.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chairunissa.challengefour.databinding.ItemUserBinding
import com.chairunissa.challengefour.datauser.UserEntry

class NoteAdapter(
    private var onEdit: (UserEntry) -> Unit,
    private var onDelete: (UserEntry) -> Unit
) : ListAdapter<UserEntry, NoteAdapter.NoteViewHolder>(NoteDiffCallBack()) {

    class NoteViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            titleTv.text = item.title
            noteTv.text = item.note
            deleteButton.setOnClickListener {
                onDelete(item)
            }
            root.setOnClickListener {
                onEdit(item)
            }
        }
    }


}

class NoteDiffCallBack : DiffUtil.ItemCallback<UserEntry>() {
    override fun areItemsTheSame(oldItem: UserEntry, newItem: UserEntry): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserEntry, newItem: UserEntry): Boolean =
        oldItem == newItem
}