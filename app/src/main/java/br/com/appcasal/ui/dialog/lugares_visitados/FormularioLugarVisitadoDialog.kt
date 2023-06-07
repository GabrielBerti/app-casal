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
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.util.Util

abstract class FormularioLugarVisitadoDialog(
        private val context: Context,
        private val viewGroup: ViewGroup) {

    private var util = Util()
    private val viewCriada = criaLayout()
    protected var estrelaMarcarda: Double = 1.0

    protected lateinit var nomeLugarVisitado: EditText
    abstract protected val tituloBotaoPositivo: String
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

    fun chama(id: Long?, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
        configuraFormulario(id, delegate)
    }

    private fun configuraFormulario(id: Long?, delegate: (lugarVisitado: LugarVisitado) -> Unit) {
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
                val lugarVisitadoCriado: LugarVisitado = if (id == null) {
                    LugarVisitado(
                        nome = nomeLugarVisitadoEmTexto,
                        nota = estrelaMarcarda,
                    )
                } else {
                    LugarVisitado(
                        id = id,
                        nome = nomeLugarVisitadoEmTexto,
                        nota = estrelaMarcarda,
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
        estrela05 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem05)
        estrela10 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem10)
        estrela15 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem15)
        estrela20 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem20)
        estrela25 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem25)
        estrela30 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem30)
        estrela35 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem35)
        estrela40 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem40)
        estrela45 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem45)
        estrela50 = view.findViewById<ImageView>(R.id.formLvViagemEstrelaViagem50)

        setListeners()

        return view
    }

    private fun setListeners() {

        estrela05.setOnClickListener {
                desmarcaEstrelas()
                if (estrelaMarcarda == 0.5) {
                    estrelaMarcarda = 0.0
                } else {
                    it.alpha = 1f
                    estrelaMarcarda = 0.5
                }
        }

        estrela10.setOnClickListener {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 1.0
        }

        estrela15.setOnClickListener {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 1.5
        }

        estrela20.setOnClickListener {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 2.0
        }

        estrela25.setOnClickListener {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 2.5
        }

        estrela30.setOnClickListener {
                desmarcaEstrelas()
                estrela05.alpha = 1f
                estrela10.alpha = 1f
                estrela15.alpha = 1f
                estrela20.alpha = 1f
                estrela25.alpha = 1f
                it.alpha = 1f
                estrelaMarcarda = 3.0
        }

        estrela35.setOnClickListener {
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

        estrela40.setOnClickListener {
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

        estrela45.setOnClickListener {
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


        estrela50.setOnClickListener {
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