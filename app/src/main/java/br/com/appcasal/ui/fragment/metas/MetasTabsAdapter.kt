package br.com.appcasal.ui.fragment.metas

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MetasTabsAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val metasNaoConcluidasFragment: MetaListFragment,
    private val metasConcluidasFragment: MetaListFragment
) : FragmentStateAdapter(fm, lifecycle) {
    private val revenuePosition = 0

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == revenuePosition) metasNaoConcluidasFragment else metasConcluidasFragment
    }
}
