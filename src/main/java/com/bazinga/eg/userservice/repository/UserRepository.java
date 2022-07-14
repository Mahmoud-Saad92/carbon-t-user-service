package com.bazinga.eg.userservice.repository;

import com.bazinga.eg.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
