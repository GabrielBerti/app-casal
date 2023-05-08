package br.com.appcasal.ui.fragment.viagens

import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentViagemBinding
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.viagens.AdicionaViagemDialog
import br.com.appcasal.ui.dialog.viagens.AlteraViagemDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaViagensViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViagemFragment : Fragment(), ClickViagem {

    private lateinit var binding: FragmentViagemBinding
    val viewModel: ListaViagensViewModel by viewModel()
    private var viagens: List<Viagem> = emptyList()
    private var searchJob: Job? = null
    private var util = Util()
    private lateinit var decorView: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentViagemBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup

        setupListeners()
        setClickListeners()
        setListeners()
        setupSwipeRefresh()

        return binding.root
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaViagens()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.viagemGetResult.collectViewState(this) {
                onStarted {
                    viewModel.recuperaViagens()
                    setupEditTextSearch()
                }
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError {
                    binding.isError = true
                    binding.noResults = false
                }
                onSuccess {
                    viagens = it
                    binding.noResults = viagens.isEmpty() && binding.etSearch.text?.toString() != ""
                    configuraAdapter(it)
                    focarCampoDeBusca()
                }
            }

            viewModel.viagemInsertResult.collectResult(this) {
                onError { handleError(R.string.erro_inserir) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llListaViagens)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.viagem_inserida_sucesso, it.local)
                    )
                    viewModel.recuperaViagens()
                }
            }

            viewModel.viagemUpdateResult.collectResult(this) {
                onError { handleError(R.string.erro_alterar) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llListaViagens)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.viagem_alterada_sucesso, it.local)
                    )
                    viewModel.recuperaViagens()
                }
            }

            viewModel.viagemDeleteResult.collectResult(this) {
                onError { handleError(R.string.erro_deletar) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.viagem_removida_sucesso)
                    )
                    viewModel.recuperaViagens()
                    binding.rvViagens.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun focarCampoDeBusca() {
        if(binding.etSearch.text.toString() != "") {
            binding.etSearch.performClick()
            binding.etSearch.requestFocus()
        } else {
            util.hideKeyboard(binding.etSearch, requireContext())
        }
    }

    private fun handleError(msg: Int) {
        util.retiraOpacidadeFundo(binding.llListaViagens)
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "viagem")
        )
    }

    private fun configuraAdapter(viagens: List<Viagem>) {
        binding.rvViagens.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvViagens.adapter = ListaViagensAdapter(viagens, requireContext(), this)
    }

    private fun setupEditTextSearch() {
        with(binding.etSearch) {
            doOnTextChanged { text, _, _, _ ->
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
            viewModel.recuperaViagens(searchText)
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.clListaViagens, msg, resources, tipoSnackbar)
    }

    override fun clickViagem(viagem: Viagem) {
        chamaDetalheViagem(viagem)
    }

    private fun setListeners() {
        binding.fabAdicionaViagem
            .setOnClickListener {
                chamaDialogDeAdicao()
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao: Int = (binding.rvViagens.adapter as ListaViagensAdapter).posicao

        when (item.itemId) {
            1 -> {
                chamaDialogDeAlteracao(viagens[posicao])
            }
            2 -> {
                viewModel.deletaViagem(viagens[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao() {
        AdicionaViagemDialog(decorView, requireContext())
            .chama(null, binding.llListaViagens) { viagemCriada ->
                viewModel.insereViagem(viagemCriada)
            }
    }

    private fun chamaDialogDeAlteracao(viagem: Viagem) {
        util.aplicaOpacidadeFundo(binding.llListaViagens)
        AlteraViagemDialog(decorView, requireContext())
            .chama(viagem, viagem.id, binding.llListaViagens) { viagemAlterada ->
                viewModel.alteraViagem(viagemAlterada)
            }
    }

    private fun chamaDetalheViagem(viagem: Viagem) {
        findNavController().navigate(
            ViagemFragmentDirections.actionViagemFragmentToViagemDetalheFragment(viagem)
        )
    }

}