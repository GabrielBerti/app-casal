package br.com.appcasal.ui.fragment.viagens

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.util.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.databinding.ViagemItemBinding
import br.com.appcasal.domain.model.Viagem

class ListaViagensAdapter(
    private var viagens: List<Viagem>,
    private var context: Context,
    private var clickViagem: ClickViagem
) :
    RecyclerView.Adapter<ListaViagensAdapter.ViagemViewHolder>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViagemViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.viagem_item, parent, false)

        return ViagemViewHolder(viewCriada)
    }

    override fun onBindViewHolder(holder: ViagemViewHolder, position: Int) {
        val viagem = viagens[position]

        holder.bind(viagem)
        holder.itemView.setOnClickListener {
            clickViagem.clickViagem(viagem)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount() = viagens.size

    var posicao = 0

    inner class ViagemViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        val binding = ViagemItemBinding.bind(itemView)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(viagem: Viagem) {
            binding.localViagem.text = viagem.local
            binding.dataInicioEFim.text = viagem.dataInicio.formataParaBrasileiro() + " até " + viagem.dataFim.formataParaBrasileiro()

            when (viagem.nota) {
                null -> {
                    escondeOuMostraEstrelas(true)
                }
                0.0 -> {
                    escondeOuMostraEstrelas(false)
                    aplicaERetiraOpacidadeEstrelas(0.0)
                }
                1.0 -> {
                    escondeOuMostraEstrelas(false)
                    aplicaERetiraOpacidadeEstrelas(1.0)
                }
                2.0 -> {
                    escondeOuMostraEstrelas(false)
                    aplicaERetiraOpacidadeEstrelas(2.0)
                }
                3.0 -> {
                    escondeOuMostraEstrelas(false)
                    aplicaERetiraOpacidadeEstrelas(3.0)
                }
                4.0 -> {
                    escondeOuMostraEstrelas(false)
                    aplicaERetiraOpacidadeEstrelas(4.0)
                }
                5.0 -> {
                    escondeOuMostraEstrelas(false)
                    aplicaERetiraOpacidadeEstrelas(5.0)
                }
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 1, Menu.NONE, "Alterar")
            menu?.add(Menu.NONE, 2, Menu.NONE, "Remover")
        }

        private fun escondeOuMostraEstrelas(notaIsNull: Boolean) {
            if(notaIsNull) {
                binding.estrelaViagem1.visibility = View.GONE
                binding.estrelaViagem2.visibility = View.GONE
                binding.estrelaViagem3.visibility = View.GONE
                binding.estrelaViagem4.visibility = View.GONE
                binding.estrelaViagem5.visibility = View.GONE
            } else {
                binding.estrelaViagem1.visibility = View.VISIBLE
                binding.estrelaViagem2.visibility = View.VISIBLE
                binding.estrelaViagem3.visibility = View.VISIBLE
                binding.estrelaViagem4.visibility = View.VISIBLE
                binding.estrelaViagem5.visibility = View.VISIBLE
            }
        }

        private fun aplicaERetiraOpacidadeEstrelas(nota: Double) {
            if(nota == 0.0) {
                binding.estrelaViagem1.alpha = 0.3f
                binding.estrelaViagem2.alpha = 0.3f
                binding.estrelaViagem3.alpha = 0.3f
                binding.estrelaViagem4.alpha = 0.3f
                binding.estrelaViagem5.alpha = 0.3f
            } else if(nota == 1.0) {
                binding.estrelaViagem2.alpha = 0.3f
                binding.estrelaViagem3.alpha = 0.3f
                binding.estrelaViagem4.alpha = 0.3f
                binding.estrelaViagem5.alpha = 0.3f

                binding.estrelaViagem1.alpha = 1f
            } else if(nota == 2.0) {
                binding.estrelaViagem3.alpha = 0.3f
                binding.estrelaViagem4.alpha = 0.3f
                binding.estrelaViagem5.alpha = 0.3f

                binding.estrelaViagem1.alpha = 1f
                binding.estrelaViagem2.alpha = 1f
            } else if(nota == 3.0) {
                binding.estrelaViagem4.alpha = 0.3f
                binding.estrelaViagem5.alpha = 0.3f

                binding.estrelaViagem1.alpha = 1f
                binding.estrelaViagem2.alpha = 1f
                binding.estrelaViagem3.alpha = 1f
            } else if(nota == 4.0) {
                binding.estrelaViagem5.alpha = 0.3f

                binding.estrelaViagem1.alpha = 1f
                binding.estrelaViagem2.alpha = 1f
                binding.estrelaViagem3.alpha = 1f
                binding.estrelaViagem4.alpha = 1f
            } else if(nota == 5.0) {
                binding.estrelaViagem1.alpha = 1f
                binding.estrelaViagem2.alpha = 1f
                binding.estrelaViagem3.alpha = 1f
                binding.estrelaViagem4.alpha = 1f
                binding.estrelaViagem5.alpha = 1f
            }
        }
    }
}

interface ClickViagem {
    fun clickViagem(viagem: Viagem)
}