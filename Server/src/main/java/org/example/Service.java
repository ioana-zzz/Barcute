package org.example;

import org.example.Interfaces.IJucatorRepository;
import org.example.Interfaces.IMeciRepository;
import org.example.Repository.BarcaRepository;
import org.example.Repository.JucatorRepository;
import org.example.Repository.MeciRepository;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class Service implements IService{

    private IBarcaRepository jocRepo;

    private IJucatorRepository jucatorRepo;

    private IMeciRepository meciRepo;

    private Map<String, IObserver> loggedClients = new HashMap<>();

    private final int defaultThreadsNo  = 5;

    public Service(IMeciRepository meciRepo, IBarcaRepository jocRepo, IJucatorRepository jucatorRepo) {
        this.meciRepo = meciRepo;
        this.jocRepo = jocRepo;
        this.jucatorRepo = jucatorRepo;

    }

    public Barca getBarca() {
        List<Long> ids = jocRepo.findAll().stream().map(joc -> joc.getId()).collect(Collectors.toList());
        Long id = ids.get((int)Math.floor(Math.random() * ids.size()));
        return jocRepo.findOne(id).get();
    }

    @Override
    public void saveMeci(Meci meci) {
        Optional<Barca> barca = jocRepo.findOne(meci.getBarca().getId());
        Optional<Jucator> jucator = jucatorRepo.findByName(meci.getJucator().getNume());
        if(!barca.isPresent() || !jucator.isPresent()) {
            throw new IllegalArgumentException("Barca sau jucator inexistent");
        }

        List<Try> tries = meci.getIncercari();
        meci.setIncercari(null);
        meci.setJucator(jucator.get());
        meci.setBarca(barca.get());
        meciRepo.save(meci);
        meci = meciRepo.findLatest();
        meci.setIncercari(tries);
        for (Try t : tries) {
            t.setMeci(meci);
        }
        meciRepo.update(meci);

            notifyAllClients(meci);
    }

    @Override
    public Barca login(String nume, IObserver observer) {
        Optional<Jucator> player = jucatorRepo.findByName(nume);

        if(player.isPresent()) {
            loggedClients.put(nume, observer);
            return getBarca();
        }

        return null;
    }



    private void notifyAllClients(Meci meci) {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (var client : loggedClients.entrySet()) {
            executor.execute(() -> {
                try {
                    client.getValue().update(meci);
                } catch (Exception e) {
                    System.err.println("Eroare la notificarea clientului " + client.getKey() + ": " + e.getMessage());
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public List<Meci> getClasament() {
        return meciRepo.findAll().stream()
                .sorted((m1,m2) -> m2.getScor() - m1.getScor())
                .collect(Collectors.toList());

    }

    @Override
    public void logout(String username, IObserver client) {
        if (loggedClients.containsKey(username)) {
            loggedClients.remove(username);
            System.out.println("Clientul " + username + " s-a deconectat.");
        } else {
            System.out.println("Clientul " + username + " nu este conectat.");
        }
    }
}
