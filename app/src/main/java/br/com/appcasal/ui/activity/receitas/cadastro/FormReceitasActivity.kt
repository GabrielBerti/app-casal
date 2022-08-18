package br.com.appcasal.ui.activity.receitas.cadastro

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.IngredienteDAO
import br.com.appcasal.dao.ReceitaDAO
import br.com.appcasal.databinding.ActivityFormReceitasBinding
import br.com.appcasal.model.Ingrediente
import br.com.appcasal.model.Receita
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.dialog.ingredientes.AdicionaIngredienteDialog
import br.com.appcasal.ui.dialog.ingredientes.AlteraIngredienteDialog

class FormReceitasActivity() : AppCompatActivity(), ClickIngrediente {

    private lateinit var activityFormReceitas: ActivityFormReceitasBinding
    private lateinit var adapter: ListaIngredientesAdapter
    private lateinit var rv: RecyclerView
    private var receitaId: Long = 0L
    private lateinit var receitaNome: TextView
    private lateinit var receitaDescricao: TextView

    private var ingredientes: MutableList<Ingrediente> = Companion.ingredientes
    private lateinit var receitaSelected: List<Receita>

    companion object {
        private val ingredientes: MutableList<Ingrediente> = mutableListOf()
    }

    private val viewDaActivity by lazy {
        window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    private val db by lazy {
        AppDatabase.instancia(this)
    }

    private lateinit var ingredienteDAO: IngredienteDAO
    private lateinit var receitaDAO: ReceitaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFormReceitas = ActivityFormReceitasBinding.inflate(layoutInflater)
        val view = activityFormReceitas.root
        setContentView(view)

        receitaNome = findViewById<TextView>(R.id.receita_nome)
        receitaDescricao = findViewById<TextView>(R.id.receita_descricao)

        ingredienteDAO = db.ingredienteDao()
        receitaDAO = db.receitaDao()

        setLayout()
        setListeners()
        configuraAdapter()
    }

    private fun setLayout() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //Mostrar o botão
        supportActionBar!!.setHomeButtonEnabled(true)      //Ativar o botão

        val extras = intent.extras
        if (extras != null) {
            receitaId = extras.getString("receitaId")!!.toLong()
            if (isUpdated(receitaId)) {
                receitaSelected = receitaDAO.buscaReceitaById(receitaId)
                receitaNome.text = receitaSelected[0].nome
                receitaDescricao.text = receitaSelected[0].descricao

                ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
                supportActionBar!!.title = resources.getString(R.string.altera_receita)
            } else {
                ingredientes = mutableListOf()
                supportActionBar!!.title = resources.getString(R.string.adicionar_receita)
            }
        }
    }

    private fun setListeners() {
        activityFormReceitas.btnInsereIngrediente.setOnClickListener() {
            chamaDialogDeAdicaoIngrediente()
            receitaNome.clearFocus()
            receitaDescricao.clearFocus()
        }

        activityFormReceitas.btnSalvarReceita.setOnClickListener() {

            //salva ou altera receita
            if (isUpdated(receitaId)) {
                receitaDAO.altera(
                    Receita(
                        receitaId,
                        receitaNome.text.toString(),
                        receitaDescricao.text.toString()
                    )
                )
            } else {
                receitaDAO.adiciona(
                    Receita(
                        receitaId,
                        receitaNome.text.toString(),
                        receitaDescricao.text.toString()
                    )
                )
            }

            val ultimaReceitaInserida = receitaDAO.buscaUltimaReceitaInserida()
            insereIngredientes(ultimaReceitaInserida.id)

            val intent = Intent()
            setResult(ListaReceitasActivity.retornoSucesso, intent)
            finish()
        }
    }

    private fun insereIngredientes(receitaId: Long) {
        ingredientes.forEach {
            if (!isUpdated(it.id)) {
                ingredienteDAO.adiciona(Ingrediente(it.id, it.descricao, false, receitaId))
            }
        }
    }

    private fun configuraAdapter() {
        rv = findViewById(R.id.lista_ingredientes_listview)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ListaIngredientesAdapter(ingredientes, this, this)
        rv.adapter = adapter
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
        AdicionaIngredienteDialog(viewGroupDaActivity, this)
            .chama(null, receitaId) { ingredienteCriado ->
                adiciona(ingredienteCriado)
            }
    }

    private fun adiciona(ingrediente: Ingrediente) {
        if (isUpdated(ingrediente.id)) {
            ingredienteDAO.adiciona(ingrediente)
            atualizaIngredientes()
        } else {
            ingredientes.add(ingrediente)
            atualizaListaIngredientesAindaNaoSalvos()
        }
    }

    private fun atualizaListaIngredientesAindaNaoSalvos() {
        rv.adapter = ListaIngredientesAdapter(ingredientes, this, this)
    }

    private fun atualizaIngredientes() {
        ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
        rv.adapter = ListaIngredientesAdapter(ingredientes, this, this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var posicao = -1
        posicao = (rv.adapter as ListaIngredientesAdapter).posicao

        when (item.itemId) {
            1 -> {
                remove(posicao)
                adapter.notifyItemRemoved(posicao)
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicao: Int) {
        if (isUpdated(ingredientes[posicao].id)) {
            //insere os ingredientes q ainda nao foram inseridos para nao perde-los quando atualizar adapter
            insereIngredientes(receitaId)
            ingredienteDAO.remove(ingredientes[posicao])
            adapter.notifyItemRemoved(posicao)
            atualizaIngredientes()
        } else {
            ingredientes.remove(ingredientes[posicao])
            adapter.notifyItemRemoved(posicao)
            atualizaListaIngredientesAindaNaoSalvos()
        }
    }

    private fun chamaDialogDeAlteracaoIngrediente(ingrediente: Ingrediente, posicao: Int) {
        AlteraIngredienteDialog(viewGroupDaActivity, this)
            .chama(ingrediente.id, receitaId, ingrediente) { receitaAlterada ->
                altera(receitaAlterada, posicao)
            }
    }

    private fun altera(ingrediente: Ingrediente, posicao: Int) {

        var encontrou: Boolean = false

        ingredientes.forEach {
            if (it.id == ingrediente.id && isUpdated(ingrediente.id)) {
                ingredienteDAO.altera(ingrediente)
                atualizaIngredientes()
                encontrou = true
                return
            }
        }

        if (!encontrou) {
            ingredientes[posicao] = ingrediente
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
        var mi = menu.findItem(R.id.clear_all);
        mi.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }
}