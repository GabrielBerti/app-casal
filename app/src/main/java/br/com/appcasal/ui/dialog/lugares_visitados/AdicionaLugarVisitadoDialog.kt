package br.com.appcasal.ui.dialog.lugares_visitados

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R

class AdicionaLugarVisitadoDialog(
    viewGroup: ViewGroup,
    context: Context
) : FormularioLugarVisitadoDialog(context,  viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(): Int {
        return R.string.adiciona_lugar_visitado
    }
}