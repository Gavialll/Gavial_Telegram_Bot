package com.bot.gavial_bot.repository;

import com.bot.gavial_bot.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Person, Long> {
    Person findByChatId(Long id);
}
