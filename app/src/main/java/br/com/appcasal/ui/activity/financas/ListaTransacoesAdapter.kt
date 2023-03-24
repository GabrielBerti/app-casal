package br.com.appcasal.ui.activity.financas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Transacao

class ListaTransacoesAdapter(
    private var transacoes: List<Transacao>,
    private var context: Context,
    private var clickTransacao: ClickTransacao
) :
    RecyclerView.Adapter<TransacoesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacoesViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.transacao_item, parent, false)

        return TransacoesViewHolder(context, viewCriada)
    }

    override fun onViewRecycled(holder: TransacoesViewHolder) {
        holder.itemView.setOnLongClickListener(null)

        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: TransacoesViewHolder, position: Int) {
        holder.bind(transacoes, position)
        val transacao = transacoes[position]
        holder.itemView.setOnClickListener {
            clickTransacao.clickTransacao(transacao)
        }

        holder.itemView.setOnLongClickListener {
            posicao = position
            false
        }
    }

    override fun getItemCount(): Int {
        return transacoes.size
    }

    var posicao = 0

}

interface ClickTransacao {
    fun clickTransacao(transacao: Transacao)
}