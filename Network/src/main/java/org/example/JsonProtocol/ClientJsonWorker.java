package org.example.JsonProtocol;

import com.google.gson.Gson;

import org.example.*;
import org.example.DTO.BarcaDTO;
import org.example.DTO.LoginDTO;
import org.example.DTO.MeciDTO;
import org.example.utils.Request;
import org.example.utils.Response;
import org.example.utils.ResponseType;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class ClientJsonWorker implements Runnable, IObserver {
    private final IService server;
    private final Socket connection;
    private BufferedReader input;
    private BufferedWriter output;
    private volatile boolean connected = true;
    private final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(ClientJsonWorker.class.getName());

    public ClientJsonWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            input = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.severe("Error initializing streams: " + e.getMessage());
            connected = false;
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String requestStr = input.readLine();
                if (requestStr == null) {
                    connected = false;
                    break;
                }
                logger.info("Received request string: " + requestStr);
                Request request = gson.fromJson(requestStr, Request.class);
                logger.info("Parsed request: " + request);
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }

            } catch (Exception e) {
                logger.severe("Error handling request: " + e.getMessage());
                connected = false;
            }
        }
        closeConnection();
    }

    private Response handleRequest(Request request) {
        logger.info("Handling request: " + request);
        try {
            Method method = this.getClass().getDeclaredMethod("handle" + request.type(), Request.class);
            return (Response) method.invoke(this, request);
        } catch (Exception e) {
            logger.severe("Failed to handle request: " + e.getMessage());
            return new Response.Builder().type(ResponseType.ERROR).data("Unknown error").build();
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.info("Sending response: " + response);
        synchronized (output) {
            String responseStr = gson.toJson(response);
            output.write(responseStr);
            output.newLine();
            output.flush();
        }
    }

    private void closeConnection() {
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException err) {
            logger.severe("Error closing connection: " + err.getMessage());
        } finally {
            connected = false;
        }
    }

    @Override
    public void update(Meci meci) {

        MeciDTO data = new MeciDTO(  meci.getBarca().getId(),meci.getJucator().getId(),
                meci.getJucator().getNume(),
                meci.getScor());

        data.setTimestamp(meci.getTimestamp());
        try {
            sendResponse(new Response.Builder()
                    .type(ResponseType.UPDATE)
                    .data(data)
                    .build());
        } catch (IOException e) {
            logger.severe("Error sending update: " + e.getMessage());
        }
    }


    private Response handleLOGIN(Request request) {
        try {
            LoginDTO dto = gson.fromJson(gson.toJson(request.data()), LoginDTO.class);
            Barca joc = server.login(dto.getNume(), this);
            if (joc == null) {
                return new Response.Builder().type(ResponseType.ERROR).data("Login failed").build();
            }
            return new Response.Builder().type(ResponseType.OK)
                    .data(new BarcaDTO(joc.getPoz1(), joc.getPoz2(), joc.getPoz3(), joc.getId()))
                    .build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        try {
            LoginDTO dto = gson.fromJson(gson.toJson(request.data()), LoginDTO.class);
            server.logout(dto.getNume(), this);
            connected = false;
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            logger.severe("Error handling 'logout' request: " + e.getMessage());
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleOVER(Request request) {
        try {
            MeciDTO dto = gson.fromJson(gson.toJson(request.data()), MeciDTO.class);
            Meci meci = new Meci();
            meci.setScor(dto.getScor());
            meci.setJucator(new Jucator(dto.getJucatorID(), dto.getJucatorName()));
            meci.setBarca(new Barca(dto.getBarcaID(), 0,0,0));
            meci.setIncercari(dto.getIncercari());
            meci.setTimestamp(dto.getTimestamp());
            server.saveMeci(meci);
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (Exception e) {
            logger.info("Error handling 'over' request: " + e.getMessage());
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleLOAD(Request request) {
        try {
            List<MeciDTO> data = new ArrayList<>();
            List<Meci> meciuri = server.getClasament();
            for (Meci meci: meciuri){
                MeciDTO meciDTO = new MeciDTO();
                meciDTO.setJucatorID(meci.getJucator().getId());
                meciDTO.setJucatorName(meci.getJucator().getNume());
                meciDTO.setBarcaID(meci.getBarca().getId());
                meciDTO.setTimestamp(meci.getTimestamp());
                meciDTO.setScor(meci.getScor());
                data.add(meciDTO);
            }
            return new Response.Builder().type(ResponseType.OK).data(data).build();
        } catch (Exception e) {
            logger.severe("Error handling 'load' request: " + e.getMessage());
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
}
