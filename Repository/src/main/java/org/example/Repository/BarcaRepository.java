package org.example.Repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Interfaces.IBarcaRepository;
import org.example.JdbcUtils;
import org.example.Barca;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BarcaRepository implements IBarcaRepository {

    protected JdbcUtils jdbc;
    private Logger logger  = LogManager.getLogger(BarcaRepository.class);


    public BarcaRepository() {
        this.jdbc = new JdbcUtils();

    }

    public Optional<Barca> findOne(Long aLong) {
        Connection con = jdbc.getConnection();
        try(var statement = con.prepareStatement("select * from barci where id = ?")) {
            statement.setLong(1, aLong);
            var result = statement.executeQuery();
            if(result.next()) {
                return Optional.of(new Barca(result.getLong("id"), result.getInt("poz1"),
                        result.getInt("poz2"),  result.getInt("poz3")));
            }
        } catch (Exception e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<Barca> findAll() {
        Connection con = jdbc.getConnection();
        var all = new ArrayList<Barca>();
        try(var statement = con.prepareStatement("select * from barci")){
            var result  = statement.executeQuery();
            while(result.next()){
                all.add(new Barca(result.getLong("id"), result.getInt("poz1"),
                        result.getInt("poz2"),  result.getInt("poz3")));
            }
        }catch(Exception e){
            logger.error(e);
        }

        return all;
    }

    public Optional<Barca> save(Barca joc) {
        Connection con = jdbc.getConnection();
        try(var statement = con.prepareStatement("insert into barci (poz1,poz2, poz3) values (?, ?, ?)")){
        statement.setInt(1,joc.getPoz1());
        statement.setInt(2,joc.getPoz2());
        statement.setInt(3,joc.getPoz3());
        statement.executeUpdate();
        return Optional.empty();
        }
        catch(Exception e){
            logger.error(e);
        }

        return Optional.of(joc);


    }

    @Override
    public Optional<Barca> delete(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Barca> update(Barca entity) {
        return Optional.empty();
    }


}
