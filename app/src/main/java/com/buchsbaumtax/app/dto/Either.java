package com.buchsbaumtax.app.dto;

import java.util.NoSuchElementException;

// Custom Either class for flexible return types
public class Either<L, R> {
    private final L left;
    private final R right;
    private final boolean isRight;

    private Either(L left, R right, boolean isRight) {
        this.left = left;
        this.right = right;
        this.isRight = isRight;
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(value, null, false);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(null, value, true);
    }

    public L getLeft() {
        if (isRight) throw new NoSuchElementException("Cannot get left value from a right Either");
        return left;
    }

    public R getRight() {
        if (!isRight) throw new NoSuchElementException("Cannot get right value from a left Either");
        return right;
    }

    public boolean isRight() {
        return isRight;
    }
}