package com.example.cuongdvph20635asm.data.model;

public class PostImgResponse {
    private String status;

    private Data data;
    private String message;

    public PostImgResponse(String status,Data data,String message) {
        this.status = status;
        this.data=data;
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
