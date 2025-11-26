🏡 SaleHome – Plataforma de Arriendos y Ventas

Proyecto 2 – Android Kotlin + Jetpack Compose
Desarrollado por: Nicolás López

📘 Índice

🏡 Descripción General

🌟 Características Principales

🛠️ Tecnologías Utilizadas

🌐 Integración con APIs

🚀 Flujos Implementados

📂 Estructura del Proyecto

🧪 Pruebas Incluidas

🔌 Backend Spring Boot

📦 APK Firmado (Evidencias)

📋 Evidencias Trello + GitHub

👨‍💻 Autor

🏡 Descripción General

SaleHome es una aplicación Android desarrollada en Kotlin utilizando Jetpack Compose, que simula una plataforma de arriendo y venta de propiedades.

Cuenta con dos roles principales:

Cliente: Visualiza catálogo, detalles y gestiona “Me interesa”.

Administrador: CRUD completo de propiedades.

Incluye integración con una API externa (JSONPlaceholder) y un microservicio propio en Spring Boot, consumido mediante Retrofit.

🌟 Características Principales

✔ Interfaz moderna con Jetpack Compose
Diseñada con Material 3, layouts responsivos y componentes declarativos.

✔ Arquitectura MVVM
Separa UI, lógica y manejo de estado.

✔ Roles diferenciados

Cliente → catálogo, detalles, favoritos

Administrador → CRUD completo

✔ Persistencia de sesión con DataStore
Funcionalidad “Recordarme”.

✔ Navigation Compose
Navegación avanzada con grafos anidados.

✔ Estado compartido

ClientViewModel → favoritos

AdminViewModel → CRUD

✔ Conexión a API externa y backend propio

🛠️ Tecnologías Utilizadas

Kotlin

Jetpack Compose

Material 3

MVVM

Navigation Compose

DataStore

Retrofit + Gson

Coroutines + Flow

JUnit / Mockito / UI Tests

🌐 Integración con APIs
🔹 API Externa – JSONPlaceholder
object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

🔹 API Propia – Spring Boot (Oracle DB + Retrofit)

Repositorio:
https://github.com/Nicolas-15/BackendMobilEV3.git

Endpoints principales

Usuarios

POST /api/users/login

POST /api/users/register

Propiedades

GET /api/properties

POST /api/properties

PUT /api/properties/{id}

DELETE /api/properties/{id}

Cliente Retrofit
object MyApiRetrofitClient {
    private const val BASE_URL = "http://10.110.236.84:8080/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

🚀 Flujos Implementados
🔐 Autenticación

Login

Registro

Recordarme

Validación de roles

Logout seguro

👤 Cliente

Home

Catálogo

Detalle propiedad

Lista de “Me interesa”

Favoritos sincronizados

🛠️ Administrador

CRUD completo:

Crear

Editar

Listar

Eliminar

Con diálogos modales y validaciones.

📂 Estructura del Proyecto
📦 com.example.proyecto2
 ┣ 📁 data
 │   ┣ datastore
 │   ┣ models
 │   ┣ network
 │   ┗ repository
 ┣ 📁 navigation
 ┣ 📁 ui
 │   ┣ screens
 │   ┗ theme
 ┣ 📁 viewmodels
 ┗ 📁 tests

🧪 Pruebas Incluidas

Unit Tests

Fake Repository

Compose UI Tests

Para ejecutar todas las pruebas:
Right click → Run ‘All Tests’

🔌 Backend Spring Boot – Ejecución

Puedes ejecutarlo desde IntelliJ / VSCode:

Abrir proyecto

Ejecutar BackendMobilApplication.java

Servidor disponible en:
http://localhost:8080/productos

Ejemplo de respuesta:

[
  {
    "id": 1,
    "nombre": "Casa en la Playa",
    "descripcion": "Hermosa vista al mar",
    "precio": 120000000,
    "categoria": "Casa",
    "imagen": "URL publica"
  }
]

📦 APK Firmado – Evidencias

APK incluido en el repositorio
Ubicación:
app/src/main/java/com/example/proyecto2/apk

Captura del APK firmado:
app/src/main/java/com/example/proyecto2/evidencias

Captura del archivo .jks usado en la firma:
app/src/main/java/com/example/proyecto2/evidencias

📋 Evidencias GitHub

✔ Actividad GitHub
com/example/proyecto2/evidencias/captura_commits.png

👨‍💻 Autor

Nicolás López
Proyecto académico — Android Jetpack Compose
