package com.kay.happyplaces.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kay.happyplaces.databinding.RowLayoutBinding
import com.kay.happyplaces.models.HappyPlaceModelEntity

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataList = emptyList<HappyPlaceModelEntity>()

    class MyViewHolder (val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MyViewHolder(RowLayoutBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}