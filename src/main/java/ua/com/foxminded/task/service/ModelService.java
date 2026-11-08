package ua.com.foxminded.task.service;

import java.util.List;

public interface ModelService<T> {

    public T findByIdDto(int id);

    public List<T> findAllDto();

    public T create(T dto);

    public T update(T dto);

}
