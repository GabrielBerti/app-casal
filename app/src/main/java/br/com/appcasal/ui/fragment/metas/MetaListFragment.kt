package br.com.appcasal.ui.fragment.metas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentMetaListaBinding
import br.com.appcasal.domain.model.Meta
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.metas.AdicionaMetaDialog
import br.com.appcasal.ui.dialog.metas.AlteraMetaDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaMetasViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MetaListFragment : Fragment(), ClickMeta {
    lateinit var binding: FragmentMetaListaBinding
    val viewModel: ListaMetasViewModel by viewModel()
    private var util = Util()
    private lateinit var decorView: ViewGroup

    private var concluidas: Boolean = true
    var metas: List<Meta> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.recuperaMetas(concluidas)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMetaListaBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
        decorView = requireActivity().window.decorView as ViewGroup

        if (concluidas) binding.fabAdicionaMeta.visibility = View.GONE
        viewModel.recuperaMetas(concluidas)

        setupListeners()
        configuraFab()
        setupSwipeRefresh()

        return binding.root
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaMetas(concluidas)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun configuraAdapter(metas: List<Meta>) {
        binding.rvMetas.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMetas.adapter = ListaMetasAdapter(metas, requireContext(), this)
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
                    binding.noResults = metas.isEmpty()
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
                    viewModel.recuperaMetas(concluidas)
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
                    viewModel.recuperaMetas(concluidas)
                }
            }

            viewModel.metaDeleteResult.collectResult(this) {
                onError { handleError(R.string.erro_deletar) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.meta_removida_sucesso)
                    )
                    viewModel.recuperaMetas(concluidas)
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

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.clListaMetas, msg, resources, tipoSnackbar)
    }

    private fun configuraFab() {
        binding.fabAdicionaMeta
            .setOnClickListener {
                chamaDialogDeAdicao()
                util.aplicaOpacidadeFundo(binding.llMetas)
            }
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

    override fun clickMeta(meta: Meta) {
        chamaDialogDeAlteracao(meta)
    }

    companion object {
        fun createInstance(
            concluidas: Boolean
        ): MetaListFragment {
            return MetaListFragment().apply {
                this.concluidas = concluidas
            }
        }
    }
}
