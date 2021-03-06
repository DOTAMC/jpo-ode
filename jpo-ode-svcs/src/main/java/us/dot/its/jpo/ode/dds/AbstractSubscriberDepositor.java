package us.dot.its.jpo.ode.dds;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ode.OdeProperties;
import us.dot.its.jpo.ode.j2735.semi.SemiDialogID;
import us.dot.its.jpo.ode.wrapper.MessageConsumer;
import us.dot.its.jpo.ode.wrapper.MessageProcessor;

public abstract class AbstractSubscriberDepositor<K, V> extends MessageProcessor<K, V> {
    
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected OdeProperties odeProperties;
    protected int depositorPort;
	protected DatagramSocket socket = null;
	protected TrustManager trustMgr;
	protected SemiDialogID dialogId;

	public AbstractSubscriberDepositor(OdeProperties odeProps, int port, SemiDialogID dialogId) {
		this.odeProperties = odeProps;
		this.depositorPort = port;
		this.dialogId = dialogId;
		
		try {
            logger.debug("Creating depositor Socket with port {}", port);
			socket = new DatagramSocket(port);
			trustMgr = new TrustManager(odeProps, socket);
		} catch (SocketException e) {
			logger.error("Error creating socket with port " + port, e);
		}
	}

    public void subscribe(MessageConsumer<K, V> consumer, String... topics) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                consumer.subscribe(topics);
            }
        });
    }

    @Override
    public Object call() throws Exception {
        byte[] encodedMsg = null;
        
        if (!trustMgr.isTrustEstablished()) {
            trustMgr.establishTrust(
                    depositorPort,
                    odeProperties.getSdcIp(), 
                    odeProperties.getSdcPort(),
                    dialogId);
        }

        encodedMsg = deposit();

        return encodedMsg;
    }

    
    public int getDepositorPort() {
        return depositorPort;
    }

    public void setDepositorPort(int depositorPort) {
        this.depositorPort = depositorPort;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public SemiDialogID getDialogId() {
        return dialogId;
    }

    public void setDialogId(SemiDialogID dialogId) {
        this.dialogId = dialogId;
    }

    protected abstract byte[] deposit();
    
}
