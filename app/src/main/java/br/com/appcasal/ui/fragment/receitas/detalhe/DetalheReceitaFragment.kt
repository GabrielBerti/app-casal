package br.com.appcasal.ui.fragment.receitas.detalhe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentReceitaDetalheBinding
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.fragment.receitas.detalhe.ListaIngredientesDetalheAdapter.CheckouIngrediente
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.DetalheReceitaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalheReceitaFragment : Fragment(), CheckouIngrediente {

    private lateinit var binding: FragmentReceitaDetalheBinding
    private val ingredienteViewModel: DetalheReceitaViewModel by viewModel()
    private val args: DetalheReceitaFragmentArgs by navArgs()

    private var util = Util()
    private var snackbar: Snackbar? = null

    private lateinit var receita: Receita
    private lateinit var decorView: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentReceitaDetalheBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup
        receita = args.receita
        binding.fragment = this

        setupListeners()
        setListeners()
        setClickListeners()
        setLayout()
        configuraAdapter()
        setupSwipeRefresh()

        return binding.root
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            ingredienteViewModel.recuperaIngredientes(receita)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {
            ingredienteViewModel.ingredienteGetResult.collectResult(this) {
                onError { binding.isError = true }
                onSuccess {
                    binding.isError = false
                    receita.ingredientes = it
                    configuraAdapter()
                }
            }

            ingredienteViewModel.ingredienteMarcouResult.collectResult(this) {
                onError {
                    createSnackBar(
                        TipoSnackbar.ERRO,
                        resources.getString(R.string.erro_marcar_desmarcar)
                    )
                }
            }

            ingredienteViewModel.ingredienteDesmarcarTodasResult.collectResult(this) {
                onError {
                    createSnackBar(
                        TipoSnackbar.ERRO,
                        resources.getString(R.string.erro_desmarcar_tudo)
                    )
                }
                onSuccess {
                    receita.ingredientes?.forEach { it.marcado = false }
                    configuraAdapter()
                }
            }
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        snackbar =
            util.createSnackBarWithReturn(
                binding.linearContentDetalhe,
                msg,
                resources,
                tipoSnackbar
            )
    }

    private fun setListeners() {
        binding.desmarcarTudo.setOnClickListener {
            dialogDesmarcaTodosIngredientes()
        }
    }

    private fun setLayout() {
        binding.receitaNomeDetalhe.text = receita.nome
        binding.receitaDescricaoDetalhe.text = receita.descricao
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configuraAdapter() {
        if (receita.ingredientes != null)
            binding.rvIngredientes.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIngredientes.adapter =
            ListaIngredientesDetalheAdapter(
                receita.ingredientes ?: listOf(),
                requireContext(),
                this
            )

    }

    private fun dialogDesmarcaTodosIngredientes() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Limpar")
        builder.setMessage("Desmarcar todos ingredientes?")

        builder.setPositiveButton(
            "Sim"
        ) { _, _ ->
            ingredienteViewModel.desmarcarTodosIngredientes(receita)
        }

        builder.setNegativeButton(
            "Não"
        ) { _, _ ->

        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun abreTelaDeAlteracao() {
        findNavController().navigate(
            DetalheReceitaFragmentDirections.actionDetalheReceitaFragmentToFormReceitaFragment(receita)
        )
    }

    override fun atualizaIngrediente(position: Int, isChecked: Boolean) {
        ingredienteViewModel.marcarDesmarcarIngrediente(
            receita.ingredientes?.get(position) ?: Ingrediente(0L, "", false)
        )
    }
}