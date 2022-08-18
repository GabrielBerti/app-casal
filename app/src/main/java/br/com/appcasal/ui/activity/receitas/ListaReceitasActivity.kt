package br.com.appcasal.ui.activity.receitas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.IngredienteDAO
import br.com.appcasal.dao.ReceitaDAO
import br.com.appcasal.databinding.ActivityListaReceitasBinding
import br.com.appcasal.model.Receita
import br.com.appcasal.ui.activity.receitas.detalhe.DetalheReceitaActivity
import br.com.appcasal.ui.activity.receitas.cadastro.FormReceitasActivity

class ListaReceitasActivity : AppCompatActivity(), ClickReceita {

    private lateinit var activityListaReceitas: ActivityListaReceitasBinding
    private lateinit var adapter: ListaReceitasAdapter
    private lateinit var rv: RecyclerView

    private var receitas: List<Receita> = Companion.receitas

    companion object {
        private val receitas: MutableList<Receita> = mutableListOf()
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

    private lateinit var receitaDao: ReceitaDAO
    private lateinit var ingredienteDAO: IngredienteDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaReceitas = ActivityListaReceitasBinding.inflate(layoutInflater)
        val view = activityListaReceitas.root

        setContentView(view)

        ingredienteDAO = db.ingredienteDao()
        receitaDao = db.receitaDao()
        receitas = receitaDao.buscaTodos()

        setToolbar()
        configuraAdapter()
        setListeners()
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
            receitaDao.removeAll()
            atualizaReceitas()
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
        activityListaReceitas.listaReceitasAdiciona
            .setOnClickListener {
                abreTelaDeCadastro()
            }
    }

    private fun abreTelaDeCadastro() {
        val it = Intent(this, FormReceitasActivity::class.java)

        it.putExtra("receitaId", "0")
        startActivity(it)
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
                remove(posicao)
                adapter.notifyItemRemoved(posicao)
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

    private fun abreTelaDeAlteracao(receita: Receita) {
        val it = Intent(this, FormReceitasActivity::class.java)
        it.putExtra("receitaId", receita.id.toString())
        startActivity(it)
    }

    private fun chamaDetalheReceita(receita: Receita) {
        val it = Intent(this, DetalheReceitaActivity::class.java)
        it.putExtra("receitaId", receita.id.toString())
        startActivity(it)
    }

}