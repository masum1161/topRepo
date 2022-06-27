package net.top.repo.repository


import net.top.repo.api.TopRepoApiService
import net.top.repo.api.response.home_page.GitResponse
import retrofit2.Response
import javax.inject.Inject

class DefaultHomeRepository @Inject constructor(
    private val topRepoApiService: TopRepoApiService
) : HomeRepository {

    override suspend fun makeGetRepoListRequest(
    ): Response<GitResponse> {
        return topRepoApiService.getRepoList()
    }


}