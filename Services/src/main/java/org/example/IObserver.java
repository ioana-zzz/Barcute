package org.example;
import org.example.Meci;

public interface IObserver {
    void update(Meci meci) throws Exception;
}
