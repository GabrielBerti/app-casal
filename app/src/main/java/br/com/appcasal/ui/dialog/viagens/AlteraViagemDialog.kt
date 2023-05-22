package br.com.appcasal.ui.dialog.viagens

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import br.com.appcasal.R
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.util.Util
import br.com.appcasal.util.extension.formataParaBrasileiro

class AlteraViagemDialog(
        viewGroup: ViewGroup,
        context: Context
) : FormularioViagemDialog(context, viewGroup) {

    private var util = Util()

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_viagem
    }

    fun chama(viagem: Viagem, id: Long, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
        viagemSemNota = viagem.nota == null
        super.chama(id, linearLayout, delegate)
        campoLocal.setText(viagem.local)
        campoDataInicio.setText(viagem.dataInicio.formataParaBrasileiro())
        campoDataFim.setText(viagem.dataFim.formataParaBrasileiro())
        estrelaMarcarda = viagem.nota
        util.exibeEstrelas(viagem.nota, estrela05, estrela10, estrela15, estrela20, estrela25, estrela30, estrela35, estrela40, estrela45, estrela50)
    }
}