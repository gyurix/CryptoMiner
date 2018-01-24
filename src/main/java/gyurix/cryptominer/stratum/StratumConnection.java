package gyurix.cryptominer.stratum;

import gyurix.cryptominer.stratum.inpackets.PacketIn;
import gyurix.cryptominer.stratum.inpackets.PacketInLoginResponse;
import gyurix.cryptominer.stratum.inpackets.data.LoginData;
import gyurix.cryptominer.stratum.outpackets.PacketOut;
import gyurix.cryptominer.stratum.outpackets.PacketOutLogin;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.net.URI;

public class StratumConnection extends Socket {
    @Getter
    private URI url;
    @Getter
    private String user, password;

    private LoggingWriter out;
    private LoggingReader in;


    public StratumConnection(URI url, String user, String password) throws Throwable {
        super(url.getHost(), url.getPort());
        this.out = new LoggingWriter(new OutputStreamWriter(this.getOutputStream()));
        this.in = new LoggingReader(new InputStreamReader(this.getInputStream()));
        this.url = url;
        this.user = user;
        this.password = password;
        sendPacket(new PacketOutLogin(new LoginData(user, password, "CryptoMiner")));
        PacketInLoginResponse job = receivePacket(PacketInLoginResponse.class);
        System.out.println(job);
    }

    @SneakyThrows
    public <T extends PacketIn> T receivePacket(Class<T> cl) {
        String s = in.readLine();
        return Utils.getGson().fromJson(s, cl);
    }

    @SneakyThrows
    public void sendPacket(PacketOut packet) {
        out.write(packet.toString() + '\n');
        out.flush();
    }

    private class LoggingReader extends BufferedReader {
        public LoggingReader(Reader reader) {
            super(reader);
        }

        public String readLine() throws IOException {
            String s = super.readLine();
            System.out.println("IN << " + s);
            return s;
        }
    }

    private class LoggingWriter extends BufferedWriter {
        public LoggingWriter(Writer arg0) {
            super(arg0);
        }

        @Override
        public void write(String str) throws IOException {
            super.write(str);
            System.out.println("OUT >> " + str);
        }
    }
}
