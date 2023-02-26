package com.greenbill.greenbill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class GreenbillApplication {

    public static void main(String[] args) {
        // milliseconds
        long milliSec = 1677429258;

        // Creating date format
        DateFormat simple = new SimpleDateFormat(
                "dd MMM yyyy HH:mm:ss:SSS Z");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(milliSec);

        // Formatting Date according to the
        // given format
        System.out.println(simple.format(result));


        SpringApplication.run(GreenbillApplication.class, args);
    }

}
