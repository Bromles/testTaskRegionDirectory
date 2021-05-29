package com.bromles.testTaskForTelda.exception;

import java.util.Map;

public class DuplicateUniqueValuesException extends Exception{

    private final Map<String, Object> violatedFields;

    public DuplicateUniqueValuesException( Map<String, Object> violatedFields) {
        this.violatedFields = violatedFields;
    }

    public Map<String, Object> getViolatedFields() {
        return violatedFields;
    }

    @Override
    public String getMessage() {
        return "Duplicate primary key or unique index";
    }
}
