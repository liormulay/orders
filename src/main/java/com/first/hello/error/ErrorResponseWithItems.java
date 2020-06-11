package com.first.hello.error;

import java.util.List;

/**
 * Use this when you want to response on problem with some items
 */
public class ErrorResponseWithItems extends ErrorResponse {

    private final List<?> items;

    public ErrorResponseWithItems(int status, String message, long timeStamp, List<?> items) {
        super(status, message, timeStamp);
        this.items = items;
    }

    public List<?> getItems() {
        return items;
    }
}
