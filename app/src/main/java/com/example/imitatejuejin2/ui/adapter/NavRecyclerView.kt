package com.example.imitatejuejin2.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.imitatejuejin2.R
import com.example.imitatejuejin2.ui.fragment.HeadFragment
import com.example.imitatejuejin2.ui.fragment.MineFragment
import java.util.Objects

class NavRecyclerView(
    private val itemList: MutableList<String>,
    private val viewPager: ViewPager2,
    private val fragment: Fragment
) : RecyclerView.Adapter<NavRecyclerView.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById(R.id.item_nav_head)
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
        // 导航栏 item 被点击时，光标跟着变化，ViewPager2 的 item 也跟着变化
        holder.itemText.setOnClickListener {
            viewPager.currentItem = holder.bindingAdapterPosition
            if (fragment is HeadFragment) {
                HeadFragment.setCurrentPosition(position)
            } else {
                MineFragment.setCurrentPosition(position)
            }
            notifyDataSetChanged()
        }
        // 设置光标和背景的变化
        val currentPosition: Int
        if (fragment is HeadFragment) {
            currentPosition = HeadFragment.getCurrentPosition()
        } else {
            currentPosition = MineFragment.getCurrentPosition()
        }
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