package br.com.appcasal.ui.activity.viagens.detalhes.lugares_visitados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.appcasal.R
import br.com.appcasal.databinding.FragmentLugaresVisitadosViagemBinding
import br.com.appcasal.ui.activity.viagens.ListaViagensAdapter
import br.com.appcasal.ui.activity.viagens.detalhes.gastos_viagem.ListaGastosViagemAdapter

class LugaresVisitadosFragment(viagemId: Long) : Fragment() {

    private lateinit var binding: FragmentLugaresVisitadosViagemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLugaresVisitadosViagemBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
