package asset.trak.views.adapter
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.entity.Selection
import com.markss.rfidtemplate.R

import java.util.*




class SearchTagAdapter(
    context: Context,
    infoList: List<Selection>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext = context
    private var mInfoList = infoList
    private var locationPosition: Int = 0
    private var tag: String? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(mContext)
                .inflate(R.layout.item_search_tag, parent, false)

        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is DataViewHolder) {
            val mData = mInfoList.get(position)
            holder.headerMaster.text = mData.rfidTags?.toLowerCase(Locale.getDefault())

            holder.cardBlock.setOnClickListener {
                locationPosition=position
                tag=mData.rfidTags
                notifyDataSetChanged()

            }


            if(locationPosition==position){
                holder.cardBlock.setCardBackgroundColor(Color.parseColor("#567845"));
                holder.headerMaster.setTextColor(Color.parseColor("#ffffff"));
            }
            else
            {
                holder.cardBlock.setCardBackgroundColor(Color.parseColor("#ffffff"));
                holder.headerMaster.setTextColor(Color.parseColor("#000000"));
            }



        }
    }


    override fun getItemCount(): Int {
        return mInfoList.size
    }

    private class DataViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val headerMaster: TextView = view.findViewById(R.id.connectTV)
        val mainLayout: ConstraintLayout = view.findViewById(R.id.app_bar_category_layout)
        val cardBlock: CardView = view.findViewById(R.id.cardBlock)

    }

    public fun getTagValue():String
    {
        return tag?:""
    }


}