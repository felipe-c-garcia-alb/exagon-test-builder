package com.alticelabs.ccp.exagon.tester.helper.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum BodyGenerator implements IBodyGenerate {
    PCF_ACCOUNT{
        public String generate(int num) throws JsonProcessingException {
            Map<String, Object> generated = new HashMap<>();
            generated.put("id", "account" + num);
            generated.put("billingAccountId", "account" + num);
            generated.put("characteristics", Arrays.array());
            return new ObjectMapper().writeValueAsString(generated);
        }
    }
}
