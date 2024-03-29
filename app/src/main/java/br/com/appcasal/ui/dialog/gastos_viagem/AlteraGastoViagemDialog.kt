package br.com.appcasal.ui.dialog.gastos_viagem

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.domain.model.GastoViagem

class AlteraGastoViagemDialog(
        viewGroup: ViewGroup,
        context: Context
) : FormularioGastoViagemDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_gasto_viagem
    }

    fun chamaAlteracao(gastoViagem: GastoViagem, id: Long, delegate: (gastoViagem: GastoViagem) -> Unit) {
        super.chama(id, delegate)
        campoDescricaoGasto.setText(gastoViagem.descricao)
        campoValorGasto.setText(gastoViagem.valor.toString())
    }
}