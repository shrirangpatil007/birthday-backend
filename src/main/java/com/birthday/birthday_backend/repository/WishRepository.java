package com.birthday.birthday_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.birthday.birthday_backend.model.Wish;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long>{

}
