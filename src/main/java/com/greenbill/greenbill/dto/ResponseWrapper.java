package com.greenbill.greenbill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper implements Serializable {
    private String message;
    private List<Object> data;
    private int status;

    public ResponseWrapper(Object body, int status,String message) {
        this.data=new ArrayList<>();
        data.add(body);
        this.setStatus(status);
        this.setMessage(message);
    }

    public ResponseWrapper(List<Object> body, int status,String message) {
        this.setData(body);
        this.setStatus(status);
        this.setMessage(message);
    }
}
