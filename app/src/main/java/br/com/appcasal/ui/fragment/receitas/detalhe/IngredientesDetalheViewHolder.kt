package br.com.appcasal.ui.fragment.receitas.detalhe

import android.graphics.Paint
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Ingrediente

class IngredientesDetalheViewHolder(
    itemView: View) :
    RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    var ingredienteDescricao = itemView.findViewById<TextView>(R.id.ingrediente_detalhe_descricao)
    var checkBoxIngrediente = itemView.findViewById<CheckBox>(R.id.ingrediente_detalhe_check_box)

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    fun bind(ingredientes: List<Ingrediente>, posicao: Int) {
        val ingrediente = ingredientes[posicao]
        iniciaDescricaoIngrediente(ingrediente)
        iniciaCheckBox(ingrediente)
        setListeners()
    }

    private fun setListeners() {
        checkBoxIngrediente.setOnClickListener {
            if (it.isSelected) {
                ingredienteDescricao.paintFlags =
                    ingredienteDescricao.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                ingredienteDescricao.paintFlags =
                    ingredienteDescricao.paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun iniciaCheckBox(ingrediente: Ingrediente) {
        checkBoxIngrediente.isChecked = ingrediente.marcado
        if (checkBoxIngrediente.isChecked) {
            ingredienteDescricao.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun iniciaDescricaoIngrediente(ingrediente: Ingrediente) {
        ingredienteDescricao.text = ingrediente.descricao
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        menu?.add(Menu.NONE, 1, Menu.NONE, "Remover")
    }


}