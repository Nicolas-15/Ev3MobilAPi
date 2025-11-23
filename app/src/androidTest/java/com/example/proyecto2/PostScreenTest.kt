package com.example.proyecto2

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.proyecto2.data.model.Post
import com.example.proyecto2.repository.PostRepository
import com.example.proyecto2.ui.screens.api.TestApiScreen
import com.example.proyecto2.viewmodels.PostViewModel
import org.junit.Rule
import org.junit.Test

class PostScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun el_titulo_de_post_debe_aparecer_en_pantalla() {
        // 1. Preparamos los datos falsos que nuestro repositorio devolverá
        val fakePosts = listOf(
            Post(1, 1, "Título 1", "Cuerpo 1"),
            Post(2, 2, "Título 2", "Cuerpo 2")
        )

        // 2. Creamos un REPOSITORIO falso
        //    Sobrescribimos la función getPosts para que devuelva nuestra lista
        //    falsa de manera instantánea y sin llamar a Retrofit.
        val fakeRepository = object : PostRepository() {
            override suspend fun getPosts(): List<Post> {
                return fakePosts
            }
        }

        // 3. Creamos una instancia REAL del ViewModel, pero le inyectamos
        //    nuestro repositorio falso en el constructor.
        //    El bloque 'init' del ViewModel llamará a fetchPosts(), que a su vez
        //    usará nuestro fakeRepository.getPosts() y obtendrá los datos falsos.
        val viewModel = PostViewModel(repository = fakeRepository)

        // 4. Renderizamos el Composable con el ViewModel real (que ahora contiene datos falsos)
        composeRule.setContent {
            TestApiScreen(viewModel = viewModel)
        }

        // 5. Validamos que la UI muestra los datos esperados.
        //    El test esperará automáticamente a que la UI se actualice.
        composeRule.onNodeWithText("Título 1").assertIsDisplayed()
        composeRule.onNodeWithText("Título 2").assertIsDisplayed()
    }
}
