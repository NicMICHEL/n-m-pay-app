package com.paynet.repository;

import com.paynet.model.UserPayApp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserPayApp, Integer> {

    public Optional<UserPayApp> findByEmail(String email);

}
