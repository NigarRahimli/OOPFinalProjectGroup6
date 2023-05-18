import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
    import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public static void main(String args[]) throws IOException{
        int portNumber = 1234;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            Thread serverThread = new Thread(() -> {
                try {
                    Socket clientSocket = serverSocket.accept();
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String message = new String(buffer, 0, bytesRead);
                    outputStream.write("Hello from the server!".getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            serverThread.start();
        }
        EventQueue.invokeLater(() -> {
            ClientGUI frame = new ClientGUI();
            frame.setVisible(true);
        });
    }
}
