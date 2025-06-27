package org.example.utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType type;
    private Object data;

    private Response(){

    }

    public Response(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ResponseType type() {
        return this.type;
    }

    public Object data() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }


    public static class Builder{
        private Response response = new Response();

        public Builder type(ResponseType type){
            response.type = type;
            return this;
        }

        public Builder data(Object data){
            response.data = data;
            return this;
        }

        public Response build(){
            return response;
        }
    }

    private void data(Object data) {
        this.data = data;
    }

    private void type(ResponseType type) {
        this.type = type;
    }
}
