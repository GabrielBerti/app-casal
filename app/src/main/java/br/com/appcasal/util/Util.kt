package br.com.appcasal.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.updatePadding
import br.com.appcasal.R
import br.com.appcasal.domain.model.TipoSnackbar
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

class Util {
    fun showKeyboard(campo: EditText, context: Context) {
        campo.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun hideKeyboard(campo: EditText, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(campo.windowToken, 0)
    }

    fun createSnackBar(view: View, msg: String, resources: Resources, tipoSnackbar: TipoSnackbar) {
        val colorBackground = if (tipoSnackbar == TipoSnackbar.SUCESSO) {
            resources.getColor(R.color.colorPrimaryVariant)
        } else {
            resources.getColor(R.color.red)
        }

        // Create the Snackbar
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(colorBackground)
            .setAction(resources.getString(R.string.fechar)) {

            }

        val layout = snackbar.view

        layout.setPadding(40, 15, 15, 50)
        layout.updatePadding(0, 0, 0, 50)
        snackbar.show()
    }

    fun createSnackBarWithReturn(
        view: View, msg: String, resources: Resources, tipoSnackbar: TipoSnackbar
    ): Snackbar {
        val colorBackground = if (tipoSnackbar == TipoSnackbar.SUCESSO) {
            resources.getColor(R.color.colorPrimaryVariant)
        } else {
            resources.getColor(R.color.red)
        }

        // Create the Snackbar
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(colorBackground)
            .setAction(resources.getString(R.string.fechar)) {

            }

        val layout = snackbar.view

        layout.setPadding(40, 15, 15, 50)
        layout.updatePadding(0, 0, 0, 50)

        snackbar.show()

        return snackbar
    }

    fun aplicaOpacidadeFundo(linearLayout: LinearLayout) {
        linearLayout.alpha = 0.3f
    }

    fun retiraOpacidadeFundo(linearLayout: LinearLayout) {
        linearLayout.alpha = 1.0f
    }

    fun converteCampoValor(valorEmTexto: String, context: Context): BigDecimal {
        return try {
            BigDecimal(valorEmTexto)
        } catch (exception: NumberFormatException) {
            Toast.makeText(
                context,
                "Falha na conversão de valor",
                Toast.LENGTH_LONG
            )
                .show()
            BigDecimal.ZERO
        }
    }

    fun exibeEstrelas(
        nota: Double,
        estrela1: ImageView,
        estrela2: ImageView,
        estrela3: ImageView,
        estrela4: ImageView,
        estrela5: ImageView
    ) {
        when (nota) {
            1.0 -> {
                estrela1.alpha = 1f
            }
            2.0 -> {
                estrela1.alpha = 1f
                estrela2.alpha = 1f
            }
            3.0 -> {
                estrela1.alpha = 1f
                estrela2.alpha = 1f
                estrela3.alpha = 1f
            }
            4.0 -> {
                estrela1.alpha = 1f
                estrela2.alpha = 1f
                estrela3.alpha = 1f
                estrela4.alpha = 1f
            }
            5.0 -> {
                estrela1.alpha = 1f
                estrela2.alpha = 1f
                estrela3.alpha = 1f
                estrela4.alpha = 1f
                estrela5.alpha = 1f
            }
        }
    }
}