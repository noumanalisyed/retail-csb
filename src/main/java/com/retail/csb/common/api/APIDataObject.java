package com.retail.csb.common.api;

import lombok.Builder;
import lombok.Getter;

/**
 * A class for API operations, it holds the error and the data after any
 * resource request. If the request is successfully performed then it will hold
 * the valid data and error will be false and if the operation does not succeed
 * due to any reason the data will hold an error message and the error will be
 * true. We could use the builtin tuple like data structure 'Pair' but this is
 * more readable.
 */
@Getter
@Builder
public class APIDataObject {
    private Boolean error;
    private Object data;
}
