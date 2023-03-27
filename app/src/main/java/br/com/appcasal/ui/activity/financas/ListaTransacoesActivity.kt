package br.com.appcasal.ui.activity.financas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.databinding.ActivityListaTransacoesBinding
import br.com.appcasal.domain.model.*
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.financas.AdicionaTransacaoDialog
import br.com.appcasal.ui.dialog.financas.AlteraTransacaoDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaTransacoesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class ListaTransacoesActivity : AppCompatActivity(), ClickTransacao {

    private lateinit var binding: ActivityListaTransacoesBinding
    val viewModel: ListaTransacoesViewModel by viewModel()

    private var util = Util()
    private lateinit var snackbar: Snackbar

    private val corBiel = R.color.biel
    private val corMari = R.color.mari
    private val corZerada = R.color.white

    private var transacoes: List<Transacao> = emptyList()

    private val viewDaActivity by lazy {
        window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista_transacoes)

        setupListeners()
        viewModel.recuperaTransacoes()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        //instancia a snackbar
        createSnackBar(
            TipoSnackbar.SUCESSO,
            resources.getString(R.string.transacao_inserida_sucesso),
            View.GONE
        )

        configuraFab()
        setupSwipeRefresh()
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.transacaoGetResult.collectViewState(this) {
                onLoading { }
                onError { configuraAdapter(listOf(Transacao(1, BigDecimal.ONE, "dfsdf", Tipo.BIEL, "21/08/2023"))) } //TODO tratar erro
                onSuccess {
                    transacoes = it
                    configuraAdapter(it)
                    viewModel.recuperaResumo()
                }
            }

            viewModel.resumoGetResult.collectViewState(this) {
                onLoading { }
                onError {
                }
                onSuccess { configuraResumo(it) }
            }

            viewModel.transacaoInsertResult.collectResult(this) {
                onError { }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacao_inserida_sucesso),
                        View.VISIBLE
                    )
                    viewModel.recuperaTransacoes()
                }
            }

            viewModel.transacaoUpdateResult.collectResult(this) {
                onError { }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacao_alterada_sucesso),
                        View.VISIBLE
                    )
                    viewModel.recuperaTransacoes()
                }
            }

            viewModel.transacaoDeleteResult.collectResult(this) {
                onError { }
                onSuccess {
                    controlaCamposFab(View.INVISIBLE, true)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacao_removida_sucesso),
                        View.VISIBLE
                    )

                    viewModel.recuperaTransacoes()
                    binding.rvTransacoes.adapter?.notifyDataSetChanged()
                }
            }

            viewModel.transacaoDeleteAllResult.collectResult(this) {
                onError { }
                onSuccess {
                    controlaCamposFab(View.INVISIBLE, true)

                    controlaCamposFab(View.INVISIBLE, true)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.transacoes_removidas_sucesso),
                        View.VISIBLE
                    )
                    viewModel.recuperaTransacoes()
                    binding.rvTransacoes.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaTransacoes()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun configuraAdapter(transacoes: List<Transacao>) {
        binding.rvTransacoes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTransacoes.adapter = ListaTransacoesAdapter(transacoes, this, this)
    }

    override fun clickTransacao(transacao: Transacao) {
        chamaDialogDeAlteracao(transacao)
    }

    private fun adicionaSaldoBiel(saldoBiel: BigDecimal) {
        with(findViewById<TextView>(R.id.resumo_card_saldo_biel)) {
            setTextColor(resources.getColor(corBiel))
            text = saldoBiel.formataParaBrasileiro()
        }
    }

    private fun adicionaSaldoMari(saldoMari: BigDecimal) {
        with(findViewById<TextView>(R.id.resumo_card_mari)) {
            setTextColor(resources.getColor(corMari))
            text = saldoMari.formataParaBrasileiro()
        }
    }

    private fun adicionaTotal(total: BigDecimal) {
        val cor = corPor(total)

        with(findViewById<TextView>(R.id.resumo_card_total)) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> {
                if (transacoes.isNotEmpty()) {
                    dialogRemoveTransacoes()
                }
                true
            }
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dialogRemoveTransacoes() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

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

    private fun configuraFab() {

        binding.fabAdicionaTransacao
            .setOnClickListener {
                snackbar.dismiss()
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

        when(item.itemId) {
            1 -> {
                viewModel.deletaTransacao(transacoes[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao(tipo: Tipo) {
        AdicionaTransacaoDialog(viewGroupDaActivity, this)
            .chama(tipo, null, binding.llHeaderAndBodyTransacoes) { transacaoCriada ->
                viewModel.insereTransacao(transacaoCriada)
            }
    }

    private fun chamaDialogDeAlteracao(transacao: Transacao) {
        util.aplicaOpacidadeFundo(binding.llHeaderAndBodyTransacoes)
        AlteraTransacaoDialog(viewGroupDaActivity, this)
            .chama(transacao, transacao.id, binding.llHeaderAndBodyTransacoes) { transacaoAlterada ->
                viewModel.alteraTransacao(transacaoAlterada)
            }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String, visibility: Int) {
        snackbar =
            util.createSnackBarWithReturn(binding.clListaTransacoes, msg, resources, tipoSnackbar, visibility)
    }
}