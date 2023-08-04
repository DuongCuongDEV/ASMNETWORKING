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
    RepositoryFragmentDirections.ActionRepositoryFragmentToDetailImageFragment action;
    private ImageFromMyServerAdapter adapter;
    private RepositoryViewModel repositoryViewModel;
    private FragmentRepositoryBinding binding;
    List<Data> imageResponseArray;
    Disposable mDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRepositoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repositoryViewModel = getRepositoryViewModel();
        //khởi tạo một số đối tượng cần thiết
        initView();
        repositoryViewModel.getAllImage();
        ListenLiveData();
    }

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

                action = RepositoryFragmentDirections.actionRepositoryFragmentToDetailImageFragment(data);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        binding.recyclerViewVer.setAdapter(adapter);
    }

    private void ListenLiveData() {
        repositoryViewModel.mutableLiveData.observe(requireActivity(), new Observer<List<Data>>() {
            @Override
            public void onChanged(List<Data> data) {
                mDisposable = repositoryViewModel.mDisposable;
                adapter.setList(data);
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