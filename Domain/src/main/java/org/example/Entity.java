package org.example;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Entity<T> {
    @Id
    @GeneratedValue(generator = "increment")
    protected T id;
    
    public Entity(){}
    
    public Entity(T id) {
        this.id = id;
    }


    public T getId() {
        return id;
    }
    
    public void setId(T id){
        this.id  = id;
    }
}
