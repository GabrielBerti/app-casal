package br.com.appcasal.ui.fragment.receitas

import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentReceitaBinding
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaReceitasViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReceitaFragment : Fragment(), ClickReceita {

    private lateinit var binding: FragmentReceitaBinding
    val viewModel: ListaReceitasViewModel by viewModel()

    private var util = Util()
    private var receitas: List<Receita> = emptyList()
    private var searchJob: Job? = null

    private lateinit var decorView: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentReceitaBinding.inflate(inflater, container, false)
        decorView = requireActivity().window.decorView as ViewGroup

        setupListeners()
        viewModel.recuperaReceitas()
        setClickListeners()
        setListeners()
        setupEditTextSearch()
        setupSwipeRefresh()

        return binding.root
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaReceitas()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.receitaGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError {
                    binding.isError = true
                    binding.noResults = false
                }
                onSuccess {
                    receitas = it
                    binding.noResults = receitas.isEmpty() && binding.etSearch.text?.toString() != ""
                    configuraAdapter(it)
                }
            }

            viewModel.receitaDeleteResult.collectResult(this) {
                onError {
                    createSnackBar(
                        TipoSnackbar.ERRO,
                        resources.getString(R.string.erro_deletar, "receita")
                    )
                }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_removida_sucesso)
                    )
                    viewModel.recuperaReceitas()
                   // binding.rvMetas.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun configuraAdapter(receitas: List<Receita>) {
        binding.rvReceitas.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvReceitas.adapter = ListaReceitasAdapter(receitas, requireContext(), this)
    }

    private fun setupEditTextSearch() {
        with(binding.etSearch) {
            doOnTextChanged { text, _, _, _ ->
                binding.etSearch.performClick()
                binding.etSearch.requestFocus()
                if (hasFocus()) searchDebounced(text.toString())
            }

            setOnEditorActionListener { v, _, _ ->
                util.hideKeyboard(this, v.context)
                true
            }
        }
    }

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(1500)
            viewModel.recuperaReceitas(searchText)
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.clListaReceitas, msg, resources, tipoSnackbar)
    }

    override fun clickReceita(receita: Receita) {
        chamaDetalheReceita(receita)
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setListeners() {
        binding.fabAdicionaReceita
            .setOnClickListener {
                abreTelaDeCadastro()
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao: Int = (binding.rvReceitas.adapter as ListaReceitasAdapter).posicao

        when (item.itemId) {
            1 -> {
                abreTelaDeAlteracao(receitas[posicao])
            }

            2 -> {
                viewModel.deletaReceita(receitas[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun abreTelaDeCadastro() {
        findNavController().navigate(
            ReceitaFragmentDirections.actionReceitaFragmentToFormReceitaFragment(null)
        )
    }

    private fun abreTelaDeAlteracao(receita: Receita) {
        findNavController().navigate(
            ReceitaFragmentDirections.actionReceitaFragmentToFormReceitaFragment(receita)
        )
    }

    private fun chamaDetalheReceita(receita: Receita) {
        findNavController().navigate(
            ReceitaFragmentDirections.actionReceitaFragmentToDetalheReceitaFragment(receita)
        )
    }

}