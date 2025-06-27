package org.example.RestServices;

import org.example.Barca;
import org.example.Repository.BarcaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/joc")
public class BarcaController {

    @Autowired
    private BarcaRepository repository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody BarcaDTORest jocDTO) {
        System.out.println("Saving sent joc ");
        try {
            Optional<Barca> joc = repository.save(new Barca(jocDTO.getPoz1(), jocDTO.getPoz2(), jocDTO.getPoz3()));
            if (joc.isPresent()) {
                return new ResponseEntity<>(jocDTO, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Barca created" , HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    private static class BarcaDTORest {
        private int poz1;
        private int poz2;
        private int  poz3;

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

        public BarcaDTORest(int poz1, int poz2, int poz3) {
            this.poz1 = poz1;
            this.poz2 = poz2;
            this.poz3 = poz3;
        }

        public int getPoz3() {
            return poz3;
        }

        public void setPoz3(int poz3) {
            this.poz3 = poz3;
        }
    }
}
