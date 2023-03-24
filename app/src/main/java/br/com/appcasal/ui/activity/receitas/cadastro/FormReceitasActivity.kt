package br.com.appcasal.ui.activity.receitas.cadastro

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.databinding.FormReceitasBinding
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.dialog.ingredientes.AdicionaIngredienteDialog
import br.com.appcasal.ui.dialog.ingredientes.AlteraIngredienteDialog
import br.com.appcasal.util.Util

class FormReceitasActivity() : AppCompatActivity(), ClickIngrediente {

    private lateinit var activityFormReceitas: FormReceitasBinding
    private lateinit var adapter: ListaIngredientesAdapter
    private lateinit var rv: RecyclerView
    private var receitaId: Long = 0L
    private var util = Util()
    private lateinit var receitaNome: EditText
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
                //receitaSelected = receitaDAO.buscaReceitaById(receitaId)
                receitaNome.setText(receitaSelected[0].nome)// = receitaSelected[0].nome
                receitaDescricao.text = receitaSelected[0].descricao

               // ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
                supportActionBar!!.title = resources.getString(R.string.altera_receita)
            } else {
                ingredientes = mutableListOf()
                supportActionBar!!.title = resources.getString(R.string.adicionar_receita)
            }
        }
    }

    private fun setListeners() {
        activityFormReceitas.fabAdicionaIngredientes.setOnClickListener() {
            chamaDialogDeAdicaoIngrediente()
            receitaNome.clearFocus()
            receitaDescricao.clearFocus()
        }

        activityFormReceitas.btnSalvarReceita.setOnClickListener() {

            if(!isValidForm()){
                createSnackBar(
                    TipoSnackbar.ERRO,
                    resources.getString(R.string.campos_receita_obrigatorios),
                )
            } else if (verificaReceitaComMesmoNome(receitaId, receitaNome.text.toString())) {
                createSnackBar(
                    TipoSnackbar.ERRO,
                    resources.getString(R.string.receita_ja_existe),
                )
            } else {
                //salva ou altera receita
                salvaReceita()
            }
        }
    }

    private fun salvaReceita() {
        if (isUpdated(receitaId)) {
//            receitaDAO.altera(
//                Receita(
//                    receitaId,
//                    receitaNome.text.toString(),
//                    receitaDescricao.text.toString()
//                )
//            )
        } else {
            ListaReceitasActivity.nomeReceitaInserida = receitaNome.text.toString()
//
//            receitaDAO.adiciona(
//                Receita(
//                    receitaId,
//                    receitaNome.text.toString(),
//                    receitaDescricao.text.toString()
//                )
//            )
        }

       // val ultimaReceitaInserida = receitaDAO.buscaUltimaReceitaInserida()
      //  insereIngredientes(ultimaReceitaInserida.id)

        val intent = Intent()
        setResult(ListaReceitasActivity.retornoSucesso, intent)
        finish()
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
            util.createSnackBar(receitaNome, msg, resources, tipoSnackbar)
    }

    private fun isValidForm(): Boolean {
        if(receitaNome.text.toString().isNullOrBlank() || ingredientes.size == 0){
            return false
        }

        return true
    }

    private fun verificaReceitaComMesmoNome(receitaId: Long, receitaNome: String): Boolean {

       // val todasReceitas = receitaDAO.buscaTodos()
        var count: Int = 0
        val isUpdated = isUpdated(receitaId)

//        todasReceitas.forEach() {
//            if(it.nome == receitaNome) {
//                count += 1
//            }
//        }

        // se nao encotrou nenhuma receita com o mesmo nome retorna false
        if (count == 0) return false
        // se for inserção e encotrou receita com o mesmo nome retorna true
        if (!isUpdated && count > 0) return true
        // se for alteracão e encotrou receita com o mesmo nome e esse nome nao é o mesmo da receita carregada retorna true
        if(isUpdated && count > 0) return receitaNome != receitaSelected[0].nome

        return false
    }

    private fun insereIngredientes(receitaId: Long) {
        ingredientes.forEach {
            if (!isUpdated(it.id)) {
             ///   ingredienteDAO.adiciona(Ingrediente(it.id, it.descricao, false, receitaId))
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
        util.aplicaOpacidadeFundo(activityFormReceitas.llFormReceita)
        AdicionaIngredienteDialog(viewGroupDaActivity, this)
            .chama(null, receitaId, activityFormReceitas.llFormReceita) { ingredienteCriado ->
                adiciona(ingredienteCriado)
                util.retiraOpacidadeFundo(activityFormReceitas.llFormReceita)
            }
    }

    private fun adiciona(ingrediente: Ingrediente) {
        if (isUpdated(ingrediente.id)) {
         //   ingredienteDAO.adiciona(ingrediente)
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
    //    ingredientes = ingredienteDAO.buscaIngredientesByReceita(receitaId)
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
        //    ingredienteDAO.remove(ingredientes[posicao])
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
            .chama(ingrediente.id, receitaId, ingrediente, activityFormReceitas.llFormReceita) { receitaAlterada ->
                altera(receitaAlterada, posicao)
            }
    }

    private fun altera(ingrediente: Ingrediente, posicao: Int) {

        var encontrou: Boolean = false

        ingredientes.forEach {
            if (it.id == ingrediente.id && isUpdated(ingrediente.id)) {
            //    ingredienteDAO.altera(ingrediente)
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