package com.alticelabs.ccp.exagon.dummyorchestrator.mock;

import com.alticelabs.ccp.exagon.infra_common_models.communication.consumer.IExagonCommunicationConsumer;
import com.alticelabs.ccp.exagon.infra_common_models.communication.producer.IExagonCommunicationProducer;
import com.alticelabs.ccp.exagon.infra_common_models.communication.utils.IExagonCommunicationFactory;
import com.alticelabs.ccp.exagon.kafka_lib.consumer.KafkaConsumerImpl;
import com.alticelabs.ccp.exagon.kafka_lib.producer.KafkaProducerImpl;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn({"kafkaProperties"})
public class CommunicationFactory implements IExagonCommunicationFactory {

    @Override
    public IExagonCommunicationConsumer getNewConsumer(String id) {
        return new KafkaConsumerImpl(id);
    }

    @Override
    public IExagonCommunicationProducer getNewProducer(String id) {
        return new KafkaProducerImpl(id);
    }
}
