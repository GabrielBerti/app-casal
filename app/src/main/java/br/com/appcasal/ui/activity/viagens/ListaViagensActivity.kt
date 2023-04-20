package br.com.appcasal.ui.activity.viagens

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.MainActivity
import br.com.appcasal.R
import br.com.appcasal.databinding.ActivityListaViagensBinding
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.ui.activity.viagens.detalhes.DetalheViagemActivity
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.collectViewState
import br.com.appcasal.ui.dialog.viagens.AdicionaViagemDialog
import br.com.appcasal.ui.dialog.viagens.AlteraViagemDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.ListaViagensViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaViagensActivity : AppCompatActivity(), ClickViagem {

    private lateinit var binding: ActivityListaViagensBinding
    val viewModel: ListaViagensViewModel by viewModel()
    private var viagens: List<Viagem> = emptyList()
    private var searchJob: Job? = null
    private var util = Util()

    private val viewDaActivity by lazy {
        window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista_viagens)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupListeners()
        viewModel.recuperaViagens()

        setListeners()
        setupSwipeRefresh()
        setupEditTextSearch()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.recuperaViagens()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            viewModel.viagemGetResult.collectViewState(this) {
                onLoading {
                    if (it) binding.isError = false
                    binding.isLoading = it
                }
                onError { binding.isError = true }
                onSuccess {
                    viagens = it
                    configuraAdapter(it)
                }
            }

            viewModel.viagemInsertResult.collectResult(this) {
                onError { handleError(R.string.erro_inserir) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llListaViagens)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.viagem_inserida_sucesso, it.local)
                    )
                    viewModel.recuperaViagens()
                }
            }

            viewModel.viagemUpdateResult.collectResult(this) {
                onError { handleError(R.string.erro_alterar) }
                onSuccess {
                    util.retiraOpacidadeFundo(binding.llListaViagens)
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.viagem_alterada_sucesso, it.local)
                    )
                    viewModel.recuperaViagens()
                }
            }

            viewModel.viagemDeleteResult.collectResult(this) {
                onError { handleError(R.string.erro_deletar) }
                onSuccess {
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.viagem_removida_sucesso)
                    )
                    viewModel.recuperaViagens()
                    binding.rvViagens.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun handleError(msg: Int) {
        util.retiraOpacidadeFundo(binding.llListaViagens)
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "viagem")
        )
    }

    private fun configuraAdapter(viagens: List<Viagem>) {
        binding.rvViagens.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvViagens.adapter = ListaViagensAdapter(viagens, this, this)
    }

    private fun setupEditTextSearch() {
        with(binding.etSearch) {
            doOnTextChanged { text, _, _, _ ->
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
            viewModel.recuperaViagens(searchText)
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.clListaViagens, msg, resources, tipoSnackbar)
    }

    override fun clickViagem(viagem: Viagem) {
        chamaDetalheViagem(viagem)
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
        binding.fabAdicionaViagem
            .setOnClickListener {
                chamaDialogDeAdicao()
            }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao: Int = (binding.rvViagens.adapter as ListaViagensAdapter).posicao

        when (item.itemId) {
            1 -> {
                chamaDialogDeAlteracao(viagens[posicao])
            }
            2 -> {
                viewModel.deletaViagem(viagens[posicao])
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun chamaDialogDeAdicao() {
        AdicionaViagemDialog(viewGroupDaActivity, this)
            .chama(null, binding.llListaViagens) { viagemCriada ->
                viewModel.insereViagem(viagemCriada)
            }
    }

    private fun chamaDialogDeAlteracao(viagem: Viagem) {
        util.aplicaOpacidadeFundo(binding.llListaViagens)
        AlteraViagemDialog(viewGroupDaActivity, this)
            .chama(viagem, viagem.id, binding.llListaViagens) { viagemAlterada ->
                viewModel.alteraViagem(viagemAlterada)
            }
    }

    private fun chamaDetalheViagem(viagem: Viagem) {
        val it = Intent(this, DetalheViagemActivity::class.java)
        it.putExtra("viagem", viagem)
        startActivity(it)
    }

}