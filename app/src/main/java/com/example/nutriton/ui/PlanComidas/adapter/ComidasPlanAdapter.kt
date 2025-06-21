package com.example.nutriton.ui.PlanComidas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutriton.R
import com.example.nutriton.databinding.ItemComidaPlanBinding
import com.example.nutriton.ui.PlanComidas.model.ComidaCompleta
import com.example.nutriton.util.ImageUtils

class ComidasPlanAdapter(
    private val onComidaClick: (ComidaCompleta) -> Unit
) : ListAdapter<ComidaCompleta, ComidasPlanAdapter.ComidaViewHolder>(ComidaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val binding = ItemComidaPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ComidaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ComidaViewHolder(
        private val binding: ItemComidaPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(comida: ComidaCompleta) {
            binding.apply {
                // Datos dinámicos desde la API
                tvNombreComida.text = comida.nombre
                tvCalorias.text = if (comida.calorias > 0) "${comida.calorias} Cal" else "- Cal"
                
                // Usar tiempo de cocción si está disponible, sino mostrar porción por defecto
                val tiempoText = comida.tiempoCoccion?.let { 
                    if (it > 0) "${it} min" else "1 Porción"
                } ?: "1 Porción"
                tvPorciones.text = tiempoText

                // Cargar imagen desde URL de la API usando Glide
                ImageUtils.loadImage(
                    ivComida,
                    comida.imagenUrl, 
                    comida.nombre
                )

                root.setOnClickListener {
                    onComidaClick(comida)
                }
            }
        }
    }

    class ComidaDiffCallback : DiffUtil.ItemCallback<ComidaCompleta>() {
        override fun areItemsTheSame(oldItem: ComidaCompleta, newItem: ComidaCompleta): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ComidaCompleta, newItem: ComidaCompleta): Boolean {
            return oldItem == newItem
        }
    }
}
