package br.com.appcasal.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.appcasal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.cardFinancas.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTransacaoFragment()
            )
        }

        binding.cardMetas.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMetaFragment()
            )
        }

        binding.cardReceitas.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToReceitaFragment()
            )
        }

        binding.cardViagens.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToViagemFragment()
            )
        }
    }

}