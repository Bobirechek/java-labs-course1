import common.Command;

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
    private static final Logger logger =
            LogManager.getLogger(UdpServerModule.class);

    private final DatagramChannel channel;
    private final Selector selector;
    private SocketAddress lastClientAddress;

    public UdpServerModule(int port) throws Exception {
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false); // Неблокирующий режим
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);

        logger.info("The UDP server listens on port {}", port);
    }

    public Command readCommand() throws Exception {
        if (selector.select(10) == 0) return null;

        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();

            if (key.isReadable()) {
                ByteBuffer buffer = ByteBuffer.allocate(65536);
                SocketAddress clientAddress = channel.receive(buffer);
                if (clientAddress != null) {
                    this.lastClientAddress = clientAddress;

                    logger.info("New connection: {}", clientAddress);
                    
                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
                        logger.info("A request was received from {}", clientAddress);
                        
                        return (Command) ois.readObject();
                    }
                }
            }
        }
        return null;
    }

    public void sendResponse(Serializable response) throws Exception {
        if (lastClientAddress != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(response);
            }
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            channel.send(buffer, lastClientAddress);

            logger.info("The response has been sent to the client {}", lastClientAddress);
        }
    }
}