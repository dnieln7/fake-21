package com.dnieln7.fake17.ui.home.cat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dnieln7.fake17.data.repository.CatRepository
import com.dnieln7.fake17.domain.Cat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class CatsViewModel(private val catRepository: CatRepository) : ViewModel() {
    private val _apiState = MutableLiveData<ApiState>()
    private val _cats = MutableLiveData<List<Cat>>()

    val cats: LiveData<List<Cat>> = _cats
    val apiState: LiveData<ApiState> = _apiState

    init {
        fetchCats()
    }

    fun fetchCats() {
        _apiState.value = ApiState.Loading

        catRepository.getDbCats()
            .flatMap {
                if (it.isEmpty()) {
                    return@flatMap catRepository.getApiCats(50).toObservable()
                } else {
                    return@flatMap Observable.just(it)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _cats.value = it
                    _apiState.value = ApiState.Success
                },
                {
                    Log.e(CatsViewModel::class.simpleName, "Error", it)
                    _apiState.value = ApiState.Error(it.toString())
                }
            )
    }

    class Factory(private val catRepository: CatRepository) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                return CatsViewModel(catRepository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}