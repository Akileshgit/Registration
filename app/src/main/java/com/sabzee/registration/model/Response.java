package com.sabzee.registration.model;

public class Response {

    boolean responce;
    String message;

    public Response(boolean responce, String message) {
        this.responce = responce;
        this.message = message;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
