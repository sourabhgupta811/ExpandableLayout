package com.samnetworks.expandablelayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.samnetworks.expandable_layout.base.ExpandableLayout
import com.samnetworks.expandablelayout.databinding.ActivityMainBinding
import com.samnetworks.expandablelayout.databinding.ExpandableViewBinding
import com.samnetworks.expandablelayout.databinding.HeaderViewBinding

class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RecyclerViewAdapter()
    }
}