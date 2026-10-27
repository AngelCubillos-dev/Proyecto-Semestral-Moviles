# Master Martini · App móvil de contenidos técnicos

## Integrantes
- Angel Cubillos
- Francisca Rojas

Docente: DIEGO PATRICIO CARES GONZALEZ · Asignatura: DSY1105 Desarrollo de Aplicaciones Móviles · CITT Duoc Antonio Varas

##TRELLO

https://trello.com/b/vCyRHQsc/proyecto-semestral-moviles-master-martini

## Descripción
Prototipo (MVP) para Master Martini Chile SPA que entrega recetas, videos técnicos y cursos a clientes y prospectos, y recoge su perfil e intereses. Usa solo datos ficticios.

## Funcionalidades

- Ingreso simple (correo + contraseña) y registro de 3 datos (contraseña con hash SHA-256 + sal) y cierre de sesión.
- Guardia de rutas: sin sesión solo se accede a login/registro.
- Inicio simplificado: saludo, tres accesos grandes (Recetas, Videos, Cursos) y 3 sugerencias según los intereses del usuario.
- Explorar con búsqueda y filtros (Recetas / Videos / Cursos).
- Detalle de receta (ingredientes marcables, pasos, compartir), de curso (lecciones desplegables) y de video (reproductor).
- Favoritos persistentes por usuario.
- Formulario de contacto con historial de consultas guardado localmente.
- Perfil: foto (cámara/galería), ubicación del negocio, datos del negocio, intereses y accesibilidad.

## Arquitectura

```text
app/src/main/java/com/example/mastermartini/
├── data/
│   ├── local/        # Room: entidades, DAOs y base de datos
│   ├── repository/   # Repositorios (usuarios, favoritos, contacto)
│   └── session/      # Sesión local y preferencia de accesibilidad
├── domain/           # Validators.kt (reglas de validación puras)
├── model/            # ContentItem y ContentCatalog (contenido ficticio)
├── viewmodel/        # AppViewModel, AuthViewModel, ProfileViewModel, ContentViewModel, ContactViewModel
├── ui/
│   ├── components/   # Campos, botones, tarjetas, reproductor, diálogos
│   ├── navigation/   # Routes y AppNavigation
│   ├── screens/      # auth, home, explore, favorites, detail, contact, profile
│   └── theme/        # Colores, tipografía y formas
├── util/             # PhotoStorage, LocationHelper, PasswordHasher
├── MasterMartiniApp.kt   # Contenedor de dependencias
└── MainActivity.kt
```

Flujo: **UI (Compose) → ViewModel (estado + validación) → Repository → Room / SharedPreferences**.

## Requisitos y ejecución

- Android Studio (versión estable más reciente), JDK 17 o superior, Android SDK 36.
- Dispositivo o emulador con Android 8.0 (API 26) o superior.

1. Clona el repositorio: `git clone <poner url ola pone el url aaa>`
2. Abre la carpeta en Android Studio y espera la sincronización de Gradle.
3. Selecciona un emulador o dispositivo y ejecuta el módulo `app`.
4. Pruebas unitarias: `./gradlew test` (Windows: `gradlew.bat test`).

Para probar la **cámara** en el emulador: *Extended controls → Camera*. Para la **ubicación**: *Extended controls → Location → Set location*.
