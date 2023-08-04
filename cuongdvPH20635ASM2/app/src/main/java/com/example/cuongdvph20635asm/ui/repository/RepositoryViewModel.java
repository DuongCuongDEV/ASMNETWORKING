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
    //khao báo đối tượng Disposable
    Disposable mDisposable;
    //list ảnh hiện tại
    List<Data> currentList;
    //list ảnh tạm để lưu trữ tạm thời dữ liệu từ list livedata
    List<Data> temps = new ArrayList<>();
    //livedata delete và theo dõi trạng thái đã xóa dữ liệu từ ảnh thành công chưa
    MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    //livedata update và theo dõi trạng thái đã update dữ liệu từ ảnh thành công chưa
    MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    //đối tạo livedata của list ảnh sẽ được lấy từ server của tôi
    MutableLiveData<List<Data>> mutableLiveData = new MutableLiveData<>();
    //đối tượng trung gian giữa viewmodel và myApi
    private MyApiRepository myApiRepository;

    //hàm tạo của viewmodel
    public RepositoryViewModel(MyApiRepository myApiRepository) {
        this.myApiRepository = myApiRepository;
    }

    //--GET-----------------------------------------------------------------------------------------
    public void getAllImage() {
        myApiRepository.callAPIGetAllImgFromMyServer().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<GetAllImgResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull GetAllImgResponse getAllImgResponse) {
                // khi lấy được dữ liệu từ server phản hồi thì thực hiện thông báo cho view
                // đang theo dõi nó (cụ thể là update lên danh sách của recycler view)
                mutableLiveData.postValue(getAllImgResponse.getData());
                //và giữ liệu trả về được lưu trong list ảnh hiện tại
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

    //--DELETE--------------------------------------------------------------------------------------
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
                // nếu list ảnh hiện tại có dữ liệu thì xóa giữ liệu của data hiện tại
                // xóa trong current list để tránh việc call lại list mới từ myserver
                if (currentList != null) {
                    temps.addAll(currentList);
                    temps.remove(data);
                    //thông báo cho view cập nhật lại list mới
                    mutableLiveData.postValue(temps);
                    //thông báo đã xóa thành công để thoát màn hình dialog
                    deleteSuccess.postValue(true);
                }
            }
        });
    }

    //--PUT-----------------------------------------------------------------------------------------
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
                            //nếu sửa thành công thì thông báo cho view cập nhật lại list
                            mutableLiveData.postValue(temps);
                            //thông báo update thành công để ẩn dialog
                            updateSuccess.postValue(true);
                        }
                    }
                });
    }

    //custom factory cho viewmodel
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
