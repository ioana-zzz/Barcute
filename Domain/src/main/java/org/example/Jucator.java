package org.example;

import jakarta.persistence.*;
import java.util.Set;
import jakarta.persistence.Entity;

@Entity
@Table(name = "jucatori")
public class Jucator extends org.example.Entity<Long> {


    @Column(nullable = false)
    private String nume;

    public Jucator(Long id, String nume) {

        setId(id);
        this.nume = nume;
    }


    public Jucator(){};

    @OneToMany(mappedBy = "jucator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Meci> jocuri;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Set<Meci> getJocuri() {
        return jocuri;
    }

    public void setJocuri(Set<Meci> jocuri) {
        this.jocuri = jocuri;
    }
}