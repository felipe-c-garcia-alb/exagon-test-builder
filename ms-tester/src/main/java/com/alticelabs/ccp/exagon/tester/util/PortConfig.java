package com.alticelabs.ccp.exagon.tester.util;

import java.util.concurrent.atomic.AtomicInteger;

public class PortConfig {
    private static final AtomicInteger ind = new AtomicInteger(0);

    public static String getPort(String[] ports){
        return ports[ind.getAndAccumulate(ports.length, (cur, n)->cur >= n-1 ? 0 : cur+1)];
    }
}
