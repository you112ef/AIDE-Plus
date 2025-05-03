package io.github.zeroaicy.aide.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aide.ui.rewrite.R


class BreadcrumbView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val recyclerView: RecyclerView = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        overScrollMode = View.OVER_SCROLL_NEVER
    }
    private val items = mutableListOf<BreadcrumbItem>()
    private var adapter: BreadcrumbAdapter? = null

    init {
        addView(recyclerView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

    fun setPath(path: List<BreadcrumbItem>, onClick: (BreadcrumbItem, Int) -> Unit) {
        items.clear()
        items.addAll(path)
        adapter = BreadcrumbAdapter(items, onClick)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(items.size - 1)

    }
}


class BreadcrumbAdapter(
    private val items: List<BreadcrumbItem>,
    private val onClick: (BreadcrumbItem, Int) -> Unit
) : RecyclerView.Adapter<BreadcrumbAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val icon: ImageView = view.findViewById(R.id.icon)
        val text: TextView = view.findViewById(R.id.text)!!
        val divider: TextView = view.findViewById(R.id.divider)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breadcrumb, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.text.text = item.name
        holder.divider.visibility = if (position == items.lastIndex) View.GONE else View.VISIBLE

        holder.itemView.setOnClickListener {
            onClick(item, position)
        }
    }
}


data class BreadcrumbItem(
    val name: String,
    val fullPath: String
)
