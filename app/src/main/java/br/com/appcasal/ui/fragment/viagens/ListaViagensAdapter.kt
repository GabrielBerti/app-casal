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

    override fun onViewRecycled(holder: ListaViagensAdapter.ViagemViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
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

            if (viagem.nota == null) {
                escondeOuMostraEstrelas(true)
            } else {
                escondeOuMostraEstrelas(false)
                aplicaERetiraOpacidadeEstrelas(viagem.nota)
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
                binding.estrelaViagem05.visibility = View.GONE
                binding.estrelaViagem10.visibility = View.GONE
                binding.estrelaViagem15.visibility = View.GONE
                binding.estrelaViagem20.visibility = View.GONE
                binding.estrelaViagem25.visibility = View.GONE
                binding.estrelaViagem30.visibility = View.GONE
                binding.estrelaViagem35.visibility = View.GONE
                binding.estrelaViagem40.visibility = View.GONE
                binding.estrelaViagem45.visibility = View.GONE
                binding.estrelaViagem50.visibility = View.GONE
            } else {
                binding.estrelaViagem05.visibility = View.VISIBLE
                binding.estrelaViagem10.visibility = View.VISIBLE
                binding.estrelaViagem15.visibility = View.VISIBLE
                binding.estrelaViagem20.visibility = View.VISIBLE
                binding.estrelaViagem25.visibility = View.VISIBLE
                binding.estrelaViagem30.visibility = View.VISIBLE
                binding.estrelaViagem35.visibility = View.VISIBLE
                binding.estrelaViagem40.visibility = View.VISIBLE
                binding.estrelaViagem45.visibility = View.VISIBLE
                binding.estrelaViagem50.visibility = View.VISIBLE
            }
        }

        private fun aplicaERetiraOpacidadeEstrelas(nota: Double) {
            if(nota == 0.0) {
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

            } else if(nota == 0.5) {
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
            } else if(nota == 1.0) {
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
            } else if(nota == 1.5) {
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
            } else if(nota == 2.0) {
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
            } else if(nota == 2.5) {
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

            } else if(nota == 3.0) {
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

            } else if(nota == 3.5) {
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

            } else if(nota == 4.0) {
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
            } else if(nota == 4.5) {
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

            } else if(nota == 5.0) {
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

interface ClickViagem {
    fun clickViagem(viagem: Viagem)
}