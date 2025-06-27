package org.example.utils;


import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private Object data;

    private Request(){

    }

    public Request(RequestType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public RequestType type() {
        return this.type;
    }

    public Object data() {
        return data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }


    public static class Builder{
        private Request request = new Request();

        public Builder type(RequestType type){
            request.type = type;
            return this;
        }

        public Builder data(Object data){
            request.data = data;
            return this;
        }

        public Request build(){
            return request;
        }
    }

    private void data(Object data) {
        this.data = data;
    }

    private void type(RequestType type) {
        this.type = type;
    }
}
