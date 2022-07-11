package br.com.appcasal.ui.dialog.metas

import android.content.Context
import android.view.ViewGroup
import br.com.appcasal.R
import br.com.appcasal.model.Meta

class AlteraMetaDialog(
        viewGroup: ViewGroup,
        private val context: Context
) : FormularioMetaDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    override fun tituloPor(): Int {
        return R.string.altera_meta
    }

    fun chama(meta: Meta, id: Long, concluido: Boolean, delegate: (meta: Meta) -> Unit) {
        super.chama(id,concluido, delegate)
        inicializaCampos(meta)
    }

    private fun inicializaCampos(meta: Meta) {
        inicializaCampoDescricao(meta)
    }

    private fun inicializaCampoDescricao(meta: Meta) {
        campoDescricao.setText(meta.descricao)
    }


}