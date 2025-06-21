package com.example.nutriton.ui.PlanComidas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutriton.R
import com.example.nutriton.databinding.ItemPlanComidaBinding
import com.example.nutriton.ui.PlanComidas.model.PlanComida

class PlanesComidasAdapter(
    private val onPlanClick: (PlanComida) -> Unit
) : ListAdapter<PlanComida, PlanesComidasAdapter.PlanViewHolder>(PlanDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanComidaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlanViewHolder(
        private val binding: ItemPlanComidaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(plan: PlanComida) {
            binding.apply {
                // Datos del plan
                tvPlanNombre.text = plan.nombre
                tvPlanDescripcion.text = plan.descripcion
                // El badge de recetas
                tvTotalRecetas.text = "${plan.tipoDieta?.nombre ?: plan.nombre} - ${plan.duracion}"

                // Cargar imagen usando Glide
                com.example.nutriton.util.ImageUtils.loadImage(
                    ivPlanImagen, 
                    plan.imagenUrl, 
                    plan.nombre
                )

                root.setOnClickListener {
                    onPlanClick(plan)
                }
            }
        }
    }

    class PlanDiffCallback : DiffUtil.ItemCallback<PlanComida>() {
        override fun areItemsTheSame(oldItem: PlanComida, newItem: PlanComida): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanComida, newItem: PlanComida): Boolean {
            return oldItem == newItem
        }
    }
}
