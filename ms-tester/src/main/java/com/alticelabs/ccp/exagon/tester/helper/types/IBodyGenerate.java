package com.alticelabs.ccp.exagon.tester.helper.types;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IBodyGenerate {
    String generate(int num) throws JsonProcessingException;
}
