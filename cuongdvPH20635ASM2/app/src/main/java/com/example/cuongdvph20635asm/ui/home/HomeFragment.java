package com.example.cuongdvph20635asm.ui.home;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.databinding.FragmentHomeBinding;
import com.example.cuongdvph20635asm.ui.home.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;



public class HomeFragment extends Fragment {
    //khai bao viewModel
    private HomeViewModel homeViewModel;
    //khai bao viewBinding
    private FragmentHomeBinding binding;
    //khai bao Disposable
    Disposable mDisposable;
    //khai bao adapter
    ImageAdapter adapter;
    //list ảnh chứa ảnh call về
    List<Data> imageResponseArray;
    //list chứa chuỗi basa64 sau khi chuyển đổi
    List<String> base64Array;
    //khao bao đối tượng downloadManager
    DownloadManager downloadManager;

    //hàm tạo view của fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //lấy viewmodel
        homeViewModel=getNoteViewModel();
        //khởi tạo một số thành phần
        initView();
        //lấy ảnh
        getListImgFromNasa();
        //sự kiện khi thao tác với màn hình
        initEvent();
        //lắng nghe sự thay đổi của livedata
        listenLiveData();
    }

    private HomeViewModel getNoteViewModel() {
        if (homeViewModel == null) {
            homeViewModel = new ViewModelProvider(this, new HomeViewModel.HomeViewModelFactory()).get(HomeViewModel.class);
        }
        return homeViewModel;
    }

    private void initView() {
        mDisposable=homeViewModel.mDisposable;
        base64Array=new ArrayList<>();
        imageResponseArray=new ArrayList<>();
        adapter = new ImageAdapter(requireActivity());
        adapter.setList(imageResponseArray);
        binding.rcyListImage.setAdapter(adapter);

    }

    private void getListImgFromNasa() {
        // Kiểm tra xem đã lấy hình ảnh từ NASA hay chưa
        if (homeViewModel.isImagesFetched) {
            //nếu chưa gọi api lấy ảnh thì sẽ gọi hàm getImgFromNasa từ viewmodel
            homeViewModel.getImgFromNasa();
        }else {
            //nếu đã gọi api thì imageResponseArray sẽ gán bằng mảng được lưu trong viewmodel
            // tránh việc gọi lại api
            imageResponseArray=homeViewModel.imageResponseArray;
            //và mảng base64Array cũng sẽ đc lấy từ viewmodel tránh việc chuyển đổi lại chuỗi
            base64Array=homeViewModel.base64Arr;
        }
    }

    private void initEvent() {
        //khi người dùng thao tác với nút dowload trên màn hình
        binding.btnGetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu mList chứa ít nhất một ảnh, thực hiện tải xuống ảnh từ mList
                if (!imageResponseArray.isEmpty()) {
                    //thực hiện tải list ảnh
                    downloadImagesFromList(requireContext(),imageResponseArray);
                }
            }
        });

        //khi người dùng thao tác với nút post list thì list sẽ được gửi tới myserver
        binding.btnPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hàm post list được sử lý trong viewmodel
                homeViewModel.postListBase64(base64Array);
            }
        });
    }

    private void listenLiveData() {
        //khi có thay đổi giữ liệu của ảnh khi call và đc post từ viewmodel
        homeViewModel.mImageResponse.observe(requireActivity(), new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                //ẩn progressbar
                binding.progressbar.setVisibility(View.GONE);
                //lưu ảnh mới nhận được từ viewmodel vào imageResponseArray
                imageResponseArray.add(data);
                //cập nhật list vào adapter
                adapter.setList(imageResponseArray);

            }
        });
        //khi có thay đổi giữ liệu của chuỗi ảnh khi đã được chuyển đổi và đc post từ viewmodel
        homeViewModel.base64LiveData.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //lưu vào trong một mảng chuỗi
                base64Array.add(s);
            }
        });
    }

    //hàm hỗ trợ dowload list ảnh vừa lấy về từ nasa
    public void downloadImagesFromList(Context context, List<Data> imageResponseArray) {
        // Lấy DownloadManager
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        // Tải xuống các ảnh từ mList
        for (int i = 0; i < imageResponseArray.size(); i++) {
            String imageUrl = imageResponseArray.get(i).getUrl();
            String fileName = "image_" + i + ".jpg";
            downloadImageFromUrl(imageUrl, fileName);
        }
    }

    private void downloadImageFromUrl(String imageUrl, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        request.setTitle(fileName);
        request.setDescription("Downloading " + fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        downloadManager.enqueue(request);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //huy dang ky khi activity destroy
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

}