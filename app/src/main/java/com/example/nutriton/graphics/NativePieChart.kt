package com.example.nutriton.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.sin

// Datos simples para el gráfico
data class NutritionData(
    val label: String,
    val value: Float,
    val color: Color
)

@Composable
fun SimplePieChart(
    data: List<NutritionData>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.value.toDouble() }.toFloat()
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            if (data.isNotEmpty() && total > 0) {
                drawPieChart(data, total)
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nutrición",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = "100%",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

private fun DrawScope.drawPieChart(
    data: List<NutritionData>,
    total: Float
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 * 0.8f
    
    var startAngle = -90f // Empezar desde arriba
    
    data.forEach { item ->
        val sweepAngle = (item.value / total) * 360f
        
        drawArc(
            color = item.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(
                center.x - radius,
                center.y - radius
            ),
            size = Size(radius * 2, radius * 2)
        )
        
        startAngle += sweepAngle
    }
}

@Preview
@Composable
fun PieChartScreen() {
    val nutritionData = listOf(
        NutritionData("Carbohidratos", 62f, Color(0xFF2196F3)), // Azul
        NutritionData("Proteínas", 50f, Color(0xFF4CAF50)),     // Verde
        NutritionData("Grasas", 30f, Color(0xFFFF9800))        // Naranja
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Gráfico de Nutrición",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            nutritionData.take(3).forEach { data ->
                SimplePieChart(
                    data = listOf(
                        data,
                        NutritionData("Restante", 100f - data.value, Color.LightGray)
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Leyenda
        Column {
            nutritionData.forEach { data ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(end = 8.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(color = data.color)
                        }
                    }
                    Text(
                        text = "${data.label}: ${data.value.toInt()}%",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
