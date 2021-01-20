package tech.bootloader.androidbasic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


/**
 *  adapter  模版代码
 */
class SimpleEmptyAdapter(
    private val datas: MutableList<Any>,
    @LayoutRes private val resource: Int
) : RecyclerView.Adapter<SimpleEmptyAdapter.ViewHolder>() {


    private lateinit var onClickListener: (view: View, index: Int) -> Unit
    private lateinit var onLongClickListener: (view: View, index: Int) -> Unit
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
//        val binding = ItemHomeFunctionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val view= binding.root
        val view = LayoutInflater.from(context).inflate(this.resource, parent, false)
        val holder = ViewHolder(view)

        if (this::onClickListener.isInitialized) {
            holder.view.setOnClickListener { v ->
                val position: Int = holder.adapterPosition
                onClickListener(v, position)
            }
        }

        if (this::onLongClickListener.isInitialized) {
            holder.view.setOnLongClickListener { v ->
                val position: Int = holder.adapterPosition
                onLongClickListener(v, position)
                return@setOnLongClickListener true
            }
        }

        return holder
    }

    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = datas[position]
        holder.bind(entity)

    }


    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(entity: Any) {

        }

    }

    fun setOnItemClickListener(body: (view: View, index: Int) -> Unit) {
        this.onClickListener = body
    }

    fun setOnItemLongClickListener(body: (view: View, index: Int) -> Unit) {
        this.onLongClickListener = body
    }


}