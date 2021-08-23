package com.yeseul.part3.chapter07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HouseViewPagerAdapter( val itemClicked : (HouseModel) -> Unit): ListAdapter<HouseModel, HouseViewPagerAdapter.ItemViewHolder>(differ) {

    inner class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(houseModel: HouseModel) {
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)

            titleTextView.text = houseModel.title
            priceTextView.text = houseModel.price

            view.setOnClickListener {
                itemClicked(houseModel)
            }

            Glide.with(thumbnailImageView.context)
                .load(houseModel.imgUrl)
                .into(thumbnailImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_house_detail, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<HouseModel>() {
            override fun areItemsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {
                return oldItem == newItem
            }

        }
    }

}