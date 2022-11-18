package br.com.appcasal.ui.dialog.viagens

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.appcasal.R
import br.com.appcasal.model.Viagem

class AlteraViagemDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioViagemDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_viagem
    }

    fun chama(viagem: Viagem, id: Long, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
        super.chama(id, linearLayout, delegate)
        campoLocal.setText(viagem.local)
    }
}