package br.com.appcasal.ui.activity.financas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.TransacaoDAO
import br.com.appcasal.databinding.ActivityListaTransacoesBinding
import br.com.appcasal.domain.model.Resumo
import br.com.appcasal.domain.model.Tipo
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.domain.model.Transacao
import br.com.appcasal.ui.dialog.financas.AdicionaTransacaoDialog
import br.com.appcasal.ui.dialog.financas.AlteraTransacaoDialog
import br.com.appcasal.util.Util
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

class ListaTransacoesActivity : AppCompatActivity(), ClickTransacao {

    private lateinit var activityListaTransacoes: ActivityListaTransacoesBinding
    private lateinit var adapter: ListaTransacoesAdapter
    private lateinit var rv: RecyclerView
    private var util = Util()
    private lateinit var clTransacoes: CoordinatorLayout
    private lateinit var snackbar: Snackbar

    private val corBiel = R.color.biel
    private val corMari = R.color.mari
    private val corZerada = R.color.white

    private var transacoes: List<Transacao> = Companion.transacoes

    companion object {
        private val transacoes: MutableList<Transacao> = mutableListOf()
    }

    private val viewDaActivity by lazy {
        window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    private val db by lazy {
        AppDatabase.instancia(this)
    }

    private lateinit var transacaoDao: TransacaoDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaTransacoes = ActivityListaTransacoesBinding.inflate(layoutInflater)
        val view = activityListaTransacoes.root

        setContentView(view)

        transacaoDao = db.transacaoDao()
        transacoes = transacaoDao.buscaTodos()

        clTransacoes = findViewById<CoordinatorLayout>(R.id.cl_lista_transacoes)

        setToolbar()
        configuraAdapter()
        configuraResumo()

        //instancia a snackbar
        createSnackBar(
            TipoSnackbar.SUCESSO,
            resources.getString(R.string.transacao_inserida_sucesso),
            View.GONE
        )

        configuraFab()

    }

    private fun setToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun configuraAdapter() {
        rv = findViewById(R.id.lista_transacoes_listview)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ListaTransacoesAdapter(transacoes, this, this)
        rv.adapter = adapter
    }

    override fun clickTransacao(transacao: Transacao) {
        chamaDialogDeAlteracao(transacao)
    }

    fun atualiza() {
        val resumo = Resumo(transacoes)
        adicionaSaldoBiel(resumo.saldoBiel)
        adicionaSaldoMari(resumo.saldoMari)
        adicionaTotal(resumo.total)
    }

    private fun adicionaSaldoBiel(saldoBiel: BigDecimal) {
        val totalSaldoBiel = saldoBiel//resumo.saldoBiel

        with(findViewById<TextView>(R.id.resumo_card_saldo_biel)) {
            setTextColor(resources.getColor(corBiel))
            text = totalSaldoBiel.formataParaBrasileiro()
        }
    }

    private fun adicionaSaldoMari(saldoMari: BigDecimal) {
        val totalSaldoMari = saldoMari
        with(findViewById<TextView>(R.id.resumo_card_mari)) {
            setTextColor(resources.getColor(corMari))
            text = totalSaldoMari.formataParaBrasileiro()
        }
    }

    private fun adicionaTotal(total: BigDecimal) {
        val total = total
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
            transacaoDao.removeAll()
            atualizaTransacoes()
            controlaCamposFab(View.INVISIBLE, true)
            createSnackBar(
                TipoSnackbar.SUCESSO,
                resources.getString(R.string.transacoes_removidas_sucesso),
                View.VISIBLE
            )
        }

        builder.setNegativeButton(
            "Não"
        ) { _, _ ->
            null
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun configuraFab() {

        activityListaTransacoes.fabAdicionaTransacao
            .setOnClickListener {
                snackbar.dismiss()
                activityListaTransacoes.fabAdicionaTransacaoClose.visibility = View.VISIBLE
                it.visibility = View.GONE
                controlaCamposFab(View.VISIBLE, false)
                util.aplicaOpacidadeFundo(activityListaTransacoes.llHeaderAndBodyTransacoes)
            }

        activityListaTransacoes.fabAdicionaTransacaoClose
            .setOnClickListener {
                activityListaTransacoes.fabAdicionaTransacao.visibility = View.VISIBLE
                it.visibility = View.GONE
                controlaCamposFab(View.GONE, false)
                util.retiraOpacidadeFundo(activityListaTransacoes.llHeaderAndBodyTransacoes)

            }

        activityListaTransacoes.listaTransacoesAdicionaSaldoBiel
            .setOnClickListener {
                chamaDialogDeAdicao(Tipo.BIEL)
                controlaCamposFab(View.GONE, true)
            }

        activityListaTransacoes.tvAdicionaLctoBiel
            .setOnClickListener {
                chamaDialogDeAdicao(Tipo.BIEL)
                controlaCamposFab(View.GONE, true)
            }

        activityListaTransacoes.listaTransacoesAdicionaSaldoMari.setOnClickListener {
            chamaDialogDeAdicao(Tipo.MARI)
            controlaCamposFab(View.GONE, true)
        }

        activityListaTransacoes.tvAdicionaLctoMari.setOnClickListener {
            chamaDialogDeAdicao(Tipo.MARI)
            controlaCamposFab(View.GONE, true)
        }
    }

    private fun controlaCamposFab(valor: Int, resetVisibilidadeFabsPrincipais: Boolean) {
        if (resetVisibilidadeFabsPrincipais) {
            resetVisibilidadeFabsPrincipais()
        }

        activityListaTransacoes.listaTransacoesAdicionaSaldoBiel.visibility = valor
        activityListaTransacoes.listaTransacoesAdicionaSaldoMari.visibility = valor
        activityListaTransacoes.tvAdicionaLctoBiel.visibility = valor
        activityListaTransacoes.tvAdicionaLctoMari.visibility = valor
    }

    private fun resetVisibilidadeFabsPrincipais() {
        activityListaTransacoes.fabAdicionaTransacao.visibility = View.VISIBLE
        activityListaTransacoes.fabAdicionaTransacaoClose.visibility = View.GONE
    }

    private fun adiciona(transacao: Transacao) {
        transacaoDao.adiciona(transacao)
        atualizaTransacoes()
    }

    private fun atualizaTransacoes() {
        transacoes = transacaoDao.buscaTodos()
        rv.adapter = ListaTransacoesAdapter(transacoes, this, this)
        configuraResumo()
    }

    private fun configuraResumo() {
        atualiza()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var posicao = -1
        posicao = (rv.adapter as ListaTransacoesAdapter).posicao

        when(item.itemId) {
            1 -> {
                val descricaoTransacaoRemovida = transacoes[posicao].descricao
                remove(posicao)
                adapter.notifyItemRemoved(posicao)

                controlaCamposFab(View.INVISIBLE, true)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.transacao_removida_sucesso, descricaoTransacaoRemovida),
                    View.VISIBLE
                )
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicaoDaTransacao: Int) {
        transacaoDao.remove(transacoes[posicaoDaTransacao])
        adapter.notifyItemRemoved(posicaoDaTransacao)
        atualizaTransacoes()
    }

    private fun chamaDialogDeAdicao(tipo: Tipo) {
        AdicionaTransacaoDialog(viewGroupDaActivity, this)
            .chama(tipo, null, activityListaTransacoes.llHeaderAndBodyTransacoes) { transacaoCriada ->
                adiciona(transacaoCriada)
                util.retiraOpacidadeFundo(activityListaTransacoes.llHeaderAndBodyTransacoes)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.transacao_inserida_sucesso, transacaoCriada.descricao),
                    View.VISIBLE
                )
            }
    }

    private fun chamaDialogDeAlteracao(transacao: Transacao) {
        util.aplicaOpacidadeFundo(activityListaTransacoes.llHeaderAndBodyTransacoes)
        AlteraTransacaoDialog(viewGroupDaActivity, this)
            .chama(transacao, transacao.id, activityListaTransacoes.llHeaderAndBodyTransacoes) { transacaoAlterada ->
                altera(transacaoAlterada)
                controlaCamposFab(View.INVISIBLE, true)
                util.retiraOpacidadeFundo(activityListaTransacoes.llHeaderAndBodyTransacoes)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.transacao_alterada_sucesso),
                    View.VISIBLE
                )
            }
    }

    private fun altera(transacao: Transacao) {
        transacaoDao.altera(transacao)
        atualizaTransacoes()
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String, visibility: Int) {
        snackbar =
            util.createSnackBarWithReturn(clTransacoes, msg, resources, tipoSnackbar, visibility)
    }
}