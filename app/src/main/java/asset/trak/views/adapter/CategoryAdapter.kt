package asset.trak.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import asset.trak.model.CategoryDataModel
import asset.trak.views.listener.OnCategoryClickListener
import com.markss.rfidtemplate.R


class CategoryAdapter(
    private val context: Context,
    private val onCategoryClickListener: OnCategoryClickListener, private val items: ArrayList<CategoryDataModel>,
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivCategoryImage: AppCompatImageView = view.findViewById(R.id.ivCategoryImage)
        var tvCategory: AppCompatTextView = view.findViewById(R.id.tvCategory)
        var tvCount: AppCompatTextView = view.findViewById(R.id.tvCount)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itme_category, parent, false)
        return CategoryHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val categoryData = items[position]

        holder.tvCategory.text = categoryData.category
        holder.tvCount.text = categoryData.count.toString()
        holder.ivCategoryImage.setImageResource(R.drawable.book)

        holder.itemView.setOnClickListener {
            onCategoryClickListener.onItemClick(categoryData)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}