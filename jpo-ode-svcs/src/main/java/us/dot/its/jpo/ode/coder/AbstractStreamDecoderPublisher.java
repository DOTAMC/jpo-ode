package us.dot.its.jpo.ode.coder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ode.OdeProperties;
import us.dot.its.jpo.ode.SerializableMessageProducerPool;
import us.dot.its.jpo.ode.eventlog.EventLogger;
import us.dot.its.jpo.ode.plugin.PluginFactory;
import us.dot.its.jpo.ode.plugin.asn1.Asn1Object;
import us.dot.its.jpo.ode.plugin.asn1.Asn1Plugin;
import us.dot.its.jpo.ode.wrapper.MessageProducer;

public abstract class AbstractStreamDecoderPublisher implements StreamDecoderPublisher {

    protected static Logger logger = LoggerFactory.getLogger(AbstractStreamDecoderPublisher.class);

    protected OdeProperties odeProperties;
    protected Asn1Plugin asn1Coder;
    protected MessageProducer<String, String> defaultProducer;
    protected SerializableMessageProducerPool<String, byte[]> messageProducerPool;

    protected AbstractStreamDecoderPublisher() {
        super();
    }

    protected AbstractStreamDecoderPublisher(OdeProperties properties) {
        super();
        this.odeProperties = properties;
        if (this.asn1Coder == null) {
            logger.info("Loading ASN1 Coder: {}", this.odeProperties.getAsn1CoderClassName());
            try {
                this.asn1Coder = (Asn1Plugin) PluginFactory.getPluginByName(properties.getAsn1CoderClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("Unable to load plugin: " + properties.getAsn1CoderClassName(), e);
            }
        }

        defaultProducer = MessageProducer.defaultStringMessageProducer(odeProperties.getKafkaBrokers(),
                odeProperties.getKafkaProducerType());
        messageProducerPool = new SerializableMessageProducerPool<>(odeProperties);
    }

    @Override
    public void decodeHexAndPublish(InputStream is) throws IOException {
        String line = null;
        Asn1Object decoded = null;

        try (Scanner scanner = new Scanner(is)) {

            boolean empty = true;
            while (scanner.hasNextLine()) {
                empty = false;
                line = scanner.nextLine();

                decoded = decode(line);
                publish(decoded);
                publish(decoded.toJson(odeProperties.getVerboseJson()));
            }
            if (empty) {
                EventLogger.logger.info("Empty file received");
                throw new IOException("Empty file received");
            }
        } catch (IOException e) {
            EventLogger.logger.info("Error occurred while decoding message: {}", line);
            throw new IOException("Error decoding data: " + line, e);
        }
    }

    @Override
    public void decodeBinaryAndPublish(InputStream is) throws IOException {
        Asn1Object decoded;

        try {
            do {
                decoded = decode(is);
                if (decoded != null) {
                    logger.debug("Decoded: {}", decoded);
                    publish(decoded);
                    publish(decoded.toJson(odeProperties.getVerboseJson()));
                }
            } while (decoded != null);

        } catch (Exception e) {
            throw new IOException("Error decoding data." + e);
        }
    }

    public void setAsn1Plugin(Asn1Plugin asn1Plugin) {
        this.asn1Coder = asn1Plugin;
    }

    public void setMessageProducerPool(SerializableMessageProducerPool<String, byte[]> messageProducerPool) {
        this.messageProducerPool = messageProducerPool;
    }
}
