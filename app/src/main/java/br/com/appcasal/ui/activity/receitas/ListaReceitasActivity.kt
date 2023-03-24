package br.com.appcasal.ui.activity.receitas

import android.app.AlertDialog
import android.content.Intent
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.IngredienteDAO
import br.com.appcasal.dao.ReceitaDAO
import br.com.appcasal.databinding.ActivityListaReceitasBinding
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.activity.receitas.cadastro.FormReceitasActivity
import br.com.appcasal.ui.activity.receitas.detalhe.DetalheReceitaActivity
import br.com.appcasal.util.Util

class ListaReceitasActivity : AppCompatActivity(), ClickReceita {

    private lateinit var activityListaReceitas: ActivityListaReceitasBinding
    private lateinit var clReceita: CoordinatorLayout
    private lateinit var adapter: ListaReceitasAdapter
    private lateinit var rv: RecyclerView
    private var util = Util()
    private var isOnResume: Boolean = false

    private var receitas: List<Receita> = Companion.receitas

    companion object {
        private val receitas: MutableList<Receita> = mutableListOf()
        val retornoSucesso = 100
        private val INSERT = "INSERT"
        var nomeReceitaInserida = ""
        private val UPDATE = "UPDATE"
        private var insertOrUpdate = ""
        private var msgSnackBar: String = ""
    }

    private val db by lazy {
        AppDatabase.instancia(this)
    }

    private lateinit var receitaDao: ReceitaDAO
    private lateinit var ingredienteDAO: IngredienteDAO

    var abrirActivityCadastro = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == retornoSucesso) {
            isOnResume = true

            msgSnackBar = if (insertOrUpdate == INSERT) {
                resources.getString(R.string.receita_inserida_sucesso, nomeReceitaInserida)
            } else {
                resources.getString(R.string.receita_alterada_sucesso)
            }

            insertOrUpdate = ""
        }
    }

    override fun onResume() {
        super.onResume()
        activityListaReceitas = ActivityListaReceitasBinding.inflate(layoutInflater)
        val view = activityListaReceitas.root

        setContentView(view)

        clReceita = findViewById<CoordinatorLayout>(R.id.cl_lista_receitas)

        ingredienteDAO = db.ingredienteDao()
        receitaDao = db.receitaDao()
        receitas = receitaDao.buscaTodos()

        setToolbar()
        configuraAdapter()
        setListeners()

        if (isOnResume) {
            createSnackBar(TipoSnackbar.SUCESSO, msgSnackBar)
        }
    }

    private fun setToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun configuraAdapter() {
        rv = findViewById(R.id.lista_metas_listview)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ListaReceitasAdapter(receitas, this, this)
        rv.adapter = adapter
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(clReceita, msg, resources, tipoSnackbar)
    }

    override fun clickReceita(receita: Receita) {
        chamaDetalheReceita(receita)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> {
                if (receitas.isNotEmpty()) {
                    dialogRemoveReceitas()
                }

                true
            }
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                isOnResume = false
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dialogRemoveReceitas() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Limpar")
        builder.setMessage("Zerar receitas ?")

        builder.setPositiveButton(
            "Sim"
        ) { _, _ ->
            removeTodasReceitas()
            createSnackBar(
                TipoSnackbar.SUCESSO,
                resources.getString(R.string.receitas_removidas_sucesso)
            )
        }

        builder.setNegativeButton(
            "Não"
        ) { _, _ ->
            null
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun setListeners() {
        activityListaReceitas.fabAdicionaReceita
            .setOnClickListener {
                abreTelaDeCadastro()
            }
    }

    private fun atualizaReceitas() {
        receitas = receitaDao.buscaTodos()
        rv.adapter = ListaReceitasAdapter(receitas, this, this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var posicao = -1
        posicao = (rv.adapter as ListaReceitasAdapter).posicao

        when (item.itemId) {
            1 -> {
                abreTelaDeAlteracao(receitas[posicao])
            }

            2 -> {
                val nomeReceitaRemovida = receitas[posicao].nome
                remove(posicao)
                adapter.notifyItemRemoved(posicao)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.receita_removida_sucesso, nomeReceitaRemovida)
                )
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicao: Int) {
        ingredienteDAO.deleteIngredientesByReceita(receitas[posicao].id)
        receitaDao.remove(receitas[posicao])
        adapter.notifyItemRemoved(posicao)
        atualizaReceitas()
    }

    private fun removeTodasReceitas() {
        receitas.forEach { receita ->
            ingredienteDAO.deleteIngredientesByReceita(receita.id)
            receitaDao.remove(receita)
        }
        atualizaReceitas()
    }

    private fun abreTelaDeCadastro() {
        val it = Intent(this, FormReceitasActivity::class.java)
        it.putExtra("receitaId", "0")
        isOnResume = false
        insertOrUpdate = INSERT
        abrirActivityCadastro.launch(it)
    }

    private fun abreTelaDeAlteracao(receita: Receita) {
        val it = Intent(this, FormReceitasActivity::class.java)
        it.putExtra("receitaId", receita.id.toString())
        isOnResume = false
        insertOrUpdate = UPDATE
        abrirActivityCadastro.launch(it)
    }

    private fun chamaDetalheReceita(receita: Receita) {
        val it = Intent(this, DetalheReceitaActivity::class.java)
        it.putExtra("receitaId", receita.id.toString())
        isOnResume = false
        startActivity(it)
    }

}