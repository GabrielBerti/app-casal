package br.com.appcasal.ui.dialog.viagens

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.appcasal.R
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.util.Util

class AlteraViagemDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioViagemDialog(context, viewGroup) {

    private var util = Util()

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_viagem
    }

    fun chama(viagem: Viagem, id: Long, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
        super.chama(id, linearLayout, delegate)
        campoLocal.setText(viagem.local)
        util.exibeEstrelas(viagem.nota, estrela1, estrela2, estrela3, estrela4, estrela5)
    }
}