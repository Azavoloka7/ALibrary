package com.zavoloka.ALibrary;

public class CustomOptional<T> {

    private final T value;

    private CustomOptional(T value) {
        this.value = value;
    }

    public static <T> CustomOptional<T> of(T value) {
        return new CustomOptional<>(value);
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }
}