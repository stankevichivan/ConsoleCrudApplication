package com.sivan.crudapp.exception;

public class JDBCRepositoryException extends RuntimeException {
    public JDBCRepositoryException(Throwable throwable) {
        super(throwable);
    }
}
