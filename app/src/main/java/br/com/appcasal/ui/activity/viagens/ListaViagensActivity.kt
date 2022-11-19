package br.com.appcasal.ui.activity.viagens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.dao.*
import br.com.appcasal.databinding.ActivityListaViagensBinding
import br.com.appcasal.model.TipoSnackbar
import br.com.appcasal.model.Viagem
import br.com.appcasal.ui.activity.receitas.detalhe.DetalheReceitaActivity
import br.com.appcasal.ui.activity.viagens.detalhes.DetalheViagemActivity
import br.com.appcasal.ui.dialog.viagens.AdicionaViagemDialog
import br.com.appcasal.ui.dialog.viagens.AlteraViagemDialog
import br.com.appcasal.util.Util
import com.google.android.material.snackbar.Snackbar

class ListaViagensActivity : AppCompatActivity(), ClickViagem {

    private lateinit var activityListaViagens: ActivityListaViagensBinding
    private lateinit var clViagem: CoordinatorLayout
    private lateinit var adapter: ListaViagensAdapter
    private lateinit var rv: RecyclerView
    private var util = Util()
    private lateinit var snackbar: Snackbar

    private var viagens: List<Viagem> = Companion.viagens

    companion object {
        private val viagens: MutableList<Viagem> = mutableListOf()
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

    private lateinit var viagemDao: ViagemDAO
    private lateinit var gastosViagemDAO: GastosViagemDAO
    private lateinit var lugaresVisitadosDAO: LugaresVisitadosDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaViagens = ActivityListaViagensBinding.inflate(layoutInflater)
        val view = activityListaViagens.root

        setContentView(view)

        clViagem = findViewById<CoordinatorLayout>(R.id.cl_lista_viagens)

        viagemDao = db.viagemDao()
        gastosViagemDAO = db.gastosViagemDao()
        lugaresVisitadosDAO = db.lugaresVisitadosDao()

        viagens = viagemDao.buscaTodos()

        setToolbar()
        configuraAdapter()
        setListeners()

        //instancia a snackbar
        createSnackBar(
            TipoSnackbar.SUCESSO,
            resources.getString(R.string.viagem_inserida_sucesso),
            View.GONE
        )
    }

    private fun setToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun configuraAdapter() {
        rv = findViewById(R.id.rv_lista_viagens)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ListaViagensAdapter(viagens, this, this)
        rv.adapter = adapter
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(clViagem, msg, resources, tipoSnackbar)
    }

    override fun clickViagem(viagem: Viagem) {
        chamaDetalheViagem(viagem)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> {
                if (viagens.isNotEmpty()) {
                    dialogRemoveViagens()
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

    private fun setListeners() {
        activityListaViagens.fabAdicionaViagem
            .setOnClickListener {
                snackbar.dismiss()
                chamaDialogDeAdicao()
            }
    }

    private fun atualizaViagens() {
        viagens = viagemDao.buscaTodos()
        rv.adapter = ListaViagensAdapter(viagens, this, this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var posicao = -1
        posicao = (rv.adapter as ListaViagensAdapter).posicao

        when (item.itemId) {
            1 -> {
                chamaDialogDeAlteracao(viagens[posicao])
            }
            2 -> {
                val nomeViagemRemovida = viagens[posicao].local
                remove(posicao)
                adapter.notifyItemRemoved(posicao)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.viagem_removida_sucesso, nomeViagemRemovida)
                )
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicao: Int) {
        lugaresVisitadosDAO.deleteLugaresVisitadosByViagem(viagens[posicao].id)
        gastosViagemDAO.deleteGastosViagemByViagem(viagens[posicao].id)
        viagemDao.remove(viagens[posicao])
        adapter.notifyItemRemoved(posicao)
        atualizaViagens()
    }

    private fun removeTodasViagens() {
        viagens.forEach { viagem ->
            lugaresVisitadosDAO.deleteLugaresVisitadosByViagem(viagem.id)
            gastosViagemDAO.deleteGastosViagemByViagem(viagem.id)
            viagemDao.remove(viagem)
        }
        atualizaViagens()
    }

    private fun adiciona(viagem: Viagem) {
        viagemDao.adiciona(viagem)
        atualizaViagens()
    }

    private fun altera(viagem: Viagem) {
        viagemDao.altera(viagem)
        atualizaViagens()
    }

    private fun chamaDialogDeAdicao() {
        AdicionaViagemDialog(viewGroupDaActivity, this)
            .chama(null, activityListaViagens.llListaViagens) { viagemCriada ->
                adiciona(viagemCriada)
                util.retiraOpacidadeFundo(activityListaViagens.llListaViagens)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.viagem_inserida_sucesso, viagemCriada.local),
                    View.VISIBLE
                )
            }
    }

    private fun chamaDialogDeAlteracao(viagem: Viagem) {
        util.aplicaOpacidadeFundo(activityListaViagens.llListaViagens)
        AlteraViagemDialog(viewGroupDaActivity, this)
            .chama(viagem, viagem.id, activityListaViagens.llListaViagens) { viagemAlterada ->
                altera(viagemAlterada)
                util.retiraOpacidadeFundo(activityListaViagens.llListaViagens)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.viagem_alterada_sucesso),
                    View.VISIBLE
                )
            }
    }

    private fun dialogRemoveViagens() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Limpar")
        builder.setMessage("Zerar viagens ?")

        builder.setPositiveButton(
            "Sim"
        ) { _, _ ->
            removeTodasViagens()
            createSnackBar(
                TipoSnackbar.SUCESSO,
                resources.getString(R.string.viagens_removidas_sucesso)
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

    private fun chamaDetalheViagem(viagem: Viagem) {
        val it = Intent(this, DetalheViagemActivity::class.java)
        it.putExtra("viagemId", viagem.id.toString())
        startActivity(it)
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String, visibility: Int) {
        snackbar =
            util.createSnackBarWithReturn(clViagem, msg, resources, tipoSnackbar, visibility)
    }

}