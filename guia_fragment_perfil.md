# 📱 Guía Completa del Fragment de Perfil - Android/Kotlin

## 📋 Índice
1. [Estructura General](#estructura-general)
2. [Componentes del Layout](#componentes-del-layout)
3. [Elementos Editables](#elementos-editables)
4. [Elementos de Navegación](#elementos-de-navegación)
5. [Switches y Notificaciones](#switches-y-notificaciones)
6. [Recursos Drawable](#recursos-drawable)
7. [Personalización y Modificaciones](#personalización-y-modificaciones)

---

## 🏗️ Estructura General

### ScrollView (Contenedor Principal)
```xml
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF">
```

**¿Qué hace?**
- Permite **desplazamiento vertical** cuando el contenido es más grande que la pantalla
- **match_parent**: Ocupa todo el ancho y alto disponible
- **background="#FFFFFF"**: Fondo blanco

**¿Qué puedes cambiar?**
- **Color de fondo**: Cambia `#FFFFFF` por otro color (ej: `#F5F5F5` para gris claro)
- **Dirección de scroll**: Usar `HorizontalScrollView` para scroll horizontal

---

## 📦 Componentes del Layout

### LinearLayout (Organizador Vertical)
```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="20dp">
```

**¿Qué hace?**
- **orientation="vertical"**: Apila elementos uno debajo del otro
- **padding="20dp"**: Espaciado interno de 20dp en todos los lados
- **wrap_content**: Se ajusta al tamaño de su contenido

**¿Qué puedes cambiar?**
- **orientation="horizontal"**: Para elementos lado a lado
- **padding**: Aumentar/disminuir espaciado (ej: `16dp`, `24dp`)
- **gravity**: Para alinear contenido (ej: `center`, `center_horizontal`)

---

## ✏️ Elementos Editables

### EditText (Campos de Entrada)
```xml
<EditText
    android:id="@+id/et_peso"
    android:layout_width="match_parent"
    android:layout_height="45dp"
    android:hint="Introduce tu peso"
    android:textSize="16sp"
    android:paddingStart="12dp"
    android:paddingEnd="12dp"
    android:background="@drawable/input_border"
    android:inputType="numberDecimal" />
```

**Atributos Explicados:**

| Atributo | ¿Qué hace? | ¿Qué puedes cambiar? |
|----------|------------|---------------------|
| `android:id` | Identificador único para el código | Cambiar nombre: `@+id/et_nombre_usuario` |
| `android:hint` | Texto que aparece cuando está vacío | Cambiar texto: `"Escribe aquí..."` |
| `android:inputType` | Tipo de teclado que aparece | `text`, `number`, `email`, `password` |
| `android:textSize` | Tamaño del texto | `14sp`, `18sp`, `20sp` |
| `android:background` | Estilo visual del borde | Referencia a drawable personalizado |

**Tipos de InputType:**
- `numberDecimal`: Números con decimales (peso, estatura)
- `number`: Solo números enteros (edad)
- `textEmailAddress`: Para emails
- `textPassword`: Para contraseñas
- `textCapWords`: Primera letra en mayúscula

---

### Spinner (Lista Desplegable)
```xml
<Spinner
    android:id="@+id/spinner_genero"
    android:layout_width="match_parent"
    android:layout_height="45dp"
    android:background="@drawable/spinner_background"
    android:paddingStart="12dp"
    android:paddingEnd="35dp" />
```

**¿Qué hace?**
- Muestra una **lista desplegable** de opciones
- **spinner_background**: Incluye la flecha hacia abajo
- **paddingEnd="35dp"**: Espacio para la flecha

**Para programar las opciones:**
```kotlin
// En el Fragment
val generos = arrayOf("Masculino", "Femenino", "Otro")
val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, generos)
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
spinner_genero.adapter = adapter
```

---

## 🎯 Elementos de Navegación

### LinearLayout Clickeable (Opciones del Menú)
```xml
<LinearLayout
    android:id="@+id/layout_gestionar_metas"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:gravity="center_vertical"
    android:padding="15dp"
    android:background="@drawable/rounded_gray_background"
    android:clickable="true"
    android:layout_marginBottom="10dp">
```

**Atributos Explicados:**

| Atributo | ¿Qué hace? | ¿Qué puedes cambiar? |
|----------|------------|---------------------|
| `android:clickable="true"` | Hace que responda a toques | `false` para desactivar |
| `android:gravity="center_vertical"` | Centra contenido verticalmente | `center`, `bottom`, `top` |
| `android:background` | Fondo gris redondeado | Cambiar por otro drawable |
| `android:layout_marginBottom` | Espacio debajo del elemento | Ajustar spacing |

### Emoji como Icono
```xml
<TextView
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:text="🏆"
    android:textSize="20sp"
    android:gravity="center"
    android:background="@drawable/circle_orange" />
```

**¿Por qué TextView y no ImageView?**
- Los **emojis** se muestran mejor en TextView
- **gravity="center"**: Centra el emoji en el círculo
- **Fácil cambio**: Solo cambiar el emoji en `android:text`

**Emojis alternativos:**
- 🏆 → 🎯, ⭐, 🏅 (para metas)
- 🍴 → 🥗, 🍎, 🥙 (para nutrición)  
- 🏃 → 💪, 🚴, 🏋️ (para actividad)

---

## 🔘 Switches y Notificaciones

### SwitchMaterial (Interruptores)
```xml
<com.google.android.material.switchmaterial.SwitchMaterial
    android:id="@+id/switch_agua"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:checked="true"
    app:thumbTint="#FFFFFF"
    app:trackTint="#00BCD4" />
```

**Atributos Explicados:**

| Atributo | ¿Qué hace? | ¿Qué puedes cambiar? |
|----------|------------|---------------------|
| `android:checked` | Estado inicial (encendido/apagado) | `true` o `false` |
| `app:thumbTint` | Color del círculo que se mueve | Cualquier color hex |
| `app:trackTint` | Color de la barra del switch | Color cuando está activo |

**Colores sugeridos:**
- Verde: `#4CAF50`
- Azul: `#2196F3`
- Naranja: `#FF9800`
- Rojo: `#F44336`

**Para programar el switch:**
```kotlin
switch_agua.setOnCheckedChangeListener { _, isChecked ->
    if (isChecked) {
        // Activar notificaciones de agua
    } else {
        // Desactivar notificaciones
    }
}
```

---

## 🎨 Recursos Drawable

### 1. input_border.xml (Borde de Campos)
```xml
<shape android:shape="rectangle">
    <stroke android:width="1dp" android:color="#E0E0E0" />
    <corners android:radius="8dp" />
    <solid android:color="#FFFFFF" />
</shape>
```

**¿Qué hace cada parte?**
- **stroke**: Borde gris claro de 1dp
- **corners**: Esquinas redondeadas de 8dp
- **solid**: Fondo blanco

**Personalizaciones:**
```xml
<!-- Borde más grueso -->
<stroke android:width="2dp" android:color="#CCCCCC" />

<!-- Más redondeado -->
<corners android:radius="12dp" />

<!-- Fondo gris claro -->
<solid android:color="#F8F8F8" />
```

### 2. rounded_gray_background.xml (Fondo de Opciones)
```xml
<shape android:shape="rectangle">
    <solid android:color="#F5F5F5" />
    <corners android:radius="12dp" />
</shape>
```

### 3. circle_orange.xml (Fondos Circulares)
```xml
<shape android:shape="oval">
    <solid android:color="#FF9800" />
</shape>
```

**Colores de los círculos:**
- **Naranja**: `#FF9800` (Gestionar Metas)
- **Cyan**: `#00BCD4` (Preferencias)
- **Azul**: `#2196F3` (Nivel Actividad)

### 4. ic_arrow_right_black.xml (Flecha Personalizada)
```xml
<vector android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#000000"
        android:pathData="M9,5l7,7l-7,7V5z" />
</vector>
```

**¿Qué hace?**
- **vector**: Icono escalable
- **fillColor**: Color de relleno (negro)
- **pathData**: Coordenadas que forman la flecha

---

## 🔧 Personalización y Modificaciones

### Cambiar Colores del Tema
```xml
<!-- En colors.xml -->
<color name="primary_color">#FF9800</color>
<color name="secondary_color">#00BCD4</color>
<color name="background_gray">#F5F5F5</color>
<color name="border_gray">#E0E0E0</color>
<color name="text_black">#000000</color>
```

### Añadir Nuevos Campos
```xml
<!-- Nuevo campo para altura -->
<LinearLayout>
    <TextView android:text="Altura:" />
    <EditText 
        android:id="@+id/et_altura"
        android:hint="Introduce tu altura"
        android:inputType="numberDecimal" />
</LinearLayout>
```

### Modificar Tamaños
```xml
<!-- Hacer iconos más grandes -->
<TextView android:layout_width="50dp" android:layout_height="50dp" />

<!-- Texto más grande -->
<TextView android:textSize="18sp" />

<!-- Más padding -->
<LinearLayout android:padding="24dp" />
```

### Cambiar Orientación de Layout
```xml
<!-- Para layout horizontal -->
<LinearLayout 
    android:orientation="horizontal"
    android:gravity="center_vertical" />
```

---

## 💡 Consejos de Programación

### En el Fragment (Kotlin):
```kotlin
class PerfilFragment : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, 
                            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Configurar clicks
        view.findViewById<LinearLayout>(R.id.layout_gestionar_metas).setOnClickListener {
            // Navegar a pantalla de metas
        }
        
        // Configurar spinner
        val generos = arrayOf("Masculino", "Femenino", "Otro")
        val adapter = ArrayAdapter(requireContext(), 
            android.R.layout.simple_spinner_item, generos)
        view.findViewById<Spinner>(R.id.spinner_genero).adapter = adapter
        
        // Configurar switches
        view.findViewById<SwitchMaterial>(R.id.switch_agua).setOnCheckedChangeListener { _, isChecked ->
            // Lógica para notificaciones
        }
    }
}
```

---

## 📝 Resumen de IDs para Programación

### Campos de Entrada:
- `et_peso` - Campo de peso
- `et_estatura` - Campo de estatura  
- `et_edad` - Campo de edad
- `spinner_genero` - Selector de género

### Navegación:
- `layout_gestionar_metas` - Opción de metas
- `layout_preferencias_nutricion` - Opción de preferencias
- `layout_nivel_actividad` - Opción de actividad

### Switches:
- `switch_agua` - Notificaciones de agua
- `switch_dieta` - Notificaciones de dieta
- `switch_pesaje` - Notificaciones de pesaje
- `switch_modo_oscuro` - Modo oscuro

---

## 🚀 ACTUALIZACIÓN: Todas las Interfaces Preparadas para Backend

### 📱 Estado de Todas las Pantallas

**✅ COMPLETAMENTE PREPARADAS PARA BACKEND:**
- 🏠 **Perfil** - `fragment_perfil.xml`
- 🎯 **Gestión de Metas** - `fragment_gestionarmetas.xml`
- 🏃 **Nivel de Actividad** - `fragment_nivelactividad.xml`
- 🍴 **Preferencias de Nutrición** - `fragment_preferenciasnutricion.xml`

---

## 🏠 1. PANTALLA DE PERFIL

### Elementos Frontend Preparados:
```kotlin
// CAMPOS DE ENTRADA - Todos vacíos con hints
et_nombre_usuario        // EditText - Nombre del usuario
tv_email_usuario        // TextView - Email (readonly, viene del backend)
et_peso                 // EditText - Peso en kg
et_estatura             // EditText - Estatura en cm
et_edad                 // EditText - Edad en años
spinner_genero          // Spinner - Género (Masculino/Femenino/Otro)

// SWITCHES - Estados que se guardan en backend
switch_agua             // Notificaciones de agua
switch_dieta            // Notificaciones de dieta  
switch_pesaje           // Notificaciones de pesaje
switch_modo_oscuro      // Modo oscuro de la app

// NAVEGACIÓN - Solo frontend, sin datos
layout_gestionar_metas      // Navega a gestión de metas
layout_preferencias_nutricion  // Navega a preferencias
layout_nivel_actividad      // Navega a nivel de actividad
```

### Cómo Conectar con Backend:
```kotlin
class PerfilFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 1. CARGAR DATOS DEL USUARIO desde backend
        cargarPerfilUsuario()
        
        // 2. CONFIGURAR LISTENERS para guardar cambios
        configurarGuardadoAutomatico()
    }
    
    private fun cargarPerfilUsuario() {
        // TODO: Llamada al backend para obtener datos del usuario
        val usuario = backendService.obtenerPerfilUsuario(userId)
        
        // Llenar campos con datos del backend
        et_nombre_usuario.setText(usuario.nombre)
        tv_email_usuario.text = usuario.email
        et_peso.setText(usuario.peso.toString())
        et_estatura.setText(usuario.estatura.toString())
        et_edad.setText(usuario.edad.toString())
        
        // Configurar spinner género
        val posicionGenero = when(usuario.genero) {
            "Masculino" -> 0
            "Femenino" -> 1
            "Otro" -> 2
            else -> 0
        }
        spinner_genero.setSelection(posicionGenero)
        
        // Configurar switches
        switch_agua.isChecked = usuario.notificacionAgua
        switch_dieta.isChecked = usuario.notificacionDieta
        switch_pesaje.isChecked = usuario.notificacionPesaje
        switch_modo_oscuro.isChecked = usuario.modoOscuro
    }
    
    private fun configurarGuardadoAutomatico() {
        // Guardar cuando el usuario cambie los campos
        et_peso.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Guardar peso en backend
                val peso = s.toString().toFloatOrNull()
                if (peso != null) {
                    backendService.actualizarPeso(userId, peso)
                }
            }
        })
        
        // Lo mismo para otros campos...
        
        // Switches
        switch_agua.setOnCheckedChangeListener { _, isChecked ->
            backendService.actualizarNotificacionAgua(userId, isChecked)
        }
    }
}
```

---

## 🎯 2. GESTIÓN DE METAS

### Elementos Frontend Preparados:
```kotlin
// DATOS DEL OBJETIVO ACTUAL - Vienen del backend
tv_objetivo_titulo      // TextView - "Objetivo: Ganar masa muscular"
tv_peso_info           // TextView - "Peso objetivo: 65kg (Actual: 70kg)"
progress_objetivo      // ProgressBar - Progreso 0-100
tv_porcentaje         // TextView - "60% completado"

// BOTONES DE ACCIÓN
btn_editar_objetivo    // Botón para editar objetivo actual
btn_agregar_objetivo   // Botón para agregar nuevo objetivo

// HITOS - Lista dinámica desde backend  
checkbox_hito_1       // CheckBox - Primer hito
checkbox_hito_2       // CheckBox - Segundo hito  
checkbox_hito_3       // CheckBox - Tercer hito
```

### Cómo Conectar con Backend:
```kotlin
private fun cargarMetasUsuario() {
    // TODO: Obtener meta actual del usuario
    val metaActual = backendService.obtenerMetaActual(userId)
    
    if (metaActual != null) {
        // Llenar datos de la meta
        tv_objetivo_titulo.text = "Objetivo: ${metaActual.tipoObjetivo}"
        tv_peso_info.text = "Peso objetivo: ${metaActual.pesoObjetivo}kg (Actual: ${metaActual.pesoActual}kg)"
        
        // Calcular y mostrar progreso
        val progreso = metaActual.calcularProgreso()
        progress_objetivo.progress = progreso
        tv_porcentaje.text = "${progreso}% completado"
        
        // Cargar hitos
        cargarHitosMeta(metaActual.hitos)
    } else {
        // No hay meta activa, mostrar mensaje
        mostrarSinMeta()
    }
}

private fun cargarHitosMeta(hitos: List<Hito>) {
    // TODO: Crear checkboxes dinámicamente para cada hito
    hitos.forEachIndexed { index, hito ->
        val checkbox = CheckBox(context)
        checkbox.text = hito.descripcion
        checkbox.isChecked = hito.completado
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            // Actualizar estado del hito en backend
            backendService.actualizarHito(hito.id, isChecked)
            // Recalcular progreso
            cargarMetasUsuario()
        }
        // Agregar checkbox al layout
        layout_hitos.addView(checkbox)
    }
}
```

---

## 🏃 3. NIVEL DE ACTIVIDAD

### Elementos Frontend Preparados:
```kotlin
// OPCIONES DE SELECCIÓN - Solo una seleccionada
layout_sedentario      // LinearLayout clickeable - Sedentario
layout_moderado        // LinearLayout clickeable - Moderado  
layout_activo          // LinearLayout clickeable - Activo
layout_muy_activo      // LinearLayout clickeable - Muy Activo

// BOTÓN DE GUARDADO
btn_guardar_nivel      // Guardar selección en backend
```

### Lógica Frontend (Ya implementada):
```kotlin
class NivelActividadFragment : Fragment() {
    
    private var nivelSeleccionado: String = "Moderado" // Por defecto
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 1. CARGAR NIVEL ACTUAL del backend
        cargarNivelActual()
        
        // 2. CONFIGURAR SELECCIÓN VISUAL (ya implementado)
        configurarSeleccion()
        
        // 3. GUARDAR EN BACKEND al presionar botón
        btn_guardar_nivel.setOnClickListener {
            guardarNivelActividad()
        }
    }
    
    private fun cargarNivelActual() {
        // TODO: Obtener nivel actual del usuario
        val nivelActual = backendService.obtenerNivelActividad(userId)
        nivelSeleccionado = nivelActual ?: "Moderado"
        
        // Actualizar UI con la selección actual
        actualizarSeleccionVisual()
    }
    
    private fun guardarNivelActividad() {
        // TODO: Guardar en backend
        backendService.actualizarNivelActividad(userId, nivelSeleccionado)
        
        // Mostrar confirmación
        Toast.makeText(context, "Nivel de actividad guardado", Toast.LENGTH_SHORT).show()
        
        // Volver atrás
        findNavController().popBackStack()
    }
}
```

---

## 🍴 4. PREFERENCIAS DE NUTRICIÓN

### Elementos Frontend Preparados:
```kotlin
// SELECCIÓN DE PLAN
spinner_plan_dieta         // Spinner - Plan de dieta seleccionado

// MACRONUTRIENTES - Porcentajes que suman 100%
et_proteinas              // EditText - Porcentaje proteínas
et_carbohidratos          // EditText - Porcentaje carbohidratos  
et_grasas                 // EditText - Porcentaje grasas

// PREFERENCIAS ALIMENTARIAS
et_alergias               // EditText - Lista de alergias
et_alimentos_favoritos    // EditText - Alimentos favoritos

// GUARDADO
btn_guardar_preferencias  // Guardar todo en backend
```

### Cómo Conectar con Backend:
```kotlin
class PreferenciasNutricionFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 1. CARGAR PREFERENCIAS ACTUALES
        cargarPreferenciasActuales()
        
        // 2. CONFIGURAR SPINNER
        setupPlanDietaSpinner()
        
        // 3. CONFIGURAR GUARDADO
        btn_guardar_preferencias.setOnClickListener {
            guardarPreferenciasNutricion()
        }
    }
    
    private fun cargarPreferenciasActuales() {
        // TODO: Obtener preferencias del usuario
        val preferencias = backendService.obtenerPreferenciasNutricion(userId)
        
        if (preferencias != null) {
            // Llenar campos con datos existentes
            seleccionarPlanDieta(preferencias.planDieta)
            et_proteinas.setText(preferencias.porcentajeProteinas.toString())
            et_carbohidratos.setText(preferencias.porcentajeCarbohidratos.toString())
            et_grasas.setText(preferencias.porcentajeGrasas.toString())
            et_alergias.setText(preferencias.alergias)
            et_alimentos_favoritos.setText(preferencias.alimentosFavoritos)
        }
    }
    
    private fun guardarPreferenciasNutricion() {
        // Obtener datos de los campos
        val preferencias = PreferenciasNutricion(
            planDieta = spinner_plan_dieta.selectedItem.toString(),
            porcentajeProteinas = et_proteinas.text.toString().toIntOrNull() ?: 0,
            porcentajeCarbohidratos = et_carbohidratos.text.toString().toIntOrNull() ?: 0,
            porcentajeGrasas = et_grasas.text.toString().toIntOrNull() ?: 0,
            alergias = et_alergias.text.toString(),
            alimentosFavoritos = et_alimentos_favoritos.text.toString()
        )
        
        // TODO: Validar que porcentajes sumen 100%
        if (validarPorcentajes(preferencias)) {
            // Guardar en backend
            backendService.guardarPreferenciasNutricion(userId, preferencias)
            Toast.makeText(context, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Los porcentajes deben sumar 100%", Toast.LENGTH_SHORT).show()
        }
    }
}
```

---

## 🔧 GUÍA DE IMPLEMENTACIÓN BACKEND

### 1. Estructura de Datos Recomendada

```kotlin
// MODELO DE USUARIO
data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val peso: Float?,
    val estatura: Float?,
    val edad: Int?,
    val genero: String?,
    val notificacionAgua: Boolean = false,
    val notificacionDieta: Boolean = false,
    val notificacionPesaje: Boolean = false,
    val modoOscuro: Boolean = false
)

// MODELO DE META
data class Meta(
    val id: String,
    val usuarioId: String,
    val tipoObjetivo: String, // "Ganar masa muscular", "Perder peso", etc.
    val pesoObjetivo: Float,
    val pesoActual: Float,
    val fechaInicio: Date,
    val fechaObjetivo: Date,
    val hitos: List<Hito>
) {
    fun calcularProgreso(): Int {
        val hitosCompletados = hitos.count { it.completado }
        return if (hitos.isNotEmpty()) {
            (hitosCompletados * 100) / hitos.size
        } else 0
    }
}

// MODELO DE HITO
data class Hito(
    val id: String,
    val metaId: String,
    val descripcion: String,
    val completado: Boolean,
    val fechaCompletado: Date?
)

// MODELO DE PREFERENCIAS NUTRICIÓN
data class PreferenciasNutricion(
    val usuarioId: String,
    val planDieta: String,
    val porcentajeProteinas: Int,
    val porcentajeCarbohidratos: Int,
    val porcentajeGrasas: Int,
    val alergias: String,
    val alimentosFavoritos: String
)

// MODELO DE NIVEL ACTIVIDAD
data class NivelActividad(
    val usuarioId: String,
    val nivel: String, // "Sedentario", "Moderado", "Activo", "Muy Activo"
    val factorActividad: Float // 1.2, 1.375, 1.55, 1.725
)
```

### 2. Interface de Backend Service

```kotlin
interface BackendService {
    
    // PERFIL
    suspend fun obtenerPerfilUsuario(userId: String): Usuario?
    suspend fun actualizarPerfil(userId: String, usuario: Usuario): Boolean
    suspend fun actualizarPeso(userId: String, peso: Float): Boolean
    suspend fun actualizarNotificaciones(userId: String, tipo: String, activo: Boolean): Boolean
    
    // METAS
    suspend fun obtenerMetaActual(userId: String): Meta?
    suspend fun crearMeta(meta: Meta): Boolean
    suspend fun actualizarMeta(meta: Meta): Boolean
    suspend fun actualizarHito(hitoId: String, completado: Boolean): Boolean
    
    // NIVEL ACTIVIDAD
    suspend fun obtenerNivelActividad(userId: String): String?
    suspend fun actualizarNivelActividad(userId: String, nivel: String): Boolean
    
    // PREFERENCIAS NUTRICIÓN  
    suspend fun obtenerPreferenciasNutricion(userId: String): PreferenciasNutricion?
    suspend fun guardarPreferenciasNutricion(userId: String, preferencias: PreferenciasNutricion): Boolean
}
```

### 3. Pasos para Implementar Backend

**Paso 1: Configurar Base de Datos**
```sql
-- Tabla usuarios
CREATE TABLE usuarios (
    id VARCHAR PRIMARY KEY,
    nombre VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    peso FLOAT,
    estatura FLOAT,
    edad INTEGER,
    genero VARCHAR,
    notificacion_agua BOOLEAN DEFAULT FALSE,
    notificacion_dieta BOOLEAN DEFAULT FALSE,
    notificacion_pesaje BOOLEAN DEFAULT FALSE,
    modo_oscuro BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla metas
CREATE TABLE metas (
    id VARCHAR PRIMARY KEY,
    usuario_id VARCHAR REFERENCES usuarios(id),
    tipo_objetivo VARCHAR NOT NULL,
    peso_objetivo FLOAT NOT NULL,
    peso_actual FLOAT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_objetivo DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla hitos
CREATE TABLE hitos (
    id VARCHAR PRIMARY KEY,
    meta_id VARCHAR REFERENCES metas(id),
    descripcion VARCHAR NOT NULL,
    completado BOOLEAN DEFAULT FALSE,
    fecha_completado TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla preferencias_nutricion
CREATE TABLE preferencias_nutricion (
    usuario_id VARCHAR PRIMARY KEY REFERENCES usuarios(id),
    plan_dieta VARCHAR,
    porcentaje_proteinas INTEGER,
    porcentaje_carbohidratos INTEGER,
    porcentaje_grasas INTEGER,
    alergias TEXT,
    alimentos_favoritos TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla nivel_actividad
CREATE TABLE nivel_actividad (
    usuario_id VARCHAR PRIMARY KEY REFERENCES usuarios(id),
    nivel VARCHAR NOT NULL,
    factor_actividad FLOAT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Paso 2: Implementar API Endpoints**
```kotlin
// Ejemplo con Ktor
fun Application.configurePerfil() {
    routing {
        route("/api/perfil") {
            get("/{userId}") {
                val userId = call.parameters["userId"]!!
                val usuario = usuarioService.obtenerPorId(userId)
                call.respond(usuario ?: HttpStatusCode.NotFound)
            }
            
            put("/{userId}") {
                val userId = call.parameters["userId"]!!
                val usuario = call.receive<Usuario>()
                val success = usuarioService.actualizar(userId, usuario)
                call.respond(if (success) HttpStatusCode.OK else HttpStatusCode.InternalServerError)
            }
        }
        
        route("/api/metas") {
            get("/{userId}") {
                val userId = call.parameters["userId"]!!
                val meta = metaService.obtenerMetaActual(userId)
                call.respond(meta ?: HttpStatusCode.NotFound)
            }
            
            post("/") {
                val meta = call.receive<Meta>()
                val success = metaService.crear(meta)
                call.respond(if (success) HttpStatusCode.Created else HttpStatusCode.InternalServerError)
            }
        }
        
        // ... más endpoints
    }
}
```

**Paso 3: Implementar en Android**
```kotlin
// Retrofit interface
interface NutritionApiService {
    @GET("api/perfil/{userId}")
    suspend fun obtenerPerfil(@Path("userId") userId: String): Usuario
    
    @PUT("api/perfil/{userId}")
    suspend fun actualizarPerfil(@Path("userId") userId: String, @Body usuario: Usuario): ResponseBody
    
    @GET("api/metas/{userId}")
    suspend fun obtenerMeta(@Path("userId") userId: String): Meta
    
    // ... más endpoints
}

// Repository
class PerfilRepository(private val apiService: NutritionApiService) {
    
    suspend fun obtenerPerfil(userId: String): Usuario? {
        return try {
            apiService.obtenerPerfil(userId)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun actualizarPerfil(userId: String, usuario: Usuario): Boolean {
        return try {
            apiService.actualizarPerfil(userId, usuario)
            true
        } catch (e: Exception) {
            false
        }
    }
}
```

---

## 📋 CHECKLIST DE IMPLEMENTACIÓN

### ✅ Frontend (Completado)
- [x] Layouts con campos vacíos y hints
- [x] IDs únicos para todos los elementos
- [x] Strings externalizados
- [x] Lógica de UI (selección, switches)
- [x] Validaciones básicas de frontend

### 🔄 Backend (Por Implementar)
- [ ] Configurar base de datos
- [ ] Crear modelos de datos
- [ ] Implementar API endpoints
- [ ] Configurar autenticación
- [ ] Implementar validaciones
- [ ] Crear tests unitarios

### 🔄 Integración (Por Implementar) 
- [ ] Conectar Retrofit con API
- [ ] Implementar Repository pattern
- [ ] Agregar manejo de errores
- [ ] Implementar cache local
- [ ] Agregar sincronización offline
- [ ] Testing de integración

---

## 🚀 Próximos Pasos

1. **Implementar Backend API** siguiendo la estructura sugerida
2. **Conectar Frontend con API** usando Retrofit
3. **Agregar Validaciones** tanto frontend como backend  
4. **Implementar Navegación** entre pantallas
5. **Agregar Persistencia Local** para modo offline
6. **Testing Completo** de todas las funcionalidades

¡Con esta guía tienes todo lo necesario para conectar tu frontend con el backend! 🎉

---

## 🚀 Próximos Pasos
