package com.apress.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.apress.quickpoll.domain.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {
}
