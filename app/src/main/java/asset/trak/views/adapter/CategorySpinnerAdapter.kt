package asset.trak.views.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import asset.trak.database.entity.CategoryMaster
import com.markss.rfidtemplate.R

class CategorySpinnerAdapter(private val context: Activity, private var categoryMaster: List<CategoryMaster>)
    : ArrayAdapter<CategoryMaster>(context, R.layout.item_spinner, categoryMaster) {
  
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater  
        val rowView = inflater.inflate(R.layout.item_spinner, null, true)
  
        val titleText = rowView.findViewById(R.id.title) as TextView

        titleText.text = categoryMaster[position].categoryName

        return rowView  
    }  
}  