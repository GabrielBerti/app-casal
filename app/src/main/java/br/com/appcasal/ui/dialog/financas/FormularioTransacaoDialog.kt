package br.com.appcasal.ui.dialog.financas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.model.Tipo
import br.com.appcasal.model.Transacao
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

    fun chama(tipo: Tipo, id: Long?, delegate: (transacao: Transacao) -> Unit) {
        configuraCampoData()
        configuraFormulario(tipo, id, delegate)
    }

    private fun configuraFormulario(tipo: Tipo, id: Long?, delegate: (transacao: Transacao) -> Unit) {
        val titulo = tituloPor(tipo)

        val dialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setPositiveButton(tituloBotaoPositivo
            ) { _, _ ->


            }
            .setNegativeButton("Cancelar", null)
            .show()

        val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener() {

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
    }

    private fun setError(descricao: String, valor: String) {
        if (descricao.isNullOrBlank()) {
            campoDescricao.error = context.getString(R.string.descricao_financas_obrigatorio)
        }

        if(valor.isNullOrBlank()) {
            campoValor.error = context.getString(R.string.descricao_financas_obrigatorio)
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