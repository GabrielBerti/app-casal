package br.com.appcasal.ui.dialog.viagens

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import br.com.appcasal.util.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.util.Util
import br.com.appcasal.util.extension.converteParaCalendar
import java.util.*

abstract class FormularioViagemDialog(
    private val context: Context,
    private val viewGroup: ViewGroup
) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected lateinit var campoLocal: EditText
    protected lateinit var campoDataInicio: EditText
    protected lateinit var campoDataFim: EditText
    abstract protected val tituloBotaoPositivo: String

    protected var estrelaMarcarda: Double? = 1.0
    protected var viagemSemNota: Boolean = false
    protected lateinit var estrela1: ImageView
    protected lateinit var estrela2: ImageView
    protected lateinit var estrela3: ImageView
    protected lateinit var estrela4: ImageView
    protected lateinit var estrela5: ImageView
    protected lateinit var cbSemNota: CheckBox

    fun chama(id: Long?, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
        configuraCamposDatas()
        configuraFormulario(id, linearLayout, delegate)

        if(viagemSemNota) {
            cbSemNota.isChecked = true
        }
    }

    private fun configuraFormulario(
        id: Long?,
        linearLayout: LinearLayout,
        delegate: (viagem: Viagem) -> Unit
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
            .show()

        val buttonPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        buttonPositive.setOnClickListener {
            var localEmTexto = campoLocal.text.toString()
            val dataInicioEmTexto = campoDataInicio.text.toString()
            val dataFimEmTexto = campoDataFim.text.toString()

            if (localEmTexto.isBlank()) {
                campoLocal.error = context.getString(R.string.local_obrigatorio)
            } else {
                localEmTexto = localEmTexto.substring(0, 1).uppercase() + localEmTexto.substring(1)

                val viagemCriada =
                    Viagem(
                        id = id ?: 0,
                        local = localEmTexto,
                        dataInicio = dataInicioEmTexto.converteParaCalendar(),
                        dataFim = dataFimEmTexto.converteParaCalendar(),
                        nota = estrelaMarcarda,
                        lugaresVisitados = listOf(),
                        gastosViagens = listOf()
                    )

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
                }, ano, mes, dia)
                .show()
        }

        campoDataFim.setText(hoje.formataParaBrasileiro())
        campoDataFim.setOnClickListener {
            DatePickerDialog(context,
                { _, ano, mes, dia ->
                    val dataSelecionada = Calendar.getInstance()
                    dataSelecionada.set(ano, mes, dia)
                    campoDataFim.setText(dataSelecionada.formataParaBrasileiro())
                }, ano, mes, dia)
                .show()
        }
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.form_viagem,
                viewGroup,
                false
            )

        campoLocal = view.findViewById<EditText>(R.id.form_local)
        campoDataInicio = view.findViewById<EditText>(R.id.form_data_inicio_viagem)
        campoDataFim = view.findViewById<EditText>(R.id.form_data_final_viagem)
        estrela1 = view.findViewById<ImageView>(R.id.form_viagem_estrela1)
        estrela2 = view.findViewById<ImageView>(R.id.form_viagem_estrela2)
        estrela3 = view.findViewById<ImageView>(R.id.form_viagem_estrela3)
        estrela4 = view.findViewById<ImageView>(R.id.form_viagem_estrela4)
        estrela5 = view.findViewById<ImageView>(R.id.form_viagem_estrela5)
        cbSemNota = view.findViewById<CheckBox>(R.id.cb_sem_nota)

        setListeners()

        //util.showKeyboard(campoLocal, context)

        return view
    }

    private fun setListeners() {
        cbSemNota.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                desmarcaEstrelas()
                estrelaMarcarda = null
            }
        }

        estrela1.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()

                if (estrelaMarcarda == 1.0) {
                    estrelaMarcarda = 0.0
                } else {
                    it.alpha = 1f
                    estrelaMarcarda = 1.0
                }
            }
        }

        estrela2.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela1.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 2.0
            }
        }

        estrela3.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela1.alpha = 1f
                estrela2.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 3.0
            }
        }

        estrela4.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela1.alpha = 1f
                estrela2.alpha = 1f
                estrela3.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 4.0
            }
        }

        estrela5.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela1.alpha = 1f
                estrela2.alpha = 1f
                estrela3.alpha = 1f
                estrela4.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 5.0
            }
        }
    }

    private fun desmarcaEstrelas() {
        estrela1.alpha = 0.3f
        estrela2.alpha = 0.3f
        estrela3.alpha = 0.3f
        estrela4.alpha = 0.3f
        estrela5.alpha = 0.3f
    }
}