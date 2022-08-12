package br.com.appcasal.ui.activity.metas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.model.Meta

class ListaMetasAdapter(
    private var metas: List<Meta>,
    private var context: Context,
    private var clickMeta: ClickMeta
) :
    RecyclerView.Adapter<MetasViewHolder>() {
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

}

interface ClickMeta {
    fun clickMeta(meta: Meta)
}