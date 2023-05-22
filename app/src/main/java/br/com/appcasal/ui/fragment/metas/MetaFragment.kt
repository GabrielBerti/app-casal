package br.com.appcasal.ui.fragment.metas

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentMetaBinding
import br.com.appcasal.domain.model.Meta
import br.com.appcasal.util.Util
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MetaFragment : Fragment() {
    private lateinit var binding: FragmentMetaBinding
    private var util = Util()
    private var searchJob: Job? = null
    private lateinit var decorView: ViewGroup

    private var teveAlteracao = false
    private var trocouDeAbaComSearchPreenchido = false
    private var trocouDeAba = false
    var positionTab = 0

    private val metasConcluidasFragment: MetaListFragment by lazy {
        MetaListFragment.createInstance(
            concluidas = true
        )
    }
    private val metasNaoConcluidasFragment: MetaListFragment by lazy {
        MetaListFragment.createInstance(
            concluidas = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentMetaBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup

        setupEditTextSearch()
        setupViewPager()
        setClickListeners()
        setupTab()

        return binding.root
    }

    private fun setupViewPager() {
        val tabsAdapter = MetasTabsAdapter(
            childFragmentManager,
            lifecycle,
            metasNaoConcluidasFragment,
            metasConcluidasFragment

        )

        val viewPager = binding.vpCategoriesStatement
        viewPager.adapter = tabsAdapter
        val tabs = binding.tabs
        setupTabsLayout(tabs, viewPager)
    }

    private fun setupTabsLayout(
        tabs: TabLayout,
        viewPager: ViewPager2
    ) {
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            val naoConcluidas = position == 0
            tab.text =
                getString(if (naoConcluidas) R.string.meta_nao_concluida else R.string.metas_concluidas)
            tab.icon = ContextCompat.getDrawable(
                requireContext(),
                if (naoConcluidas) R.drawable.ic_alvo else R.drawable.ic_check
            )
        }.attach()
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            trocouDeAba = false
            delay(1000)
            if (!trocouDeAba) {
                if (positionTab == 0) {
                    metasNaoConcluidasFragment.viewModel.recuperaMetas(false, searchText)
                } else {
                    metasConcluidasFragment.viewModel.recuperaMetas(true, searchText)
                }
            }
        }
    }

    private fun setupEditTextSearch() {
        with(binding.etSearch) {
            doOnTextChanged { text, _, _, _ ->
                if (hasFocus() && !trocouDeAbaComSearchPreenchido) searchDebounced(text.toString())
                if (trocouDeAbaComSearchPreenchido) trocouDeAbaComSearchPreenchido = false
            }

            setOnEditorActionListener { v, _, _ ->
                util.hideKeyboard(this, v.context)
                true
            }
        }
    }

    private fun setupTab() {
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                positionTab = tab.position
                refazRequestCasoTiverAlteracao()
                configuraCampoSearchAoTrocarAba()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                positionTab = tab?.position ?: 0
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                positionTab = tab?.position ?: 0
            }
        })
    }

    private fun configuraCampoSearchAoTrocarAba() {
        with(binding.etSearch) {
            if (text.toString() != "") {
                if (positionTab == 0) {
                    metasConcluidasFragment.viewModel.recuperaMetas(true)
                } else {
                    metasNaoConcluidasFragment.viewModel.recuperaMetas(false)
                }

                trocouDeAbaComSearchPreenchido = true
                setText("")
            }
            trocouDeAba = true
            clearFocus()
            util.hideKeyboard(this, requireContext())
        }
    }

    private fun refazRequestCasoTiverAlteracao() {
        if (teveAlteracao) {
            if (positionTab == 0) {
                metasNaoConcluidasFragment.viewModel.recuperaMetas(false)
            } else {
                if (metasConcluidasFragment.metas != emptyList<Meta>()) {
                    metasConcluidasFragment.viewModel.recuperaMetas(true)
                }
            }

            teveAlteracao = false
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val fragment = if (positionTab == 0) metasNaoConcluidasFragment else metasConcluidasFragment

        if (fragment.binding.rvMetas.adapter != null) {
            val posicao: Int = (fragment.binding.rvMetas.adapter as ListaMetasAdapter).posicao

            when (item.itemId) {
                1 -> {
                    fragment.metas[posicao].concluido = !fragment.metas[posicao].concluido
                    fragment.viewModel.alteraMeta(fragment.metas[posicao])
                    teveAlteracao = true
                }

                2 -> {
                    fragment.viewModel.deletaMeta(fragment.metas[posicao])
                }
            }
        }

        return super.onContextItemSelected(item)
    }
}