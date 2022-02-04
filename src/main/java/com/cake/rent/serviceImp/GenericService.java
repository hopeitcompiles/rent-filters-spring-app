package com.cake.rent.serviceImp;

import com.cake.rent.service.IGenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class GenericService<T,ID extends Serializable> implements IGenericService<T,ID> {
    @Override
    public T save(T entity) {
        return getDao().save(entity);
    }

    @Override
    public void delete(ID id) {
        getDao().deleteById(id);
    }

    @Override
    public T findByid(ID id) {
        Optional<T> optional=getDao().findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        List<T> result= new ArrayList<>();
        getDao().findAll().forEach(result::add);
        return result;
    }
    @Override
    public Page<T> getAll(Pageable pageable) {
        Page<T> result= getDao().findAll(pageable);
        return result;
    }
    @Override
    public Optional<T> selectId(ID id) {
        return getDao().findById(id);
    }
    public abstract JpaRepository<T, ID> getDao();
}
