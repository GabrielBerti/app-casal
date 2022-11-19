package br.com.appcasal.ui.activity.viagens.detalhes.gastos_viagem

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.model.GastoViagem

class ListaGastosViagemAdapter(
    private var gastoViagem: List<GastoViagem>,
    private var context: Context,
    private var clickGastoViagem: ClickGastoViagem
) :
    RecyclerView.Adapter<ListaGastosViagemAdapter.GastosViagemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastosViagemViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.gastos_viagem_item, parent, false)

        return GastosViagemViewHolder(viewCriada)
    }

    override fun onBindViewHolder(holder: GastosViagemViewHolder, position: Int) {
        holder.bind(gastoViagem, position)
        val gastosViagem = gastoViagem[position]
        holder.itemView.setOnClickListener {
            clickGastoViagem.clickGastoViagem(gastosViagem)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return gastoViagem.size
    }

    var posicao = 0

    inner class GastosViagemViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        private val descricaoGasto = itemView.findViewById<TextView>(R.id.descricao_gasto)
        private val valorGasto = itemView.findViewById<TextView>(R.id.valor_gasto)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(gastoViagem: List<GastoViagem>, posicao: Int) {
            val gastoViagem = gastoViagem[posicao]

            descricaoGasto.text = gastoViagem.descricao
            valorGasto.text = gastoViagem.valor.formataParaBrasileiro()
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 1, Menu.NONE, "Remover")
        }
    }
}

interface ClickGastoViagem {
    fun clickGastoViagem(gastoViagem: GastoViagem)
}