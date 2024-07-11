package com.paynet.repository;

import com.paynet.model.UserPayApp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserPayApp, Integer> {

public UserPayApp findByEmail(String email);
}
