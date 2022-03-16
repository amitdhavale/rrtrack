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

interface OnAssetsClickListener {
    fun onGoalClick(position: Int)
}

class AssetsAdapter(
    private val context: Context,
    private val onGoalClickListener: OnAssetsClickListener, private val items: ArrayList<HomeGoalData>,
) :
    RecyclerView.Adapter<AssetsAdapter.HomeGoalsHolder>() {
    inner class HomeGoalsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCategory: TextView = view.findViewById(R.id.tvCategory)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGoalsHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itme_assests, parent, false)
        return HomeGoalsHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeGoalsHolder, position: Int) {
        val homeGoalsItem = items[position]
        holder.tvCategory.text = homeGoalsItem.title

        holder.itemView.setOnClickListener {
            onGoalClickListener.onGoalClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}