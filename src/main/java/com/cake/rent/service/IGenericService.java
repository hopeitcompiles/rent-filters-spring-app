package com.cake.rent.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IGenericService <T,ID extends Serializable>{
    public T save(T entity);
    public void delete(ID id);
    public T findByid(ID id);
    public Page<T> getAll(Pageable pageable);
    public List<T> getAll();
    public Optional<T> selectId(ID id);
}
