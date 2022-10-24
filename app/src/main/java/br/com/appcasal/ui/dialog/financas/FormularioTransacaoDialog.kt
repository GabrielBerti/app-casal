package br.com.appcasal.ui.dialog.financas

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
import android.widget.Toast
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.model.Tipo
import br.com.appcasal.model.Transacao
import br.com.appcasal.util.Util
import java.math.BigDecimal
import java.util.Calendar

abstract class FormularioTransacaoDialog(
        private val context: Context,
        private val viewGroup: ViewGroup) {

    private val viewCriada = criaLayout()
    protected val campoValor =  viewCriada.findViewById<EditText>(R.id.form_transacao_valor)//formTransacaoBinding.formTransacaoValor
    protected val campoDescricao = viewCriada.findViewById<EditText>(R.id.descricao)//formTransacaoBinding.descricao
    protected val campoData = viewCriada.findViewById<EditText>(R.id.form_transacao_data)//formTransacaoBinding.formTransacaoData
    abstract protected val tituloBotaoPositivo: String
    private var util = Util()

    fun chama(tipo: Tipo, id: Long?, linearLayout: LinearLayout, delegate: (transacao: Transacao) -> Unit) {
        configuraCampoData()
        configuraFormulario(tipo, id, linearLayout, delegate)
    }

    private fun configuraFormulario(tipo: Tipo, id: Long?, linearLayout: LinearLayout, delegate: (transacao: Transacao) -> Unit) {
        val titulo = tituloPor(tipo)

        val dialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
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

        val buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        val buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        buttonPositive.setOnClickListener {
            val valorEmTexto = campoValor.text.toString()
            val dataEmTexto = campoData.text.toString()
            val descricaoEmTexto = campoDescricao.text.toString()

            if (isValidForm(descricaoEmTexto, valorEmTexto)) {
                val valor = converteCampoValor(valorEmTexto)
                val data = dataEmTexto//.converteParaCalendar()

                var transacaoCriada: Transacao

                if(id == null) {
                    transacaoCriada = Transacao(
                        tipo = tipo,
                        valor = valor,
                        data = data,
                        descricao = descricaoEmTexto
                    )
                } else {
                    transacaoCriada = Transacao(
                        id = id,
                        tipo = tipo,
                        valor = valor,
                        data = data,
                        descricao = descricaoEmTexto
                    )
                }

                delegate(transacaoCriada)
                dialog.dismiss()
            } else {
                setError(descricaoEmTexto, valorEmTexto)
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

    private fun setError(descricao: String, valor: String) {
        if (descricao.isNullOrBlank()) {
            campoDescricao.error = context.getString(R.string.descricao_financas_obrigatorio)
        }

        if(valor.isNullOrBlank()) {
            campoValor.error = context.getString(R.string.valor_obrigatorio)
        }
    }

    private fun isValidForm(descricao: String, valor: String): Boolean {
        if(descricao.isNullOrBlank() || valor.isNullOrBlank()) {
            return false
        }

        return true
    }

    abstract protected fun tituloPor(tipo: Tipo): Int

    private fun converteCampoValor(valorEmTexto: String): BigDecimal {
        return try {
            BigDecimal(valorEmTexto)
        } catch (exception: NumberFormatException) {
            Toast.makeText(
                context,
                "Falha na conversão de valor",
                    Toast.LENGTH_LONG)
                    .show()
            BigDecimal.ZERO
        }
    }

    private fun configuraCampoData() {
        val hoje = Calendar.getInstance()

        val ano = hoje.get(Calendar.YEAR)
        val mes = hoje.get(Calendar.MONTH)
        val dia = hoje.get(Calendar.DAY_OF_MONTH)

        campoData.setText(hoje.formataParaBrasileiro())
        campoData.setOnClickListener {
            DatePickerDialog(context,
                    { _, ano, mes, dia ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(ano, mes, dia)
                        campoData.setText(dataSelecionada.formataParaBrasileiro())
                    }
                    , ano, mes, dia)
                    .show()
        }
    }

    private fun criaLayout(): View {
        return LayoutInflater.from(context)
                .inflate(R.layout.form_transacao,
                        viewGroup,
                        false)
    }
}