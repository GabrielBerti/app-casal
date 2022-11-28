package br.com.appcasal.ui.activity.viagens.detalhes.lugares_visitados

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.model.GastoViagem
import br.com.appcasal.model.LugarVisitado
import br.com.appcasal.ui.activity.viagens.ClickViagem

class ListaLugaresVisitadosViagemAdapter(
    private var lugarVisitado: List<LugarVisitado>,
    private var context: Context,
    private var clickLugarVisitadoViagem: ClickLugarVisitadoViagem
) :
    RecyclerView.Adapter<ListaLugaresVisitadosViagemAdapter.LugarVisitadoViagemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarVisitadoViagemViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.lugar_visitado_item, parent, false)

        return LugarVisitadoViagemViewHolder(viewCriada)
    }

    override fun onBindViewHolder(holder: LugarVisitadoViagemViewHolder, position: Int) {
        holder.bind(lugarVisitado, position)
        val gastosViagem = lugarVisitado[position]
        holder.itemView.setOnClickListener {
            clickLugarVisitadoViagem.clickLugarVisitadoViagem(gastosViagem)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return lugarVisitado.size
    }

    var posicao = 0

    inner class LugarVisitadoViagemViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        private val nomeLugarVisitado = itemView.findViewById<TextView>(R.id.nome_lugar_visitado)
        private val estrela1 = itemView.findViewById<ImageView>(R.id.estrela1)
        private val estrela2 = itemView.findViewById<ImageView>(R.id.estrela2)
        private val estrela3 = itemView.findViewById<ImageView>(R.id.estrela3)
        private val estrela4 = itemView.findViewById<ImageView>(R.id.estrela4)
        private val estrela5 = itemView.findViewById<ImageView>(R.id.estrela5)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(lugarVisitado: List<LugarVisitado>, posicao: Int) {
            val lugarVisitado = lugarVisitado[posicao]

            nomeLugarVisitado.text = lugarVisitado.nome
            exibeEstrelas(lugarVisitado.legal)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 1, Menu.NONE, "Remover")
        }

        private fun exibeEstrelas(nota: Int) {
            when (nota) {
                1 -> {
                    estrela1.alpha = 1f
                }
                2 -> {
                    estrela1.alpha = 1f
                    estrela2.alpha = 1f
                }
                3 -> {
                    estrela1.alpha = 1f
                    estrela2.alpha = 1f
                    estrela3.alpha = 1f
                }
                4 -> {
                    estrela1.alpha = 1f
                    estrela2.alpha = 1f
                    estrela3.alpha = 1f
                    estrela4.alpha = 1f
                }
                5 -> {
                    estrela1.alpha = 1f
                    estrela2.alpha = 1f
                    estrela3.alpha = 1f
                    estrela4.alpha = 1f
                    estrela5.alpha = 1f
                }
            }
        }
    }
}

interface ClickLugarVisitadoViagem {
    fun clickLugarVisitadoViagem(lugarVisitado: LugarVisitado)
}