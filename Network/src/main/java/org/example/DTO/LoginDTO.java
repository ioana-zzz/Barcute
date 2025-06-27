package org.example.DTO;

public class LoginDTO {
    private String nume;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public LoginDTO(String nume) {
        this.nume = nume;
    }
}
