package com.example.cuongdvph20635asm.data.repository;

import com.example.cuongdvph20635asm.data.model.Data;
import com.example.cuongdvph20635asm.data.model.GetAllImgResponse;
import com.example.cuongdvph20635asm.data.model.PostImgResponse;
import com.example.cuongdvph20635asm.data.network.MyApiService;

import io.reactivex.rxjava3.core.Observable;

public class MyApiRepository {
    private MyApiService myApiService;

    public MyApiRepository(MyApiService myApiService) {
        this.myApiService=myApiService;
    }

    public Observable<PostImgResponse> callAPIPostImgToMyServer(Data imageRequet){
        return myApiService.callAPIPostImgToMyServer(imageRequet);
    }
    public Observable<GetAllImgResponse> callAPIGetAllImgFromMyServer(){
        return myApiService.callAPIGetAllImgFromServer();
    }

    public Observable<PostImgResponse> callAPIEditbyID(String id,Data data){
        return myApiService.callAPIEditbyID(id, data);
    }

    public Observable<PostImgResponse> callAPIDeleteImgById(String id){
        return myApiService.callAPIDeleteByID(id);
    }
}