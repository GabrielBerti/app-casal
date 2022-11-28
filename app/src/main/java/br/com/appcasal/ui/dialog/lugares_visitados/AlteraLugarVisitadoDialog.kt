package br.com.appcasal.ui.dialog.lugares_visitados

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.model.LugarVisitado
import br.com.appcasal.util.Util

class AlteraLugarVisitadoDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioLugarVisitadoDialog(context, viewGroup) {

    private var util = Util()

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_lugar_visitado
    }

    fun chamaAlteracao(lugarVisitado: LugarVisitado, id: Long, idViagem: Long, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
        super.chama(id, idViagem, delegate)
        nomeLugarVisitado.setText(lugarVisitado.nome)
        util.exibeEstrelas(lugarVisitado.legal, estrela1, estrela2, estrela3, estrela4, estrela5)
    }
}