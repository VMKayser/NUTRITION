package com.example.nutriton.ui.PlanComidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.databinding.FragmentPlancomidasBinding

class PlanComidasFragment : Fragment() {

    private var _binding: FragmentPlancomidasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val PlanComidasViewModel =
            ViewModelProvider(this).get(PlanComidasViewModel::class.java)

        _binding = FragmentPlancomidasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPlanComidas
        PlanComidasViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}