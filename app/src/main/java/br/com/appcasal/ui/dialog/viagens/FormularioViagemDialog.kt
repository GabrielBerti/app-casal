package br.com.appcasal.ui.dialog.viagens

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.model.Viagem
import br.com.appcasal.util.Util
import java.util.*


abstract class FormularioViagemDialog(
        private val context: Context,
        private val viewGroup: ViewGroup) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected lateinit var campoLocal: EditText
    protected lateinit var campoDataInicio: EditText
    protected lateinit var campoDataFim: EditText
    abstract protected val tituloBotaoPositivo: String

    fun chama(id: Long?, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
        configuraCamposDatas()
        configuraFormulario(id, linearLayout, delegate)
    }

    private fun configuraFormulario(id: Long?, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
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
            val localEmTexto = campoLocal.text.toString()
            val dataInicioEmTexto = campoDataInicio.text.toString()
            val dataFimEmTexto = campoDataFim.text.toString()

            if(localEmTexto.isNullOrBlank()) {
                campoLocal.error = context.getString(R.string.local_obrigatorio)
            } else {
                var viagemCriada: Viagem = if (id == null) {
                    Viagem(
                        local = localEmTexto,
                        dataInicio = dataInicioEmTexto,
                        dataFim = dataFimEmTexto
                    )
                } else {
                    Viagem(
                        id = id,
                        local = localEmTexto,
                        dataInicio = dataInicioEmTexto,
                        dataFim = dataFimEmTexto
                    )
                }

                util.hideKeyboard(campoLocal, context)

                delegate(viagemCriada)
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
        util.hideKeyboard(campoLocal, context)
        dialog.dismiss()
    }

    private fun configuraCamposDatas() {
        val hoje = Calendar.getInstance()

        val ano = hoje.get(Calendar.YEAR)
        val mes = hoje.get(Calendar.MONTH)
        val dia = hoje.get(Calendar.DAY_OF_MONTH)

        campoDataInicio.setText(hoje.formataParaBrasileiro())
        campoDataInicio.setOnClickListener {
            DatePickerDialog(context,
                { _, ano, mes, dia ->
                    val dataSelecionada = Calendar.getInstance()
                    dataSelecionada.set(ano, mes, dia)
                    campoDataInicio.setText(dataSelecionada.formataParaBrasileiro())
                }
                , ano, mes, dia)
                .show()
        }

        campoDataFim.setText(hoje.formataParaBrasileiro())
        campoDataFim.setOnClickListener {
            DatePickerDialog(context,
                { _, ano, mes, dia ->
                    val dataSelecionada = Calendar.getInstance()
                    dataSelecionada.set(ano, mes, dia)
                    campoDataFim.setText(dataSelecionada.formataParaBrasileiro())
                }
                , ano, mes, dia)
                .show()
        }
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.form_viagem,
                        viewGroup,
                        false)

        campoLocal = view.findViewById<EditText>(R.id.form_local)
        campoDataInicio = view.findViewById<EditText>(R.id.form_data_inicio_viagem)
        campoDataFim = view.findViewById<EditText>(R.id.form_data_final_viagem)
        util.showKeyboard(campoLocal, context)

        return view
    }
}