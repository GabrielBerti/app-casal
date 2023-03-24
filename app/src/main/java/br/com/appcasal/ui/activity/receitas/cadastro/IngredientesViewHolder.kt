package br.com.appcasal.ui.activity.receitas.cadastro

import android.graphics.Paint
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Ingrediente

class IngredientesViewHolder(
    itemView: View
) :
    RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    var ingredienteDescricao = itemView.findViewById<TextView>(R.id.ingrediente_descricao)

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    fun bind(ingredientes: List<Ingrediente>, posicao: Int) {
        val ingrediente = ingredientes[posicao]
        adicionaDescricaoIngrediente(ingrediente)
    }

    private fun adicionaDescricaoIngrediente(ingrediente: Ingrediente) {
        ingredienteDescricao.paintFlags = ingredienteDescricao.paintFlags or Paint.UNDERLINE_TEXT_FLAG
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