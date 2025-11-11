# Proyecto de Arriendos y Ventas (Proyecto 2)

Proyecto académico de una aplicación Android nativa desarrollada en Kotlin con Jetpack Compose. La aplicación simula una plataforma para la visualización y gestión de propiedades en arriendo, con perfiles diferenciados para Clientes y Administradores.

## 🌟 Características Principales

- **Interfaz Moderna y Reactiva:** Construida 100% con Jetpack Compose, siguiendo los principios de diseño de Material 3.
- **Arquitectura MVVM:** Se implementa el patrón Model-View-ViewModel para separar la lógica de negocio de la interfaz de usuario, resultando en un código más limpio, escalable y fácil de testear.
- **Doble Perfil de Usuario:**
    - **Cliente:** Puede navegar por el catálogo de propiedades, ver detalles y añadir propiedades a una lista de "intereses".
    - **Administrador:** Tiene acceso a un panel de gestión para añadir, editar y eliminar propiedades de la plataforma.
- **Persistencia de Sesión:** Utiliza **Jetpack DataStore** para recordar las credenciales del usuario, permitiendo una experiencia de "Recordarme" al iniciar sesión.
- **Navegación Robusta:** Gestionada con **Jetpack Navigation Compose**, permitiendo flujos de navegación complejos, incluyendo grafos anidados para las secciones de cliente.
- **Gestión de Estado Centralizada:** Se usan `ViewModels` para gestionar el estado de la UI, compartiendo datos entre pantallas de manera eficiente (por ejemplo, la lista de favoritos del cliente).

## 🛠️ Tecnologías y Componentes Clave

- **Lenguaje:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Componentes de Jetpack:**
    - `ViewModel`: Para la lógica de negocio y gestión de estado.
    - `Navigation Compose`: Para la navegación entre pantallas.
    - `DataStore Preferences`: Para la persistencia de datos clave-valor (sesión del usuario).
    - `Material 3`: Para los componentes de la interfaz de usuario.
- **Manejo de Asincronía:** Kotlin Coroutines y Flow para operaciones en segundo plano y flujos de datos reactivos.

## 🚀 Flujos Implementados

### Flujo de Autenticación
1.  **Login:** El usuario introduce sus credenciales. La aplicación valida el rol (Cliente o Admin) y navega a la pantalla correspondiente.
2.  **Recordar Sesión:** Si el usuario activa la opción "Recordarme", sus credenciales se guardan localmente usando DataStore para autocompletar el formulario en futuras sesiones.
3.  **Registro:** Permite a nuevos usuarios crear una cuenta (funcionalidad básica).

### Flujo del Cliente
1.  **Home:** Un menú principal que dirige al catálogo o a "Mis Intereses".
2.  **Catálogo:** Muestra la lista de todas las propiedades disponibles.
3.  **Detalle de Propiedad:** Al seleccionar una propiedad, se muestra una pantalla con su información detallada.
4.  **Mis Intereses (Favoritos):** El cliente puede marcar una propiedad como "Me interesa", añadiéndola a una lista personal. Desde esta lista, también puede eliminar propiedades. El estado se comparte entre la pantalla de detalle y la de "Mis Intereses" gracias a un `ClientViewModel` compartido.

### Flujo del Administrador
1.  **Panel de Control:** Un menú que dirige a la gestión de propiedades.
2.  **Gestión de Propiedades (CRUD):**
    - **Crear (Create):** Un botón flotante abre un diálogo para añadir una nueva propiedad.
    - **Leer (Read):** Se muestra la lista completa de propiedades.
    - **Actualizar (Update):** Un botón de "Editar" en cada propiedad abre el diálogo con los datos precargados para modificarlos.
    - **Eliminar (Delete):** Un botón de "Eliminar" permite quitar propiedades de la lista, con un diálogo de confirmación para evitar errores.
    - Toda la lógica es manejada por un `AdminViewModel`.

## 📂 Estructura del Proyecto

El proyecto está organizado en paquetes para una mejor separación de responsabilidades:

- **`data`**: Contiene las fuentes de datos (DataStore, modelos de datos).
- **`navigation`**: Define las rutas y el grafo de navegación de la aplicación.
- **`ui/screens`**: Contiene cada una de las pantallas (Composables) de la aplicación.
- **`ui/viewmodels`**: Contiene los ViewModels que dan vida a las pantallas.
- **`ui/theme`**: Tema de la aplicación generado por defecto.

## Próximos Pasos

- **Integración con Base de Datos:** Migrar la lógica de datos en memoria (`FakeProductDataSource`) a una base de datos local utilizando **SQLite con Room** para una persistencia de datos real y compartida entre perfiles.

