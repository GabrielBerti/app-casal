package br.com.appcasal.ui.dialog.financas

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.model.Tipo

class AdicionaTransacaoDialog(
        viewGroup: ViewGroup,
        context: Context
) : FormularioTransacaoDialog(context,  viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(tipo: Tipo): Int {
        if (tipo == Tipo.BIEL) {
            return R.string.adiciona_lancamento_biel
        }
        return R.string.adiciona_lancamento_mari
    }
}