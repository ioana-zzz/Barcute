package org.example.Interfaces;

import org.example.Jucator;

import java.util.Optional;

public interface IJucatorRepository extends IRepository<Long, Jucator> {
    Optional<Jucator> findByName(String name);
}
