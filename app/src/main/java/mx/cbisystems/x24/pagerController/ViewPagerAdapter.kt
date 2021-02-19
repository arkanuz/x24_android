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
                return FavoriteFragment()
            }
            1 -> {
                return StoresFragment()
            }
            2 -> {
                return ProfileFragment()
            }
            3 -> {
                return PromosFragment()
            }
            else -> return PromosFragment()
        }
    }
}