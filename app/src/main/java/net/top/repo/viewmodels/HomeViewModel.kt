package net.top.repo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.top.repo.api.response.home_page.GitResponse
import net.top.repo.repository.DefaultHomeRepository
import net.top.repo.utilities.Resource
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gson: Gson,
    private val homeRepository: DefaultHomeRepository
) : ViewModel() {
    private val _getRepoListResponse =
        MutableLiveData<Resource<GitResponse>>()

    fun clearData() {
        _getRepoListResponse.postValue(Resource.Empty())
    }

    //Get Repo List
    val getRepoListResponse: LiveData<Resource<GitResponse>>
        get() = _getRepoListResponse

    fun getRepoList(
        isNetworkAvailable: Boolean = true
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (homeRepository is DefaultHomeRepository) {
                _getRepoListResponse.postValue(Resource.Loading())
            }
            try {
                val response = homeRepository.makeGetRepoListRequest()
                val result = response.body()
                val errorResult = response.errorBody()
                if (response.isSuccessful && response.code() == 200 && result != null) {
                    _getRepoListResponse.postValue(Resource.Success(result))
                } else if (response.code() == 400 && errorResult != null) {
                } else {
                    _getRepoListResponse.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                _getRepoListResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
}