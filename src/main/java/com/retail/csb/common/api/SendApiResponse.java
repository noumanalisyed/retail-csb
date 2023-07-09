package com.retail.csb.common.api;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SendApiResponse {

    /**
     * Sends back a response to a request with data. Checks if request succeed or
     * not, sets the status code according to that
     *
     * @param dataObject Response object or the data to be sent in response of a
     *                   request
     * @return CompletableFuture of ResponseEntity
     */
    public static CompletableFuture<ResponseEntity<?>> dispatch(final APIDataObject dataObject) {
        final var response = dataObject.getError()
                ? RestAPIResponse.restFailure(HttpStatus.CONFLICT, dataObject.getData())
                : RestAPIResponse.restSuccess(dataObject.getData());
        return CompletableFuture.completedFuture(response);
    }
}
