package org.example;
import org.example.Barca;

import java.util.List;
import java.util.Map;

public interface IService {


    void saveMeci(Meci meci);

    Barca login(String nume, IObserver client) throws Exception;

    List<Meci> getClasament();


    void logout(String username, IObserver client);



}
