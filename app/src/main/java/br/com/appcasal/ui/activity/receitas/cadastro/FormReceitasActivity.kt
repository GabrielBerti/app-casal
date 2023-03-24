package br.com.appcasal.ui.activity.receitas.cadastro

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.ingredientes.AdicionaIngredienteDialog
import br.com.appcasal.ui.dialog.ingredientes.AlteraIngredienteDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ReceitaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormReceitasActivity() : AppCompatActivity(), ClickIngrediente {

    private lateinit var activityFormReceitas: FormReceitasBinding
    val receitaViewModel: ReceitaViewModel by viewModel()

    private lateinit var adapter: ListaIngredientesAdapter
    private lateinit var rv: RecyclerView
    private var receita: Receita? = null
    private var util = Util()
    private lateinit var receitaNome: EditText
    private lateinit var receitaDescricao: TextView

    private var ingredientes: MutableList<Ingrediente>? = null

    private val viewDaActivity by lazy {
        window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    //private lateinit var ingredienteDAO: IngredienteDAO
    //private lateinit var receitaDAO: ReceitaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFormReceitas = FormReceitasBinding.inflate(layoutInflater)
        val view = activityFormReceitas.root
        setContentView(view)

        receitaNome = findViewById<EditText>(R.id.receita_nome)
        receitaDescricao = findViewById<TextView>(R.id.receita_descricao)

        //ingredienteDAO = db.ingredienteDao()
        //receitaDAO = db.receitaDao()

        setupListeners()

        setLayout()
        setListeners()
        configuraAdapter()
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            receitaViewModel.receitaInsertResult.collectResult(this) {
                onError { }
                onSuccess {
                    receita = it
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
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_alterada_sucesso)
                    )
                }
            }
        }
    }

    private fun setLayout() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar!!.setHomeButtonEnabled(true)      //Ativar o botão

        receita = intent.getSerializableExtra("receita") as Receita?

        if (receita != null) {
            receitaNome.setText(receita?.nome)// = receitaSelected[0].nome
            receitaDescricao.text = receita?.descricao//receitaSelected[0].descricao

            supportActionBar!!.title = resources.getString(R.string.altera_receita)
        } else {
            ingredientes = mutableListOf()
            supportActionBar!!.title = resources.getString(R.string.adicionar_receita)
        }

    }

    private fun setListeners() {
        activityFormReceitas.fabAdicionaIngredientes.setOnClickListener() {
            chamaDialogDeAdicaoIngrediente()
            receitaNome.clearFocus()
            receitaDescricao.clearFocus()
        }

        activityFormReceitas.btnSalvarReceita.setOnClickListener() {

            if (!isValidForm()) {
                createSnackBar(
                    TipoSnackbar.ERRO,
                    resources.getString(R.string.campos_receita_obrigatorios),
                )
            } else {
                //salva ou altera receita
                salvaReceita()
            }
        }
    }

    private fun salvaReceita() {
        if (receita != null) {
            receitaViewModel.alteraReceita(
                Receita(
                    receita?.id ?: 0,
                    receitaNome.text.toString(),
                    receitaDescricao.text.toString(),
                    listOf()
                )
            )
        } else {
            receitaViewModel.insereReceita(
                Receita(
                    0,
                    receitaNome.text.toString(),
                    receitaDescricao.text.toString(),
                    listOf()
                )
            )
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(receitaNome, msg, resources, tipoSnackbar)
    }

    private fun isValidForm(): Boolean {
        if (receitaNome.text.toString().isNullOrBlank() || ingredientes == null) {
            return false
        }

        return true
    }

    private fun insereIngredientes(receitaId: Long) {
        ingredientes?.forEach {
            if (!isUpdated(it.id)) {
                ///   ingredienteDAO.adiciona(Ingrediente(it.id, it.descricao, false, receitaId))
            }
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
        util.aplicaOpacidadeFundo(activityFormReceitas.llFormReceita)
        AdicionaIngredienteDialog(viewGroupDaActivity, this)
            .chama(
                null,
                receita?.id ?: 0,
                activityFormReceitas.llFormReceita
            ) { ingredienteCriado ->
                adiciona(ingredienteCriado)
                util.retiraOpacidadeFundo(activityFormReceitas.llFormReceita)
            }
    }

    private fun adiciona(ingrediente: Ingrediente) {
        if (isUpdated(ingrediente.id)) {
            //   ingredienteDAO.adiciona(ingrediente)
            atualizaIngredientes()
        } else {
            ingredientes?.add(ingrediente)
            atualizaListaIngredientesAindaNaoSalvos()
        }
    }

    private fun atualizaListaIngredientesAindaNaoSalvos() {
        if (ingredientes != null) {
            rv.adapter = ListaIngredientesAdapter(ingredientes!!, this, this)

        }
    }

    private fun atualizaIngredientes() {
        //    ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
        if (ingredientes != null) {
            rv.adapter = ListaIngredientesAdapter(ingredientes!!, this, this)
        }
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
            //insere os ingredientes q ainda nao foram inseridos para nao perde-los quando atualizar adapter
            insereIngredientes(receita?.id ?: 0)
            //    ingredienteDAO.remove(ingredientes[posicao])
            adapter.notifyItemRemoved(posicao)
            atualizaIngredientes()
        } else {
            ingredientes?.remove(ingredientes!![posicao])
            adapter.notifyItemRemoved(posicao)
            atualizaListaIngredientesAindaNaoSalvos()
        }
    }

    private fun chamaDialogDeAlteracaoIngrediente(ingrediente: Ingrediente, posicao: Int) {
        AlteraIngredienteDialog(viewGroupDaActivity, this)
            .chama(
                ingrediente.id,
                receita?.id ?: 0,
                ingrediente,
                activityFormReceitas.llFormReceita
            ) { receitaAlterada ->
                altera(receitaAlterada, posicao)
            }
    }

    private fun altera(ingrediente: Ingrediente, posicao: Int) {

        var encontrou = false

        ingredientes?.forEach {
            if (it.id == ingrediente.id && isUpdated(ingrediente.id)) {
                //    ingredienteDAO.altera(ingrediente)
                atualizaIngredientes()
                encontrou = true
                return
            }
        }

        if (!encontrou) {
            ingredientes!![posicao] = ingrediente
            atualizaListaIngredientesAindaNaoSalvos()
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