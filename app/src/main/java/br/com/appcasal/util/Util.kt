package br.com.appcasal.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.updatePadding
import br.com.appcasal.R
import br.com.appcasal.model.TipoSnackbar
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
        imm.hideSoftInputFromWindow(campo.windowToken, 0);
    }

    fun createSnackBar(view: View, msg: String, resources: Resources, tipoSnackbar: TipoSnackbar) {
        val colorBackground = if(tipoSnackbar == TipoSnackbar.SUCESSO) {
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
                null
            }

        val layout = snackbar.view

        layout.setPadding(40, 15, 15, 50)
        layout.updatePadding(0, 0, 0, 50)
        snackbar.show()
    }

    fun createSnackBarWithReturn(view: View, msg: String, resources: Resources, tipoSnackbar: TipoSnackbar,
                                 visibility: Int): Snackbar {
        val colorBackground = if(tipoSnackbar == TipoSnackbar.SUCESSO) {
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
                null
            }

        val layout = snackbar.view

        layout.setPadding(40, 15, 15, 50)
        layout.updatePadding(0, 0, 0, 50)
        if(visibility == View.VISIBLE) {
            snackbar.show()
        }

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
                Toast.LENGTH_LONG)
                .show()
            BigDecimal.ZERO
        }
    }
}