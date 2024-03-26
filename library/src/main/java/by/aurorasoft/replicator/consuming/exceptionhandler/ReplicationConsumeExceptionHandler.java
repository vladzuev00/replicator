package by.aurorasoft.replicator.consuming.exceptionhandler;

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;

//TODO: refactor, possible receive ReplicationConsumeException
@Component
public final class ReplicationConsumeExceptionHandler implements StreamsUncaughtExceptionHandler {

    public ReplicationConsumeExceptionHandler(StreamsBuilderFactoryBean streamBuilderFactoryBean) {
        streamBuilderFactoryBean.setStreamsUncaughtExceptionHandler(this);
    }

    @Override
    public StreamThreadExceptionResponse handle(final Throwable exception) {
        System.out.println("Uncaught exception: " + exception);
        return REPLACE_THREAD;
    }
}
