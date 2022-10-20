package br.com.appcasal.ui.dialog.financas

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.appcasal.R
import br.com.appcasal.model.Tipo
import br.com.appcasal.model.Transacao

class AlteraTransacaoDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioTransacaoDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(tipo: Tipo): Int {
        return R.string.altera_lancamento_mari
    }

    fun chama(transacao: Transacao, id: Long, linearLayout: LinearLayout, delegate: (transacao: Transacao) -> Unit) {
        val tipo = transacao.tipo
        super.chama(tipo, id, linearLayout, delegate)
        inicializaCampos(transacao)
    }

    private fun inicializaCampos(transacao: Transacao) {
        inicializaCampoValor(transacao)
        inicializaCampoData(transacao)
        inicializaCampoDescricao(transacao)
    }

    private fun inicializaCampoDescricao(transacao: Transacao) {
        campoDescricao.setText(transacao.descricao)
    }

    private fun inicializaCampoData(transacao: Transacao) {
        campoData.setText(transacao.data/*.formataParaBrasileiro()*/)
    }

    private fun inicializaCampoValor(transacao: Transacao) {
        campoValor.setText(transacao.valor.toString())
    }

}