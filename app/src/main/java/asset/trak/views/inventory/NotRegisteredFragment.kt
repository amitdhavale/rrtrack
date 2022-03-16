package asset.trak.views.inventory

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.database.entity.Inventorymaster
import asset.trak.database.entity.ScanTag
import asset.trak.database.entity.Selection
import asset.trak.views.adapter.NotFoundAdapter
import asset.trak.views.adapter.NotRegsiteredAdapter
import asset.trak.views.baseclasses.BaseFragment
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.application.Application.bookDao
import com.markss.rfidtemplate.application.Application.roomDatabaseBuilder
import com.markss.rfidtemplate.common.ResponseHandlerInterfaces
import com.markss.rfidtemplate.inventory.InventoryListItem
import com.zebra.rfid.api3.RFIDResults
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_not_registered.*

@AndroidEntryPoint
class NotRegisteredFragment(val locationId: Int) : BaseFragment(R.layout.fragment_not_registered) {
    private lateinit var notFoundAdapter: NotRegsiteredAdapter


     var rfidTags = ArrayList<ScanTag>()
    override fun onResume() {
        super.onResume()
        setAdaptor()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdaptor()
        tvSelectAll.setOnClickListener {
            if(tvSelectAll.getTag().equals("0"))
            {
                if(!rfidTags.isEmpty())
                {
                    tvSelectAll.visibility=View.VISIBLE
                    tvSelectAll.setTag("1")
                    tvSelectAll.text=getString(R.string.deselect_all)
                    for (i in 0 until rfidTags.size)  {
                        rfidTags[i].isSelected=true
                    }
                    notFoundAdapter = NotRegsiteredAdapter(requireContext(),requireActivity().supportFragmentManager, rfidTags)
                    rvRegistered.adapter = notFoundAdapter
                    notFoundAdapter?.notifyDataSetChanged()
                }
                else
                {
                    tvSelectAll.visibility=View.GONE
                }
            }
            else
            {
                tvSelectAll.setTag("0")
                tvSelectAll.text=getString(R.string.select_all)
                for (i in 0 until rfidTags.size)  {
                    rfidTags[i].isSelected=false
                }
                notFoundAdapter = NotRegsiteredAdapter(requireContext(),requireActivity().supportFragmentManager, rfidTags)
                rvRegistered.adapter = notFoundAdapter
                notFoundAdapter?.notifyDataSetChanged()
            }
        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notFoundAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setAdaptor() {
        rfidTags.clear()
        val inventoryMasterList = bookDao.getPendingInventoryScan(locationId)
        if(inventoryMasterList.isEmpty() || inventoryMasterList==null)
        {

        }
        else{
            val inventorymaster = inventoryMasterList.get(0)

            rfidTags.addAll(bookDao?.getAssetNotRegistered(inventorymaster.scanID) ?: arrayListOf())
            if(rfidTags.isEmpty())
            {
                tvSelectAll.visibility=View.GONE
            }

            notFoundAdapter = NotRegsiteredAdapter(requireContext(),requireActivity().supportFragmentManager, rfidTags)
            rvRegistered.adapter = notFoundAdapter
            notFoundAdapter?.notifyDataSetChanged()
        }



    }

    fun updateList(){
        setAdaptor()
    }

}