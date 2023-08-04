package com.example.cuongdvph20635asm.ui.home;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.cuongdvph20635asm.MyApplication;
import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.data.model.PostImgResponse;
import com.example.cuongdvph20635asm.data.network.ApiService;
import com.example.cuongdvph20635asm.data.network.MyApiService;
import com.example.cuongdvph20635asm.data.repository.ApiNasaRepository;
import com.example.cuongdvph20635asm.data.repository.MyApiRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    Disposable mDisposable;
     boolean isImagesFetched = true;
    //key api
    private static final String API_KEY  = "92sahtcXFlcebDYwcbterGuS8EGGWMBQs2K4ikf8";
    List<String> dateArray;
    List<Data> imageResponseArray=new ArrayList<>();
    List<String> base64Arr=new ArrayList<>();
    MutableLiveData<String> base64LiveData = new MutableLiveData<>();
    MutableLiveData<Data> mImageResponse=new MutableLiveData<>();
    private ApiNasaRepository nasaRepository;
    private MyApiRepository myApiRepository;
    Data mRequest;

    public HomeViewModel(ApiNasaRepository nasaRepository, MyApiRepository myApiRepository) {
        this.nasaRepository=nasaRepository;
        this.myApiRepository=myApiRepository;
    }

    public void getImgFromNasa() {
        if (isImagesFetched) {
            isImagesFetched = false;
            LocalDate startDate = null;
            LocalDate endDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = LocalDate.of(2023, 2, 1);
                endDate = LocalDate.of(2023, 3, 1);
            }
            dateArray = createArray(startDate, endDate);
            for (String date : dateArray) {
                callApiImageFromNasa(API_KEY, date);
            }
        }
    }

    public List<String> createArray(LocalDate startDate, LocalDate endDate) {
        List<String> dateArray = new ArrayList<>();
        LocalDate currentDate = startDate;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            while (!currentDate.isAfter(endDate)) {
                dateArray.add(currentDate.toString());
                currentDate = currentDate.plusDays(1);
            }
        }

        return dateArray;
    }

    public void callApiImageFromNasa(String key,String date) {
    nasaRepository.callAPIGetImgFromNasa(key,date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe( Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext( Data imageResponse) {
                            mImageResponse.postValue(imageResponse);
                            imageResponseArray.add(imageResponse);
                            convertImageToBase64(imageResponse);

                    }

                    @Override
                    public void onError( Throwable e) {//noi nhan loi neu co
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void convertImageToBase64(Data image) {
        Context context = MyApplication.getInstance().getApplicationContext();

        Glide.with(context)
                .asBitmap()
                .load(image.getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String base64Image = convertBitmapToBase64(resource);
                        base64Arr.add(base64Image);
                        base64LiveData.setValue(base64Image);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void postListBase64(List<String> urls) {
        for (int i = 0; i < imageResponseArray.size(); i++) {
            postOne(i,urls.get(i));
        }
    }
    private void postOne(int i,String urlBase64) {
        mRequest = new Data(
                imageResponseArray.get(i).getCopyright(),
                imageResponseArray.get(i).getDate(),
                imageResponseArray.get(i).getExplanation(),
                imageResponseArray.get(i).getHdurl(),
                imageResponseArray.get(i).getMedia_type(),
                imageResponseArray.get(i).getService_version(),
                imageResponseArray.get(i).getTitle(),
                urlBase64
        );
                myApiRepository.callAPIPostImgToMyServer(mRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostImgResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull PostImgResponse postImgResponse) {
                        Log.d("TAG", "onNext: " + postImgResponse.getStatus());
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


    public static class HomeViewModelFactory implements ViewModelProvider.Factory {

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HomeViewModel.class)) {
                ApiNasaRepository repo = new ApiNasaRepository(ApiService.Factory.create());
                MyApiRepository myRepo= new MyApiRepository(MyApiService.Factory.create());
                return (T) new HomeViewModel(repo,myRepo);
            }
            throw new IllegalArgumentException("Unable to construct ViewModel");
        }
    }
}

