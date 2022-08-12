package br.com.appcasal.ui.activity.receitas.cadastro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.model.Ingrediente

class ListaIngredientesAdapter(
    private var ingredientes: List<Ingrediente>,
    private var context: Context,
    private var clickIngrediente: ClickIngrediente
) :
    RecyclerView.Adapter<IngredientesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientesViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.ingrediente_item, parent, false)

        return IngredientesViewHolder(viewCriada)
    }

    override fun onViewRecycled(holder: IngredientesViewHolder) {
        holder.itemView.setOnLongClickListener(null)

        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: IngredientesViewHolder, position: Int) {
        holder.bind(ingredientes, position)
        val ingrediente = ingredientes[position]

        holder.itemView.setOnClickListener {
            clickIngrediente.clickIngrediente(ingrediente, position)
        }


        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return ingredientes.size
    }

    var posicao = 0

}

interface ClickIngrediente {
    fun clickIngrediente(ingrediente: Ingrediente, posicao: Int)
}