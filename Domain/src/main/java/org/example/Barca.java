package org.example;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "barci")
public class Barca extends org.example.Entity<Long> {




    @Column
    private Integer poz1;

    @Column
    private Integer poz2;

    public Barca(Long id, Integer poz1, Integer poz2, Integer poz3) {
        setId(id);
        this.poz1 = poz1;
        this.poz2 = poz2;
        this.poz3 = poz3;
    }

    @Column
    private Integer poz3;


    @OneToMany(mappedBy = "barca", cascade = CascadeType.ALL, orphanRemoval = true, fetch =
            FetchType.EAGER)
    private Set<Meci> meciuri;


    public Barca(){}

    public Barca(int poz1, int poz2, int poz3){
        this.poz1 = poz1;
        this.poz2 = poz2;
        this.poz3 = poz3;

    }

    public Integer getPoz1() {
        return poz1;
    }

    public void setPoz1(Integer poz1) {
        this.poz1 = poz1;
    }

    public Integer getPoz2() {
        return poz2;
    }

    public void setPoz2(Integer poz2) {
        this.poz2 = poz2;
    }

    public Integer getPoz3() {
        return poz3;
    }

    public void setPoz3(Integer poz3) {
        this.poz3 = poz3;
    }


    public Set<Meci> getMeciuri() {
        return meciuri;
    }
}
