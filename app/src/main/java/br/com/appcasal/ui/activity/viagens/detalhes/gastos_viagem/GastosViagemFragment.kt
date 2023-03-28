package br.com.appcasal.ui.activity.viagens.detalhes.gastos_viagem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentGastosViagemBinding
import br.com.appcasal.domain.model.*
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.gastos_viagem.AdicionaGastoViagemDialog
import br.com.appcasal.ui.dialog.gastos_viagem.AlteraGastoViagemDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.GastosViagemViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class GastosViagemFragment(private val viagem: Viagem) : Fragment(), ClickGastoViagem {

    private lateinit var binding: FragmentGastosViagemBinding
    val viewModel: GastosViagemViewModel by viewModel()

    private var gastosViagem: List<GastoViagem> = listOf()

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
        binding = FragmentGastosViagemBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gastosViagem = viagem.gastosViagens.toMutableList()
        setupListeners()
        setListeners()
        exibeValorTotalGasto()
        configuraAdapter()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaGastosViagem(viagem)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.gastoViagemGetResult.collectViewState(this) {
                onLoading { }
                onError { }
                onSuccess {
                    gastosViagem = it.toMutableList()
                    configuraAdapter()
                    exibeValorTotalGasto()
                }
            }

            viewModel.gastoViagemInsertResult.collectResult(this) {
                onError { }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.gasto_viagem_inserido_sucesso, it.descricao)
                    )
                    viewModel.recuperaGastosViagem(viagem)
                }
            }

            viewModel.gastoViagemUpdateResult.collectResult(this) {
                onError { }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.gasto_viagem_alterado_sucesso)
                    )
                    viewModel.recuperaGastosViagem(viagem)
                }
            }

            viewModel.gastoViagemDeleteResult.collectResult(this) {
                onError { }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.gasto_viagem_removido_sucesso)
                    )
                    viewModel.recuperaGastosViagem(viagem)
                    binding.rvGastosViagem.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun exibeValorTotalGasto() {
        binding.totalGastoViagem.text = String.format(
            requireContext().getString(R.string.total_gasto_viagem),
            somaValorTotalGastoViagem()
        )
    }

    private fun setListeners() {
        binding.totalGastoViagem.setOnClickListener {
            chamaDialogDeAdicao()
        }
    }

    private fun configuraAdapter() {
        binding.rvGastosViagem.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvGastosViagem.adapter = ListaGastosViagemAdapter(gastosViagem, requireContext(), this)
    }

    private fun somaValorTotalGastoViagem(): String {
        var totalGasto = BigDecimal.ZERO

        gastosViagem.forEach {
            totalGasto += it.valor
        }

        return totalGasto.formataParaBrasileiro()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao: Int = (binding.rvGastosViagem.adapter as ListaGastosViagemAdapter).posicao

        when (item.itemId) {
            1 -> {
                viewModel.deletaGastoViagem(gastosViagem[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao() {
        AdicionaGastoViagemDialog(viewGroupDaActivity, requireContext())
            .chama(null) { gastoViagemCriado ->
                viewModel.insereGastoViagem(gastoViagemCriado, viagem)
            }
    }

    private fun chamaDialogDeAlteracao(gastoViagem: GastoViagem) {
        AlteraGastoViagemDialog(viewGroupDaActivity, requireContext())
            .chamaAlteracao(gastoViagem, gastoViagem.id) { gastoViagemAlterado ->
                viewModel.alteraGastoViagem(gastoViagemAlterado, viagem)
            }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        snackbar = util.createSnackBarWithReturn(
            requireActivity().findViewById(R.id.cl_detalhe_viagem),
            msg,
            resources,
            tipoSnackbar,
            View.VISIBLE
        )
    }

    override fun clickGastoViagem(gastoViagem: GastoViagem) {
        chamaDialogDeAlteracao(gastoViagem)
    }
}
