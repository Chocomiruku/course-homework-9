package com.chocomiruku.homework9.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chocomiruku.homework9.R
import com.chocomiruku.homework9.databinding.ListItemBinding

class ModelAdapter(private val models: List<Model>) :
    RecyclerView.Adapter<ModelAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(models[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Model) {
            val context = binding.idText.context

            binding.idText.text = context.getString(R.string.id).plus(" " + model.id)
            binding.userIdText.text = context.getString(R.string.user_id).plus(" " + model.userId)
            binding.titleText.text = model.title
            binding.bodyText.text = model.body
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class ModelsDiffUtilCallback(
        private val oldList: List<Model>,
        private val newList: List<Model>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
    }

    override fun getItemCount() = models.size
}