package br.com.appcasal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.appcasal.databinding.ActivityMainBinding
import br.com.appcasal.ui.activity.financas.ListaTransacoesActivity
import br.com.appcasal.ui.activity.metas.ListaMetasActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()

    }

    private fun setListeners() {
        binding.viewFinancas.setOnClickListener {
            val intent = Intent(this, ListaTransacoesActivity::class.java)
            startActivity(intent)
        }

        binding.viewViagens.setOnClickListener {

        }

        binding.viewMetas.setOnClickListener {
            val intent = Intent(this, ListaMetasActivity::class.java)
            startActivity(intent)
        }

        binding.viewReceitas.setOnClickListener {

        }
    }
}