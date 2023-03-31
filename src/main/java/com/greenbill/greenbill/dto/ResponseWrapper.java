package com.greenbill.greenbill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper implements Serializable {
    private String message;
    private List<Object> data=new ArrayList<>();
    private int status;

    public ResponseWrapper(Object body, int status, String message) {
        this.setData(body);
        this.setStatus(status);
        this.setMessage(message);
    }
    public void setData(Object data) {
        try {
            for (var element :(Collection)data) {
                this.data.add(element);
            }
        }catch (Exception e){
            this.data.add(data);
        }

    }
}
