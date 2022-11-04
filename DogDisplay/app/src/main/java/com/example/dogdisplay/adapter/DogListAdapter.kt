package com.example.dogdisplay.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.dogdisplay.R
import com.example.dogdisplay.model.Dog
import com.example.dogdisplay.viewholder.DogListHolder

//显示DogList数据
private const val TAG = "DogListAdapter"
class DogListAdapter : ListAdapter<Dog,DogListHolder>(DogDiffCallback()) {

/*    private val dogList  = mutableListOf<Dog>()
    fun setList(dogList: List<Dog>) {
        this.dogList.addAll(dogList)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.dog_list_item, parent, false)
        return DogListHolder(view)
    }

    override fun onBindViewHolder(holder: DogListHolder, position: Int) {
        Log.d(TAG, "inner onBindViewHolder")
       // val dog = dogList.get(position)
        val dog = getItem(position)
        Log.d(TAG, "dog = $dog")
        holder.bind(dog)
        holder.bindItemClick(onClick)
    }

    private lateinit var onClick: (position: Int) -> Unit

    fun setOnItemClickListener(onClick : (position: Int) -> Unit){
        this.onClick = onClick
    }

/*    override fun getItemCount(): Int {
        return dogList.size
    }*/
}

class DogDiffCallback : DiffUtil.ItemCallback<Dog>() {

    override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
        return oldItem == newItem
    }

}