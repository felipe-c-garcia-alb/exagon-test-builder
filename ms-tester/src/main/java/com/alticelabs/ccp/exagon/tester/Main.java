package com.alticelabs.ccp.exagon.tester;

import com.alticelabs.ccp.exagon.tester.helper.types.BodyGenerator;
import com.alticelabs.ccp.exagon.tester.runner.SendRequests;

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
        final Arguments arguments = new Arguments(args[0], args[1], args[2]);
        // Send request
        Map<Integer, Integer> responseCodes = new HashMap<>();
        System.out.println(Arrays.toString(buildPorts(arguments.portNum)));

        SendRequests sendRequests = new SendRequests(BodyGenerator.PCF_ACCOUNT,
                buildPorts(args[0]),
                "/account",
                100,
                200);
        sendRequests.run();
        sleep(5000);

        sendRequests.clearRequests();

        sendRequests = new SendRequests(BodyGenerator.PCF_ACCOUNT,
                buildPorts(args[0]),
                "/account",
                arguments.returnRequestNum(),
                arguments.returnRate());
        sendRequests.run();
        sleep(60000);
        List<Future<HttpResponse<String>>> listAnswers = sendRequests.getListAnswers();
        int timeout = 0;
        for (Future<HttpResponse<String>> future : listAnswers) {
            int status = 0;
            if (future.isDone()) {
                HttpResponse<String> response = future.get();
                status = response.statusCode();
           } else {
                System.out.println("Indice do problema: " + listAnswers.indexOf(future));
                future.cancel(true);
                timeout++;
            }
            responseCodes.compute(status, (k, v) -> (v == null) ? 1 : v+1);
        }
        System.out.println("Total de pedidos: " + listAnswers.size()
                + "\nPedidos sem resposta: " + timeout);
        sendRequests.clearRequests();
        responseCodes.forEach((k,v) -> System.out.println("Status code:" + k + " - Quantidade: " + v));
    }

    private static String[] buildPorts(String arg) {
        List<String> list = new ArrayList<>();
        Integer quantity = Integer.valueOf(arg);
        final Integer port = 8080;
        for(int a = 0; a < quantity;a++){
            int curPort = port + a;
            list.add(Integer.toString(curPort));
        }
        return list.toArray(new String[0]);
    }

    private record Arguments(String portNum, String requestNum, String rate){

        public int returnRequestNum(){
            return Integer.parseInt(requestNum);
        }

        public int returnRate(){
            return Integer.parseInt(rate);
        }
        @Override
        public String toString() {
            return "Arguments{" +
                    "portNum='" + portNum + '\'' +
                    ", requestNum='" + requestNum + '\'' +
                    ", rate='" + rate + '\'' +
                    '}';
        }
    };
}