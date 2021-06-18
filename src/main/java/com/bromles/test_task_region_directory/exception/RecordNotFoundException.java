package com.bromles.test_task_region_directory.exception;

/**
 * Исключение, генерируемое при отсутствии в репозитории искомой записи
 */
public class RecordNotFoundException extends Exception {
    private final String fieldName;

    /**
     * Конструктор, генерирующий исключение на основе названия поля, по которому осуществлялся поиск
     *
     * @param fieldName Тип: {@link String}. Название поля, по которому осуществлялся поиск
     */
    public RecordNotFoundException(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Конструктор исключения без параметров
     */
    public RecordNotFoundException() {
        fieldName = null;
    }

    /**
     * Получает название поля, по которому осуществлялся поиск
     *
     * @return Возвращает название поля, по которому осуществлялся поиск
     */
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
