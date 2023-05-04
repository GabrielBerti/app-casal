package br.com.appcasal.ui.fragment.viagens.detalhes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentViagemDetalheBinding
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.ui.fragment.viagens.detalhes.gastos_viagem.GastosViagemFragment
import br.com.appcasal.ui.fragment.viagens.detalhes.lugares_visitados.LugaresVisitadosFragment

class DetalheViagemFragment : Fragment() {

    private lateinit var binding: FragmentViagemDetalheBinding
    private lateinit var viagem: Viagem
    private lateinit var decorView: ViewGroup
    private val args: DetalheViagemFragmentArgs by navArgs()
    private lateinit var fragmentGastosViagem: GastosViagemFragment
    private lateinit var fragmentLugaresVisitadosViagem: LugaresVisitadosFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentViagemDetalheBinding.inflate(inflater, container, false)

        decorView = requireActivity().window.decorView as ViewGroup

        viagem = args.viagem
        setListeners()
        setClickListeners()
        binding.localViagem.text = viagem.local
        instanciaFragmentGastos()

        return binding.root
    }

    private fun setClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setListeners() {
        binding.btnGastosViagem.setOnClickListener {
            setShapeButtonSelectAndUnselected(
                binding.btnGastosViagem,
                binding.btnLugaresVisitadosViagem
            )
            instanciaFragmentGastos()
        }

        binding.btnLugaresVisitadosViagem.setOnClickListener {
            setShapeButtonSelectAndUnselected(
                binding.btnLugaresVisitadosViagem,
                binding.btnGastosViagem
            )
            instanciaFragmentLugaresVisitados()
        }
    }

    private fun instanciaFragmentGastos() {
        fragmentGastosViagem = GastosViagemFragment(viagem)

        childFragmentManager.beginTransaction()
            .replace(R.id.fl_detalhes_viagem, fragmentGastosViagem)
            .addToBackStack(null)
            .commit()
    }

    private fun instanciaFragmentLugaresVisitados() {
        fragmentLugaresVisitadosViagem = LugaresVisitadosFragment(viagem)

        childFragmentManager.beginTransaction()
            .replace(R.id.fl_detalhes_viagem, fragmentLugaresVisitadosViagem)
            .addToBackStack(null)
            .commit()
    }

    private fun setShapeButtonSelectAndUnselected(
        buttonSelected: AppCompatButton,
        buttonUnselected: AppCompatButton
    ) {
        buttonSelected.setBackgroundResource(R.drawable.button_detalhe_viagem_selected)
        buttonSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryVariant))

        buttonUnselected.setBackgroundResource(R.drawable.button_detalhe_viagem_unselected)
        buttonUnselected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }
}