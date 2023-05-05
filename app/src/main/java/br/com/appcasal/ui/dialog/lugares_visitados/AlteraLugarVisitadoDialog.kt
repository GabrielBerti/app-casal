package br.com.appcasal.ui.dialog.lugares_visitados

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.util.Util

class AlteraLugarVisitadoDialog(
        viewGroup: ViewGroup,
        context: Context
) : FormularioLugarVisitadoDialog(context, viewGroup) {

    private var util = Util()

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_lugar_visitado
    }

    fun chamaAlteracao(lugarVisitado: LugarVisitado, id: Long, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
        super.chama(id, delegate)
        nomeLugarVisitado.setText(lugarVisitado.nome)
        util.exibeEstrelas(lugarVisitado.nota, estrela05, estrela10, estrela15, estrela20, estrela25, estrela30, estrela35, estrela40, estrela45, estrela50)
        estrelaMarcarda = lugarVisitado.nota
    }
}