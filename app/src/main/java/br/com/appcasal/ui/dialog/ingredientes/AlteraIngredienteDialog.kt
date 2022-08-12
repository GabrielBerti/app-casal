package br.com.appcasal.ui.dialog.ingredientes

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.model.Ingrediente
import br.com.appcasal.model.Receita

class AlteraIngredienteDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioIngredienteDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_ingrediente
    }

    fun chama(id: Long?, receitaId: Long, ingrediente: Ingrediente, delegate: (ingrediente: Ingrediente) -> Unit) {
        super.chama(id, receitaId, delegate)
        inicializaCampos(ingrediente)
    }

    private fun inicializaCampos(ingrediente: Ingrediente) {
        inicializaCampoDescricao(ingrediente)
    }

    private fun inicializaCampoDescricao(ingrediente: Ingrediente) {
        campoDescricaoIngrediente.setText(ingrediente.descricao)
    }

}