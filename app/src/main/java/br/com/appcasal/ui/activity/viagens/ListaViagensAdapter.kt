package br.com.appcasal.ui.activity.viagens

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.util.Util

class ListaViagensAdapter(
    private var viagens: List<Viagem>,
    private var context: Context,
    private var clickViagem: ClickViagem
) :
    RecyclerView.Adapter<ListaViagensAdapter.ViagemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViagemViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.viagem_item, parent, false)

        return ViagemViewHolder(viewCriada)
    }

    override fun onBindViewHolder(holder: ViagemViewHolder, position: Int) {
        holder.bind(viagens, position)
        val viagem = viagens[position]
        holder.itemView.setOnClickListener {
            clickViagem.clickViagem(viagem)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return viagens.size
    }

    var posicao = 0

    inner class ViagemViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        var localViagem = itemView.findViewById<TextView>(R.id.local_viagem)
        var dataInicioFim = itemView.findViewById<TextView>(R.id.data_inicio_e_fim)
        var estrela1 = itemView.findViewById<ImageView>(R.id.estrelaViagem1)
        var estrela2 = itemView.findViewById<ImageView>(R.id.estrelaViagem2)
        var estrela3 = itemView.findViewById<ImageView>(R.id.estrelaViagem3)
        var estrela4 = itemView.findViewById<ImageView>(R.id.estrelaViagem4)
        var estrela5 = itemView.findViewById<ImageView>(R.id.estrelaViagem5)
        private var util = Util()

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(viagens: List<Viagem>, posicao: Int) {
            val viagem = viagens[posicao]

            localViagem.text = viagem.local
            dataInicioFim.text = viagem.dataInicio + " até " + viagem.dataFim
            util.exibeEstrelas(viagem.nota, estrela1, estrela2, estrela3, estrela4, estrela5)
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
}

interface ClickViagem {
    fun clickViagem(viagem: Viagem)
}