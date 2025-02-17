package com.arny.aircraftrefueling.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arny.aircraftrefueling.R
import com.arny.aircraftrefueling.databinding.FragmentHomeBinding
import dagger.android.support.AndroidSupportInjection

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        binding.btnPoo.setOnClickListener {
            navController.navigate(R.id.action_nav_home_to_deicingFragment)
        }
        binding.btnRefuel.setOnClickListener {
            navController.navigate(R.id.action_nav_home_to_refuelFragment)
        }
        binding.btnSettings.setOnClickListener {
            navController.navigate(R.id.action_nav_home_to_settingsFragment)
        }
    }
}