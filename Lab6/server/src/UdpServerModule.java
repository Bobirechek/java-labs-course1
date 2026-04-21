import common.Command;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class UdpServerModule {
    private final DatagramChannel channel;
    private final Selector selector;
    private SocketAddress lastClientAddress;

    public UdpServerModule(int port) throws Exception {
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false); // Неблокирующий режим
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
    }

    // Модуль чтения: возвращает сразу объект команды
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
                    
                    // Десериализация объекта из байтов
                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
                        return (Command) ois.readObject();
                    }
                }
            }
        }
        return null;
    }

    // Модуль отправки: принимает объект ответа
    public void sendResponse(Serializable response) throws Exception {
        if (lastClientAddress != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(response);
            }
            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
            channel.send(buffer, lastClientAddress);
        }
    }
}