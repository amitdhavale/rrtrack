package asset.trak.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import asset.trak.model.HomeGoalData
import asset.trak.views.adapter.HomeGoalsAdapter
import asset.trak.views.adapter.OnGoalClickListener
import asset.trak.views.baseclasses.BaseFragment
import asset.trak.views.module.InventoryViewModel
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(R.layout.fragment_search), OnGoalClickListener {

    private lateinit var homeGoalsAdapter: HomeGoalsAdapter
    private var listHomeGoalsData = ArrayList<HomeGoalData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listHomeGoalsData = ArrayList()
        listHomeGoalsData.add(HomeGoalData("MY DEVICES", "123"))
        listHomeGoalsData.add(HomeGoalData("MY OTHER ASSETS", "70"))
        listHomeGoalsData.add(HomeGoalData("MY LIBRARY", "5000"))

        homeGoalsAdapter = HomeGoalsAdapter(requireContext(), this, listHomeGoalsData)
        goalsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        goalsRV.itemAnimator = DefaultItemAnimator()
        goalsRV.adapter = homeGoalsAdapter

    }

    override fun onGoalClick(navToScreen: String) {

    }
}