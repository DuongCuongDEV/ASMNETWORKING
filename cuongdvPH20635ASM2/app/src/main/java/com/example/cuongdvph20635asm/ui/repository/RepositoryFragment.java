package com.example.cuongdvph20635asm.ui.repository;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.databinding.FragmentRepositoryBinding;
import com.example.cuongdvph20635asm.ui.repository.adapter.ImageFromMyServerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class RepositoryFragment extends Fragment {
    //khai báo action khi điều hướng giữa màn hình list và dialog xem chi tiết
    RepositoryFragmentDirections.ActionRepositoryFragmentToDetailImageFragment action;
    //khai báo adapter
    private ImageFromMyServerAdapter adapter;
    //khai báo viewmodel
    private RepositoryViewModel repositoryViewModel;
    //khai báo viewbinding
    private FragmentRepositoryBinding binding;
    //khai báo list để hiện lên adapter
    List<Data> imageResponseArray;
    //khai báo đối tượng hủy đăng ký Disposable
    Disposable mDisposable;

    //hàm tạo view của fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRepositoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //lấy viewmodel
        repositoryViewModel = getRepositoryViewModel();
        //khởi tạo một số đối tượng cần thiết
        initView();
        //lấy giữ liệu ảnh bằng việc gọi api tới myserver
        repositoryViewModel.getAllImage();
        //lắng nghe sự thay đổi của dữ liệu
        ListenLiveData();
    }

    //hàm lấy viewmodel
    private RepositoryViewModel getRepositoryViewModel() {
        if (repositoryViewModel == null) {
            repositoryViewModel = new ViewModelProvider(requireActivity(), new RepositoryViewModel.RepositoryViewModelFactory()).get(RepositoryViewModel.class);
        }
        return repositoryViewModel;
    }

    private void initView() {
        imageResponseArray = new ArrayList<>();
        adapter = new ImageFromMyServerAdapter(requireActivity(), new ImageFromMyServerAdapter.OnClickItem() {
            @Override
            public void onclickItem(Data data) {
                //lắng nghe data truyền tới khi người dùng click vào item trong list
                //truyền data cho action
                action = RepositoryFragmentDirections.actionRepositoryFragmentToDetailImageFragment(data);
                //thực hiện điều hướng sang dialog của màn chi tiết
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        binding.recyclerViewVer.setAdapter(adapter);
    }

    private void ListenLiveData() {
        //khi có sự thay đổi dữ liệu từ list ảnh call về
        repositoryViewModel.mutableLiveData.observe(requireActivity(), new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> data) {
                mDisposable = repositoryViewModel.mDisposable;
                //thực hiện update dữ liệu mới lên adapter
                adapter.setList(data);
            }
        });
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