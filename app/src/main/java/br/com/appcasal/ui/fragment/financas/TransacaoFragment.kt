package br.com.appcasal.ui.fragment.financas

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentTransacaoBinding
import br.com.appcasal.domain.model.*
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.financas.AdicionaTransacaoDialog
import br.com.appcasal.ui.dialog.financas.AlteraTransacaoDialog
import br.com.appcasal.util.Util
import br.com.appcasal.util.extension.formataParaBrasileiro
import br.com.appcasal.viewmodel.ListaTransacoesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class TransacaoFragment : Fragment(), ClickTransacao {

    private lateinit var binding: FragmentTransacaoBinding
    val viewModel: ListaTransacoesViewModel by viewModel()

    private var util = Util()
    private var snackbar: Snackbar? = null

    private val corBiel = R.color.biel
    private val corMari = R.color.mari
    private val corZerada = R.color.white

    private var transacoes: List<Transacao> = emptyList()
    private lateinit var decorView: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentTransacaoBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup

        setupListeners()
        setClickListeners()
        recuperaResumoETransacoes()
        configuraFab()
        setupSwipeRefresh()

        return binding.root
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.transacaoGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError { binding.isError = true }
                onSuccess {
                    transacoes = it
                    configuraAdapter(it)
                }
            }

            viewModel.resumoGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.listaTransacoesResumo.isError = false
                    binding.listaTransacoesResumo.isLoading = it
                }
                onError { binding.listaTransacoesResumo.isError = true }
                onSuccess {
                    configuraResumo(it)
                }
            }

            viewModel.transacaoInsertResult.collectResult(this) {
                onError { handleError(R.string.erro_inserir) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacao_inserida_sucesso, it.descricao)
                    )
                    recuperaResumoETransacoes()
                }
            }

            viewModel.transacaoUpdateResult.collectResult(this) {
                onError { handleError(R.string.erro_alterar) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacao_alterada_sucesso, it.descricao)
                    )
                    recuperaResumoETransacoes()
                }
            }

            viewModel.transacaoDeleteResult.collectResult(this) {
                onError { handleError(R.string.erro_deletar) }
                onSuccess {
                    controlaCamposFab(View.INVISIBLE, true)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacao_removida_sucesso)
                    )

                    recuperaResumoETransacoes()
                    binding.rvTransacoes.adapter?.notifyDataSetChanged()
                }
            }

            viewModel.transacaoDeleteAllResult.collectResult(this) {
                onError { handleError(R.string.erro_remover_transacoes) }
                onSuccess {
                    controlaCamposFab(View.INVISIBLE, true)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacoes_removidas_sucesso)
                    )

                    recuperaResumoETransacoes()
                    binding.rvTransacoes.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            goBack()
        }

        binding.btZerar.setOnClickListener {
            dialogRemoveTransacoes()
        }
    }

    private fun recuperaResumoETransacoes() {
        viewModel.recuperaTransacoes()
        viewModel.recuperaResumo()
    }

    private fun handleError(msg: Int) {
        util.retiraOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "transação")
        )
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            recuperaResumoETransacoes()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun configuraAdapter(transacoes: List<Transacao>) {
        binding.rvTransacoes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTransacoes.adapter = ListaTransacoesAdapter(transacoes, requireContext(), this)
    }

    override fun clickTransacao(transacao: Transacao) {
        chamaDialogDeAlteracao(transacao)
    }

    private fun adicionaSaldoBiel(saldoBiel: BigDecimal) {
        with(binding.listaTransacoesResumo.resumoCardSaldoBiel) {
            setTextColor(resources.getColor(corBiel))
            text = saldoBiel.formataParaBrasileiro()
        }
    }

    private fun adicionaSaldoMari(saldoMari: BigDecimal) {
        with(binding.listaTransacoesResumo.resumoCardMari) {
            setTextColor(resources.getColor(corMari))
            text = saldoMari.formataParaBrasileiro()
        }
    }

    private fun adicionaTotal(total: BigDecimal) {
        val cor = corPor(total)

        with(binding.listaTransacoesResumo.resumoCardTotal) {
            setTextColor(cor)

            if (total.toDouble() > 0) {
                text = "Mari deve " + total.formataParaBrasileiro() + " para Biel"
            } else if (total.toDouble() < 0) {
                text = "Biel deve " + normalizaRetornaValorPositivo(total) + " para Mari"
            } else {
                text = resources.getString(R.string.tudo_zerado)
                setTextColor(resources.getColor(corZerada))
            }

        }
    }

    private fun normalizaRetornaValorPositivo(total: BigDecimal): String {
        val retorno = total * -BigDecimal.ONE

        return retorno.formataParaBrasileiro()
    }

    private fun corPor(valor: BigDecimal): Int {
        if (valor >= BigDecimal.ZERO) {
            return resources.getColor(corBiel)
        }
        return resources.getColor(corMari)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    private fun configuraFab() {

        binding.fabAdicionaTransacao
            .setOnClickListener {
                snackbar?.dismiss()
                binding.fabAdicionaTransacaoClose.visibility = View.VISIBLE
                it.visibility = View.GONE
                controlaCamposFab(View.VISIBLE, false)
                util.aplicaOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
            }

        binding.fabAdicionaTransacaoClose
            .setOnClickListener {
                binding.fabAdicionaTransacao.visibility = View.VISIBLE
                it.visibility = View.GONE
                controlaCamposFab(View.GONE, false)
                util.retiraOpacidadeFundo(binding.llHeaderAndBodyTransacoes)

            }

        binding.listaTransacoesAdicionaSaldoBiel
            .setOnClickListener {
                chamaDialogDeAdicao(Tipo.BIEL)
                controlaCamposFab(View.GONE, true)
            }

        binding.tvAdicionaLctoBiel
            .setOnClickListener {
                chamaDialogDeAdicao(Tipo.BIEL)
                controlaCamposFab(View.GONE, true)
            }

        binding.listaTransacoesAdicionaSaldoMari.setOnClickListener {
            chamaDialogDeAdicao(Tipo.MARI)
            controlaCamposFab(View.GONE, true)
        }

        binding.tvAdicionaLctoMari.setOnClickListener {
            chamaDialogDeAdicao(Tipo.MARI)
            controlaCamposFab(View.GONE, true)
        }
    }

    private fun controlaCamposFab(valor: Int, resetVisibilidadeFabsPrincipais: Boolean) {
        if (resetVisibilidadeFabsPrincipais) {
            resetVisibilidadeFabsPrincipais()
        }

        binding.listaTransacoesAdicionaSaldoBiel.visibility = valor
        binding.listaTransacoesAdicionaSaldoMari.visibility = valor
        binding.tvAdicionaLctoBiel.visibility = valor
        binding.tvAdicionaLctoMari.visibility = valor
    }

    private fun resetVisibilidadeFabsPrincipais() {
        binding.fabAdicionaTransacao.visibility = View.VISIBLE
        binding.fabAdicionaTransacaoClose.visibility = View.GONE
    }

    private fun configuraResumo(resumo: Resumo) {
        val saldoBiel = resumo.saldoBiel ?: BigDecimal.ZERO
        val saldoMari = resumo.saldoMari ?: BigDecimal.ZERO

        adicionaSaldoBiel(saldoBiel)
        adicionaSaldoMari(saldoMari)

        val saldoTotal = saldoBiel.subtract(saldoMari)
        adicionaTotal(saldoTotal)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao: Int = (binding.rvTransacoes.adapter as ListaTransacoesAdapter).posicao

        when (item.itemId) {
            1 -> {
                viewModel.deletaTransacao(transacoes[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao(tipo: Tipo) {
        AdicionaTransacaoDialog(decorView, requireContext())
            .chama(tipo, null, binding.llHeaderAndBodyTransacoes) { transacaoCriada ->
                viewModel.insereTransacao(transacaoCriada)
            }
    }

    private fun chamaDialogDeAlteracao(transacao: Transacao) {
        util.aplicaOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
        AlteraTransacaoDialog(decorView, requireContext())
            .chama(
                transacao,
                transacao.id,
                binding.llHeaderAndBodyTransacoes
            ) { transacaoAlterada ->
                viewModel.alteraTransacao(transacaoAlterada)
            }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        snackbar =
            util.createSnackBarWithReturn(
                binding.clListaTransacoes,
                msg,
                resources,
                tipoSnackbar
            )
    }

    fun goBack() = findNavController().popBackStack()

    fun dialogRemoveTransacoes() {
        if(transacoes.isNotEmpty()) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Limpar")
            builder.setMessage("Zerar transacoes ?")

            builder.setPositiveButton(
                "Sim"
            ) { _, _ ->
                viewModel.deletaTodasTransacoes()
            }

            builder.setNegativeButton(
                "Não"
            ) { _, _ ->

            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }
}