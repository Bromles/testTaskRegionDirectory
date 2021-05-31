package com.bromles.testTaskForTelda.exception;

public class RecordNotFoundException extends Exception {
    private final String fieldName;

    public RecordNotFoundException(String fieldName) {
        this.fieldName = fieldName;
    }

    public RecordNotFoundException() {
        fieldName = null;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        if (fieldName != null && fieldName.trim().length() != 0) {
            return "No records found by " + fieldName;
        }
        else {
            return "There are no records";
        }
    }
}
