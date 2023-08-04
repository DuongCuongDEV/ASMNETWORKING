package com.example.cuongdvph20635asm.data.repository;

import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.data.network.ApiService;

import io.reactivex.rxjava3.core.Observable;

public class ApiNasaRepository {
    private ApiService apiService;

    public ApiNasaRepository(ApiService apiService) {
        this.apiService=apiService;
    }

    public Observable<Data> callAPIGetImgFromNasa(String api_key, String date){
        return apiService.callAPIGetImgFromNasa(api_key,date);
    }
}
