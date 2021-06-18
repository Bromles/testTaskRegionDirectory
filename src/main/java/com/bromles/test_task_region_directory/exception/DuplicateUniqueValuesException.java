package com.bromles.test_task_region_directory.exception;

import java.util.Map;

/**
 * Исключение, генерируемое при попытке добавить в репозиторий сущность, нарушающую уникальность некоторых полей
 */
public class DuplicateUniqueValuesException extends Exception {

    private final Map<String, Object> violatedFields;

    /**
     * Конструктор, генерирующий исключение на основе списка полей, в которых обнаружены дубликаты
     *
     * @param violatedFields Тип: {@link Map Map<String, Object>}. Список пар, состоящих из названия поля-дубликата и
     *                       самого дублирующего значения
     */
    public DuplicateUniqueValuesException(Map<String, Object> violatedFields) {
        this.violatedFields = violatedFields;
    }

    /**
     * Получает список полей-дубликатов
     *
     * @return Возвращает список пар, состоящих из названия поля-дубликата и самого
     * дублирующего значения
     */
    public Map<String, Object> getViolatedFields() {
        return violatedFields;
    }

    @Override
    public String getMessage() {
        return "Duplicate primary key or unique index";
    }
}
