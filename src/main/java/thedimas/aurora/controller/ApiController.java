package thedimas.aurora.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import thedimas.aurora.response.ApiResponse;

@SuppressWarnings("unused")
public class ApiController {
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(message);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(status.value());
        response.setMessage(message);
        return new ResponseEntity<>(response, status);
    }
}
