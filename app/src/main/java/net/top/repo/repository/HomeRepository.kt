package net.top.repo.repository

import net.top.repo.api.response.home_page.GitResponse
import retrofit2.Response

interface HomeRepository {
    suspend fun makeGetRepoListRequest(): Response<GitResponse>
}