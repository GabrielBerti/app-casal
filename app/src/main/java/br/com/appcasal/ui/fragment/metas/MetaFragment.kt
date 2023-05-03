package br.com.appcasal.ui.fragment.metas

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentMetaBinding
import br.com.appcasal.domain.model.Meta
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.metas.AdicionaMetaDialog
import br.com.appcasal.ui.dialog.metas.AlteraMetaDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaMetasViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MetaFragment : Fragment(), ClickMeta {

    private lateinit var binding: FragmentMetaBinding

    private var util = Util()
    val viewModel: ListaMetasViewModel by viewModel()
    private var searchJob: Job? = null

    private var metas: List<Meta> = emptyList()
    private var posicaoSpinner = 0

    private lateinit var decorView: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentMetaBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup

        setupListeners()
        viewModel.recuperaMetas()

        setClickListeners()
        configuraSpinner()
        configuraFab()
        setupSwipeRefresh()
        setupEditTextSearch()
        //spinnerStatus.background.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)

        return binding.root
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(1500)
            viewModel.recuperaMetas(viewModel.filterByConcluidas, searchText)
        }
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

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.metaGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError {
                    binding.isError = true
                    binding.noResults = false
                }
                onSuccess {
                    metas = it
                    binding.noResults = metas.isEmpty() && (binding.etSearch.text?.toString() != "" || posicaoSpinner != 0)
                    configuraAdapter(it)
                }
            }

            viewModel.metaInsertResult.collectResult(this) {
                onError { handleError(R.string.erro_inserir) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llMetas)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.meta_inserida_sucesso, it.descricao)
                    )
                    viewModel.recuperaMetas()
                }
            }

            viewModel.metaUpdateResult.collectResult(this) {
                onError { handleError(R.string.erro_alterar) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llMetas)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.meta_alterada_sucesso)
                    )
                    viewModel.recuperaMetas()
                }
            }

            viewModel.metaDeleteResult.collectResult(this) {
                onError { handleError(R.string.erro_deletar) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.meta_removida_sucesso)
                    )
                    viewModel.recuperaMetas()
                    binding.rvMetas.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun handleError(msg: Int) {
        util.retiraOpacidadeFundo(binding.llMetas)
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "meta")
        )
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaMetas()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun configuraAdapter(metas: List<Meta>) {
        binding.rvMetas.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMetas.adapter = ListaMetasAdapter(metas, requireContext(), this)
    }

    private fun configuraSpinner() {
        val options = arrayOf(
            resources.getString(R.string.todas),
            resources.getString(R.string.meta_nao_concluida),
            resources.getString(R.string.meta_concluida)
        )

        binding.spinnerStatus.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
        binding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                posicao: Int,
                id: Long
            ) {
                posicaoSpinner = posicao
                when (posicao) {
                    0 -> {
                        viewModel.recuperaMetas()
                    }

                    1 -> {
                        viewModel.recuperaMetas(false)
                    }

                    2 -> {
                        viewModel.recuperaMetas(true)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                parent?.setSelection(0)
            }

        }
    }

    override fun clickMeta(meta: Meta) {
        chamaDialogDeAlteracao(meta)
    }

    private fun configuraFab() {
        binding.fabAdicionaMeta
            .setOnClickListener {
                chamaDialogDeAdicao()
                util.aplicaOpacidadeFundo(binding.llMetas)
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(binding.rvMetas.adapter != null) {
            val posicao: Int = (binding.rvMetas.adapter as ListaMetasAdapter).posicao

            when (item.itemId) {
                1 -> {
                    metas[posicao].concluido = !metas[posicao].concluido

                    val msgSnackbar: String = if(metas[posicao].concluido) {
                        resources.getString(R.string.meta_concluida_sucesso)
                    } else {
                        resources.getString(R.string.meta_desconcluida_sucesso)
                    }

                    viewModel.alteraMeta(metas[posicao])

                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        msgSnackbar
                    )
                }

                2 -> {
                    viewModel.deletaMeta(metas[posicao])
                }
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao() {
        AdicionaMetaDialog(decorView, requireContext(), metas)
            .chama(null, false, binding.llMetas) { metaCriada ->
                viewModel.insereMeta(metaCriada)
            }
    }

    private fun chamaDialogDeAlteracao(meta: Meta) {
        util.aplicaOpacidadeFundo(binding.llMetas)
        AlteraMetaDialog(decorView, requireContext(), metas)
            .chama(meta, meta.id, meta.concluido, binding.llMetas) { metaAlterada ->
                viewModel.alteraMeta(metaAlterada)
            }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.clListaMetas, msg, resources, tipoSnackbar)
    }
}