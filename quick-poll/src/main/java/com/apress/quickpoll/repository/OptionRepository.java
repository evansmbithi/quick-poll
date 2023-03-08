package com.apress.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.apress.quickpoll.domain.Option;

public interface OptionRepository extends CrudRepository<Option, Long> {
    
}
