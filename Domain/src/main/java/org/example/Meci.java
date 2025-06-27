package org.example;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "meciuri")
public class Meci extends org.example.Entity<Long> {



    @ManyToOne
    @JoinColumn(name = "jucator", nullable = false)
    private Jucator jucator;

    @ManyToOne
    @JoinColumn(name = "barca", nullable = false)
    private Barca barca;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private int scor;

    @OneToMany(mappedBy = "meci", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Try> incercari;

    public Meci() {}

    public Meci(Jucator jucator, Barca barca, int scor, LocalDateTime timestamp) {
        this.jucator = jucator;
        this.barca = barca;
        this.scor = scor;
        this.timestamp = timestamp;
    }


    public Jucator getJucator() {
        return jucator;
    }

    public void setJucator(Jucator jucator) {
        this.jucator = jucator;
    }

    public Barca getBarca() {
        return barca;
    }

    public void setBarca(Barca barca) {
        this.barca = barca;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getScor() {
        return scor;
    }

    public void setScor(int scor) {
        this.scor = scor;
    }

    public List<Try> getIncercari() {
        return incercari;
    }

    public void setIncercari(List<Try> incercari) {
        this.incercari = incercari;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meci)) return false;
        Meci meci = (Meci) o;
        return scor == meci.scor &&
                Objects.equals(jucator, meci.jucator) &&
                Objects.equals(barca, meci.barca) &&
                Objects.equals(timestamp, meci.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jucator, barca, timestamp, scor);
    }
}
