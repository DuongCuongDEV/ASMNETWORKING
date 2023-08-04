package com.example.cuongdvph20635asm.ui.repository;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.databinding.FragmentDetailImageBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.disposables.Disposable;


public class DetailImageFragment extends BottomSheetDialogFragment {
    //khai báo đối tượng viewbinding
    private FragmentDetailImageBinding binding;
    //khai báo viewmodel
    RepositoryViewModel repositoryViewModel;
    //khai báo data sẽ nhận từ màn trước
    Data data;
    //khai báo đối tượng Disposable
    Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //lấy viewmodel
        repositoryViewModel = getRepositoryViewModel();
        //lấy data được truyền từ màn(fragment) trước
        assert getArguments() != null;
        data = DetailImageFragmentArgs.fromBundle(getArguments()).getDataArg();
        //set thông tin của ảnh lên giao diện của dialog
        initView(data);
        //lắng nghe sự kiện trên dialog
        listenEvent();
        //lắng nghe sự thay đổi của giữ liệu
        listenLiveData();
    }

    //hàm lấy viewmodel
    private RepositoryViewModel getRepositoryViewModel() {
        if (repositoryViewModel == null) {
            repositoryViewModel = new ViewModelProvider(requireActivity(), new RepositoryViewModel.RepositoryViewModelFactory()).get(RepositoryViewModel.class);
        }
        return repositoryViewModel;
    }

    private void initView(Data data) {
        mDisposable = repositoryViewModel.mDisposable;
        Picasso.get().load(data.getHdurl()).placeholder(R.drawable.loading).error(R.drawable.baseline_hide_image_24).fit().centerCrop().into(binding.imageView);
        binding.tvTitle.setText(data.getTitle());
        if (data.getCopyright() == null || data.getCopyright().isEmpty()) {
            binding.tvCopyright.setText("Noname");
        } else {

            binding.tvCopyright.setText(data.getCopyright().replace("\n", ""));
        }
    }

    private void listenEvent() {
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repositoryViewModel.DeleteImg(data);
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data temp = new Data(data.get_id(), binding.tvCopyright.getText().toString(), data.getDate(), data.getExplanation(), data.getHdurl(), data.getMedia_type(), data.getService_version(), binding.tvTitle.getText().toString(), data.getUrl());
                repositoryViewModel.EditImg(data.get_id(), temp);
            }
        });
    }

    private void listenLiveData() {
        repositoryViewModel.deleteSuccess.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    dismiss();
                    repositoryViewModel.deleteSuccess.postValue(false);
                }
            }
        });
        repositoryViewModel.updateSuccess.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    repositoryViewModel.getAllImage();
                    repositoryViewModel.updateSuccess.postValue(false);
                    dismiss();
                }
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