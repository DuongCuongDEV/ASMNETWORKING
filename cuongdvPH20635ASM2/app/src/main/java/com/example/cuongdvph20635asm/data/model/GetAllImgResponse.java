package com.example.cuongdvph20635asm.data.model;

import java.util.List;

public class GetAllImgResponse {
    private String status;

    private List<Data> data;
    private String message;

    public GetAllImgResponse(String status, List<Data> data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
