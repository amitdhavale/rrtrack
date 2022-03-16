package asset.trak.views.adapter

import androidx.fragment.app.Fragment
import asset.trak.views.inventory.NotFoundFragment
import asset.trak.views.inventory.DifferentLoactionFragment
import asset.trak.views.inventory.NotRegisteredFragment
import asset.trak.views.inventory.ReconcileAssetsFragment
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import asset.trak.database.entity.AssetCatalogue


class ReconcileAssetsPagerAdapter (private val fm: ReconcileAssetsFragment) : FragmentStatePagerAdapter(fm.parentFragmentManager) {


    private var mCurrentFragment: Fragment? = null

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> NotFoundFragment(fm.locationId)
            1 -> DifferentLoactionFragment(fm.locationId)
            2 -> NotRegisteredFragment(fm.locationId)
            else -> NotFoundFragment(fm.locationId)
        }
    }


    fun getCurrentFragment(): Fragment? {

        return mCurrentFragment
    }


   override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (getCurrentFragment() !== `object`) {
            mCurrentFragment = `object` as Fragment
        }
        super.setPrimaryItem(container!!, position, `object`)
    }



    override fun getCount(): Int {
        return 3
    }


    
}