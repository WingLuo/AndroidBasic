package tech.bootloader.androidbasic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, private var fragments: List<Fragment>) :
    FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(index: Int): Fragment {
        return fragments[index]
    }

    override fun getCount(): Int {
        return fragments.size
    }

}