package asset.trak.views.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.entity.AssetClassification
import asset.trak.views.listener.OnAssetReconiClickListener
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application


class AssetReconillationAdapter(
    private val mContext: Context, private val onAssetReconiClickListener: OnAssetReconiClickListener,
    private val items: ArrayList<AssetClassification>
) :
    RecyclerView.Adapter<AssetReconillationAdapter.CategoryHolder>() {
    inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        var rlParentLayout: RelativeLayout = view.findViewById(R.id.rlParentLayout)
        var ivDeviceImage: AppCompatImageView = view.findViewById(R.id.ivDeviceImage)
        var tvLocationName: AppCompatTextView = view.findViewById(R.id.tvLocationName)
        var tvRegisteredCount: AppCompatTextView = view.findViewById(R.id.tvRegisteredCount)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asset_reconcillation, parent, false)
        return CategoryHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val assetClassificationData = items[position]



        if (position == 0) {
            holder.tvLocationName.text = mContext.getString(R.string.lblMyDevices)

            holder.ivDeviceImage.setImageResource(R.drawable.ic_my_device)
            ViewCompat.setBackgroundTintList(
                holder.rlParentLayout,
                ColorStateList.valueOf(mContext.getColor(R.color.light_orange))
            )

        } else if (position == 1) {
            holder.tvLocationName.text = mContext.getString(R.string.lblMyOtherAssets)

            holder.ivDeviceImage.setImageResource(R.drawable.ic_other_assets)
            ViewCompat.setBackgroundTintList(
                holder.rlParentLayout,
                ColorStateList.valueOf(mContext.getColor(R.color.light_green))
            )

        } else if (position == 2) {
            holder.tvRegisteredCount.text = Application.roomDatabaseBuilder?.getBookDao()?.getCount().toString()
            holder.tvLocationName.text = mContext.getString(R.string.lblMyLibrary)

            holder.ivDeviceImage.setImageResource(R.drawable.ic_library)
            ViewCompat.setBackgroundTintList(
                holder.rlParentLayout,
                ColorStateList.valueOf(mContext.getColor(R.color.lightt_blue))
            )
        }


        holder.itemView.setOnClickListener {
            onAssetReconiClickListener.onItemClick(assetClassificationData)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}