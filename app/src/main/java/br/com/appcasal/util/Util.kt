package br.com.appcasal.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

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

}