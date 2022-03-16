package asset.trak.views.adapter




import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.entity.SubCategoryMaster
import asset.trak.views.fragments.SubCategoryFragment
import com.markss.rfidtemplate.R


class SubCategoryAdapter(
    private val fragment: Fragment,
    private var items: ArrayList<SubCategoryMaster>
) :
    RecyclerView.Adapter<SubCategoryAdapter.CategoryViewHolder>() , Filterable {

    private var mFilteredList: List<SubCategoryMaster>?=null

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCategory: TextView = view.findViewById(R.id.tvCategory)
        var tvCount: TextView = view.findViewById(R.id.tvCount)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itme_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvCategory.text = item.subCategoryName
        holder.tvCount.text = item.SubCategoryAssetCount.toString()

        holder.itemView.setOnClickListener {

            if (fragment is SubCategoryFragment)
                fragment.onSubCategoryClick(item)
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }



    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<SubCategoryMaster>()

                if (mFilteredList == null)
                    mFilteredList = items

                if (constraint != null) {
                    if (mFilteredList != null && mFilteredList!!.size > 0) {
                        for (mFilterData in mFilteredList!!) {
                            if (mFilterData.subCategoryName!!.toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(mFilterData)
                        }
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
                items = (results.values as ArrayList<SubCategoryMaster>)
                notifyDataSetChanged()
            }
        }
    }

}