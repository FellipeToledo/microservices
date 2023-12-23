package com.azvtech.securityservice.result;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Fellipe Toledo
 */
@Getter
@Setter
public class Result {

    private boolean flag; // Two values: true means success, false means not success
    private Integer code; // Status code. e.g., 200
    private LocalDateTime date; // Current date time
    private String message; // Response message
    private Object data; // The response payload

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, Integer code, LocalDateTime date, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.date = date;
        this.message = message;
        this.data = data;
    }
}
