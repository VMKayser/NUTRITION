package com.example.nutriton.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.nutriton.R

object ImageUtils {
    
    fun loadImage(imageView: ImageView, url: String?, placeholderText: String = "Imagen") {
        val context = imageView.context
        
        Glide.with(context)
            .load(url ?: generatePlaceholderUrl(placeholderText))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
    
    private fun generatePlaceholderUrl(text: String): String {
        val encodedText = text.replace(" ", "+")
        return "https://via.placeholder.com/400x300/4CAF50/FFFFFF?text=$encodedText"
    }
    
    fun getRecetaPlaceholder(nombre: String): String {
        return "https://via.placeholder.com/400x300/2E7D32/FFFFFF?text=${nombre.replace(" ", "+")}"
    }
    
    fun getPlanPlaceholder(nombre: String): String {
        return "https://via.placeholder.com/400x300/1976D2/FFFFFF?text=${nombre.replace(" ", "+")}"
    }
}
