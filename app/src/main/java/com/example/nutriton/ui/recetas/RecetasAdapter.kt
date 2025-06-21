package com.example.nutriton.ui.recetas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutriton.R
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.databinding.ItemRecetaBinding

class RecetasAdapter(
    private val onRecetaClick: (Receta) -> Unit
) : ListAdapter<Receta, RecetasAdapter.RecetaViewHolder>(RecetaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val binding = ItemRecetaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecetaViewHolder(
        private val binding: ItemRecetaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRecetaClick(getItem(position))
                }
            }
        }

        fun bind(receta: Receta) {
            binding.apply {
                tvNombreReceta.text = receta.nombre
                tvDescripcionReceta.text = receta.descripcion
                tvCaloriasReceta.text = "${receta.informacion_nutricional?.calorias ?: 0} kcal"
                tvTiempoPreparacion.text = "${receta.tiempo_preparacion ?: 0} min"

                // Mostrar tipos de dieta
                val tiposDieta = receta.tipos_dieta?.joinToString(", ") { it.nombre } ?: ""
                tvTiposDieta.text = tiposDieta
                
                // Cargar imagen
                Glide.with(itemView.context)
                    .load(receta.imagen_url)
                    .placeholder(R.drawable.placeholder_receta)
                    .fallback(R.drawable.placeholder_receta)
                    .centerCrop()
                    .into(ivImagenReceta)
            }
        }
    }

    class RecetaDiffCallback : DiffUtil.ItemCallback<Receta>() {
        override fun areItemsTheSame(oldItem: Receta, newItem: Receta): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Receta, newItem: Receta): Boolean {
            return oldItem == newItem
        }
    }
}