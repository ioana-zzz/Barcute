package org.example.DTO;

import org.example.Try;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class MeciDTO {
        private Long barcaID;


        private String timestamp;
        private Long jucatorID;
        private String jucatorName;
        private List<Integer> incercari;
        private int scor;

        public MeciDTO(){};

        public MeciDTO(Long barcaID, Long jucatorID, String jucatorName,int scor) {
                this.barcaID = barcaID;
                this.jucatorID = jucatorID;
                this.jucatorName = jucatorName;
                this.scor = scor;
        }



        public Long getBarcaID() {
                return barcaID;
        }

        public void setBarcaID(Long barcaID) {
                this.barcaID = barcaID;
        }

        public Long getJucatorID() {
                return jucatorID;
        }

        public void setJucatorID(Long jucatorID) {
                this.jucatorID = jucatorID;
        }

        public String getJucatorName() {
                return jucatorName;
        }

        public void setJucatorName(String jucatorName) {
                this.jucatorName = jucatorName;
        }



        public int getScor() {
                return scor;
        }

        public void setScor(int scor) {
                this.scor = scor;
        }




        public void setTimestamp(LocalDateTime timestamp) {
                this.timestamp = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }


        public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }






        public List<Try> getIncercari() {
                return incercari.stream().map(
                        i -> new Try( i)
                ).toList();
        }

        public void setIncercari(List<Try> incercari) {
                this.incercari = incercari.stream().map(i-> i.getPozitie()).toList();
        }


}

