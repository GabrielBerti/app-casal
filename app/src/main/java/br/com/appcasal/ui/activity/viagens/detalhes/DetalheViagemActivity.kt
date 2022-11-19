package br.com.appcasal.ui.activity.viagens.detalhes

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import br.com.appcasal.R
import br.com.appcasal.databinding.ActivityDetalheViagemBinding
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity
import br.com.appcasal.ui.activity.viagens.ListaViagensActivity
import br.com.appcasal.ui.activity.viagens.detalhes.gastos_viagem.GastosViagemFragment
import br.com.appcasal.ui.activity.viagens.detalhes.lugares_visitados.LugaresVisitadosFragment


class DetalheViagemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheViagemBinding
    private var viagemId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheViagemBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setToolbar()
        setListeners()

        val extras = intent.extras
        if (extras != null) {
            viagemId = extras.getString("viagemId")!!.toLong()
        }

        instanciaFragmentGastos()
    }

    private fun setToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun setListeners() {
        binding.btnGastosViagem.setOnClickListener {
            setShapeButtonSelectAndUnselected(binding.btnGastosViagem, binding.btnLugaresVisitadosViagem)
            instanciaFragmentGastos()
        }

        binding.btnLugaresVisitadosViagem.setOnClickListener {
            setShapeButtonSelectAndUnselected(binding.btnLugaresVisitadosViagem, binding.btnGastosViagem)
            instanciaFragmentLugaresVisitados()
        }
    }

    private fun instanciaFragmentGastos() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.fl_detalhes_viagem, GastosViagemFragment(viagemId))
        ft.commit()
    }

    private fun instanciaFragmentLugaresVisitados() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.fl_detalhes_viagem, LugaresVisitadosFragment(viagemId))
        ft.commit()
    }

    private fun setShapeButtonSelectAndUnselected(buttonSelected: AppCompatButton, buttonUnselected: AppCompatButton) {
        buttonSelected.setBackgroundResource(R.drawable.button_detalhe_viagem_selected)
        buttonSelected.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant))

        buttonUnselected.setBackgroundResource(R.drawable.button_detalhe_viagem_unselected)
        buttonUnselected.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(
                        this,
                        ListaViagensActivity::class.java
                    )
                )
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}