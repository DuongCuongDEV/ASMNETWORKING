package com.example.cuongdvph20635asm.ui.repository;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.data.model.GetAllImgResponse;
import com.example.cuongdvph20635asm.data.model.PostImgResponse;
import com.example.cuongdvph20635asm.data.network.MyApiService;
import com.example.cuongdvph20635asm.data.repository.MyApiRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepositoryViewModel extends ViewModel {
    Disposable mDisposable;
    List<Data> currentList;
    List<Data> temps = new ArrayList<>();
    MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    MutableLiveData<List<Data>> mutableLiveData = new MutableLiveData<>();
    private MyApiRepository myApiRepository;

    public RepositoryViewModel(MyApiRepository myApiRepository) {
        this.myApiRepository = myApiRepository;
    }

    public void getAllImage() {
        myApiRepository.callAPIGetAllImgFromMyServer().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<GetAllImgResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull GetAllImgResponse getAllImgResponse) {
                mutableLiveData.postValue(getAllImgResponse.getData());
                currentList = getAllImgResponse.getData();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void DeleteImg(Data data) {
        myApiRepository.callAPIDeleteImgById(data.get_id()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<PostImgResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull PostImgResponse postImgResponse) {
                Log.d("delete", "onNext: " + postImgResponse.getMessage());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                if (currentList != null) {
                    temps.addAll(currentList);
                    temps.remove(data);
                    mutableLiveData.postValue(temps);
                    deleteSuccess.postValue(true);
                }
            }
        });
    }

    public void EditImg(String id, Data data) {
        myApiRepository.callAPIEditbyID(id, data)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostImgResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull PostImgResponse postImgResponse) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        if (currentList != null) {
                            mutableLiveData.postValue(temps);
                            updateSuccess.postValue(true);
                        }
                    }
                });
    }

    public static class RepositoryViewModelFactory implements ViewModelProvider.Factory {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RepositoryViewModel.class)) {
                MyApiRepository repo = new MyApiRepository(MyApiService.Factory.create());
                return (T) new RepositoryViewModel(repo);
            }
            throw new IllegalArgumentException("Unable to construct ViewModel");
        }
    }
}
