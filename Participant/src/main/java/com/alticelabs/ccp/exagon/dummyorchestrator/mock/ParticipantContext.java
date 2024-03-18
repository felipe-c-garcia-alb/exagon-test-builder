package com.alticelabs.ccp.exagon.dummyorchestrator.mock;

public class ParticipantContext {

    private final String sagaId;
    private final String statusAddress;
    private final String stepIdentifier;
    private final String tsRead;
    private final String tsWrite;



    private final String tsReference;

    private final String qualifier;

    private ParticipantContext(Builder builder) {
        sagaId = builder.sagaId;
        statusAddress = builder.statusAddress;
        stepIdentifier = builder.stepIdentifier;
        tsRead = builder.tsRead;
        tsWrite = builder.tsWrite;
        qualifier = builder.qualifier;
        tsReference = builder.tsReference;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ParticipantContext copy) {
        Builder builder = new Builder();
        builder.sagaId = copy.getSagaId();
        builder.statusAddress = copy.getStatusAddress();
        builder.stepIdentifier = copy.getStepIdentifier();
        builder.tsRead = copy.getTsRead();
        builder.tsReference = copy.getTsReference();
        builder.tsWrite = copy.getTsWrite();
        builder.qualifier = copy.getQualifier();
        return builder;
    }

    public String getSagaId() {
        return sagaId;
    }

    public String getStatusAddress() {
        return statusAddress;
    }

    public String getStepIdentifier() {
        return stepIdentifier;
    }

    public String getTsRead() {
        return tsRead;
    }

    public String getTsWrite() {
        return tsWrite;
    }
    public String getQualifier() {
        return qualifier;
    }
    public String getTsReference() {
        return tsReference;
    }


    /**
     * {@code ParticipantContext} builder static inner class.
     */
    public static final class Builder {
        private String sagaId;
        private String statusAddress;
        private String stepIdentifier;
        private String tsRead;
        private String tsReference;
        private String tsWrite;

        private String qualifier;

        private Builder() {
        }

        /**
         * Sets the {@code sagaId} and returns a reference to this Builder enabling method chaining.
         *
         * @param sagaId the {@code sagaId} to set
         * @return a reference to this Builder
         */
        public Builder sagaId(String sagaId) {
            this.sagaId = sagaId;
            return this;
        }

        /**
         * Sets the {@code statusAddress} and returns a reference to this Builder enabling method chaining.
         *
         * @param statusAddress the {@code statusAddress} to set
         * @return a reference to this Builder
         */
        public Builder statusAddress(String statusAddress) {
            this.statusAddress = statusAddress;
            return this;
        }

        /**
         * Sets the {@code stepIdentifier} and returns a reference to this Builder enabling method chaining.
         *
         * @param stepIdentifier the {@code stepIdentifier} to set
         * @return a reference to this Builder
         */
        public Builder stepIdentifier(String stepIdentifier) {
            this.stepIdentifier = stepIdentifier;
            return this;
        }

        /**
         * Sets the {@code readTimestamp} and returns a reference to this Builder enabling method chaining.
         *
         * @param readTimestamp the {@code readTimestamp} to set
         * @return a reference to this Builder
         */
        public Builder tsRead(String readTimestamp) {
            this.tsRead = readTimestamp;
            return this;
        }

        /**
         * Sets the {@code writeTimestamp} and returns a reference to this Builder enabling method chaining.
         *
         * @param writeTimestamp the {@code writeTimestamp} to set
         * @return a reference to this Builder
         */
        public Builder tsWrite(String writeTimestamp) {
            this.tsWrite = writeTimestamp;
            return this;
        }
        public Builder tsReference(String tsReference) {
            this.tsReference = tsReference;
            return this;
        }

        public Builder qualifier(String qualifier) {
            this.qualifier = qualifier;
            return this;
        }

        /**
         * Returns a {@code ParticipantContext} built from the parameters previously set.
         *
         * @return a {@code ParticipantContext} built with parameters of this {@code ParticipantContext.Builder}
         */
        public ParticipantContext build() {
            return new ParticipantContext(this);
        }
    }
}
