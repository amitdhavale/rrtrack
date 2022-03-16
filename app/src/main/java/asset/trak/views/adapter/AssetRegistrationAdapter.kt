package asset.trak.views.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.entity.AssetClassification
import asset.trak.views.listener.OnAssetRegisClickListener
import com.google.android.material.card.MaterialCardView
import com.markss.rfidtemplate.R


class AssetRegistrationAdapter(
    private val mContext: Context, private val onAssetRegisClickListener: OnAssetRegisClickListener,
    private val items: ArrayList<AssetClassification>
) :
    RecyclerView.Adapter<AssetRegistrationAdapter.CategoryHolder>() {
    inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llParentLayout: LinearLayout = view.findViewById(R.id.llParentLayout)
        var ivDeviceImage: AppCompatImageView = view.findViewById(R.id.ivDeviceImage)
        var tvLocationName: AppCompatTextView = view.findViewById(R.id.tvLocationName)
        var tvCount: AppCompatTextView = view.findViewById(R.id.tvCount)
        var laysectionweightgraph:MaterialCardView = view.findViewById(R.id.section_weight_graph)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asset_registration, parent, false)
        return CategoryHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val assetClassificationData = items[position]


        holder.tvLocationName.text = assetClassificationData.className
        holder.tvCount.text = assetClassificationData.AssetCount.toString()


        if(assetClassificationData.AssetCount==0)
        {
            holder.laysectionweightgraph.visibility=View.GONE
        }
        else{
            holder.laysectionweightgraph.visibility=View.VISIBLE
        }
        if (position == 0) {
            holder.ivDeviceImage.setImageResource(R.drawable.ic_library)
            ViewCompat.setBackgroundTintList(
                holder.llParentLayout,
                ColorStateList.valueOf(mContext.getColor(R.color.light_green))
            )


        } else if (position == 1) {
            holder.ivDeviceImage.setImageResource(R.drawable.ic_other_assets)
            ViewCompat.setBackgroundTintList(
                holder.llParentLayout,
                ColorStateList.valueOf(mContext.getColor(R.color.light_green))
            )

        } else if (position == 2) {
            holder.ivDeviceImage.setImageResource(R.drawable.ic_my_device)
            ViewCompat.setBackgroundTintList(
                holder.llParentLayout,
                ColorStateList.valueOf(mContext.getColor(R.color.light_orange))
            )
        }


        holder.itemView.setOnClickListener {
            onAssetRegisClickListener.onItemClick(assetClassificationData)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}