package io.noster.TravelSns.dao;

import io.noster.TravelSns.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Mapper
public interface UserMapper extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
