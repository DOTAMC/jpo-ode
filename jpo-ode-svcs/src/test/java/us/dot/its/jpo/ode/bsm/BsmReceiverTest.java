package us.dot.its.jpo.ode.bsm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import us.dot.its.jpo.ode.OdeProperties;
import us.dot.its.jpo.ode.SerializableMessageProducerPool;
import us.dot.its.jpo.ode.plugin.asn1.Asn1Object;
import us.dot.its.jpo.ode.plugin.j2735.J2735Bsm;
import us.dot.its.jpo.ode.plugin.j2735.oss.OssAsn1Coder;
import us.dot.its.jpo.ode.wrapper.MessageProducer;

public class BsmReceiverTest {

	/*
	 * This test mimics end-to-end testing on a single machine by sending a uper
	 * encoded bsm to the local machine.
	 */
	@Test
	@Ignore
	public void endToEndTest() {
		int port = 12321;
		// String odeIp = "2001:4802:7801:102:be76:4eff:fe20:eb5"; // ode
		// instance
		// String odeIp = "162.242.218.130";
		String odeIp = "127.0.0.1";
		int odePort = 46800;

		String uperBsmHex = "401480CA4000000000000000000000000000000000000000000000000000000000000000F800D9EFFFB7FFF00000000000000000000000000000000000000000000000000000001FE07000000000000000000000000000000000001FF0";

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
			System.out.println("OBU - Started socket with port " + port);
		} catch (SocketException e) {
			System.out.println("OBU - Error creating socket with port " + port);
			e.printStackTrace();
		}

		byte[] uperBsmByte = null;
		try {
			uperBsmByte = Hex.decodeHex(uperBsmHex.toCharArray());
		} catch (DecoderException e) {
			System.out.println("OBU - Error decoding hex string into bytes");
			e.printStackTrace();
		}

		DatagramPacket reqPacket = new DatagramPacket(uperBsmByte, uperBsmByte.length,
				new InetSocketAddress(odeIp, odePort));
		System.out.println("OBU - Printing uperBsm in hex: \n" + uperBsmHex);
		System.out.println("\nOBU - Sending uperBsm to ODE - Ip: " + odeIp + " Port: " + odePort);
		try {
			socket.send(reqPacket);
		} catch (IOException e) {
			System.out.println("OBU - Error Sending uperBsm to ODE");
			e.printStackTrace();
		}

		if (socket != null) {
			socket.close();
			System.out.println("OBU - Closed socket with port " + port);
		}
	}

	@Injectable
	OdeProperties mockOdeProperties;

	@Mocked
	OssAsn1Coder mockedOssAsn1Coder;

	@Mocked
	DatagramSocket mockedDatagramSocket;

	@SuppressWarnings("rawtypes")
	@Mocked
	SerializableMessageProducerPool mockedSMPP;

	@SuppressWarnings("rawtypes")
	@Mocked
	MessageProducer mockedMessageProducer;

	@Test
	public void testConstructor() throws SocketException {

		new Expectations() {
			{
				new OssAsn1Coder();

				mockOdeProperties.getBsmReceiverPort();
				result = 1234;

				new DatagramSocket(1234);

				new SerializableMessageProducerPool<>(mockOdeProperties);

				MessageProducer.defaultStringMessageProducer(anyString, anyString);
			}
		};
		new BsmReceiver(mockOdeProperties);
	}

	@Test
	public void testConstructorException()
			throws SocketException {

		new Expectations() {
			{
				new OssAsn1Coder();
				result = mockedOssAsn1Coder;

				mockOdeProperties.getBsmReceiverPort();
				result = 1234;

				new DatagramSocket(1234);
				result = new SocketException();
			}
		};
		new BsmReceiver(mockOdeProperties);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRun(@Mocked DatagramPacket mockedDatagramPacket, @Mocked J2735Bsm mockedJ2735Bsm) throws IOException {
		byte[] msg = { 0x00, 0x00 };
		new Expectations() {
			{
				new OssAsn1Coder();
				result = mockedOssAsn1Coder;

				mockOdeProperties.getBsmReceiverPort();
				result = 1234;

				new DatagramSocket(1234);

				new SerializableMessageProducerPool<>(mockOdeProperties);

				MessageProducer.defaultStringMessageProducer(anyString, anyString);
				new DatagramPacket((byte[])any, anyInt);
				result = mockedDatagramPacket;
				
				mockedDatagramPacket.getLength();
				result = 2;
				
				mockedOssAsn1Coder.decodeUPERBsmBytes(msg);
				result = mockedJ2735Bsm;
				mockedMessageProducer.send(anyString, null, anyString);
				mockedMessageProducer.send(anyString, null, (Byte[]) any);
			}
		};
		BsmReceiver bsmReceiver = new BsmReceiver(mockOdeProperties);
		bsmReceiver.setStopped(true);
		bsmReceiver.run();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRunException(@Mocked DatagramPacket mockedDatagramPacket, @Mocked J2735Bsm mockedJ2735Bsm) throws IOException {
		new Expectations() {
			{
				new OssAsn1Coder();
				result = mockedOssAsn1Coder;

				mockOdeProperties.getBsmReceiverPort();
				result = 1234;

				new DatagramSocket(1234);
				result = mockedDatagramSocket;

				new SerializableMessageProducerPool<>(mockOdeProperties);

				MessageProducer.defaultStringMessageProducer(anyString, anyString);
				new DatagramPacket((byte[])any, anyInt);
				result = mockedDatagramPacket;
				
				mockedDatagramSocket.receive(mockedDatagramPacket);
				result = new SocketException();
			}
		};
		BsmReceiver bsmReceiver = new BsmReceiver(mockOdeProperties);
		bsmReceiver.setStopped(true);
		bsmReceiver.run();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDecodeData(@Mocked J2735Bsm mockedJ2735Bsm) throws SocketException {
		byte[] msg = { 0x01, 0x02 };
		new Expectations() {
			{
				mockedOssAsn1Coder.decodeUPERBsmBytes(msg);
				result = mockedJ2735Bsm;
				mockedMessageProducer.send(anyString, null, anyString);
				mockedMessageProducer.send(anyString, null, (Byte[]) any);
			}
		};
		BsmReceiver bsmReceiver = new BsmReceiver(mockOdeProperties);
		bsmReceiver.decodeData(msg);
	}

	@Test
	public void testDecodeDataError(@Mocked J2735Bsm mockedJ2735Bsm) throws SocketException {
		byte[] msg = { 0x01, 0x02 };
		new Expectations() {
			{
				mockedOssAsn1Coder.decodeUPERBsmBytes(msg);
				result = new Asn1Object();
			}
		};
		BsmReceiver bsmReceiver = new BsmReceiver(mockOdeProperties);
		bsmReceiver.decodeData(msg);
	}
}