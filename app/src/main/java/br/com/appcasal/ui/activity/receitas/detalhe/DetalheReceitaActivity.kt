package br.com.appcasal.ui.activity.receitas.detalhe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.databinding.ActivityReceitaDetalheBinding
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.activity.receitas.detalhe.ListaIngredientesDetalheAdapter.CheckouIngrediente

class DetalheReceitaActivity : AppCompatActivity(), CheckouIngrediente {

    private lateinit var activityReceitaDetalhe: ActivityReceitaDetalheBinding
    private lateinit var adapter: ListaIngredientesDetalheAdapter
    private lateinit var rv: RecyclerView

    private var receitaId: Long = 0L
    private lateinit var receitaNome: TextView
    private lateinit var receitaDescricao: TextView
    private lateinit var buttonDesmarcaTodosIngredientes: Button

    private var ingredientes: MutableList<Ingrediente> = Companion.ingredientes
    private lateinit var receitaSelected: List<Receita>

    companion object {
        private val ingredientes: MutableList<Ingrediente> = mutableListOf()
    }

  //  private lateinit var receitaDao: ReceitaDAO
   // private lateinit var ingredienteDAO: IngredienteDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReceitaDetalhe = ActivityReceitaDetalheBinding.inflate(layoutInflater)
        val view = activityReceitaDetalhe.root

        setContentView(view)

        receitaNome = findViewById<TextView>(R.id.receita_nome_detalhe)
        receitaDescricao = findViewById<TextView>(R.id.receita_descricao_detalhe)
        buttonDesmarcaTodosIngredientes = findViewById<Button>(R.id.desmarcar_tudo)

      //  ingredienteDAO = db.ingredienteDao()
     //   receitaDao = db.receitaDao()

        setListeners()
        setLayout()
        configuraAdapter()
    }

    private fun setListeners() {
        buttonDesmarcaTodosIngredientes.setOnClickListener() {
            dialogDesmarcaTodosIngredientes()
        }
    }

    private fun setLayout() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.detalhe_receita)

        val extras = intent.extras
        if (extras != null) {
            receitaId = extras.getString("receitaId")!!.toLong()
       //     receitaSelected = receitaDao.buscaReceitaById(receitaId)
            receitaNome.text = receitaSelected[0].nome
            receitaDescricao.text = receitaSelected[0].descricao

          //  ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configuraAdapter() {
        rv = findViewById(R.id.lista_ingredientes_detalhe_listview)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        atualizaAdapter()
        rv.adapter = adapter
    }

    private fun atualizaAdapter() {
        adapter = ListaIngredientesDetalheAdapter(ingredientes, this, this)
    }

    private fun dialogDesmarcaTodosIngredientes() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Limpar")
        builder.setMessage("Desmarcar todos ingredientes?")

        builder.setPositiveButton(
            "Sim"
        ) { _, _ ->
       //     ingredienteDAO.desmarcaTodosIngredientes()
        //    ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
            atualizaAdapter()
            rv.adapter = adapter
        }

        builder.setNegativeButton(
            "Não"
        ) { _, _ ->
            null
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
        ingredientes[position].marcado = isChecked
     //   ingredienteDAO.altera(ingredientes[position])
        atualizaAdapter()
    }
}