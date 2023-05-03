package br.com.appcasal.ui.fragment.viagens.detalhes.lugares_visitados

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.databinding.LugarVisitadoItemBinding
import br.com.appcasal.domain.model.LugarVisitado

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
        val binding = LugarVisitadoItemBinding.bind(itemView)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(lugaresVisitados: List<LugarVisitado>, posicao: Int) {
            val lugarVisitado = lugaresVisitados[posicao]

            binding.nomeLugarVisitado.text = lugarVisitado.nome
            when (lugarVisitado.nota) {
                0.0 -> {
                    aplicaERetiraOpacidadeEstrelas(0.0)
                }
                1.0 -> {
                    aplicaERetiraOpacidadeEstrelas(1.0)
                }
                2.0 -> {
                    aplicaERetiraOpacidadeEstrelas(2.0)
                }
                3.0 -> {
                    aplicaERetiraOpacidadeEstrelas(3.0)
                }
                4.0 -> {
                    aplicaERetiraOpacidadeEstrelas(4.0)
                }
                5.0 -> {
                    aplicaERetiraOpacidadeEstrelas(5.0)
                }
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 1, Menu.NONE, "Remover")
        }

        private fun aplicaERetiraOpacidadeEstrelas(nota: Double) {
            if(nota == 0.0) {
                binding.estrela1.alpha = 0.3f
                binding.estrela2.alpha = 0.3f
                binding.estrela3.alpha = 0.3f
                binding.estrela4.alpha = 0.3f
                binding.estrela5.alpha = 0.3f
            } else if(nota == 1.0) {
                binding.estrela2.alpha = 0.3f
                binding.estrela3.alpha = 0.3f
                binding.estrela4.alpha = 0.3f
                binding.estrela5.alpha = 0.3f

                binding.estrela1.alpha = 1f
            } else if(nota == 2.0) {
                binding.estrela3.alpha = 0.3f
                binding.estrela4.alpha = 0.3f
                binding.estrela5.alpha = 0.3f

                binding.estrela1.alpha = 1f
                binding.estrela2.alpha = 1f
            } else if(nota == 3.0) {
                binding.estrela4.alpha = 0.3f
                binding.estrela5.alpha = 0.3f

                binding.estrela1.alpha = 1f
                binding.estrela2.alpha = 1f
                binding.estrela3.alpha = 1f
            } else if(nota == 4.0) {
                binding.estrela5.alpha = 0.3f

                binding.estrela1.alpha = 1f
                binding.estrela2.alpha = 1f
                binding.estrela3.alpha = 1f
                binding.estrela4.alpha = 1f
            } else if(nota == 5.0) {
                binding.estrela1.alpha = 1f
                binding.estrela2.alpha = 1f
                binding.estrela3.alpha = 1f
                binding.estrela4.alpha = 1f
                binding.estrela5.alpha = 1f
            }
        }
    }
}

interface ClickLugarVisitadoViagem {
    fun clickLugarVisitadoViagem(lugarVisitado: LugarVisitado)
}