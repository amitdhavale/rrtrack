package asset.trak.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.database.entity.ScanTag
import asset.trak.database.entity.Selection
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.home.MainActivity
import com.markss.rfidtemplate.locate_tag.LocateOperationsFragment
import com.markss.rfidtemplate.rfid.RFIDController


class NotRegsiteredAdapter(private val context: Context,private val fragment: FragmentManager, private var items: ArrayList<ScanTag> ) :
    RecyclerView.Adapter<NotRegsiteredAdapter.NotFoundHolder>(), Filterable {
    private var mFilteredList: List<ScanTag>? = null

    inner class NotFoundHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var clMain: ConstraintLayout = view.findViewById(R.id.clMain)
        var tvSearch: TextView = view.findViewById(R.id.tvSearch)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotFoundHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_not_registered, parent, false)
        return NotFoundHolder(itemView)
    }
    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<ScanTag>()


                if (mFilteredList == null)
                    mFilteredList = items
                if (constraint != null) {
                    if (mFilteredList != null && mFilteredList!!.isNotEmpty()) {
                        for (mFilterData in mFilteredList!!) {

                            if (mFilterData.rfidTag?.lowercase()?.contains(constraint.toString().lowercase())==true)

                                results.add(mFilterData)                            }
                    }
                    oReturn.count = results.size
                    oReturn.values = results
                } else {
                    oReturn.count = items.size
                    oReturn.values = items
                }
                return oReturn
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                items = (results.values as ArrayList<ScanTag>)
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: NotFoundHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.rfidTag
 holder.clMain.setBackgroundResource(if (item.isSelected) R.drawable.rectangle_background_light_blue else R.drawable.rectangle_background_border)


        holder.tvSearch.setOnClickListener{

            Application.locateTag = item.rfidTag

            Application.comefrom ="hide"
            // holder.tvSearch.visibility=View.GONE

            replaceFragment(
                fragment, LocateOperationsFragment(),
                R.id.content_frame
            )

        }



        holder.itemView.setOnClickListener {
            Application.locateTag = item.rfidTag
    items[position].isSelected=!items[position].isSelected
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    fun replaceFragment(fragmentManager: FragmentManager?, fragment: Fragment, id: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(id, fragment, MainActivity.TAG_CONTENT_FRAGMENT)?.addToBackStack(null)?.commit()
    }
}