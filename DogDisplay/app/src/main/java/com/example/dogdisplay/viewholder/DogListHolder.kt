package com.example.dogdisplay.viewholder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogdisplay.*
import com.example.dogdisplay.model.Dog

private const val TAG = "DogListHolder"
class DogListHolder(view: View) : RecyclerView.ViewHolder(view){
    lateinit var dog:Dog

    private val image: ImageView = itemView.findViewById(R.id.dogImageView)

    private val dogNameTextView: TextView = itemView.findViewById(R.id.dogNameTextView)

    // 把数据和视图的绑定工作都放在Holder里处理
    fun bind(dog: Dog) {
        this.dog = dog
        val imageIdentity =
            GlobalApp.context.resources.getIdentifier(
                this.dog.avatarFilename, "drawable",
                GlobalApp.context.packageName
            )
        image.setImageResource(imageIdentity)
        dogNameTextView.text = this.dog.name + "  " +
                if (this.dog.adopted){
                    "Adopted"
                }else{
                    "Not Adopted"
                }
    }


    fun bindItemClick(onClick : (position: Int) -> Unit) {
        Log.d(TAG,"absoluteAdapterPosition = $absoluteAdapterPosition")
        //将 absoluteAdapterPosition 数据回传
        //设置 itemView 监听
        itemView.setOnClickListener{
            onClick(absoluteAdapterPosition)
        }
    }
}
