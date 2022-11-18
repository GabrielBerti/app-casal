package br.com.appcasal.ui.dialog.viagens

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R

class AdicionaViagemDialog(
    viewGroup: ViewGroup,
    context: Context
) : FormularioViagemDialog(context,  viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(): Int {
        return R.string.adiciona_viagem
    }
}