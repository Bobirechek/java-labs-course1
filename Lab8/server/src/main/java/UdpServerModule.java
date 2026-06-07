import common.Command;
import common.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;


public class UdpServerModule {
    private static final Logger logger = LogManager.getLogger(UdpServerModule.class);

    private final DatagramChannel channel;
    private final Selector selector;

    public UdpServerModule(int port) throws Exception {
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        logger.info("UDP server listening on port {}", port);
    }

    public static class RawPacket {
        public final byte[] data;
        public final SocketAddress clientAddress;

        public RawPacket(byte[] data, SocketAddress clientAddress) {
            this.data = data;
            this.clientAddress = clientAddress;
        }
    }

    public RawPacket receiveRaw() throws Exception {
        if (selector.select(10) == 0) return null;

        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();

            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(65536);
                SocketAddress clientAddress = channel.receive(buffer);

                if (clientAddress != null) {
                    logger.info("Received raw packet from {}", clientAddress);
                    byte[] data = new byte[buffer.position()];
                    buffer.flip();
                    buffer.get(data);
                    return new RawPacket(data, clientAddress);
                }
            }
        }
        return null;
    }

    public static Command deserializeCommand(byte[] data) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Command) ois.readObject();
        }
    }


    public synchronized void sendResponse(Response response, SocketAddress clientAddress) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
        }
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        channel.send(buffer, clientAddress);
        logger.info("Response sent to {}", clientAddress);
    }
}