package com.alticelabs.ccp.exagon.tester;

import com.alticelabs.ccp.exagon.tester.helper.types.BodyGenerator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // Send request
        List<Future<HttpResponse<String>>> listAnswers = new LinkedList<>();
        Map<Integer, Integer> responseCodes = new HashMap<>();
        int executionId = new Random().nextInt(999);
        do{
            String values = BodyGenerator.PCF_ACCOUNT.generate(executionId + listAnswers.size());
            //System.out.println(values);
            HttpClient client = HttpClient.newHttpClient();
            String port = "8080"; //new String[]{"8080", "8081"}[new Random().nextInt(0,1)];
            HttpRequest request = HttpRequest.newBuilder()
                    .header("requestId", UUID.randomUUID().toString())
                    .header("correlationId", UUID.randomUUID().toString())
                    .uri(URI.create("http://localhost:"+port+"/account"))
                    .POST(HttpRequest.BodyPublishers.ofString(values))
                    .build();
            long miliPreTime = System.currentTimeMillis();
            listAnswers.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                System.out.println("Status code: " + response.statusCode()
                        + " - listAnswer size: " + listAnswers.size()
                        + " - Time Elapsed: "+ (System.currentTimeMillis() - miliPreTime));
                return response;
            }));
            sleep(100);
        }while(listAnswers.size() < 1000);
        sleep(15000);
        int timeout = 0;
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[] {"RequestBody; Response Body; statusCode"});
        for (Future<HttpResponse<String>> future : listAnswers) {
            int status = 0;
            if (future.isDone()) {
                HttpResponse<String> response = future.get();
                status = response.statusCode();
/*                System.out.println("Request: "
                        + response.request().headers().firstValue("requestId")
                        + "\nStatus code: " + status);*/
//                dataLines.add(new String[]{
//                        response.request().headers().firstValue("requestId") + ";"
//                        +response.body().subSequence(0,Math.min(50, response.body().length()-1)) + ";"
//                        +String.valueOf(response.statusCode())
//                });
            } else {
                future.cancel(true);
//                System.out.println("Timeout on socket!");
//                dataLines.add(new String[]{"Timeout por problema de partição;0;0"});
                timeout++;
            }
            responseCodes.compute(status, (k, v) -> (v == null) ? 1 : v+1);
        }

//        dataLines.stream()
//                .map(strings -> new CsvUtil().convertToCSV(strings))
//                .forEach(System.out::println);

        System.out.println("Total de pedidos: " + listAnswers.size()
                + "\nPedidos com erro de particionamento: " + timeout);

        responseCodes.forEach((k,v) -> System.out.println("Status code:" + k + " - Quantidade: " + v));
    }
}