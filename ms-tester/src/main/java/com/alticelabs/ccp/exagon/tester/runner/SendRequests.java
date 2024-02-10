package com.alticelabs.ccp.exagon.tester.runner;

import com.alticelabs.ccp.exagon.tester.helper.types.IBodyGenerate;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Future;

import static com.alticelabs.ccp.exagon.tester.util.PortConfig.getPort;
import static java.lang.Thread.sleep;

public class SendRequests implements Runnable {

    private IBodyGenerate iBodyGenerate;

    @Getter
    private final List<Future<HttpResponse<String>>> listAnswers;

    private final String[] ports;

    private final String endpoint;

    private final int requestNum;

    private final int sleepTime;
    public SendRequests(IBodyGenerate iBodyGenerate, String[] ports, String endpoint, int requestNum, int sleepTime) {
        this.iBodyGenerate = iBodyGenerate;
        this.ports = ports;
        this.endpoint = endpoint;
        this.requestNum = requestNum;
        this.sleepTime = sleepTime;
        this.listAnswers = new LinkedList<>();
    }

    @Override
    public void run() {
        int executionId = new Random().nextInt(999);
        HttpClient client = HttpClient.newHttpClient();
        do{
            String values = null;
            try {
                values = iBodyGenerate.generate(getNum(executionId));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            //System.out.println(values);
            String port = getPort(ports); //new String[]{"8080", "8081"}[new Random().nextInt(0,1)];
            HttpRequest request = HttpRequest.newBuilder()
                    .header("requestId", UUID.randomUUID().toString())
                    .header("correlationId", UUID.randomUUID().toString())
                    .uri(URI.create("http://localhost:"+port+endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(values))
                    .build();
            final long miliPreTime = System.currentTimeMillis();
            listAnswers.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                System.out.println("Status code: " + response.statusCode()
                        + " - listAnswer size: " + listAnswers.size()
                        + " - Time Elapsed: "+ (System.currentTimeMillis() - miliPreTime));
                return response;
            }));
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while(listAnswers.size() < requestNum);
    }

    private int getNum(int executionId) {
        return (executionId * requestNum ) + listAnswers.size();
    }

    public void clearRequests(){
        this.listAnswers.clear();
    }

}
