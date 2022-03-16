package asset.trak.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import asset.trak.model.HomeGoalData
import com.markss.rfidtemplate.R

interface OnGoalClickListener {
    fun onGoalClick(navToScreen: String)
}

class HomeGoalsAdapter(
    private val context: Context,
    private val onGoalClickListener: OnGoalClickListener, private val items: ArrayList<HomeGoalData>,
) :
    RecyclerView.Adapter<HomeGoalsAdapter.HomeGoalsHolder>() {
    inner class HomeGoalsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCategory: TextView = view.findViewById(R.id.tvCategory)
        var tvCount: TextView = view.findViewById(R.id.tvCount)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGoalsHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itme_category, parent, false)
        return HomeGoalsHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeGoalsHolder, position: Int) {
        val homeGoalsItem = items[position]
        holder.tvCategory.text = homeGoalsItem.title
        holder.tvCount.text = homeGoalsItem.image
        holder.itemView.setOnClickListener {
            onGoalClickListener.onGoalClick(items[position].title ?:"")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}