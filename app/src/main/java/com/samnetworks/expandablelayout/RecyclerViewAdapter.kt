package com.samnetworks.expandablelayout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.samnetworks.expandablelayout.databinding.ExpandableViewBinding
import com.samnetworks.expandablelayout.databinding.HeaderViewBinding
import com.samnetworks.expandablelayout.databinding.RecyclerViewItemBinding

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
    var list:MutableList<Boolean> = mutableListOf()
    lateinit var inflater: LayoutInflater
    var lastExpandedPosition = -1
    init {
        for(i in 0..100)
            list.add(false)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        if(!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RecyclerViewItemBinding>(inflater,R.layout.recycler_view_item,parent,false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int = list.size

    inner class RecyclerViewHolder(private val rootBinding: RecyclerViewItemBinding):RecyclerView.ViewHolder(rootBinding.root),ExpandableInterface{
        var headerBinding:HeaderViewBinding
        var expandableBinding:ExpandableViewBinding
        init {
            val bindings = rootBinding.expandableLayout.initView<HeaderViewBinding,ExpandableViewBinding>(R.layout.header_view,R.layout.expandable_view)
            headerBinding = bindings.first
            expandableBinding = bindings.second
        }
        fun bindData(){
            if(list[adapterPosition]){
                rootBinding.expandableLayout.expand(0)
            }else{
                rootBinding.expandableLayout.collapse(0)
            }
            headerBinding.root.setOnClickListener {
                if(lastExpandedPosition==adapterPosition){
                    rootBinding.expandableLayout.toggle()
                }else{
                    if(lastExpandedPosition!=-1) {
                        val lastExpandedViewHolder =
                            (itemView.parent as RecyclerView).findViewHolderForAdapterPosition(
                                lastExpandedPosition
                            )
                        if (lastExpandedViewHolder is ExpandableInterface) {
                            lastExpandedViewHolder.collapse()
                        }
                        list[lastExpandedPosition] = false
                    }
                    rootBinding.expandableLayout.expand()
                    list[adapterPosition] = true
                }
                lastExpandedPosition = adapterPosition
            }
        }

        override fun expand() {
            rootBinding.expandableLayout.expand()
        }

        override fun collapse() {
            rootBinding.expandableLayout.collapse()
        }
    }
    interface ExpandableInterface{
        fun expand()
        fun collapse()
    }
}