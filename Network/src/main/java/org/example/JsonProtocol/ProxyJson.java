package org.example.JsonProtocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.example.*;
import org.example.DTO.BarcaDTO;
import org.example.DTO.LoginDTO;
import org.example.DTO.MeciDTO;
import org.example.utils.Request;
import org.example.utils.RequestType;
import org.example.utils.Response;
import org.example.utils.ResponseType;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyJson implements IService {
    private final String host;
    private final int port;

    private IObserver client;

    private BufferedReader input;
    private BufferedWriter output;
    private Socket connection;

    private BlockingQueue<Response> responses = new LinkedBlockingQueue<>();
    private volatile boolean finished;
    private Logger logger = LogManager.getLogger(ProxyJson.class);
    private Gson gson = new Gson();

    public ProxyJson(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            finished = false;
            System.out.println("Connection established");
            startReader();
        } catch (IOException e) {
            throw new Exception("Error initializing connection: " + e.getMessage(), e);
        }

        logger.info("Connection initialized");
    }

    private void closeConnection() {
        finished = true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
            client = null;
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage(), e);
        }

        logger.info("Connection closed");
    }

    private void sendRequest(Request request) throws Exception {
        try {
            logger.info("Sending request: " + request);
            String requestString = gson.toJson(request);
            output.write(requestString);
            output.newLine();  // Important to add newline for BufferedReader.readLine()
            output.flush();
            logger.info("Request sent");

        } catch (IOException e) {
            throw new Exception("Error sending request: " + e.getMessage());
        }
    }

    private Response readResponse() throws Exception {
        try {
            return responses.take();
        } catch (InterruptedException e) {
            throw new Exception("Error reading response: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread readerThread = new Thread(new ReaderThread());
        readerThread.start();
    }

    @Override
    public void saveMeci(Meci meci) {
        logger.info("PROXY: SAVING MECI");
        try {
            MeciDTO dto = new MeciDTO(meci.getBarca().getId(),meci.getJucator().getId(), meci.getJucator().getNume(),meci.getScor());
            dto.setTimestamp(meci.getTimestamp());
            dto.setIncercari(meci.getIncercari());
            Request request = new Request.Builder().type(RequestType.OVER).data(dto).build();
            sendRequest(request);
            Response response = readResponse();

            if (response.type() == ResponseType.ERROR) {
                throw new Exception(gson.toJson(response.data()));
            }

        } catch (Exception e) {
            logger.error("Error saving match: " + e.getMessage(), e);
        }
    }

    @Override
    public Barca login(String username, IObserver client) throws Exception {
        try {
            logger.info("Login attempt with username: " + username);

            initializeConnection();
            this.client = client;
            var dto = new LoginDTO(username);

            Request request = new Request.Builder().type(RequestType.LOGIN).data(dto).build();
            sendRequest(request);

            Response response = readResponse();

            if (response.type() == ResponseType.ERROR) {
                throw new Exception(gson.toJson(response.data()));
            }

            BarcaDTO joc = gson.fromJson(gson.toJson(response.data()), BarcaDTO.class);


            return new Barca(joc.getId(), joc.getPoz1(), joc.getPoz2(), joc.getPoz3());
        }catch(Exception e)

        {
            logger.error("Error during login: " + e.getMessage(), e);
            //closeConnection();
            throw new Exception("Login failed: " + e.getMessage());
        }
    }

    @Override
    public List<Meci> getClasament() {
        logger.info("PROXY: GETTING CLASAMENT");
        try {
            Request request = new Request.Builder().type(RequestType.LOAD).data(null).build();
            sendRequest(request);
            Response response = readResponse();

            if (response.type() == ResponseType.ERROR) {
                throw new Exception(gson.toJson(response.data()));
            }

            Type listType = new TypeToken<ArrayList<MeciDTO>>(){}.getType();
            List<MeciDTO> meciDTOs = gson.fromJson(gson.toJson(response.data()), listType);
            List<Meci> meciList = new ArrayList<>();

            for (MeciDTO dto : meciDTOs) {
                Meci meci = new Meci();
                meci.setScor(dto.getScor());
                meci.setTimestamp(dto.getTimestamp());
                Jucator jucator = new Jucator(dto.getJucatorID(), dto.getJucatorName());
                meci.setJucator(jucator);
                Barca joc = new Barca();
                joc.setId(dto.getBarcaID());
                meci.setBarca(joc);
                meciList.add(meci);
            }
            return meciList;

        } catch (Exception e) {
            logger.error("Error getting clasament: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }



    @Override
    public void logout(String username, IObserver client) {
        logger.info("Logout attempt for client: " + client);
        if (this.client == null || !this.client.equals(client)) {
            logger.warn("Client not logged in or already logged out");
            return;
        }

        try {
            Request request = new Request.Builder().type(RequestType.LOGOUT).data(new LoginDTO(username)).build();
            sendRequest(request);
            closeConnection();
            logger.info("Client logged out successfully");
        } catch (Exception e) {
            logger.error("Error during logout: " + e.getMessage(), e);
        }
    }


    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    String responseString = input.readLine();
                    logger.info("Response received: " + responseString);
                    if (responseString == null) {
                        finished = true;
                        break;
                    }

                    Response response = gson.fromJson(responseString, Response.class);
                    logger.debug("response received " + response);

                    if (response.type() == ResponseType.UPDATE) {
                        logger.info("Update received from server");
                        MeciDTO dto = gson.fromJson(gson.toJson(response.data()), MeciDTO.class);
                        Meci meci = new Meci();
                        meci.setScor(dto.getScor());
                        meci.setTimestamp(dto.getTimestamp());
                        Jucator juc = new Jucator(dto.getJucatorID(), dto.getJucatorName());
                        meci.setJucator(juc);
                        Barca joc = new Barca();
                        joc.setId(dto.getBarcaID());
                        meci.setBarca(joc);

                        client.update(meci);
                    } else {
                        try {
                            responses.put(response);
                        } catch (InterruptedException e) {
                            logger.error(e);
                            logger.error(e.getStackTrace());
                        }
                    }
                } catch (Exception e) {
                    if (!finished) {
                        logger.error("Error reading response", e);

                        finished = true;
                    }
                }
            }
        }
    }
}