package com.apress.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.apress.quickpoll.domain.Vote;

public interface VoteRepository extends CrudRepository<Vote, Long> {
}
