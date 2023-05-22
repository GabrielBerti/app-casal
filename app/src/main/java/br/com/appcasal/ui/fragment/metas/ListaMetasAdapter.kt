package br.com.appcasal.ui.fragment.metas

import android.content.Context
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.databinding.MetaItemBinding
import br.com.appcasal.domain.model.Meta

class ListaMetasAdapter(
    private var metas: List<Meta>,
    private var context: Context,
    private var clickMeta: ClickMeta
) :
    RecyclerView.Adapter<ListaMetasAdapter.MetasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetasViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.meta_item, parent, false)

        return MetasViewHolder(context, viewCriada)
    }

    override fun onViewRecycled(holder: MetasViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: MetasViewHolder, position: Int) {
        holder.bind(metas, position)
        val meta = metas[position]
        holder.itemView.setOnClickListener {
            clickMeta.clickMeta(meta)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return metas.size
    }

    var posicao = 0

    inner class MetasViewHolder(
        private val context: Context,
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        val binding = MetaItemBinding.bind(itemView)
        var metaConcluida = false

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(metas: List<Meta>, posicao: Int) {
            val meta = metas[posicao]
            metaConcluida = meta.concluido

            adicionaIcone(meta)
            adicionaDescricao(meta)
            adicionaCheckBox(meta)
        }

        private fun adicionaDescricao(meta: Meta) {
            binding.metaDescricao.text = meta.descricao
        }

        private fun adicionaIcone(meta: Meta) {
            if(meta.concluido) {
                binding.metaIcone.setBackgroundResource(R.drawable.ic_check)
            } else {
                binding.metaIcone.setBackgroundResource(R.drawable.ic_alvo)
            }
        }

        private fun adicionaCheckBox(meta: Meta) {
            if (meta.concluido) {
                binding.clMetaItem.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimaryVariant
                    )
                )
            } else {
                binding.clMetaItem.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorDark
                    )
                )
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            if (menu != null) {
                if (metaConcluida) {
                    menu.add(Menu.NONE, 1, Menu.NONE, "Desconcluir meta")
                } else {
                    menu.add(Menu.NONE, 1, Menu.NONE, "Concluir meta")
                }

                menu.add(Menu.NONE, 2, Menu.NONE, "Remover")
            }
        }
    }
}

interface ClickMeta {
    fun clickMeta(meta: Meta)
}