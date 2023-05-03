package br.com.appcasal.ui.fragment.receitas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Receita

class ListaReceitasAdapter(
    private var receitas: List<Receita>,
    private var context: Context,
    private var clickReceita: ClickReceita
) :
    RecyclerView.Adapter<ReceitasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitasViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.receita_item, parent, false)

        return ReceitasViewHolder(context, viewCriada)
    }

    override fun onBindViewHolder(holder: ReceitasViewHolder, position: Int) {
        holder.bind(receitas, position)
        val receita = receitas[position]
        holder.itemView.setOnClickListener {
            clickReceita.clickReceita(receita)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return receitas.size
    }

    var posicao = 0

}

interface ClickReceita {
    fun clickReceita(receita: Receita)
}