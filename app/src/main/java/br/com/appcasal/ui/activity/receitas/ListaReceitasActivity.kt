package br.com.appcasal.ui.activity.receitas

import android.content.Intent
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.databinding.ActivityListaReceitasBinding
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.activity.receitas.cadastro.FormReceitasActivity
import br.com.appcasal.ui.activity.receitas.detalhe.DetalheReceitaActivity
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaReceitasViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaReceitasActivity : AppCompatActivity(), ClickReceita {

    private lateinit var binding: ActivityListaReceitasBinding
    val viewModel: ListaReceitasViewModel by viewModel()

    private var util = Util()
    private var receitas: List<Receita> = emptyList()
    private var searchJob: Job? = null

    override fun onResume() {
        super.onResume()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista_receitas)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupListeners()
        viewModel.recuperaReceitas()

        setListeners()
        setupSwipeRefresh()
        setupEditTextSearch()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaReceitas()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.receitaGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError {
                    binding.isError = true
                    binding.noResults = false
                }
                onSuccess {
                    receitas = it
                    binding.noResults = receitas.isEmpty() && binding.etSearch.text?.toString() != ""
                    configuraAdapter(it)
                }
            }

            viewModel.receitaDeleteResult.collectResult(this) {
                onError {
                    createSnackBar(
                        TipoSnackbar.ERRO,
                        resources.getString(R.string.erro_deletar, "receita")
                    )
                }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_removida_sucesso)
                    )
                    viewModel.recuperaReceitas()
                   // binding.rvMetas.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun configuraAdapter(receitas: List<Receita>) {
        binding.rvReceitas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvReceitas.adapter = ListaReceitasAdapter(receitas, this, this)
    }

    private fun setupEditTextSearch() {
        with(binding.etSearch) {
            doOnTextChanged { text, _, _, _ ->
                binding.etSearch.performClick()
                binding.etSearch.requestFocus()
                if (hasFocus()) searchDebounced(text.toString())
            }

            setOnEditorActionListener { v, _, _ ->
                util.hideKeyboard(this, v.context)
                true
            }
        }
    }

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(1500)
            viewModel.recuperaReceitas(searchText)
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.clListaReceitas, msg, resources, tipoSnackbar)
    }

    override fun clickReceita(receita: Receita) {
        chamaDetalheReceita(receita)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val it = Intent(this, MainActivity::class.java)
                startActivity(it)
                return super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setListeners() {
        binding.fabAdicionaReceita
            .setOnClickListener {
                abreTelaDeCadastro()
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao: Int = (binding.rvReceitas.adapter as ListaReceitasAdapter).posicao

        when (item.itemId) {
            1 -> {
                abreTelaDeAlteracao(receitas[posicao])
            }

            2 -> {
                viewModel.deletaReceita(receitas[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun abreTelaDeCadastro() {
        val it = Intent(this, FormReceitasActivity::class.java)
        startActivity(it)
    }

    private fun abreTelaDeAlteracao(receita: Receita) {
        val it = Intent(this, FormReceitasActivity::class.java)
        it.putExtra("receita", receita)
        startActivity(it)
    }

    private fun chamaDetalheReceita(receita: Receita) {
        val it = Intent(this, DetalheReceitaActivity::class.java)
        it.putExtra("receita", receita)
        startActivity(it)
    }

}