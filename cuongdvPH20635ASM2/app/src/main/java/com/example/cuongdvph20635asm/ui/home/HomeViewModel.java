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
    //khơi tao Disposable
    Disposable mDisposable;
    //khai báo biến kiểm tra xem đã gọi api lấy list ảnh chưa
     boolean isImagesFetched = true;
    //key api
    private static final String API_KEY  = "92sahtcXFlcebDYwcbterGuS8EGGWMBQs2K4ikf8";
    //khai bao list ngay lay anh
    List<String> dateArray;
    //khai bao list lưu ảnh
    List<Data> imageResponseArray=new ArrayList<>();
    //khai báo list chuỗi ảnh sau chuyển đổi
    List<String> base64Arr=new ArrayList<>();
    //đối tượng livedata của ảnh sau khi được chuyển đổi
    MutableLiveData<String> base64LiveData = new MutableLiveData<>();
    // đối tượng livedata của ảnh trả về từ nasa
    MutableLiveData<Data> mImageResponse=new MutableLiveData<>();
    // đối tượng trung gian giữa viewmodel và apinasa
    private ApiNasaRepository nasaRepository;
    // đối tượng trung gian giữa viewmodel và myapi
    private MyApiRepository myApiRepository;
    // biến mRequest sử dụng khi post data ảnh lên my server
    Data mRequest;

    //hàm tạo của viewmodel
    public HomeViewModel(ApiNasaRepository nasaRepository, MyApiRepository myApiRepository) {
        this.nasaRepository=nasaRepository;
        this.myApiRepository=myApiRepository;
    }

    //--GET----------------------------------------------------------------------------------------
    public void getImgFromNasa() {
        if (isImagesFetched) {
            // Các bước lấy hình ảnh từ NASA tại đây
            // ...
            isImagesFetched = false; // Đánh dấu đã lấy hình ảnh từ NASA
            LocalDate startDate = null;
            LocalDate endDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startDate = LocalDate.of(2023, 6, 21);
                endDate = LocalDate.of(2023, 7, 21);
            }
            //tao mang ngay lay anh tu 21/6 -21/7
            dateArray = createArray(startDate, endDate);
            //thuc hien vong for call api lay anh tung ngay tu 21/6 -21/7
            for (String date : dateArray) {
                callApiImageFromNasa(API_KEY, date);
            }
        }
    }

    //hàm tạo mảng ngày lấy ảnh
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

    //ham callAPI get ảnh từ nasa
    public void callApiImageFromNasa(String key,String date) {
    nasaRepository.callAPIGetImgFromNasa(key,date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //dang ky luong nhan du lieu tai mainThread
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe( Disposable d) {
                        //nhan ve doi tuong Disposable
                        mDisposable = d;
                    }

                    @Override
                    public void onNext( Data imageResponse) {//noi nhan phan hoi
                        //luu link anh tra ve vao mList
                            mImageResponse.postValue(imageResponse);
                            imageResponseArray.add(imageResponse);
                            convertImageToBase64(imageResponse);

                    }

                    @Override
                    public void onError( Throwable e) {//noi nhan loi neu co
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {//khi hoan thanh
                    }
                });
    }

    //hàm chuyển ảnh sang base64
    private void convertImageToBase64(Data image) {
        Context context = MyApplication.getInstance().getApplicationContext();

        Glide.with(context)
                .asBitmap()
                .load(image.getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Chuyển đổi Bitmap thành dạng Base64
                        String base64Image = convertBitmapToBase64(resource);
                        // Lưu vào mảng base64ImagesList
                        base64Arr.add(base64Image);
                        // Cập nhật LiveData nếu cần
                        base64LiveData.setValue(base64Image);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Xử lý khi ảnh bị xóa khỏi bộ nhớ cache
                    }
                });
    }
    //hàm chuyển từ bipmap sang chuổi báe64
    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    //--POST----------------------------------------------------------------------------------------

    //duyệt mảng ảnh base64 được truyền vào và thực hiện post lần lượt lên myserver
    public void postListBase64(List<String> urls) {
        for (int i = 0; i < imageResponseArray.size(); i++) {
            postOne(i,urls.get(i));
        }
    }
    //hàm post dữ liệu 1 ảnh lên myserver
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


    //viết custom lại factory cho viewmodel
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

