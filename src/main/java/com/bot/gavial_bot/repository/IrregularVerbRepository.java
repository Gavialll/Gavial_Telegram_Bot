package com.bot.gavial_bot.repository;

import com.bot.gavial_bot.entity.IrregularVerb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IrregularVerbRepository extends JpaRepository<IrregularVerb, Long> {
}
