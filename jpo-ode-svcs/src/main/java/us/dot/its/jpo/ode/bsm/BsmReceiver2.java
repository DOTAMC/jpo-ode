package us.dot.its.jpo.ode.bsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.oss.asn1.AbstractData;

import us.dot.its.jpo.ode.OdeProperties;
import us.dot.its.jpo.ode.j2735.dsrc.BasicSafetyMessage;
import us.dot.its.jpo.ode.plugin.j2735.J2735Bsm;
import us.dot.its.jpo.ode.plugin.j2735.oss.OssBsm;
import us.dot.its.jpo.ode.plugin.j2735.oss.OssBsmPart2Content.OssBsmPart2Exception;
import us.dot.its.jpo.ode.udp.AbstractUdpReceiverPublisher;
import us.dot.its.jpo.ode.util.JsonUtils;
import us.dot.its.jpo.ode.util.SerializationUtils;
import us.dot.its.jpo.ode.wrapper.MessageProducer;

public class BsmReceiver2 extends AbstractUdpReceiverPublisher {

	private static Logger logger = LoggerFactory.getLogger(BsmReceiver2.class);

	private OdeProperties odeProperties;

    private MessageProducer<String, byte[]> byteArrayProducer;
    private MessageProducer<String, String> stringProducer;

	@Autowired
	public BsmReceiver2(OdeProperties odeProps) {
	    super(odeProps.getBsmReceiverPort(), odeProps.getBsmBufferSize());
		this.odeProperties = odeProps;

        // Create a String producer for hex BSMs
        stringProducer = MessageProducer.defaultStringMessageProducer(odeProperties.getKafkaBrokers(),
                odeProperties.getKafkaProducerType());

        // Create a ByteArray producer for UPER BSMs and VSDs
        byteArrayProducer = MessageProducer.defaultByteArrayMessageProducer(odeProperties.getKafkaBrokers(),
                odeProperties.getKafkaProducerType());
	}

    @Override
    protected void publish(AbstractData data) {
        try {
            if (data instanceof BasicSafetyMessage) {
                logger.debug("Received BSM");
                J2735Bsm genericBsm = OssBsm.genericBsm((BasicSafetyMessage) data);
                
                logger.debug("Publishing BSM to topic {}", 
                        odeProperties.getKafkaTopicBsmSerializedPojo());
                byteArrayProducer.send(odeProperties.getKafkaTopicBsmSerializedPojo(), null,
                        new SerializationUtils<J2735Bsm>().serialize((J2735Bsm) genericBsm));

                String bsmJson = JsonUtils.toJson(genericBsm, odeProperties.getVerboseJson());
                stringProducer.send(odeProperties.getKafkaTopicBsmRawJson(), null, bsmJson);
                logger.debug("Published bsm to the topics {} and {}",
                        odeProperties.getKafkaTopicBsmSerializedPojo(), 
                        odeProperties.getKafkaTopicBsmRawJson());
            } else {
                logger.error("Unknown message type received {}", data.getClass().getName());
            }
        } catch (OssBsmPart2Exception e) {
            logger.error("Unable to convert BSM", e);
        }
    }
}