package br.com.appcasal.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.appcasal.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenResumed {
            delay(500L)
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            )
        }

        return binding.root
    }
}
