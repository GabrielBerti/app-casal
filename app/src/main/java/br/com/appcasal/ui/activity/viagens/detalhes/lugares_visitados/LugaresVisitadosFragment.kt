package br.com.appcasal.ui.activity.viagens.detalhes.lugares_visitados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.LugaresVisitadosDAO
import br.com.appcasal.databinding.FragmentLugaresVisitadosViagemBinding
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.dialog.lugares_visitados.AdicionaLugarVisitadoDialog
import br.com.appcasal.ui.dialog.lugares_visitados.AlteraLugarVisitadoDialog
import br.com.appcasal.util.Util
import com.google.android.material.snackbar.Snackbar

class LugaresVisitadosFragment(private val viagemId: Long) : Fragment(), ClickLugarVisitadoViagem {

    private lateinit var binding: FragmentLugaresVisitadosViagemBinding
    private lateinit var rv: RecyclerView
    private lateinit var adapter: ListaLugaresVisitadosViagemAdapter
    private lateinit var snackbar: Snackbar
    private var util = Util()
    private var lugaresVisitados: List<LugarVisitado> = Companion.lugaresVisitados
    private lateinit var lugaresVisitadosDAO: LugaresVisitadosDAO

    companion object {
        private val lugaresVisitados: MutableList<LugarVisitado> = mutableListOf()
    }

    private val viewDaActivity by lazy {
        requireActivity().window.decorView
    }

    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    private val db by lazy {
        AppDatabase.instancia(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLugaresVisitadosViagemBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lugaresVisitadosDAO = db.lugaresVisitadosDao()

        lugaresVisitados = lugaresVisitadosDAO.buscaLugaresVisitadosByViagem(viagemId = viagemId)

        configuraAdapter()
        setListeners()
    }

    private fun setListeners() {
        binding.tvAddLugarVisitado.setOnClickListener {
            chamaDialogDeAdicao()
        }
    }

    private fun configuraAdapter() {
        rv = binding.rvListaLugaresVisitadosViagem

        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = ListaLugaresVisitadosViagemAdapter(lugaresVisitados, requireContext(), this)
        rv.adapter = adapter
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var posicao = -1
        posicao = (rv.adapter as ListaLugaresVisitadosViagemAdapter).posicao

        when (item.itemId) {
            1 -> {
                val nomeLugarVisitadoRemovido = lugaresVisitados[posicao].nome
                remove(posicao)
                adapter.notifyItemRemoved(posicao)

                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(
                        R.string.lugar_visitado_removido_sucesso,
                        nomeLugarVisitadoRemovido
                    ),
                    View.VISIBLE
                )
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun atualizaLugaresVisitados() {
        lugaresVisitados = lugaresVisitadosDAO.buscaLugaresVisitadosByViagem(viagemId)
        rv.adapter = ListaLugaresVisitadosViagemAdapter(lugaresVisitados, requireContext(), this)
    }

    private fun remove(posicaoDaTransacao: Int) {
        lugaresVisitadosDAO.remove(lugaresVisitados[posicaoDaTransacao])
        adapter.notifyItemRemoved(posicaoDaTransacao)
        atualizaLugaresVisitados()
    }

    private fun chamaDialogDeAdicao() {
        AdicionaLugarVisitadoDialog(viewGroupDaActivity, requireContext())
            .chama(null, viagemId) { lugarVisitadoCriado ->
                adiciona(lugarVisitadoCriado)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(
                        R.string.lugar_visitado_inserido_sucesso,
                        lugarVisitadoCriado.nome
                    ),
                    View.VISIBLE
                )
            }
    }

    private fun chamaDialogDeAlteracao(lugarVisitado: LugarVisitado) {
        AlteraLugarVisitadoDialog(viewGroupDaActivity, requireContext())
            .chamaAlteracao(lugarVisitado, lugarVisitado.id, lugarVisitado.viagemId) { transacaoAlterada ->
                altera(transacaoAlterada)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.transacao_alterada_sucesso),
                    View.VISIBLE
                )
            }
    }

    private fun adiciona(lugarVisitado: LugarVisitado) {
        lugaresVisitadosDAO.adiciona(lugarVisitado)
        atualizaLugaresVisitados()
    }

    private fun altera(lugarVisitado: LugarVisitado) {
        lugaresVisitadosDAO.altera(lugarVisitado)
        atualizaLugaresVisitados()
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String, visibility: Int) {
        snackbar = util.createSnackBarWithReturn(
            requireActivity().findViewById(R.id.cl_detalhe_viagem),
            msg,
            resources,
            tipoSnackbar,
            visibility
        )
    }

    override fun clickLugarVisitadoViagem(lugarVisitado: LugarVisitado) {
        chamaDialogDeAlteracao(lugarVisitado)
    }
}

