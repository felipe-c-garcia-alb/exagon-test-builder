package com.alticelabs.ccp.exagon.dummyorchestrator.mock;

import com.alticelabs.ccp.exagon.common_models.annotations.ExagonCommand;
import com.alticelabs.ccp.exagon.common_models.annotations.ExagonNotification;
import com.alticelabs.ccp.exagon.common_models.annotations.ExagonRequest;
import com.alticelabs.ccp.exagon.infra_common_models.communication.consumer.IExagonCommunicationConsumer;
import com.alticelabs.ccp.exagon.infra_common_models.communication.exception.ExagonCommunicationProducerException;
import com.alticelabs.ccp.exagon.infra_common_models.communication.models.Event;
import com.alticelabs.ccp.exagon.infra_common_models.communication.producer.IExagonCommunicationProducer;
import com.alticelabs.ccp.exagon.infra_common_models.communication.utils.IExagonCommunicationFactory;
import com.alticelabs.pcf.commands.GeneratePRVLDR;
import com.alticelabs.pcf.commands.ManageServiceReferences;
import com.alticelabs.pcf.commands.PublishADR;
import com.alticelabs.pcf.notification.ManageServiceReferencesResult;
import com.alticelabs.pcf.notification.enums.ServiceCriteriaResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class MockParticipant {
    private IExagonCommunicationProducer producer;
    private IExagonCommunicationConsumer consumer;
    private static final String STATUS_ADDRESS = "statusAddress";
    private static final String STEP_IDENTIFIER = "stepIdentifier";
    private static final String READ_TIMESTAMP = "readTimestamp";
    private static final String WRITE_TIMESTAMP = "writeTimestamp";
    private static final String QUALIFIER = "qualifier";

    @Bean
    public CommandLineRunner consoleRunner(IExagonCommunicationFactory communicationFactory) {
        return args -> {
            this.producer = communicationFactory.getNewProducer("internal");
            this.consumer = communicationFactory.getNewConsumer("internal");

            consumer.subscribeEvent(getSource(ManageServiceReferences.class), ManageServiceReferences.class);
            consumer.subscribeEvent(getSource(GeneratePRVLDR.class), GeneratePRVLDR.class);
            consumer.subscribeEvent(getSource(PublishADR.class), PublishADR.class);
            consumer.start();
            consumerStartConsuming(consumer);
        };
    }

    private void consumerStartConsuming(IExagonCommunicationConsumer consumer) {
        (new Thread(() -> {
            while (true) {
                Event event = consumer.getNext();
                if (event != null) {
                    final ParticipantContext participantContext = buildParticipantContext(event);
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                    if (event.getPayload() instanceof ManageServiceReferences) {
                        try {
                            createManageServiceReferencesResult(participantContext);
                            createManageServiceReferencesFlow(participantContext);
                        } catch (ExagonCommunicationProducerException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (event.getPayload() instanceof GeneratePRVLDR) {
                        try {
                            createGeneratePRVLDRResult(participantContext);
                            createGeneratePRVLDRFlow(participantContext);
                        } catch (ExagonCommunicationProducerException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (event.getPayload() instanceof PublishADR) {
                        try {
                            createPublishADRResult(participantContext);
                            createPublishADRFlow(participantContext);
                        } catch (ExagonCommunicationProducerException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        })).start();
    }

    private ParticipantContext buildParticipantContext(Event event) {
        String sagaId = event.getSagaId();
        String statusAddress = event.getHeaders().get(STATUS_ADDRESS);
        String stepIdentifier = event.getHeaders().get(STEP_IDENTIFIER);
        String readTimestamp = event.getHeaders().get(READ_TIMESTAMP);
        String writeTimestamp = event.getHeaders().get(WRITE_TIMESTAMP);
        String qualifier = event.getHeaders().getOrDefault(QUALIFIER, "none");

        return ParticipantContext.builder()
                .sagaId(sagaId)
                .statusAddress(statusAddress)
                .stepIdentifier(stepIdentifier)
                .readTimestamp(readTimestamp)
                .writeTimestamp(writeTimestamp)
                .qualifier(qualifier)
                .build();
    }

    private void createManageServiceReferencesResult(ParticipantContext participantContext) throws ExagonCommunicationProducerException {
        ManageServiceReferencesResult referencesResult = new ManageServiceReferencesResult();
        referencesResult.setResult(ServiceCriteriaResult.OK);
        referencesResult.setErrorCause("so porque sim");

        Event eventResult = new Event(participantContext.getSagaId(), getSource(ManageServiceReferencesResult.class), referencesResult);
        addAllHeaders(eventResult, participantContext);
        producer.produce(eventResult);
    }

    private void createManageServiceReferencesFlow(ParticipantContext participantContext) throws ExagonCommunicationProducerException {
        Event eventFlow = new Event(participantContext.getSagaId(), participantContext.getStatusAddress(), "OK");
        addAllHeaders(eventFlow, participantContext);
        producer.produce(eventFlow);
    }

    private void createGeneratePRVLDRResult(ParticipantContext participantContext) throws ExagonCommunicationProducerException {
        LDRResult ldrResult = new LDRResult();

        Event eventResult = new Event(participantContext.getSagaId(), getSource(LDRResult.class), ldrResult);
        addAllHeaders(eventResult, participantContext);
        producer.produce(eventResult);
    }

    private void createGeneratePRVLDRFlow(ParticipantContext participantContext) throws ExagonCommunicationProducerException {
        Event eventFlow = new Event(participantContext.getSagaId(), participantContext.getStatusAddress(), "OK");
        addAllHeaders(eventFlow, participantContext);
        producer.produce(eventFlow);
    }

    private void createPublishADRResult(ParticipantContext participantContext) throws ExagonCommunicationProducerException {
        ADRResult adrResult = new ADRResult();

        Event eventResult = new Event(participantContext.getSagaId(), getSource(ADRResult.class), adrResult);
        addAllHeaders(eventResult, participantContext);
        producer.produce(eventResult);
    }

    private void createPublishADRFlow(ParticipantContext participantContext) throws ExagonCommunicationProducerException {
        Event eventFlow = new Event(participantContext.getSagaId(), participantContext.getStatusAddress(), "OK");
        addAllHeaders(eventFlow, participantContext);
        producer.produce(eventFlow);
    }

    private void addAllHeaders(Event event, ParticipantContext participantContext) {
        event.addHeader(READ_TIMESTAMP, participantContext.getReadTimestamp());
        event.addHeader(WRITE_TIMESTAMP, participantContext.getWriteTimestamp());
        event.addHeader(STATUS_ADDRESS, participantContext.getStatusAddress());
        event.addHeader(STEP_IDENTIFIER, participantContext.getStepIdentifier());
        event.addHeader(QUALIFIER, participantContext.getQualifier());
    }

    private String getSource(Class<?> classZ) {
        ExagonNotification notification = classZ.getAnnotation(ExagonNotification.class);
        ExagonCommand command = classZ.getAnnotation(ExagonCommand.class);
        ExagonRequest request = classZ.getAnnotation(ExagonRequest.class);
        if (notification != null) {
            return Objects.equals(notification.value(), "") ? classZ.getName() : notification.value();
        } else if (command != null) {
            return Objects.equals(command.value(), "") ? classZ.getName() : command.value();
        } else if (request != null) {
            return Objects.equals(request.value(), "") ? classZ.getName() : request.value();
        } else {
            return null;
        }
    }
}
