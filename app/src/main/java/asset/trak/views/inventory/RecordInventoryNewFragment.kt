package asset.trak.views.inventory

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import asset.trak.database.entity.LocationMaster
import asset.trak.model.AssetSyncRequestDataModel
import asset.trak.views.baseclasses.BaseFragment
import asset.trak.views.module.InventoryViewModel
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.common.ResponseHandlerInterfaces.*
import com.markss.rfidtemplate.inventory.InventoryFragment
import kotlinx.android.synthetic.main.fragment_record_inventory_new.*
import kotlinx.android.synthetic.main.fragment_record_inventory_new.ivBackButton
import kotlinx.android.synthetic.main.fragment_record_inventory_new.spinnerLocation
import kotlinx.android.synthetic.main.fragment_record_inventory_new.tvRegisteredCount
import kotlinx.android.synthetic.main.fragment_view_inventory.*


class RecordInventoryNewFragment : BaseFragment(R.layout.fragment_record_inventory_new){

    private var listOfLocations= ArrayList<LocationMaster>()
    private val inventoryViewModel: InventoryViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialisation()
        setAdaptor()
        listeners()

    }
    private fun initialisation() {
        listOfLocations.clear()
        listOfLocations.addAll(Application.roomDatabaseBuilder.getBookDao().getLocationMasterList())
    }

    private fun setAdaptor() {
        val listOfItems=ArrayList<String>()
        listOfLocations.forEach {
            listOfItems.add(it.locationName ?:"")
        }

        val spinnerArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOfItems)
        spinnerLocation.adapter = spinnerArrayAdapter

    }
    private fun listeners() {
        ivBackButton.setOnClickListener {
            getBackToPreviousFragment()
        }


        btnReconcile.setOnClickListener {
            replaceFragment(
                requireActivity().supportFragmentManager, InventoryFragment(),
                R.id.content_frame
            )
        }

        spinnerLocation.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }

    }

}