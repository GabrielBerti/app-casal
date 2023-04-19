package br.com.appcasal.ui.activity.viagens.detalhes.lugares_visitados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentLugaresVisitadosViagemBinding
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.lugares_visitados.AdicionaLugarVisitadoDialog
import br.com.appcasal.ui.dialog.lugares_visitados.AlteraLugarVisitadoDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.LugaresVisitadosViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LugaresVisitadosFragment(private val viagem: Viagem) : Fragment(), ClickLugarVisitadoViagem {

    private lateinit var binding: FragmentLugaresVisitadosViagemBinding
    val viewModel: LugaresVisitadosViewModel by viewModel()
    private var lugaresVisitados: List<LugarVisitado> = listOf()

    private lateinit var snackbar: Snackbar
    private var util = Util()

    private val viewDaActivity by lazy {
        requireActivity().window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLugaresVisitadosViagemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lugaresVisitados = viagem.lugaresVisitados
        setupListeners()
        configuraAdapter()
        setListeners()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaLugaresVisitados(viagem)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.lugarVisitadoGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError { binding.isError = true }
                onSuccess {
                    lugaresVisitados = it.toMutableList()
                    configuraAdapter()
                }
            }

            viewModel.lugarVisitadoInsertResult.collectResult(this) {
                onError { handleError(R.string.erro_inserir) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.lugar_visitado_inserido_sucesso, it.nome)
                    )
                    viewModel.recuperaLugaresVisitados(viagem)
                }
            }

            viewModel.lugarVisitadoUpdateResult.collectResult(this) {
                onError { handleError(R.string.erro_alterar) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.lugar_visitado_alterado_sucesso, it.nome)
                    )
                    viewModel.recuperaLugaresVisitados(viagem)
                }
            }

            viewModel.lugarVisitadoDeleteResult.collectResult(this) {
                onError { handleError(R.string.erro_deletar) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.lugar_visitado_removido_sucesso)
                    )
                    viewModel.recuperaLugaresVisitados(viagem)
                    binding.rvLugaresVisitadosViagem.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun handleError(msg: Int) {
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "lugar visitado")
        )
    }

    private fun setListeners() {
        binding.tvAddLugarVisitado.setOnClickListener {
            chamaDialogDeAdicao()
        }
    }

    private fun configuraAdapter() {
        binding.rvLugaresVisitadosViagem.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLugaresVisitadosViagem.adapter =
            ListaLugaresVisitadosViagemAdapter(lugaresVisitados, requireContext(), this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao: Int = (binding.rvLugaresVisitadosViagem.adapter as ListaLugaresVisitadosViagemAdapter).posicao

        when (item.itemId) {
            1 -> {
                viewModel.deletaLugarVisitado(lugaresVisitados[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao() {
        AdicionaLugarVisitadoDialog(viewGroupDaActivity, requireContext())
            .chama(null) { lugarVisitadoCriado ->
                viewModel.insereLugarVisitado(lugarVisitadoCriado, viagem)
            }
    }

    private fun chamaDialogDeAlteracao(lugarVisitado: LugarVisitado) {
        AlteraLugarVisitadoDialog(viewGroupDaActivity, requireContext())
            .chamaAlteracao(lugarVisitado, lugarVisitado.id) { lugarVisitadoAlterado ->
                viewModel.alteraLugarVisitado(lugarVisitadoAlterado, viagem)
            }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        snackbar = util.createSnackBarWithReturn(
            requireActivity().findViewById(R.id.cl_detalhe_viagem),
            msg,
            resources,
            tipoSnackbar
        )
    }

    override fun clickLugarVisitadoViagem(lugarVisitado: LugarVisitado) {
        chamaDialogDeAlteracao(lugarVisitado)
    }
}

