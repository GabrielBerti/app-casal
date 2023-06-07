package br.com.appcasal.ui.fragment.receitas.cadastro

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentFormReceitasBinding
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.model.TipoSnackbar
import br.com.appcasal.ui.collectResult
import br.com.appcasal.ui.dialog.ingredientes.AdicionaIngredienteDialog
import br.com.appcasal.ui.dialog.ingredientes.AlteraIngredienteDialog
import br.com.appcasal.util.Util
import br.com.appcasal.viewmodel.FormReceitasViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormReceitasFragment : Fragment(), ClickIngrediente {

    private lateinit var binding: FragmentFormReceitasBinding
    private val viewModel: FormReceitasViewModel by viewModel()
    private val args: FormReceitasFragmentArgs by navArgs()
    private lateinit var receita: Receita
    private var util = Util()

    private var ingredientes: MutableList<Ingrediente> = arrayListOf()
    private lateinit var decorView: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentFormReceitasBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup

        setupListeners()
        setClickListeners()
        setLayout()
        setListeners()
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
            if(receita.id != 0L) {
                viewModel.recuperaIngredientes(receita)
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupListeners() {
        lifecycleScope.launch {

            viewModel.receitaInsertResult.collectResult(this) {
                onError { handleErrorReceita(R.string.erro_inserir) }
                onSuccess {
                    receita = it
                    insereIngredientes()
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_inserida_sucesso, it.nome)
                    )
                }
            }

            viewModel.receitaUpdateResult.collectResult(this) {
                onError { handleErrorReceita(R.string.erro_alterar) }
                onSuccess {
                    receita = it
                    insereIngredientes()
                    createSnackBar(
                        TipoSnackbar.SUCESSO,
                        resources.getString(R.string.receita_alterada_sucesso, it.nome)
                    )
                }
            }

            viewModel.ingredienteInsertResult.collectResult(this) {
                onError { handleErrorIngrediente(R.string.erro_inserir) }
                onSuccess {
                    viewModel.recuperaIngredientes(receita)
                }
            }

            viewModel.ingredienteUpdateResult.collectResult(this) {
                onError { handleErrorIngrediente(R.string.erro_alterar) }
                onSuccess {
                    viewModel.recuperaIngredientes(receita)
                }
            }

            viewModel.ingredienteDeleteResult.collectResult(this) {
                onError { handleErrorIngrediente(R.string.erro_deletar) }
                onSuccess {
                    viewModel.recuperaIngredientes(receita)
                }
            }

            viewModel.ingredienteGetResult.collectResult(this) {
                onError { binding.isError = true }
                onSuccess {
                    binding.isError = false
                    ingredientes = it.toMutableList()
                    atualizaIngredientes()
                }
            }
        }
    }

    private fun handleErrorReceita(msg: Int) {
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "receita")
        )
    }

    private fun handleErrorIngrediente(msg: Int) {
        createSnackBar(
            TipoSnackbar.ERRO,
            resources.getString(msg, "ingrediente")
        )
    }

    private fun setLayout() {
        receita = args.receita ?: Receita(0L, "", "", listOf())

        if (receita.id != 0L) {
            binding.receitaNome.setText(receita.nome)
            binding.receitaDescricao.setText(receita.descricao)
            ingredientes = receita.ingredientes?.toMutableList() ?: arrayListOf()

            binding.titulo.text = resources.getString(R.string.altera_receita)
        } else {
            ingredientes = mutableListOf()
            binding.titulo.text = resources.getString(R.string.adicionar_receita)
        }

    }

    private fun setListeners() {
        binding.fabAdicionaIngredientes.setOnClickListener {
            chamaDialogDeAdicaoIngrediente()
            binding.receitaNome.clearFocus()
            binding.receitaDescricao.clearFocus()
        }

        binding.btnSalvarReceita.setOnClickListener {

            if (!isValidForm()) {
                createSnackBar(
                    TipoSnackbar.ERRO,
                    resources.getString(R.string.campos_receita_obrigatorios),
                )
            } else {
                salvarOuAlterarReceita()
            }
        }
    }

    private fun salvarOuAlterarReceita() {
        val nomeReceitaEmTexto = binding.receitaNome.text.toString()

        if (receita.id != 0L) {
            viewModel.alteraReceita(
                Receita(
                    receita.id,
                    nomeReceitaEmTexto,
                    binding.receitaDescricao.text.toString(),
                    listOf()
                )
            )
        } else {
            viewModel.insereReceita(
                Receita(
                    0,
                    nomeReceitaEmTexto,
                    binding.receitaDescricao.text.toString(),
                    listOf()
                )
            )
        }
    }

    private fun createSnackBar(tipoSnackbar: TipoSnackbar, msg: String) {
        util.createSnackBar(binding.receitaNome, msg, resources, tipoSnackbar)
    }

    private fun isValidForm(): Boolean {
        if (binding.receitaNome.text.toString().isBlank() || ingredientes.size == 0) {
            return false
        }

        return true
    }

    private fun insereIngredientes() {
        val ingredientesAindaNaoInseridos: MutableList<Ingrediente> = arrayListOf()
        lifecycleScope.launch {
            ingredientes.forEach {
                if (!isUpdated(it.id)) {
                    ingredientesAindaNaoInseridos.add(it)
                }
            }

            if (ingredientesAindaNaoInseridos.isNotEmpty())
                viewModel.insereIngrediente(ingredientesAindaNaoInseridos, receita)
        }
    }

    private fun configuraAdapter() {
        binding.rvIngredientes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIngredientes.adapter = ListaIngredientesAdapter(ingredientes, requireContext(), this)
    }

    override fun clickIngrediente(ingrediente: Ingrediente, posicao: Int) {
        chamaDialogDeAlteracaoIngrediente(ingrediente, posicao)
    }

    private fun chamaDialogDeAdicaoIngrediente() {
        util.aplicaOpacidadeFundo(binding.llFormReceita)
        AdicionaIngredienteDialog(decorView, requireContext())
            .chama(
                null,
                binding.llFormReceita
            ) { ingredienteCriado ->
                adiciona(ingredienteCriado)
                util.retiraOpacidadeFundo(binding.llFormReceita)
            }
    }

    private fun chamaDialogDeAlteracaoIngrediente(ingrediente: Ingrediente, posicao: Int) {
        AlteraIngredienteDialog(decorView, requireContext())
            .chama(
                ingrediente.id,
                ingrediente,
                binding.llFormReceita
            ) { receitaAlterada ->
                altera(receitaAlterada, posicao)
            }
    }


    private fun altera(ingrediente: Ingrediente, posicao: Int) {
        if (isUpdated(ingrediente.id)) {
            viewModel.alteraIngrediente(ingrediente, receita)
            return
        }

        ingredientes.forEach {
            if (it.id == ingrediente.id) {
                ingredientes[posicao] = ingrediente
                atualizaIngredientes()
                return
            }
        }
    }

    private fun adiciona(ingrediente: Ingrediente) {
        if (isUpdated(receita.id)) {
            viewModel.insereIngrediente(listOf(ingrediente), receita)
        } else {
            ingredientes.add(ingrediente)
            atualizaIngredientes()
        }
    }

    private fun atualizaIngredientes() {
        binding.rvIngredientes.adapter = ListaIngredientesAdapter(ingredientes, requireContext(), this)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao: Int = (binding.rvIngredientes.adapter as ListaIngredientesAdapter).posicao

        when (item.itemId) {
            1 -> {
                remove(posicao)
                (binding.rvIngredientes.adapter as ListaIngredientesAdapter).notifyItemRemoved(posicao)
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun remove(posicao: Int) {
        if (isUpdated(ingredientes[posicao].id)) {
            viewModel.deletaIngrediente(ingredientes[posicao])
        } else {
            ingredientes.remove(ingredientes[posicao])
            (binding.rvIngredientes.adapter as ListaIngredientesAdapter).notifyItemRemoved(posicao)
            atualizaIngredientes()
        }
    }

    private fun isUpdated(number: Long): Boolean {
        return number != 0L
    }
}