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
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.activity.receitas.detalhe.ListaIngredientesDetalheAdapter.CheckouIngrediente
import br.com.appcasal.ui.collectResult
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.DetalheReceitaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalheReceitaActivity : AppCompatActivity(), CheckouIngrediente {

    private lateinit var binding: ActivityReceitaDetalheBinding
    private val ingredienteViewModel: DetalheReceitaViewModel by viewModel()

    private var util = Util()
    private var snackbar: Snackbar? = null

    private lateinit var receita: Receita

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receita_detalhe)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupListeners()
        setListeners()
        setLayout()
        configuraAdapter()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            ingredienteViewModel.recuperaIngredientes(receita)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            ingredienteViewModel.ingredienteGetResult.collectResult(this) {
                onError { binding.isError = true }
                onSuccess {
                    binding.isError = false
                    receita.ingredientes = it
                    configuraAdapter()
                }
            }

            ingredienteViewModel.ingredienteMarcouResult.collectResult(this) {
                onError {
                    createSnackBar(
                        TipoSnackbar.ERRO,
                        resources.getString(R.string.erro_marcar_desmarcar)
                    )
                }
            }

            ingredienteViewModel.ingredienteDesmarcarTodasResult.collectResult(this) {
                onError {
                    createSnackBar(
                        TipoSnackbar.ERRO,
                        resources.getString(R.string.erro_desmarcar_tudo)
                    )
                }
                onSuccess {
                    receita.ingredientes?.forEach { it.marcado = false }
                    configuraAdapter()
                }
            }
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        snackbar =
            util.createSnackBarWithReturn(
                binding.linearContentDetalhe,
                msg,
                resources,
                tipoSnackbar
            )
    }

    private fun setListeners() {
        binding.desmarcarTudo.setOnClickListener {
            dialogDesmarcaTodosIngredientes()
        }
    }

    private fun setLayout() {
        supportActionBar?.title = resources.getString(R.string.detalhe_receita)

        receita = intent.extras?.getParcelable("receita") ?: Receita(0L, "", "", listOf())

        binding.receitaNomeDetalhe.text = receita.nome
        binding.receitaDescricaoDetalhe.text = receita.descricao
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configuraAdapter() {
        if (receita.ingredientes != null)
            binding.rvIngredientes.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvIngredientes.adapter =
            ListaIngredientesDetalheAdapter(receita.ingredientes ?: listOf(), this, this)

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
        ingredienteViewModel.marcarDesmarcarIngrediente(
            receita.ingredientes?.get(position) ?: Ingrediente(0L, "", false)
        )
    }
}