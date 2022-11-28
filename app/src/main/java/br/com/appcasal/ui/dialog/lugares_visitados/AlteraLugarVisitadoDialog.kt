package br.com.appcasal.ui.dialog.lugares_visitados

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.model.GastoViagem
import br.com.appcasal.model.LugarVisitado

class AlteraLugarVisitadoDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioLugarVisitadoDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_lugar_visitado
    }

    fun chamaAlteracao(lugarVisitado: LugarVisitado, id: Long, idViagem: Long, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
        super.chama(id, idViagem, delegate)
        nomeLugarVisitado.setText(lugarVisitado.nome)
    }
}