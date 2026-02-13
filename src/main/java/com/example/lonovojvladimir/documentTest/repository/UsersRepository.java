package com.example.lonovojvladimir.documentTest.repository;

import com.example.lonovojvladimir.documentTest.domain.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<UsersEntity, Long>, CrudRepository<UsersEntity, Long> {


}
