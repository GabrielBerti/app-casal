package br.com.appcasal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.appcasal.databinding.ActivityMainBinding
import br.com.appcasal.ui.activity.financas.ListaTransacoesActivity
import br.com.appcasal.ui.activity.metas.ListaMetasActivity
import br.com.appcasal.ui.activity.receitas.ListaReceitasActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        setListeners()
    }

    private fun setListeners() {
        binding.cardFinancas.setOnClickListener {
            val intent = Intent(this, ListaTransacoesActivity::class.java)
            startActivity(intent)
        }

        binding.cardMetas.setOnClickListener {
            val intent = Intent(this, ListaMetasActivity::class.java)
            startActivity(intent)
        }

        binding.cardReceitas.setOnClickListener {
            val intent = Intent(this, ListaReceitasActivity::class.java)
            startActivity(intent)
        }

        binding.cardViagens.setOnClickListener {

        }
    }
}