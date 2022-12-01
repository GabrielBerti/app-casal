package br.com.appcasal.ui.dialog.lugares_visitados

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import br.com.appcasal.R
import br.com.appcasal.model.LugarVisitado
import br.com.appcasal.util.Util

abstract class FormularioLugarVisitadoDialog(
        private val context: Context,
        private val viewGroup: ViewGroup) {

    private var util = Util()
    private val viewCriada = criaLayout()
    private var estrelaMarcarda = 1

    protected lateinit var nomeLugarVisitado: EditText
    abstract protected val tituloBotaoPositivo: String
    protected lateinit var estrela1: ImageView
    protected lateinit var estrela2: ImageView
    protected lateinit var estrela3: ImageView
    protected lateinit var estrela4: ImageView
    protected lateinit var estrela5: ImageView

    fun chama(id: Long?, idViagem: Long, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
        configuraFormulario(id, idViagem, delegate)
    }

    private fun configuraFormulario(id: Long?, idViagem: Long, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
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
            val nomeLugarVisitadoEmTexto = nomeLugarVisitado.text.toString()

            if(nomeLugarVisitadoEmTexto.isBlank()) {
                nomeLugarVisitado.error = context.getString(R.string.nome_lugar_visitado_obrigatorio)
            } else {
                var lugarVisitadoCriado: LugarVisitado = if (id == null) {
                    LugarVisitado(
                        nome = nomeLugarVisitadoEmTexto,
                        legal = estrelaMarcarda,
                        viagemId = idViagem
                    )
                } else {
                    LugarVisitado(
                        id = id,
                        nome = nomeLugarVisitadoEmTexto,
                        legal = estrelaMarcarda,
                        viagemId = idViagem
                    )
                }

                util.hideKeyboard(nomeLugarVisitado, context)

                delegate(lugarVisitadoCriado)
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
        util.hideKeyboard(nomeLugarVisitado, context)
        dialog.dismiss()
    }

    abstract protected fun tituloPor(): Int

    private fun criaLayout(): View {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.form_lugares_visitados_viagem,
                        viewGroup,
                        false)

        nomeLugarVisitado = view.findViewById<EditText>(R.id.form_nome_lugar_visitado)
        estrela1 = view.findViewById<ImageView>(R.id.form_estrela1)
        estrela2 = view.findViewById<ImageView>(R.id.form_estrela2)
        estrela3 = view.findViewById<ImageView>(R.id.form_estrela3)
        estrela4 = view.findViewById<ImageView>(R.id.form_estrela4)
        estrela5 = view.findViewById<ImageView>(R.id.form_estrela5)

        setListeners()

        return view
    }

    private fun setListeners() {
        estrela1.setOnClickListener {
            desmarcaEstrelas()
            it.alpha = 1f
            estrelaMarcarda = 1
        }

        estrela2.setOnClickListener {
            desmarcaEstrelas()
            estrela1.alpha = 1f
            it.alpha = 1f
            estrelaMarcarda = 2
        }

        estrela3.setOnClickListener {
            desmarcaEstrelas()
            estrela1.alpha = 1f
            estrela2.alpha = 1f
            it.alpha = 1f
            estrelaMarcarda = 3
        }

        estrela4.setOnClickListener {
            desmarcaEstrelas()
            estrela1.alpha = 1f
            estrela2.alpha = 1f
            estrela3.alpha = 1f
            it.alpha = 1f
            estrelaMarcarda = 4
        }

        estrela5.setOnClickListener {
            desmarcaEstrelas()
            estrela1.alpha = 1f
            estrela2.alpha = 1f
            estrela3.alpha = 1f
            estrela4.alpha = 1f
            it.alpha = 1f
            estrelaMarcarda = 5
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