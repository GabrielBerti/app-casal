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
import br.com.appcasal.util.extension.somentePrimeiraLetraMaiuscula
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

    protected lateinit var estrela05: ImageView
    protected lateinit var estrela10: ImageView
    protected lateinit var estrela15: ImageView
    protected lateinit var estrela20: ImageView
    protected lateinit var estrela25: ImageView
    protected lateinit var estrela30: ImageView
    protected lateinit var estrela35: ImageView
    protected lateinit var estrela40: ImageView
    protected lateinit var estrela45: ImageView
    protected lateinit var estrela50: ImageView
    protected lateinit var cbSemNota: CheckBox

    fun chama(id: Long?, linearLayout: LinearLayout, delegate: (viagem: Viagem) -> Unit) {
        configuraCamposDatas(id)
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
            val localEmTexto = campoLocal.text.toString()
            val dataInicioEmTexto = campoDataInicio.text.toString()
            val dataFimEmTexto = campoDataFim.text.toString()

            if (localEmTexto.isBlank()) {
                campoLocal.error = context.getString(R.string.local_obrigatorio)
            } else {

                val viagemCriada =
                    Viagem(
                        id = id ?: 0,
                        local = localEmTexto.somentePrimeiraLetraMaiuscula(),
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

    private fun configuraCamposDatas(id: Long?) {
        val hoje = Calendar.getInstance()

        val ano = hoje.get(Calendar.YEAR)
        val mes = hoje.get(Calendar.MONTH)
        val dia = hoje.get(Calendar.DAY_OF_MONTH)

        if ((id ?: 0.0) == 0.0) {
            campoDataInicio.setText(hoje.formataParaBrasileiro())
            campoDataFim.setText(hoje.formataParaBrasileiro())
        }

        campoDataInicio.setOnClickListener {
            DatePickerDialog(context,
                { _, ano, mes, dia ->
                    val dataSelecionada = Calendar.getInstance()
                    dataSelecionada.set(ano, mes, dia)
                    campoDataInicio.setText(dataSelecionada.formataParaBrasileiro())
                }, ano, mes, dia)
                .show()
        }

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
        estrela05 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem05)
        estrela10 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem10)
        estrela15 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem15)
        estrela20 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem20)
        estrela25 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem25)
        estrela30 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem30)
        estrela35 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem35)
        estrela40 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem40)
        estrela45 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem45)
        estrela50 = view.findViewById<ImageView>(R.id.formViagemEstrelaViagem50)
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

        estrela05.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()

                if (estrelaMarcarda == 0.5) {
                    estrelaMarcarda = 0.0
                } else {
                    it.alpha = 1f
                    estrelaMarcarda = 0.5
                }
            }
        }

        estrela10.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 1.0
            }
        }

        estrela15.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 1.5
            }
        }

        estrela20.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 2.0
            }
        }

        estrela25.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 2.5
            }
        }

        estrela30.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                estrela25.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 3.0
            }
        }

        estrela35.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                estrela25.alpha = 1f
                estrela30.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 3.5
            }
        }

        estrela40.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                estrela25.alpha = 1f
                estrela30.alpha = 1f
                estrela35.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 4.0
            }
        }

        estrela45.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                estrela25.alpha = 1f
                estrela30.alpha = 1f
                estrela35.alpha = 1f
                estrela40.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 4.5
            }
        }

        estrela50.setOnClickListener {
            if (!cbSemNota.isChecked) {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                estrela25.alpha = 1f
                estrela30.alpha = 1f
                estrela35.alpha = 1f
                estrela40.alpha = 1f
                estrela45.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 5.0
            }
        }
    }

    private fun desmarcaEstrelas() {
        estrela05.alpha = 0.3f
        estrela10.alpha = 0.3f
        estrela15.alpha = 0.3f
        estrela20.alpha = 0.3f
        estrela25.alpha = 0.3f
        estrela30.alpha = 0.3f
        estrela35.alpha = 0.3f
        estrela40.alpha = 0.3f
        estrela45.alpha = 0.3f
        estrela50.alpha = 0.3f
    }
}