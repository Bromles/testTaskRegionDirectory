package com.bromles.test_task_region_directory.service;

import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.exception.RecordNotFoundException;

import java.util.List;

/**
 * Интерфейс сервиса справочника регионов
 */
public interface IRegionDirectoryService {

    /**
     * Добавляет новый регион в репозиторий
     *
     * @param regionDTO Тип: {@link RegionDTO}. Объект трансфера данных, на основе которого генерируется сущность для
     *                  добавления в репозиторий
     * @throws DuplicateUniqueValuesException Исключение, генерируемое при попытке добавить сущность с идентификатором,
     *                                        уже существующим в репозитории
     */
    void add(RegionDTO regionDTO) throws DuplicateUniqueValuesException;

    /**
     * Получает список регионов
     *
     * @return Возвращает список объектов трансфера данных, сгенерированный на основе списка всех регионов
     * @throws RecordNotFoundException Исключение, генерируемое при отсутствии регионов в репозитории
     */
    List<RegionDTO> getAll() throws RecordNotFoundException;

    /**
     * Получает регион по идентификатору
     *
     * @param id Тип: {@link String}. Идентификатор региона, по которому осуществляется поиск в репозитории
     * @return Возвращает объект трансфера данных, сгенерированный на основе найденного в репозитории региона
     * @throws RecordNotFoundException Исключение, генерируемое при отсутствии в репозитории региона с данным
     *                                 идентификатором
     */
    RegionDTO getById(String id) throws RecordNotFoundException;

    /**
     * Получает список регионов по наименованию
     *
     * @param name Тип: {@link String}. Наименование региона, по которому осуществляется поиск в репозитории
     * @return Возвращает список объектов трансфера данных, сгенерированный на основе списка регионов с данным
     * наименованием
     * @throws RecordNotFoundException Исключение, генерируемое при отсутствии в репозитории регионов с данным
     *                                 наименованием
     */
    List<RegionDTO> getByName(String name) throws RecordNotFoundException;

    /**
     * Получает список регионов по началу наименования
     *
     * @param nameBeginning Тип: {@link String}. Начало наименования региона, по которому осуществляется поиск в
     *                      репозитории
     * @return Возвращает список объектов трансфера данных, сгенерированный на основе списка регионов, у которых
     * начало наименования совпадает с данным
     * @throws RecordNotFoundException Исключение, генерируемое при отсутствии в репозитории регионов, у которых
     *                                 начало наименования совпадает с данным
     */
    List<RegionDTO> getByNameBeginning(String nameBeginning) throws RecordNotFoundException;

    /**
     * Получает список регионов по сокращенному наименованию
     *
     * @param shortName Тип: {@link String}. Сокращенное наименование региона, по которому осуществляется поиск в
     *                  репозитории
     * @return Возвращает список объектов трансфера данных, сгенерированный на основе списка регионов с данным
     * сокращенным наименованием
     * @throws RecordNotFoundException Исключение, генерируемое при отсутствии в репозитории регионов с данным
     *                                 сокращенным наименованием
     */
    List<RegionDTO> getByShortName(String shortName) throws RecordNotFoundException;

    /**
     * Обновляет регион по идентификатору
     *
     * @param id        Тип: {@link String}. Идентификатор, по которому осуществляется поиск региона в репозитории для
     *                  обновления
     * @param regionDTO Тип: {@link RegionDTO}. Объект трансфера данных, на основе которого генерируется сущность, заменяющая
     *                  данные в репозитории
     * @throws RecordNotFoundException        Исключение, генерируемое при отсутствии в репозитории региона с данным
     *                                        идентификатором
     * @throws DuplicateUniqueValuesException Исключение, генерируемое при попытке обновить идентификатор региона на
     *                                        уже существующий в репозитории
     */
    void updateById(String id, RegionDTO regionDTO) throws RecordNotFoundException, DuplicateUniqueValuesException;

    /**
     * Удаляет регион по идентификатору
     *
     * @param id Тип: {@link String}. Идентификатор, по которому осуществляется поиск региона в репозитории для удаления
     * @throws RecordNotFoundException Исключение, генерируемое при отсутствии в репозитории региона с данным
     *                                 идентификатором
     */
    void deleteById(String id) throws RecordNotFoundException;
}
