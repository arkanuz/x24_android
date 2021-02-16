package mx.cbisystems.x24

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

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