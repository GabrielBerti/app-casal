package br.com.appcasal.ui.activity.financas

import android.content.Context
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.domain.model.Tipo
import br.com.appcasal.domain.model.Transacao

class TransacoesViewHolder(private val context: Context,
                           itemView: View
) :
    RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

    val transacaoData =  itemView.findViewById<TextView>(R.id.transacao_data)
    val transacaoDescricao =  itemView.findViewById<TextView>(R.id.transacao_descricao)
    val transacaoIcone =  itemView.findViewById<ImageView>(R.id.transacao_icone)
    val transacaoValor =  itemView.findViewById<TextView>(R.id.transacao_valor)

    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    fun bind(transacoes: List<Transacao>, posicao: Int) {
        val transacao = transacoes[posicao]

        adicionaValor(transacao)
        adicionaIcone(transacao)
        adicionaDescricao(transacao)
        adicionaData(transacao)
    }

    private fun adicionaData(transacao: Transacao) {
        transacaoData.text = transacao.data
        //.formataParaBrasileiro()
    }

    private fun adicionaDescricao(transacao: Transacao) {
        transacaoDescricao.text = transacao.descricao

    }

    private fun adicionaIcone(transacao: Transacao) {
        val icone = iconePor(transacao.tipo)
        transacaoIcone.setBackgroundResource(icone)
    }

    private fun iconePor(tipo: Tipo): Int {
        if (tipo == Tipo.BIEL) {
            return R.drawable.icone_transacao_item_biel
        }
        return R.drawable.icone_transacao_item_mari
    }

    private fun adicionaValor(transacao: Transacao) {
        val cor: Int = corPor(transacao.tipo)
        transacaoValor.setTextColor(cor)
        transacaoValor.text = transacao.valor.formataParaBrasileiro()
    }

    private fun corPor(tipo: Tipo): Int {
        if (tipo == Tipo.BIEL) {
            return ContextCompat.getColor(context, R.color.biel)
        }
        return ContextCompat.getColor(context, R.color.mari)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        if (menu != null) {
            menu.add(Menu.NONE, 1, Menu.NONE, "Remover")
        }
    }


}