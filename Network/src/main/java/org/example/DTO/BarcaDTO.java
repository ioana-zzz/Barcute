package org.example.DTO;

public class BarcaDTO {
    private int poz1;
    private int poz2;
    private int poz3;
    private Long id;

    public BarcaDTO(int poz1, int poz2, int poz3, Long id) {
        this.poz1 = poz1;
        this.poz2 = poz2;
        this.poz3 = poz3;
        this.id = id;
    }

    public BarcaDTO(int poz1, int poz2, int poz3) {
        this.poz1 = poz1;
        this.poz2 = poz2;
        this.poz3 = poz3;
    }

    public int getPoz1() {
        return poz1;
    }

    public void setPoz1(int poz1) {
        this.poz1 = poz1;
    }

    public int getPoz2() {
        return poz2;
    }

    public void setPoz2(int poz2) {
        this.poz2 = poz2;
    }

    public int getPoz3() {
        return poz3;
    }

    public void setPoz3(int poz3) {
        this.poz3 = poz3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
