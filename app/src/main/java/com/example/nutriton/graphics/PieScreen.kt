package com.example.nutriton.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import androidx.compose.ui.tooling.preview.Preview
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily

// Usando fuente por defecto ya que no existe amaranth.ttf
val AmaranthFontFamily = FontFamily.Default

@Preview
@Composable
fun PieScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 5.dp)
    ) {
        Text(
            text = "Gráfico de pastel",
            fontWeight = FontWeight.Bold,
            fontFamily = AmaranthFontFamily
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            DatosGraficar.datos.take(3).forEach { dato ->
                PieChartWithCenter(
                    label = dato.label,
                    porcentaje = dato.value,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun PieChartWithCenter(label: String, porcentaje: Float, modifier: Modifier = Modifier) {
    val restante = 100f - porcentaje
    val slices = listOf(
        PieChartData.Slice(
            value = porcentaje,
            color = Utils().colorAleatorio(),
        ),
        PieChartData.Slice(
            value = restante,
            color = Utils().colorPlateado(),
        )
    )
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        PieChart(
            pieChartData = PieChartData(slices),
            modifier = Modifier.fillMaxSize(),
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer()
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = AmaranthFontFamily
            )
            Text(
                text = "${"%.1f".format(porcentaje)}%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
