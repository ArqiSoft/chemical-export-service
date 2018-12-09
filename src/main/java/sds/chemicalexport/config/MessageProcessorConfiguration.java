package sds.chemicalexport.config;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.npspot.jtransitlight.JTransitLightException;
import com.npspot.jtransitlight.consumer.ReceiverBusControl;
import com.npspot.jtransitlight.consumer.setting.ConsumerSettings;
import com.npspot.jtransitlight.publisher.IBusControl;
import sds.chemicalexport.domain.commands.ExportFile;
import sds.chemicalexport.commandhandlers.ExportFileCommandMessageCallback;
import sds.messaging.callback.AbstractMessageProcessor;

@Component
public class MessageProcessorConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorConfiguration.class);

    
    @Autowired
    public MessageProcessorConfiguration(IBusControl busControl, 
            ReceiverBusControl receiver, 
            AbstractMessageProcessor<ExportFile> exportFileProcessor,
            BlockingQueue<ExportFile> exportFileQueue,
            @Value("${exportFileQueueName}") String exportFileQueueName,
            @Value("${EXECUTORS_THREAD_COUNT:5}") Integer threadCount) 
                    throws JTransitLightException, IOException, InterruptedException {
        
        receiver.subscribe(new ExportFile().getQueueName(), exportFileQueueName, 
                ConsumerSettings.newBuilder().withDurable(true).build(), 
                new ExportFileCommandMessageCallback(ExportFile.class, exportFileQueue));
        
        LOGGER.debug("EXECUTORS_THREAD_COUNT is set to {}", threadCount);
        
        Executors.newSingleThreadExecutor().submit(() -> {
            final ExecutorService threadPool = 
                    Executors.newFixedThreadPool(threadCount);
            
            while (true) {
                // wait for message
                final ExportFile message = exportFileQueue.take();
                
                // submit to processing pool
                threadPool.submit(() -> exportFileProcessor.doProcess(message));
                Thread.sleep(10);
            }
        });
                
    }
}
