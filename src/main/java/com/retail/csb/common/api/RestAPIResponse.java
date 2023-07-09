package com.retail.csb.common.api;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.SneakyThrows;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestAPIResponse<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();;

    @JsonProperty("status")
    private final HttpStatus status;
    @JsonProperty("data")
    private final T data;
    @JsonProperty
    private final String error;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy.hh:mm:ss")
    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public static ResponseEntity<?> restSuccess(final Object data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestAPIResponse.builder().status(HttpStatus.OK).data(data).build());
    }

    public static ResponseEntity<?> restFailure(final HttpStatus status, final String error) {
        return ResponseEntity.status(status).body(RestAPIResponse.builder().status(status).error(error).build());
    }

    public static ResponseEntity<?> restFailure(final HttpStatus status, final Object error) {
        return ResponseEntity.status(status)
                .body(RestAPIResponse.builder().status(status).error(error.toString()).build());
    }

    @SneakyThrows
    public static String restFailureToString(final HttpStatus status, final String error) {
        return objectMapper.writeValueAsString(RestAPIResponse.builder().status(status).error(error).build());
    }
}
