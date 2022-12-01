package br.com.appcasal.ui.dialog.gastos_viagem

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import br.com.appcasal.R
import br.com.appcasal.model.GastoViagem
import br.com.appcasal.util.Util

abstract class FormularioGastoViagemDialog(
        private val context: Context,
        private val viewGroup: ViewGroup) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected lateinit var campoValorGasto: EditText
    protected lateinit var campoDescricaoGasto: EditText
    abstract protected val tituloBotaoPositivo: String

    fun chama(id: Long?, idViagem: Long, delegate: (gastoViagem: GastoViagem) -> Unit) {
        configuraFormulario(id, idViagem, delegate)
    }

    private fun configuraFormulario(id: Long?, idViagem: Long, delegate: (gastoViagem: GastoViagem) -> Unit) {
        val titulo = tituloPor()

        val dialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setCancelable(false)
            .setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    closeDialog(dialog)
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
            val descricaoGastoEmTexto = campoDescricaoGasto.text.toString()
            val valorGastoEmTexto = campoValorGasto.text.toString()
            val valorGasto = util.converteCampoValor(valorGastoEmTexto, context)

            if(descricaoGastoEmTexto.isNullOrBlank()) {
                campoDescricaoGasto.error = context.getString(R.string.descricao_gasto_obrigatorio)
            } else if(valorGastoEmTexto.isNullOrBlank()) {
                campoValorGasto.error = context.getString(R.string.valor_gasto_obrigatorio)
            } else if(valorGastoEmTexto == "0") {
                campoValorGasto.error = context.getString(R.string.valor_gasto_maior_que_zero)
            } else {
                var gastoViagemCriada: GastoViagem = if (id == null) {
                    GastoViagem(
                        valor = valorGasto,
                        descricao = descricaoGastoEmTexto,
                        viagemId = idViagem
                    )
                } else {
                    GastoViagem(
                        id = id,
                        valor = valorGasto,
                        descricao = descricaoGastoEmTexto,
                        viagemId = idViagem
                    )
                }

                util.hideKeyboard(campoDescricaoGasto, context)

                delegate(gastoViagemCriada)
                dialog.dismiss()
            }
        }

        buttonNegative.setOnClickListener {
            closeDialog(dialog)
        }
    }

    private fun closeDialog(
        dialog: DialogInterface
    ) {
        util.hideKeyboard(campoDescricaoGasto, context)
        dialog.dismiss()
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.form_gastos_viagem,
                        viewGroup,
                        false)

        campoValorGasto = view.findViewById<EditText>(R.id.form_valor_gasto)
        campoDescricaoGasto = view.findViewById<EditText>(R.id.form_descricao_gasto)
        //util.showKeyboard(campoValorGasto, context)

        return view
    }
}