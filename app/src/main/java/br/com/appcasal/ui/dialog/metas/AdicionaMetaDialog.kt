package br.com.appcasal.ui.dialog.metas

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R

class AdicionaMetaDialog(
        viewGroup: ViewGroup,
        context: Context
) : FormularioMetaDialog(context,  viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(): Int {
        return R.string.adiciona_meta
    }
}