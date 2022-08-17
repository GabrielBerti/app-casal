package br.com.appcasal.ui.dialog.ingredientes

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import br.com.appcasal.R
import br.com.appcasal.model.Ingrediente
import br.com.appcasal.util.Util


abstract class FormularioIngredienteDialog(
    private val context: Context,
    private val viewGroup: ViewGroup) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected lateinit var campoDescricaoIngrediente: EditText
    abstract protected val tituloBotaoPositivo: String

    fun chama(id: Long?, idReceita: Long, delegate: (ingrediente: Ingrediente) -> Unit) {
        configuraFormulario(id, idReceita, delegate)
    }

    private fun configuraFormulario(id: Long?, idReceita: Long, delegate: (ingrediente: Ingrediente) -> Unit) {
        val titulo = tituloPor()

        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setPositiveButton(tituloBotaoPositivo
            ) { _, _ ->

                val campoDescricaoIngredienteEmTexto = campoDescricaoIngrediente.text.toString()

                var ingredienteCriado: Ingrediente

                if(id == null) {
                    ingredienteCriado = Ingrediente(
                        receitaId = idReceita,
                        descricao = campoDescricaoIngredienteEmTexto,
                        marcado = false
                    )
                } else {
                    ingredienteCriado = Ingrediente(
                        id = id,
                        receitaId = idReceita,
                        descricao = campoDescricaoIngredienteEmTexto,
                        marcado = false
                    )
                }

                util.hideKeyboard(campoDescricaoIngrediente, context)

                delegate(ingredienteCriado)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.form_ingrediente,
                viewGroup,
                false)

        campoDescricaoIngrediente = view.findViewById<EditText>(R.id.descricao_ingrediente)
        util.showKeyboard(campoDescricaoIngrediente, context)

        return view
    }
}