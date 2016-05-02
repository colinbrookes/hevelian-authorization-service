package com.hevelian.identity.core.api;

import java.io.Serializable;

public class PrimitiveResult<T> implements Serializable {
    private static final long serialVersionUID = 1817325084758139175L;
    private final T result;

    public PrimitiveResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

}