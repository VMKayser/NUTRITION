package com.example.nutriton.ui.Progreso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.databinding.FragmentProgresoBinding

class ProgresoFragment : Fragment() {

    private var _binding: FragmentProgresoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ProgresoViewModel =
            ViewModelProvider(this).get(ProgresoViewModel::class.java)

        _binding = FragmentProgresoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textProgreso
        ProgresoViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}