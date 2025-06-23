package com.romankryvolapov.resolve.resolve.models.exceptions;

public class DependencyNotCompletedException extends AppException {
    public DependencyNotCompletedException() {
        super("Cannot complete task with uncompleted dependency");
    }
}
