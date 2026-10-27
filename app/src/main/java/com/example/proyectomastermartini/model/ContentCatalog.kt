package com.example.mastermartini.model

import com.example.mastermartini.R

/**
 * Catálogo local con contenido ficticio para el MVP académico.
 * Para agregar contenido real y autorizado basta con sumar elementos a esta lista.
 *
 * IMÁGENES: una ilustración por categoría en res/drawable (chocolate, galletas, bolleria,
 * helado, pasteleria, hojaldre, empanada). Para cambiar la imagen de un contenido,
 * modifica el valor de imageRes de ese elemento (ej: R.drawable.helado).
 */
object ContentCatalog {

    val interests = listOf(
        "Chocolatería", "Pastelería de vitrina", "Heladería", "Bollería",
        "Masas de hoja", "Galletas", "Empanadas", "Productos de horneo"
    )

    val businessTypes = listOf(
        "Panadería", "Pastelería", "Chocolatería", "Casino", "Restaurante", "Emprendimiento", "Otro"
    )

    val businessSizes = listOf("Micro (1-4 personas)", "Pequeño (5-19 personas)", "Mediano (20 o más)")

    val contactTopics = listOf("Consulta de productos", "Cursos y capacitaciones", "Invitación a eventos", "Cotización", "Otro")

    val items: List<ContentItem> = listOf(
        // ---------------- RECETAS ----------------
        ContentItem(
            id = "receta-galletas-chips",
            type = ContentType.Recipe,
            category = "Galletas",
            title = "Galletas con chips de chocolate",
            description = "Crujientes por fuera, suaves por dentro. Una receta versátil para sumar a tu vitrina.",
            meta = "35 min · 12 unidades",
            imageRes = R.drawable.galletas,
            ingredients = listOf(
                Ingredient("Harina", "180 g"), Ingredient("Mantequilla", "100 g"),
                Ingredient("Azúcar", "100 g"), Ingredient("Huevo", "1 unidad"),
                Ingredient("Chips de chocolate", "100 g")
            ),
            steps = listOf(
                "Mezcla la mantequilla a temperatura ambiente con el azúcar. Agrega el huevo e integra.",
                "Incorpora la harina y los chips de chocolate hasta obtener una masa uniforme.",
                "Forma 12 porciones sobre una bandeja con papel para hornear, dejando espacio entre ellas.",
                "Hornea a 180 °C durante 12 a 15 minutos. Deja enfriar antes de servir."
            ),
            relatedVideoId = "video-templado"
        ),
        ContentItem(
            id = "receta-empanadas-queso",
            type = ContentType.Recipe,
            category = "Empanadas",
            title = "Empanadas de queso al horno",
            description = "Una preparación dorada y crujiente, con relleno de queso fundido para servir caliente.",
            meta = "50 min · 10 unidades",
            imageRes = R.drawable.empanada,
            ingredients = listOf(
                Ingredient("Harina", "300 g"), Ingredient("Mantequilla", "60 g"),
                Ingredient("Agua tibia", "120 ml"), Ingredient("Queso mantecoso", "250 g"),
                Ingredient("Huevo", "1 unidad")
            ),
            steps = listOf(
                "Mezcla la harina con la mantequilla y el agua tibia hasta formar una masa suave.",
                "Amasa durante unos minutos, cubre y deja reposar por 20 minutos.",
                "Estira la masa, corta discos y rellena cada uno con queso.",
                "Cierra las empanadas, pincela con huevo y hornea a 190 °C hasta dorar."
            ),
            relatedVideoId = "video-hojaldre"
        ),
        ContentItem(
            id = "receta-brownie",
            type = ContentType.Recipe,
            category = "Chocolatería",
            title = "Brownie húmedo de chocolate",
            description = "Textura densa y húmeda, ideal para porciones de vitrina o para acompañar helado.",
            meta = "45 min · 9 porciones",
            imageRes = R.drawable.chocolate,
            ingredients = listOf(
                Ingredient("Chocolate cobertura", "200 g"), Ingredient("Mantequilla", "120 g"),
                Ingredient("Azúcar", "150 g"), Ingredient("Huevos", "3 unidades"),
                Ingredient("Harina", "80 g")
            ),
            steps = listOf(
                "Derrite el chocolate con la mantequilla a baño María y deja entibiar.",
                "Bate los huevos con el azúcar e integra la mezcla de chocolate.",
                "Agrega la harina tamizada con movimientos envolventes.",
                "Vierte en un molde de 20 cm y hornea a 170 °C por 25 minutos."
            ),
            relatedVideoId = "video-templado"
        ),
        ContentItem(
            id = "receta-croissant",
            type = ContentType.Recipe,
            category = "Bollería",
            title = "Medialunas de mantequilla",
            description = "Masa laminada suave con aroma a mantequilla, perfecta para el desayuno.",
            meta = "3 h · 12 unidades",
            imageRes = R.drawable.bolleria,
            ingredients = listOf(
                Ingredient("Harina de fuerza", "500 g"), Ingredient("Mantequilla para laminar", "250 g"),
                Ingredient("Leche", "200 ml"), Ingredient("Azúcar", "60 g"), Ingredient("Levadura fresca", "20 g")
            ),
            steps = listOf(
                "Prepara la masa base con harina, leche, azúcar y levadura. Reposa en frío 1 hora.",
                "Incorpora la mantequilla y realiza dos dobleces simples con descansos de 30 minutos.",
                "Estira, corta triángulos y enrolla formando las medialunas.",
                "Fermenta 1 hora, pincela con huevo y hornea a 190 °C por 15 minutos."
            ),
            relatedVideoId = "video-hojaldre"
        ),
        ContentItem(
            id = "receta-helado-vainilla",
            type = ContentType.Recipe,
            category = "Heladería",
            title = "Helado artesanal de vainilla",
            description = "Base cremosa de vainilla, una receta simple para iniciarse en la heladería.",
            meta = "40 min + reposo · 8 porciones",
            imageRes = R.drawable.helado,
            ingredients = listOf(
                Ingredient("Leche entera", "500 ml"), Ingredient("Crema", "250 ml"),
                Ingredient("Yemas", "5 unidades"), Ingredient("Azúcar", "120 g"),
                Ingredient("Vaina de vainilla", "1 unidad")
            ),
            steps = listOf(
                "Calienta la leche con la crema y la vainilla sin que hierva.",
                "Bate las yemas con el azúcar y agrega la leche caliente de a poco.",
                "Cocina a fuego bajo hasta que cubra el dorso de una cuchara. Enfría en refrigerador.",
                "Procesa en la máquina de helados o congela batiendo cada 45 minutos."
            )
        ),

        // ---------------- VIDEOS TÉCNICOS ----------------
        ContentItem(
            id = "video-templado",
            type = ContentType.Video,
            category = "Chocolatería",
            title = "Introducción al templado de chocolate",
            description = "Conoce las etapas del templado y aprende a reconocer un acabado brillante y uniforme.",
            meta = "Chocolatería · 8 min",
            imageRes = R.drawable.chocolate,
            rawVideoName = "video_templado"
        ),
        ContentItem(
            id = "video-hojaldre",
            type = ContentType.Video,
            category = "Masas de hoja",
            title = "Laminado de masa de hojaldre",
            description = "Técnica paso a paso para lograr capas definidas y un horneado parejo.",
            meta = "Masas de hoja · 12 min",
            imageRes = R.drawable.hojaldre,
            rawVideoName = "video_hojaldre"
        ),
        ContentItem(
            id = "video-decoracion",
            type = ContentType.Video,
            category = "Pastelería de vitrina",
            title = "Decoración de vitrina con manga",
            description = "Usos de las boquillas más comunes para acabados profesionales en tortas individuales.",
            meta = "Pastelería · 10 min",
            imageRes = R.drawable.pasteleria,
            rawVideoName = "video_decoracion"
        ),
        ContentItem(
            id = "video-ganache",
            type = ContentType.Video,
            category = "Chocolatería",
            title = "Ganache perfecta: proporciones y usos",
            description = "Cómo ajustar la proporción de chocolate y crema según el uso: relleno, cobertura o trufa.",
            meta = "Chocolatería · 9 min",
            imageRes = R.drawable.chocolate,
            rawVideoName = "video_ganache"
        ),

        // ---------------- CURSOS ----------------
        ContentItem(
            id = "curso-chocolateria",
            type = ContentType.Course,
            category = "Chocolatería",
            title = "Fundamentos de chocolatería",
            description = "Aprende a trabajar el chocolate: tipos de cobertura, templado y ganache para tu negocio.",
            meta = "3 lecciones · 29 min",
            imageRes = R.drawable.chocolate,
            lessonIds = listOf("video-templado", "video-ganache", "video-decoracion"),
            level = "Nivel inicial"
        ),
        ContentItem(
            id = "curso-masas",
            type = ContentType.Course,
            category = "Masas de hoja",
            title = "Masas de hoja y bollería",
            description = "Domina el laminado y obtén productos de bollería con volumen y capas uniformes.",
            meta = "2 lecciones · 22 min",
            imageRes = R.drawable.hojaldre,
            lessonIds = listOf("video-hojaldre", "video-decoracion"),
            level = "Nivel intermedio"
        ),
        ContentItem(
            id = "curso-vitrina",
            type = ContentType.Course,
            category = "Pastelería de vitrina",
            title = "Pastelería de vitrina para emprendedores",
            description = "Ideas y técnicas para armar una vitrina atractiva, rentable y de alta rotación.",
            meta = "2 lecciones · 19 min",
            imageRes = R.drawable.pasteleria,
            lessonIds = listOf("video-decoracion", "video-ganache"),
            level = "Nivel inicial"
        )
    )

    private val byId = items.associateBy { it.id }

    fun find(id: String): ContentItem? = byId[id]

    fun lessonsOf(course: ContentItem): List<ContentItem> = course.lessonIds.mapNotNull { byId[it] }

    fun featured(): List<ContentItem> =
        listOf("curso-chocolateria", "receta-galletas-chips", "video-templado").mapNotNull { byId[it] }
}
