package mx.cbisystems.x24.pagerController

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import mx.cbisystems.x24.FavoriteFragment
import mx.cbisystems.x24.ProfileFragment
import mx.cbisystems.x24.PromosFragment
import mx.cbisystems.x24.StoresFragment

class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> {
                val fragment = FavoriteFragment()
                return fragment
            }
            1 -> {
                val fragment = StoresFragment()
                return fragment
            }
            2 -> {
                val fragment = ProfileFragment()
                return fragment
            }
            3 -> {
                val fragment =  PromosFragment()
                return fragment
            }
            else -> return PromosFragment()
        }
    }
}