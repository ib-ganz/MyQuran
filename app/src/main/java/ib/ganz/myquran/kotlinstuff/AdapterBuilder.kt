package ib.ganz.myquran.kotlinstuff

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

class AdapterBuilder<T> {
    lateinit var context: Context
    lateinit var list: MutableList<T>
    lateinit var viewHolder: Vh
    lateinit var binder: Vh.() -> Unit
    var layout = 0

    val adapter: RecyclerView.Adapter<Vh> = object : RecyclerView.Adapter<Vh>(), SpecialAdapter<T> {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
            viewHolder = Vh(LayoutInflater.from(context).inflate(layout, parent, false))
            return viewHolder
        }
        override fun getItemCount() = list.size
        override fun onBindViewHolder(holder: Vh, position: Int) = binder(holder)
        override fun getList(): MutableList<T> = list
    }

    fun bind(block: Vh.() -> Unit) {
        binder = block
    }

    class Vh(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        val context = containerView.context!!
        val pos = adapterPosition
        fun itemClick(block: () -> Unit) = containerView click block
    }

    interface SpecialAdapter<T> {
        fun getList(): MutableList<T>
    }
}

inline fun <T> adapter(builder: AdapterBuilder<T>.() -> Unit) = AdapterBuilder<T>().apply(builder).adapter

fun <T> adapter(
    context: Context,
    list: MutableList<T>,
    layout: Int,
    binder: AdapterBuilder.Vh.() -> Unit
) : RecyclerView.Adapter<AdapterBuilder.Vh> {

    return AdapterBuilder<T>().apply {
        this.context = context
        this.list = list
        this.layout = layout
        this.binder = binder
    }.adapter
}