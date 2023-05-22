package br.com.appcasal.ui.dialog.metas

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import br.com.appcasal.R
import br.com.appcasal.domain.model.Meta
import br.com.appcasal.util.Util
import br.com.appcasal.util.extension.somentePrimeiraLetraMaiuscula


abstract class FormularioMetaDialog(
        private val context: Context,
        private val viewGroup: ViewGroup,
        private val metas: List<Meta>) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected lateinit var campoDescricao: EditText
    abstract protected val tituloBotaoPositivo: String

    fun chama(id: Long?, concluido: Boolean, linearLayout: LinearLayout, delegate: (meta: Meta) -> Unit) {
        //configuraCampoData()
        configuraFormulario(id, concluido, linearLayout, delegate)
    }

    private fun configuraFormulario(id: Long?, concluido: Boolean, linearLayout: LinearLayout, delegate: (meta: Meta) -> Unit) {
        val titulo = tituloPor()

        val dialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setCancelable(false)
            .setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    closeDialog(linearLayout, dialog)
                }
                false
            }
            .setPositiveButton(tituloBotaoPositivo
            ) { _, _ ->
            }
            .setNegativeButton("Cancelar", null)
            .show()

        val buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        buttonPositive.setOnClickListener {
            if(campoDescricao.text.toString().isBlank()) {
                campoDescricao.error = context.getString(R.string.descricao_obrigatorio)
            } else if (verificaMetaComMesmaDescricao(campoDescricao.text.toString())) {
                campoDescricao.error = context.getString(R.string.descricao_meta_ja_existe)
            } else {
                val descricaoEmTexto = campoDescricao.text.toString().somentePrimeiraLetraMaiuscula()

                val metaCriada: Meta = if (id == null) {
                    Meta(
                        descricao = descricaoEmTexto,
                        concluido = false
                    )
                } else {
                    Meta(
                        id = id,
                        descricao = descricaoEmTexto,
                        concluido = concluido
                    )
                }

                util.hideKeyboard(campoDescricao, context)

                delegate(metaCriada)
                dialog.dismiss()
            }
        }

        buttonNegative.setOnClickListener {
            closeDialog(linearLayout, dialog)
        }
    }

    private fun closeDialog(
        linearLayout: LinearLayout,
        dialog: DialogInterface
    ) {
        util.retiraOpacidadeFundo(linearLayout)
        util.hideKeyboard(campoDescricao, context)
        dialog.dismiss()
    }

    private fun verificaMetaComMesmaDescricao(descricaoMeta: String): Boolean {
        metas.forEach() {
            if(it.descricao == descricaoMeta) {
                return true
            }
        }
        return false
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.form_metas,
                        viewGroup,
                        false)

        campoDescricao = view.findViewById<EditText>(R.id.descricao_meta)
        util.showKeyboard(campoDescricao, context)

        return view
    }
}