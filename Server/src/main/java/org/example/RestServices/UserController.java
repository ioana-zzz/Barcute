package org.example.RestServices;

import org.example.Jucator;
import org.example.Meci;
import org.example.Repository.JucatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JucatorRepository repository;

    @RequestMapping(value = "/{nickname}", method = RequestMethod.GET)
    public ResponseEntity<List<MeciDTORest>> getByName(@PathVariable String nickname) {
        System.out.println("Get by nickname " + nickname);
        Optional<Jucator> jucator = repository.findByName(nickname);
        if (jucator.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Jucator j = jucator.get();

            List<Meci> meciuri = j.getJocuri().stream().toList();
            List<MeciDTORest> dtos = meciuri.stream()
                    .filter(meci -> meci.getScor() != -9)
                    .map(meci -> new MeciDTORest(
                            meci.getScor(),
                            meci.getIncercari().stream().map(t -> t.getPozitie()).toList(),
                            meci.getBarca().getPoz1(),
                            meci.getBarca().getPoz2(),
                            meci.getBarca().getPoz3())

                    )
                    .toList();

            return new ResponseEntity<>(dtos, HttpStatus.OK);
        }
    }

    private static class MeciDTORest {
        private int scor;
        private List<Integer> pozitii;
        private int poz1;
        private int poz2;
        private int poz3;

        public int getScor() {
            return scor;
        }

        public void setScor(int scor) {
            this.scor = scor;
        }

        public List<Integer> getPozitii() {
            return pozitii;
        }

        public void setPozitii(List<Integer> pozitii) {
            this.pozitii = pozitii;
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

        public MeciDTORest(int scor, List<Integer> pozitii, int poz1, int poz2, int poz3) {
            this.scor = scor;
            this.pozitii = pozitii;
            this.poz1 = poz1;
            this.poz2 = poz2;
            this.poz3 = poz3;
        }
    }
}
