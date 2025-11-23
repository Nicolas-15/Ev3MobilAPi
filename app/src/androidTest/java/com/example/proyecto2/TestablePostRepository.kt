package com.example.proyecto2

import com.example.proyecto2.data.model.Post
import com.example.proyecto2.data.network.ApiService
import com.example.proyecto2.repository.PostRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

//Crear la subclase de PostRepository para poder inyectar el APIservice manual
class TestablePostRepository(private val testApiService: ApiService) : PostRepository() {
    override suspend fun getPosts(): List<Post> {
        return testApiService.getPosts()
    }
}

class PostRepositoryTest : StringSpec({
    "getPosts() debe retornar una lista de post simulada"{
        //Simulamos el resultado de la API
        val fakePosts = listOf(
            Post(1, 1, "Título 1", "Cuerpo 1"),
            Post(2, 2, "Título 2", "Cuerpo 2")
        )
        //Creamos un mock de ApiService
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } returns fakePosts
        //Usamos la clase de test inyectando el mock
        val repo = TestablePostRepository(mockApi)
        //Ejecutar el test
        runTest {
            repo.getPosts() shouldContainExactly fakePosts
        }
    }
})
