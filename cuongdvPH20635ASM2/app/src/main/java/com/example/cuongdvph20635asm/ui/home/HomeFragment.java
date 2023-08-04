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
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Disposable mDisposable;
    ImageAdapter adapter;
    List<Data> imageResponseArray;
    List<String> base64Array;
    DownloadManager downloadManager;

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
        initView();
        getListImgFromNasa();
        initEvent();
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
        if (homeViewModel.isImagesFetched) {
            homeViewModel.getImgFromNasa();
        }else {

            imageResponseArray=homeViewModel.imageResponseArray;
            base64Array=homeViewModel.base64Arr;
        }
    }

    private void initEvent() {
        binding.btnPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.postListBase64(base64Array);
            }
        });
    }

    private void listenLiveData() {
        homeViewModel.mImageResponse.observe(requireActivity(), new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                binding.progressbar.setVisibility(View.GONE);
                imageResponseArray.add(data);
                adapter.setList(imageResponseArray);

            }
        });
        homeViewModel.base64LiveData.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                base64Array.add(s);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

}