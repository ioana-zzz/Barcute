package org.example.Interfaces;

import org.example.Meci;

public interface IMeciRepository extends IRepository<Long, Meci>{
    Meci findLatest();
}
