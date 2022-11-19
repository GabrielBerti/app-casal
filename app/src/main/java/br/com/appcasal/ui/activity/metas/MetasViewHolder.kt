package br.com.appcasal.ui.activity.metas

import android.content.Context
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.model.Meta

class MetasViewHolder(
    private val context: Context,
    itemView: View
) :
    RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    val metaConcluidoCheckBox = itemView.findViewById<TextView>(R.id.meta_concluido)
    val metaDescricao = itemView.findViewById<TextView>(R.id.meta_descricao)
    val metaIcone = itemView.findViewById<ImageView>(R.id.meta_icone)
    val linearMetaItem = itemView.findViewById<LinearLayout>(R.id.linear_meta_item)
    var metaConcluida = false

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    fun bind(metas: List<Meta>, posicao: Int) {
        val meta = metas[posicao]
        metaConcluida = meta.concluido

        adicionaIcone()
        adicionaDescricao(meta)
        adicionaCheckBox(meta)
    }

    private fun adicionaDescricao(meta: Meta) {
        metaDescricao.text = meta.descricao

    }

    private fun adicionaIcone() {
        metaIcone.setBackgroundResource(R.drawable.ic_alvo)
    }

    private fun adicionaCheckBox(meta: Meta) {
        if (meta.concluido) {
            metaConcluidoCheckBox.setText(R.string.meta_concluida)
            linearMetaItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryVariant))
            metaConcluidoCheckBox.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            metaConcluidoCheckBox.setText(R.string.meta_nao_concluida)
            //linearMetaItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
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