package br.com.appcasal.ui.dialog.gastos_viagem

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R

class AdicionaGastoViagemDialog(
    viewGroup: ViewGroup,
    context: Context
) : FormularioGastoViagemDialog(context,  viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPor(): Int {
        return R.string.adiciona_gasto_viagem
    }
}