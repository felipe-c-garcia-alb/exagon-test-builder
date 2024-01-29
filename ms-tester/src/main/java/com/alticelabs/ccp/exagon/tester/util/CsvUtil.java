package com.alticelabs.ccp.exagon.tester.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvUtil {

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(";"));
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        return escapedData.replaceAll("\\n", " ");
    }
}
