package br.com.appcasal.ui.activity.receitas

import android.content.Context
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.util.extension.somentePrimeiraLetraMaiuscula

class ReceitasViewHolder(
    private val context: Context,
    itemView: View
) :
    RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    var receitaNome = itemView.findViewById<TextView>(R.id.receita_nome)
    var receitaIcone = itemView.findViewById<ImageView>(R.id.receita_icone)

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    fun bind(receitas: List<Receita>, posicao: Int) {
        val receita = receitas[posicao]

        adicionaIcone()
        adicionaNomeReceita(receita)

    }

    private fun adicionaNomeReceita(receita: Receita) {
        receitaNome.text = receita.nome.somentePrimeiraLetraMaiuscula()
    }

    private fun adicionaIcone() {
        receitaIcone.setBackgroundResource(R.drawable.ic_pizza)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        menu?.add(Menu.NONE, 1, Menu.NONE, "Alterar")
        menu?.add(Menu.NONE, 2, Menu.NONE, "Remover")
    }


}