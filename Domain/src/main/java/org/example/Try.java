package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.*;

@Entity
@Table(name  = "incercari")
public class Try extends org.example.Entity<Long> {

    @ManyToOne
    @JoinColumn(name = "meci", nullable = false)
    private Meci meci;

    @Column(nullable = false)
    private int pozitie;




    public Try(){}
    public Try(int poz){
        this.pozitie = poz;
    }


    public Meci getMeci() {
        return meci;
    }

    public void setMeci(Meci meci) {
        this.meci = meci;
    }

    public int getPozitie() {
        return pozitie;
    }

    public void setPozitie(int pozitie) {
        this.pozitie = pozitie;
    }
}
