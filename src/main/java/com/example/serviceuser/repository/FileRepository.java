package com.example.serviceuser.repository;

import com.example.serviceuser.pojo.Files;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FileRepository  extends MongoRepository<Files, String> {

    @Query(value = "{name : '?0'}")
     public Files findByname(String name);
}
