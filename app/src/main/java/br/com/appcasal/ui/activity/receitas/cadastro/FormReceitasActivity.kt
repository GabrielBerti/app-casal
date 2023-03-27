package br.com.appcasal.ui.activity.receitas.cadastro

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.databinding.FormReceitasBinding
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.dialog.ingredientes.AdicionaIngredienteDialog
import br.com.appcasal.ui.dialog.ingredientes.AlteraIngredienteDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.IngredienteViewModel
import br.com.appcasal.viewmodel.ReceitaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormReceitasActivity : AppCompatActivity(), ClickIngrediente {

    private lateinit var binding: FormReceitasBinding
    private val receitaViewModel: ReceitaViewModel by viewModel()
    private val ingredienteViewModel: IngredienteViewModel by viewModel()

    private lateinit var adapter: ListaIngredientesAdapter
    private lateinit var rv: RecyclerView
    private var receita: Receita? = null
    private var util = Util()

    private var ingredientes: MutableList<Ingrediente>? = null

    private val viewDaActivity by lazy {
        window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.form_receitas)

        setupListeners()

        setLayout()
        setListeners()
        configuraAdapter()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            if(receita != null) {
                ingredienteViewModel.recuperaIngredientes(receita!!)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {

            receitaViewModel.receitaInsertResult.collectResult(this) {
                onError { }
                onSuccess {
                    receita = it
                    insereIngredientes()
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_inserida_sucesso, it.descricao)
                    )
                }
            }

            receitaViewModel.receitaUpdateResult.collectResult(this) {
                onError { }
                onSuccess {
                    receita = it
                    insereIngredientes()
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_alterada_sucesso)
                    )
                }
            }

            ingredienteViewModel.ingredienteInsertResult.collectResult(this) {
                onError {
                }
                onSuccess {
                    ingredienteViewModel.recuperaIngredientes(receita!!)
                }
            }

            ingredienteViewModel.ingredienteUpdateResult.collectResult(this) {
                onError {
                }
                onSuccess {
                    ingredienteViewModel.recuperaIngredientes(receita!!)
                }
            }

            ingredienteViewModel.ingredienteDeleteResult.collectResult(this) {
                onError {
                }
                onSuccess {
                    ingredienteViewModel.recuperaIngredientes(receita!!)
                }
            }

            ingredienteViewModel.ingredienteGetResult.collectResult(this) {
                onError {
                }
                onSuccess {
                    ingredientes = it.toMutableList()
                    atualizaIngredientes()
                }
            }
        }
    }

    private fun setLayout() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar!!.setHomeButtonEnabled(true)      //Ativar o botão

        receita = intent.extras?.getParcelable("receita") as Receita?

        if (receita != null) {
            binding.receitaNome.setText(receita?.nome)
            binding.receitaDescricao.setText(receita?.descricao)
            ingredientes = receita?.ingredientes?.toMutableList() ?: arrayListOf()

            supportActionBar!!.title = resources.getString(R.string.altera_receita)
        } else {
            ingredientes = mutableListOf()
            supportActionBar!!.title = resources.getString(R.string.adicionar_receita)
        }

    }

    private fun setListeners() {
        binding.fabAdicionaIngredientes.setOnClickListener {
            chamaDialogDeAdicaoIngrediente()
            binding.receitaNome.clearFocus()
            binding.receitaDescricao.clearFocus()
        }

        binding.btnSalvarReceita.setOnClickListener {

            if (!isValidForm()) {
                createSnackBar(
                    TipoSnackbar.ERRO,
                    resources.getString(R.string.campos_receita_obrigatorios),
                )
            } else {
                salvarOuAlterarReceita()
            }
        }
    }

    private fun salvarOuAlterarReceita() {
        if (receita != null) {
            receitaViewModel.alteraReceita(
                Receita(
                    receita?.id ?: 0,
                    binding.receitaNome.text.toString(),
                    binding.receitaDescricao.text.toString(),
                    listOf()
                )
            )
        } else {
            receitaViewModel.insereReceita(
                Receita(
                    0,
                    binding.receitaNome.text.toString(),
                    binding.receitaDescricao.text.toString(),
                    listOf()
                )
            )
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.receitaNome, msg, resources, tipoSnackbar)
    }

    private fun isValidForm(): Boolean {
        if (binding.receitaNome.text.toString().isBlank() || ingredientes?.size == 0) {
            return false
        }

        return true
    }

    private fun insereIngredientes() {
        val ingredientesAindaNaoInseridos: MutableList<Ingrediente> = arrayListOf()
        lifecycleScope.launch {
            ingredientes?.forEach {
                if (!isUpdated(it.id)) {
                    ingredientesAindaNaoInseridos.add(it)
                }
            }

            if (ingredientesAindaNaoInseridos.isNotEmpty())
                ingredienteViewModel.insereIngrediente(ingredientesAindaNaoInseridos, receita!!)
        }
    }

    private fun configuraAdapter() {
        if (ingredientes != null) {
            rv = findViewById(R.id.lista_ingredientes_listview)
            rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = ListaIngredientesAdapter(ingredientes!!, this, this)
            rv.adapter = adapter
        }

    }

    override fun clickIngrediente(ingrediente: Ingrediente, posicao: Int) {
        chamaDialogDeAlteracaoIngrediente(ingrediente, posicao)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun chamaDialogDeAdicaoIngrediente() {
        util.aplicaOpacidadeFundo(binding.llFormReceita)
        AdicionaIngredienteDialog(viewGroupDaActivity, this)
            .chama(
                null,
                receita?.id ?: 0,
                binding.llFormReceita
            ) { ingredienteCriado ->
                adiciona(ingredienteCriado)
                util.retiraOpacidadeFundo(binding.llFormReceita)
            }
    }

    private fun chamaDialogDeAlteracaoIngrediente(ingrediente: Ingrediente, posicao: Int) {
        AlteraIngredienteDialog(viewGroupDaActivity, this)
            .chama(
                ingrediente.id,
                receita?.id ?: 0,
                ingrediente,
                binding.llFormReceita
            ) { receitaAlterada ->
                altera(receitaAlterada, posicao)
            }
    }


    private fun altera(ingrediente: Ingrediente, posicao: Int) {
        if (isUpdated(ingrediente.id)) {
            ingredienteViewModel.alteraIngrediente(ingrediente, receita!!)
            return
        }

        ingredientes?.forEach {
            if (it.id == ingrediente.id) {
                ingredientes!![posicao] = ingrediente
                atualizaIngredientes()
                return
            }
        }
    }

    private fun adiciona(ingrediente: Ingrediente) {
        if (isUpdated(receita?.id ?: 0)) {
            ingredienteViewModel.insereIngrediente(listOf(ingrediente), receita!!)
        } else {
            ingredientes?.add(ingrediente)
            atualizaIngredientes()
        }
    }

    private fun atualizaIngredientes() {
        rv.adapter = ListaIngredientesAdapter(ingredientes ?: listOf(), this, this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao: Int = (rv.adapter as ListaIngredientesAdapter).posicao

        when (item.itemId) {
            1 -> {
                remove(posicao)
                adapter.notifyItemRemoved(posicao)
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicao: Int) {
        if (isUpdated(ingredientes!![posicao].id)) {
            ingredienteViewModel.deletaIngrediente(ingredientes!![posicao])
        } else {
            ingredientes?.remove(ingredientes!![posicao])
            adapter.notifyItemRemoved(posicao)
            atualizaIngredientes()
        }
    }

    private fun isUpdated(number: Long): Boolean {
        return number != 0L
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Botão adicional na ToolBar
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        ListaReceitasActivity::class.java
                    )
                )
                finishAffinity()
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val mi = menu.findItem(R.id.clear_all)
        mi.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }
}