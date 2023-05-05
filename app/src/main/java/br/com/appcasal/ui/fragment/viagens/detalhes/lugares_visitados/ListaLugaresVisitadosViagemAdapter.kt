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
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LugarVisitadoViagemViewHolder {
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
            aplicaERetiraOpacidadeEstrelas(lugarVisitado.nota)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 1, Menu.NONE, "Remover")
        }

        private fun aplicaERetiraOpacidadeEstrelas(nota: Double) {
            if (nota == 0.0) {
                binding.estrelaViagem05.alpha = 0.3f
                binding.estrelaViagem10.alpha = 0.3f
                binding.estrelaViagem15.alpha = 0.3f
                binding.estrelaViagem20.alpha = 0.3f
                binding.estrelaViagem25.alpha = 0.3f
                binding.estrelaViagem30.alpha = 0.3f
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

            } else if (nota == 0.5) {
                binding.estrelaViagem10.alpha = 0.3f
                binding.estrelaViagem15.alpha = 0.3f
                binding.estrelaViagem20.alpha = 0.3f
                binding.estrelaViagem25.alpha = 0.3f
                binding.estrelaViagem30.alpha = 0.3f
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
            } else if (nota == 1.0) {
                binding.estrelaViagem15.alpha = 0.3f
                binding.estrelaViagem20.alpha = 0.3f
                binding.estrelaViagem25.alpha = 0.3f
                binding.estrelaViagem30.alpha = 0.3f
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
            } else if (nota == 1.5) {
                binding.estrelaViagem20.alpha = 0.3f
                binding.estrelaViagem25.alpha = 0.3f
                binding.estrelaViagem30.alpha = 0.3f
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
            } else if (nota == 2.0) {
                binding.estrelaViagem25.alpha = 0.3f
                binding.estrelaViagem30.alpha = 0.3f
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
            } else if (nota == 2.5) {
                binding.estrelaViagem30.alpha = 0.3f
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
                binding.estrelaViagem25.alpha = 1.0f

            } else if (nota == 3.0) {
                binding.estrelaViagem35.alpha = 0.3f
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
                binding.estrelaViagem25.alpha = 1.0f
                binding.estrelaViagem30.alpha = 1.0f

            } else if (nota == 3.5) {
                binding.estrelaViagem40.alpha = 0.3f
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
                binding.estrelaViagem25.alpha = 1.0f
                binding.estrelaViagem30.alpha = 1.0f
                binding.estrelaViagem35.alpha = 1.0f

            } else if (nota == 4.0) {
                binding.estrelaViagem45.alpha = 0.3f
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
                binding.estrelaViagem25.alpha = 1.0f
                binding.estrelaViagem30.alpha = 1.0f
                binding.estrelaViagem35.alpha = 1.0f
                binding.estrelaViagem40.alpha = 1.0f
            } else if (nota == 4.5) {
                binding.estrelaViagem50.alpha = 0.3f

                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
                binding.estrelaViagem25.alpha = 1.0f
                binding.estrelaViagem30.alpha = 1.0f
                binding.estrelaViagem35.alpha = 1.0f
                binding.estrelaViagem40.alpha = 1.0f
                binding.estrelaViagem45.alpha = 1.0f

            } else if (nota == 5.0) {
                binding.estrelaViagem05.alpha = 1.0f
                binding.estrelaViagem10.alpha = 1.0f
                binding.estrelaViagem15.alpha = 1.0f
                binding.estrelaViagem20.alpha = 1.0f
                binding.estrelaViagem25.alpha = 1.0f
                binding.estrelaViagem30.alpha = 1.0f
                binding.estrelaViagem35.alpha = 1.0f
                binding.estrelaViagem40.alpha = 1.0f
                binding.estrelaViagem45.alpha = 1.0f
                binding.estrelaViagem50.alpha = 1.0f
            }
        }
    }
}

interface ClickLugarVisitadoViagem {
    fun clickLugarVisitadoViagem(lugarVisitado: LugarVisitado)
}