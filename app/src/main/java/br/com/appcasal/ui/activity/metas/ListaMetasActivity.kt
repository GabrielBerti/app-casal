package br.com.appcasal.ui.activity.metas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.MetaDAO
import br.com.appcasal.databinding.ActivityListaMetasBinding
import br.com.appcasal.model.Meta
import br.com.appcasal.model.TipoSnackbar
import br.com.appcasal.ui.dialog.metas.AdicionaMetaDialog
import br.com.appcasal.ui.dialog.metas.AlteraMetaDialog
import br.com.appcasal.util.Util

class ListaMetasActivity : AppCompatActivity(), ClickMeta {

    private lateinit var activityListaMetas: ActivityListaMetasBinding
    private lateinit var clMetas: CoordinatorLayout
    private lateinit var adapter: ListaMetasAdapter
    private lateinit var rv: RecyclerView
    private var util = Util()

    private lateinit var spinnerStatus: Spinner

    private var metas: List<Meta> = Companion.metas

    companion object {
        private val metas: MutableList<Meta> = mutableListOf()
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

    private lateinit var metaDao: MetaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListaMetas = ActivityListaMetasBinding.inflate(layoutInflater)
        val view = activityListaMetas.root

        setContentView(view)

        metaDao = db.metaDao()
        metas = metaDao.buscaTodos()

        clMetas = findViewById<CoordinatorLayout>(R.id.cl_lista_metas)

        setToolbar()
        configuraAdapter()
        configuraSpinner()
        configuraFab()
        //spinnerStatus.background.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
    }

    private fun setToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun configuraAdapter() {
        rv = findViewById(R.id.lista_metas_listview)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ListaMetasAdapter(metas, this, this)
        rv.adapter = adapter
    }

    private fun configuraSpinner() {
        val options = arrayOf(
            resources.getString(R.string.todas),
            resources.getString(R.string.meta_nao_concluida),
            resources.getString(R.string.meta_concluida)
        )

        spinnerStatus = findViewById(R.id.spinner_status)
        spinnerStatus.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, posicao: Int, id: Long) {

                //val text = parent!!.getChildAt(0) as TextView
                //text.setTextColor(getResources().getColor(R.color.white))

                when(posicao) {
                    0 -> {
                        metas = metaDao.buscaTodos()
                    }

                    1 -> {
                        metas = metaDao.buscaNaoConcluidas()
                    }

                    2 -> {
                        metas = metaDao.buscaConcluidas()
                    }
                }
                atualizaAdapter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                parent?.setSelection(0)
            }

        }
    }

    override fun clickMeta(meta: Meta) {
        chamaDialogDeAlteracao(meta)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> {
                if (metas.isNotEmpty()) {
                    dialogRemoveMetas()
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

    private fun dialogRemoveMetas() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Limpar")
        builder.setMessage("Zerar metas ?")

        builder.setPositiveButton(
            "Sim"
        ) { _, _ ->
            metaDao.removeAll()
            atualizaMetas()
            createSnackBar(TipoSnackbar.SUCESSO, resources.getString(R.string.metas_removidas_sucesso))
        }

        builder.setNegativeButton(
            "Não"
        ) { _, _ ->
            null
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun configuraFab() {
        activityListaMetas.fabAdicionaMeta
            .setOnClickListener {
                chamaDialogDeAdicao()
            }
    }

    private fun adiciona(meta: Meta) {
        metaDao.adiciona(meta)
        atualizaMetas()
    }

    private fun atualizaMetas() {
        metas = metaDao.buscaTodos()
        rv.adapter = ListaMetasAdapter(metas, this, this)
    }

    private fun atualizaAdapter() {
        rv.adapter = ListaMetasAdapter(metas, this, this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        var posicao = -1
        posicao = (rv.adapter as ListaMetasAdapter).posicao

        when (item.itemId) {
            1 -> {
                metas[posicao].concluido = true
                altera(metas[posicao])
                createSnackBar(TipoSnackbar.SUCESSO, resources.getString(R.string.meta_concluida_sucesso))
            }

            2 -> {
                remove(posicao)
                adapter.notifyItemRemoved(posicao)
                createSnackBar(TipoSnackbar.SUCESSO, resources.getString(R.string.meta_removida_sucesso))
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicao: Int) {
        metaDao.remove(metas[posicao])
        adapter.notifyItemRemoved(posicao)
        atualizaMetas()
    }

    private fun chamaDialogDeAdicao() {
        AdicionaMetaDialog(viewGroupDaActivity, this)
            .chama(null, false) { metaCriada ->
                adiciona(metaCriada)
                createSnackBar(TipoSnackbar.SUCESSO, resources.getString(R.string.meta_inserida_sucesso))
            }
    }

    private fun chamaDialogDeAlteracao(meta: Meta) {
        AlteraMetaDialog(viewGroupDaActivity, this)
            .chama(meta, meta.id, meta.concluido) { metaAlterada ->
                altera(metaAlterada)
                createSnackBar(TipoSnackbar.SUCESSO, resources.getString(R.string.meta_alterada_sucesso))
            }
    }

    private fun altera(meta: Meta) {
        metaDao.altera(meta)
        atualizaMetas()
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(clMetas, msg, resources, tipoSnackbar)
    }
}