package br.com.appcasal.ui.activity.viagens.detalhes.gastos_viagem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.financask.extension.formataParaBrasileiro
import br.com.appcasal.R
import br.com.appcasal.dao.AppDatabase
import br.com.appcasal.dao.GastosViagemDAO
import br.com.appcasal.databinding.FragmentGastosViagemBinding
import br.com.appcasal.model.GastoViagem
import br.com.appcasal.model.TipoSnackbar
import br.com.appcasal.ui.dialog.gastos_viagem.AdicionaGastosViagemDialog
import br.com.appcasal.ui.dialog.gastos_viagem.AlteraGastosViagemDialog
import br.com.appcasal.util.Util
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal

class GastosViagemFragment(private val viagemId: Long) : Fragment(), ClickGastoViagem {

    private lateinit var binding: FragmentGastosViagemBinding
    private lateinit var adapter: ListaGastosViagemAdapter
    private lateinit var rv: RecyclerView
    private lateinit var snackbar: Snackbar
    private var util = Util()
    private var gastosViagem: List<GastoViagem> = Companion.gastoViagem
    private lateinit var gastosViagemDAO: GastosViagemDAO

    companion object {
        private val gastoViagem: MutableList<GastoViagem> = mutableListOf()
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
        binding = FragmentGastosViagemBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gastosViagemDAO = db.gastosViagemDao()

        gastosViagem = gastosViagemDAO.buscaGastosViagemByViagem(viagemId = viagemId)

        exibeValorTotalGasto()
        configuraAdapter()
        setListeners()
    }

    private fun exibeValorTotalGasto() {
        binding.totalGastoViagem.text = String.format(
            requireContext().getString(R.string.total_gasto_viagem),
            somaValorTotalGastoViagem()
        )
    }

    private fun setListeners() {
        binding.totalGastoViagem.setOnClickListener {
            chamaDialogDeAdicao()
        }
    }

    private fun configuraAdapter() {
        rv = binding.rvListaGastosViagem

        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = ListaGastosViagemAdapter(gastosViagem, requireContext(), this)
        rv.adapter = adapter
    }

    private fun somaValorTotalGastoViagem(): String {
        var totalGasto = BigDecimal.ZERO

        gastosViagem.forEach {
            totalGasto += it.valor
        }

        return totalGasto.formataParaBrasileiro()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var posicao = -1
        posicao = (rv.adapter as ListaGastosViagemAdapter).posicao

        when (item.itemId) {
            1 -> {
                val descricaoGastoRemovido = gastosViagem[posicao].descricao
                remove(posicao)
                adapter.notifyItemRemoved(posicao)

                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(
                        R.string.gasto_viagem_removido_sucesso,
                        descricaoGastoRemovido
                    ),
                    View.VISIBLE
                )
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicaoDaTransacao: Int) {
        gastosViagemDAO.remove(gastosViagem[posicaoDaTransacao])
        adapter.notifyItemRemoved(posicaoDaTransacao)
        atualizaGastosViagem()
        exibeValorTotalGasto()
    }

    private fun atualizaGastosViagem() {
        gastosViagem = gastosViagemDAO.buscaGastosViagemByViagem(viagemId)
        rv.adapter = ListaGastosViagemAdapter(gastosViagem, requireContext(), this)
    }

    private fun chamaDialogDeAdicao() {
        AdicionaGastosViagemDialog(viewGroupDaActivity, requireContext())
            .chama(null, viagemId) { viagemCriada ->
                adiciona(viagemCriada)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(
                        R.string.gasto_viagem_inserido_sucesso,
                        viagemCriada.descricao
                    ),
                    View.VISIBLE
                )
            }
    }

    private fun chamaDialogDeAlteracao(gastoViagem: GastoViagem) {
        AlteraGastosViagemDialog(viewGroupDaActivity, requireContext())
            .chamaAlteracao(gastoViagem, gastoViagem.id, gastoViagem.viagemId) { transacaoAlterada ->
                altera(transacaoAlterada)
                createSnackBar(
                    TipoSnackbar.SUCESSO,
                    resources.getString(R.string.transacao_alterada_sucesso),
                    View.VISIBLE
                )
            }
    }

    private fun adiciona(gastoViagem: GastoViagem) {
        gastosViagemDAO.adiciona(gastoViagem)
        atualizaGastosViagem()
        exibeValorTotalGasto()
    }

    private fun altera(gastoViagem: GastoViagem) {
        gastosViagemDAO.altera(gastoViagem)
        atualizaGastosViagem()
        exibeValorTotalGasto()
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String, visibility: Int) {
        snackbar = util.createSnackBarWithReturn(
            binding.llGastosViagem,
            msg,
            resources,
            tipoSnackbar,
            visibility
        )
    }

    override fun clickGastoViagem(gastoViagem: GastoViagem) {
        chamaDialogDeAlteracao(gastoViagem)
    }
}
