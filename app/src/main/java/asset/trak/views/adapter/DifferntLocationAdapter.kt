package asset.trak.views.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.modelsrrtrack.AssetMain
import com.bumptech.glide.Glide

import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.home.MainActivity
import com.markss.rfidtemplate.locate_tag.LocateOperationsFragment
import com.markss.rfidtemplate.rfid.RFIDController
import java.io.File


class DifferntLocationAdapter(private val context: Context, private val fragment: FragmentManager,
                              private var items: ArrayList<AssetMain>) :
    RecyclerView.Adapter<DifferntLocationAdapter.NotFoundHolder>(),Filterable {
    private var mFilteredList: List<AssetMain>? = null

    inner class NotFoundHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvAuthor: TextView = view.findViewById(R.id.tvAuthor)

        var ivCheck: ImageView = view.findViewById(R.id.ivCheck)
        var clMain: ConstraintLayout = view.findViewById(R.id.clMain)
        var tvCategory: TextView = view.findViewById(R.id.tvCategory)

        var tvSearch: TextView = view.findViewById(R.id.tvSearch)
        var tv: TextView =view.findViewById(R.id.tv)
        var ivBook: AppCompatImageView =view.findViewById(R.id.ivBook)
        var tvEdition: TextView =view.findViewById(R.id.tvTag)



    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotFoundHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itme_not_found, parent, false)
        return NotFoundHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotFoundHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.Supplier


        if( item.SampleType.isNullOrEmpty())
        {
            holder.tvAuthor.text = "-"
        }
        else
        {
            holder.tvAuthor.text = item.SampleType
        }
        holder.tvCategory.text = item.SampleNature+" | "+item.Season
        holder.tvEdition.text = item.Location+" - "+item.Class
//        if( item.bookAttributes?.author.isNullOrEmpty())
//        {
//            holder.tvAuthor.text = "-"
//        }
//        else
//        {
//            holder.tvAuthor.text = item.bookAttributes?.author
//        }
//
//        holder.tvCategory.text = item.assetCatalogue.categoryName
//
//        if(item.assetCatalogue?.locationName.equals("") || item.assetCatalogue?.locationName==null)
//        {
//            holder.tvEdition.text = "${context.getString(R.string.edition)}: -"
//        }
//        else
//        {
//            holder.tvEdition.text = "${context.getString(R.string.edition)} ${item.assetCatalogue.locationName}"
//        }

        holder.tvSearch.visibility=View.VISIBLE
        holder.tvSearch.setOnClickListener{

            Application.locateTag = item.AssetRFID
            RFIDController.accessControlTag = item.AssetRFID
            Application.PreFilterTag = item.AssetRFID
            Application.comefrom ="show"

            replaceFragment(
                fragment, LocateOperationsFragment(),
                R.id.content_frame
            )

        }


//        if(item.assetCatalogue.imagePathFile?.isNotEmpty()==true)
//        {
        //    holder.ivBook.visibility=View.VISIBLE
            holder.tv.visibility=View.GONE
//            Glide.with(context)
//                .load(File(item.assetCatalogue.imagePathFile.toString()))
//                .placeholder(R.color.light_gray)
//                .fitCenter()
//                .error(R.drawable.ic_not_found_error)
//                .into(holder.ivBook)
//        }
//        else{
//            holder.ivBook.visibility=View.GONE
//            holder.tv.visibility=View.VISIBLE
//            holder.tv.text = item.assetCatalogue?.assetName?.substring(0,2)?.toUpperCase()
//
//        }





//        if (item.assetCatalogue.isSelected) {
//            holder.ivCheck.visibility = View.VISIBLE
//
//        }
//        else {
//            holder.ivCheck.visibility = View.GONE
//
//        }

        holder.clMain.setBackgroundResource(if (item.isSelected) R.drawable.rectangle_background_light_blue else R.drawable.rectangle_background_border)
        holder.clMain.setOnClickListener {
         items[position].isSelected=!items[position].isSelected
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int {
        return if (items == null) 0 else items.size

    }



    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<AssetMain>()


                if (mFilteredList == null)
                    mFilteredList = items
                if (constraint != null) {
                    if (mFilteredList != null && mFilteredList!!.isNotEmpty()) {
                        for (mFilterData in mFilteredList!!) {

                            if (mFilterData.Supplier?.lowercase()?.contains(constraint.toString().lowercase())==true
                                || mFilterData.Location?.lowercase()?.contains(constraint.toString().lowercase())==true)
//                                mFilterData.assetCatalogue.rfidTag?.lowercase()?.contains(constraint.toString().lowercase())==true||
//                                mFilterData.assetCatalogue.categoryName?.lowercase()?.contains(constraint.toString().lowercase())==true||
//                                mFilterData.assetCatalogue.subCategoryName?.lowercase()?.contains(constraint.toString().lowercase())==true||
//                                mFilterData.assetCatalogue.locationName?.lowercase()?.contains(constraint.toString().lowercase())==true||
//                                mFilterData.assetCatalogue.searchTags?.lowercase()?.contains(constraint.toString().lowercase())==true||
//                                mFilterData.bookAttributes?.publisher?.lowercase()?.contains(constraint.toString().lowercase())==true)

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
                items = (results.values as ArrayList<AssetMain>)
                notifyDataSetChanged()
            }
        }
    }
    fun replaceFragment(fragmentManager: FragmentManager?, fragment: Fragment, id: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(id, fragment, MainActivity.TAG_CONTENT_FRAGMENT)?.addToBackStack(null)?.commit()
    }
}