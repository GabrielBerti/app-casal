package br.com.appcasal.ui.dialog.ingredientes

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
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.util.Util


abstract class FormularioIngredienteDialog(
    private val context: Context,
    private val viewGroup: ViewGroup
) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected lateinit var campoDescricaoIngrediente: EditText
    abstract protected val tituloBotaoPositivo: String

    fun chama(id: Long?, linearLayout: LinearLayout, delegate: (ingrediente: Ingrediente) -> Unit) {
        configuraFormulario(id, linearLayout, delegate)
    }

    private fun configuraFormulario(
        id: Long?,
        linearLayout: LinearLayout,
        delegate: (ingrediente: Ingrediente) -> Unit
    ) {
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
            .setPositiveButton(
                tituloBotaoPositivo
            ) { _, _ ->
            }
            .setNegativeButton("Cancelar", null)
            .setCancelable(false)
            .show()

        val buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            var campoDescricaoIngredienteEmTexto = campoDescricaoIngrediente.text.toString()

            if (campoDescricaoIngredienteEmTexto.isBlank()) {
                campoDescricaoIngrediente.error =
                    context.getString(R.string.nome_ingrediente_obrigatorio)
            } else {
                campoDescricaoIngredienteEmTexto = campoDescricaoIngredienteEmTexto.substring(0, 1).uppercase() + campoDescricaoIngredienteEmTexto.substring(1).lowercase()

                val ingredienteCriado: Ingrediente = if (id == null) {
                    Ingrediente(
                        //receitaId = idReceita,
                        descricao = campoDescricaoIngredienteEmTexto,
                        marcado = false
                    )
                } else {
                    Ingrediente(
                        id = id,
                       // receitaId = idReceita,
                        descricao = campoDescricaoIngredienteEmTexto,
                        marcado = false
                    )
                }

                util.hideKeyboard(campoDescricaoIngrediente, context)

                delegate(ingredienteCriado)
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
        util.hideKeyboard(campoDescricaoIngrediente, context)
        dialog.dismiss()
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.form_ingrediente,
                viewGroup,
                false
            )

        campoDescricaoIngrediente = view.findViewById<EditText>(R.id.descricao_ingrediente)
        util.showKeyboard(campoDescricaoIngrediente, context)

        return view
    }
}