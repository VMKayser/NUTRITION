package com.example.nutriton.ui.PlanComidas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nutriton.databinding.ItemPlanComidaBinding
import com.example.nutriton.ui.PlanComidas.model.PlanComida

class TiposDietaAdapter(
    private val onPlanClick: (PlanComida) -> Unit
) : ListAdapter<PlanComida, TiposDietaAdapter.PlanComidaViewHolder>(PlanComidaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanComidaViewHolder {
        val binding = ItemPlanComidaBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return PlanComidaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanComidaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlanComidaViewHolder(
        private val binding: ItemPlanComidaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: PlanComida) {
            binding.apply {
                // Verifica si estos IDs existen en tu layout
                // Si no existen, comenta estas líneas o actualiza con los IDs correctos
                try {
                    tvPlanNombre.text = plan.tipoDieta?.nombre?.uppercase() ?: plan.nombre.uppercase()
                    tvPlanDescripcion.text = plan.tipoDieta?.descripcion ?: plan.descripcion
                    tvTotalRecetas.text = "${plan.totalRecetas} recetas"
                } catch (e: Exception) {
                    // Maneja el caso donde los IDs no existen
                }

                // TODO: Cargar imagen desde URL cuando esté disponible
                // Glide.with(ivPlanImagen.context)
                //     .load(plan.imagenUrl)
                //     .placeholder(R.drawable.ic_launcher_background)
                //     .into(ivPlanImagen)

                root.setOnClickListener {
                    onPlanClick(plan)
                }
            }
        }
    }

    class PlanComidaDiffCallback : DiffUtil.ItemCallback<PlanComida>() {
        override fun areItemsTheSame(oldItem: PlanComida, newItem: PlanComida): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanComida, newItem: PlanComida): Boolean {
            return oldItem == newItem
        }
    }
}
