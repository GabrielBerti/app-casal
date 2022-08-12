package br.com.appcasal.ui.activity.receitas.detalhe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.IngredienteDAO
import br.com.appcasal.dao.ReceitaDAO
import br.com.appcasal.databinding.ActivityReceitaDetalheBinding
import br.com.appcasal.model.Ingrediente
import br.com.appcasal.model.Receita
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity

class DetalheReceitaActivity : AppCompatActivity() {

    private lateinit var activityReceitaDetalhe: ActivityReceitaDetalheBinding
    private lateinit var adapter: ListaIngredientesDetalheAdapter
    private lateinit var rv: RecyclerView

    private var receitaId: Long = 0L
    private lateinit var receitaNome: TextView
    private lateinit var receitaDescricao: TextView

    private var ingredientes: MutableList<Ingrediente> = Companion.ingredientes
    private lateinit var receitaSelected: List<Receita>

    companion object {
        private val ingredientes: MutableList<Ingrediente> = mutableListOf()
    }

    private val db by lazy {
        AppDatabase.instancia(this)
    }

    private lateinit var receitaDao: ReceitaDAO
    private lateinit var ingredienteDAO: IngredienteDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReceitaDetalhe = ActivityReceitaDetalheBinding.inflate(layoutInflater)
        val view = activityReceitaDetalhe.root

        setContentView(view)

        receitaNome = findViewById<TextView>(R.id.receita_nome_detalhe)
        receitaDescricao = findViewById<TextView>(R.id.receita_descricao_detalhe)

        ingredienteDAO = db.ingredienteDao()
        receitaDao = db.receitaDao()

        setLayout()
        configuraAdapter()
    }

    private fun setLayout() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.detalhe_receita)

        val extras = intent.extras
        if (extras != null) {
            receitaId = extras.getString("receitaId")!!.toLong()
            receitaSelected = receitaDao.buscaReceitaById(receitaId)
            receitaNome.text = receitaSelected[0].nome
            receitaDescricao.text = receitaSelected[0].descricao

            ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configuraAdapter() {
        rv = findViewById(R.id.lista_ingredientes_detalhe_listview)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ListaIngredientesDetalheAdapter(ingredientes, this, resources.getDrawable(R.drawable.line2))
        rv.adapter = adapter
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
}