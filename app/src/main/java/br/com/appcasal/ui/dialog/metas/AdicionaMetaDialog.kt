package br.com.appcasal.ui.dialog.metas

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.domain.model.Meta

class AdicionaMetaDialog(
    viewGroup: ViewGroup,
    context: Context,
    metas: List<Meta>
) : FormularioMetaDialog(context,  viewGroup, metas) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(): Int {
        return R.string.adiciona_meta
    }
}