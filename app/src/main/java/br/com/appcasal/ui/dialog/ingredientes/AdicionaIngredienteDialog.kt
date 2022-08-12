package br.com.appcasal.ui.dialog.ingredientes

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R

class AdicionaIngredienteDialog(
        viewGroup: ViewGroup,
        context: Context
) : FormularioIngredienteDialog(context,  viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(): Int {
        return R.string.adiciona_ingrediente
    }
}