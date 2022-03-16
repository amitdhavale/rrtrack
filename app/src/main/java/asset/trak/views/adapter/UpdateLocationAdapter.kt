package asset.trak.views.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.model.LocationUpdate
import asset.trak.utils.inter.UpdateItemInterface
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.locate_tag.LocateOperationsFragment
import com.markss.rfidtemplate.rfid.RFIDController
import java.io.File


class UpdateLocationAdapter(private val context: Context, private val fragment: FragmentManager,
                            private var items: ArrayList<LocationUpdate>, updateItemInterface: UpdateItemInterface,   private var loginErrorDialog:Dialog?
) :
           RecyclerView.Adapter<UpdateLocationAdapter.NotFoundHolder>() {
        private var mFilteredList: List<String>? = null
    var selectedPosition = -1
    private var updateItemInterface = updateItemInterface


    inner class NotFoundHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tvTitle: TextView = view.findViewById(R.id.tvTitle)


            var ivCheck: ImageView = view.findViewById(R.id.ivCheck)
            var clMain: ConstraintLayout = view.findViewById(R.id.clMain)


        }

        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotFoundHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.update_location_item, parent, false)
            return NotFoundHolder(itemView)
        }

        override fun onBindViewHolder(holder: NotFoundHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            //holder.tvTitle.text = item.title
           // holder.clMain.setBackgroundResource(if (item.isSelected) R.color.lightt_blue else R.drawable.rectangle_background)
            if (selectedPosition == position)
                holder.ivCheck.visibility = View.VISIBLE
            else
                holder.ivCheck.visibility = View.GONE



            holder.clMain.setOnClickListener {

                if (item != null) {
                    updateItemInterface.onUpdateItemCallback(item)
                }
                selectedPosition = position;
                notifyDataSetChanged();


            }
        }

        override fun getItemCount(): Int {
            return if (items == null) 0 else items.size

        }

    fun updateReceiptsList(newlist: List<LocationUpdate>) {
        items.clear()
        items.addAll(newlist)
        notifyDataSetChanged()
    }

}
