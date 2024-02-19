package com.example.imitatejuejin2.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.imitatejuejin2.R

class NavRecyclerView(
    private val itemList: MutableList<String>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<NavRecyclerView.ViewHolder>() {

    private var currentPosition: Int = 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById(R.id.item_nav_head)
        val cursorView: ImageView = view.findViewById(R.id.cursorView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_nav_head, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = itemList[position]
        holder.itemText.text = item
        holder.itemText.setOnClickListener {
            viewPager.currentItem = holder.bindingAdapterPosition
            currentPosition = position
            notifyDataSetChanged()
        }
//        holder.cursorView.visibility =
//            if (currentPosition == position) View.VISIBLE
//            else View.GONE
        if (currentPosition == position) {
            holder.itemText.setBackgroundResource(R.drawable.bg_nav_article_type_item_selected)
            holder.itemText.paint.isFakeBoldText = true
        } else {
            holder.itemText.background = null
            holder.itemText.paint.isFakeBoldText = false
        }
    }

    override fun getItemCount(): Int = itemList.size
}