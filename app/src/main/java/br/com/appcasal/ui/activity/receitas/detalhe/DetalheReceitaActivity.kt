package br.com.appcasal.ui.activity.receitas.detalhe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.ActivityReceitaDetalheBinding
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.activity.receitas.detalhe.ListaIngredientesDetalheAdapter.CheckouIngrediente
import br.com.appcasal.ui.collectResult
import br.com.appcasal.viewmodel.DetalheReceitaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalheReceitaActivity : AppCompatActivity(), CheckouIngrediente {

    private lateinit var binding: ActivityReceitaDetalheBinding
    private val ingredienteViewModel: DetalheReceitaViewModel by viewModel()

    private lateinit var receita: Receita

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receita_detalhe)

        setupListeners()
        setListeners()
        setLayout()
        configuraAdapter()
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            ingredienteViewModel.ingredienteMarcouResult.collectResult(this) {
                onError { }
            }

            ingredienteViewModel.ingredienteDesmarcarTodasResult.collectResult(this) {
                onError { }
                onSuccess {
                    receita.ingredientes?.forEach { it.marcado = false }
                    configuraAdapter()
                }
            }
        }
    }

    private fun setListeners() {
        binding.desmarcarTudo.setOnClickListener {
            dialogDesmarcaTodosIngredientes()
        }
    }

    private fun setLayout() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.detalhe_receita)

        receita = intent.extras?.getParcelable("receita")!!

            binding.receitaNomeDetalhe.text = receita.nome
            binding.receitaDescricaoDetalhe.text = receita.descricao
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configuraAdapter() {
        if(receita.ingredientes != null)
            binding.rvIngredientes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvIngredientes.adapter = ListaIngredientesDetalheAdapter(receita.ingredientes!!, this, this)

    }

    private fun dialogDesmarcaTodosIngredientes() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Limpar")
        builder.setMessage("Desmarcar todos ingredientes?")

        builder.setPositiveButton(
            "Sim"
        ) { _, _ ->
            ingredienteViewModel.desmarcarTodosIngredientes(receita)
        }

        builder.setNegativeButton(
            "Não"
        ) { _, _ ->

        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        ListaReceitasActivity::class.java
                    )
                )
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun atualizaIngrediente(position: Int, isChecked: Boolean) {

        if(receita.ingredientes?.get(position) != null)
            ingredienteViewModel.marcarDesmarcarIngrediente(receita.ingredientes?.get(position)!!)
    }
}