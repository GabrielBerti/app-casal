package br.com.appcasal.ui.dialog.metas

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import br.com.appcasal.R
import br.com.appcasal.model.Meta

abstract class FormularioMetaDialog(
        private val context: Context,
        private val viewGroup: ViewGroup) {

    private val viewCriada = criaLayout()
    protected val campoDescricao = viewCriada.findViewById<EditText>(R.id.descricao_meta)
    abstract protected val tituloBotaoPositivo: String

    fun chama(id: Long?, concluido: Boolean, delegate: (meta: Meta) -> Unit) {
        //configuraCampoData()
        configuraFormulario(id, concluido, delegate)
    }

    private fun configuraFormulario(id: Long?, concluido: Boolean, delegate: (meta: Meta) -> Unit) {
        val titulo = tituloPor()

        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setPositiveButton(tituloBotaoPositivo
            ) { _, _ ->

                val descricaoEmTexto = campoDescricao.text.toString()

                var metaCriada: Meta

                if(id == null) {
                    metaCriada = Meta(
                        descricao = descricaoEmTexto,
                        concluido = false
                    )
                } else {
                    metaCriada = Meta(
                        id = id,
                        descricao = descricaoEmTexto,
                        concluido = concluido
                    )
                }

                delegate(metaCriada)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        return LayoutInflater.from(context)
                .inflate(R.layout.form_metas,
                        viewGroup,
                        false)
    }
}