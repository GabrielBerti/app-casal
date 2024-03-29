package br.com.appcasal.ui.fragment.receitas.detalhe

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.domain.model.Ingrediente


class ListaIngredientesDetalheAdapter(
    private var ingredientes: List<Ingrediente>,
    private var context: Context,
    private var checkouIngrediente: CheckouIngrediente
    ) :
    RecyclerView.Adapter<IngredientesDetalheViewHolder>() {

    private val CHECK_BOX_STATES = arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked)
    )


    private val CHECK_BOX_COLORS = intArrayOf(
        Color.WHITE,
        Color.WHITE
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientesDetalheViewHolder {
        val viewCriada: View = LayoutInflater.from(context)
            .inflate(R.layout.ingrediente_detalhe_item, parent, false)

        return IngredientesDetalheViewHolder(viewCriada)
    }

    override fun onBindViewHolder(holder: IngredientesDetalheViewHolder, position: Int) {
        holder.bind(ingredientes, position)
        setupCheckBox(holder, position)
    }

    private fun setupCheckBox(holder: IngredientesDetalheViewHolder, position: Int) {
        holder.checkBoxIngrediente.buttonTintList = ColorStateList(
            CHECK_BOX_STATES,
            CHECK_BOX_COLORS
        )

        holder.checkBoxIngrediente.setOnCheckedChangeListener { _, isChecked ->
            checkouIngrediente.atualizaIngrediente(position, isChecked)
            holder.checkBoxIngrediente.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int {
        return ingredientes.size
    }

    interface CheckouIngrediente {
        fun atualizaIngrediente(position: Int, isChecked: Boolean)
    }
}