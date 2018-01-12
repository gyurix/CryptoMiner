/********************************************************************************
 * MiNya pjeject
 * Copyright 2014 nyatla.jp
 * https://github.com/nyatla/JMiNya
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 ********************************************************************************/

package jp.nyatla.minya.stratum;

import jp.nyatla.minya.MinyaException;
import jp.nyatla.minya.MinyaLog;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * StratumJsonを送受信するソケットクラス。
 * 送信電文の整形と、受信電文の解釈を隠ぺいします。
 */
public class StratumSocket extends Socket {
    private int _id;
    private BufferedReader _rx;
    private BufferedWriter _tx;

    protected StratumSocket() {
    }

    public StratumSocket(URI i_url) throws UnknownHostException, IOException {
        super(i_url.getHost(), i_url.getPort());
        this._tx = new LoggingWriter(new OutputStreamWriter(this.getOutputStream()));
        this._rx = new LoggingReader(new InputStreamReader(this.getInputStream()));
        this._id = 1;
    }

    public static void main(String[] args) {
        //JsonParseTest
        ObjectMapper mapper = new ObjectMapper();
        try {
            StratumJson s1 = new StratumJsonMethodGetVersion(mapper.readTree(StratumJsonMethodGetVersion.TEST_PATT));
            StratumJson s2 = new StratumJsonMethodMiningNotify(mapper.readTree(StratumJsonMethodMiningNotify.TEST_PATT));
            StratumJson s3 = new StratumJsonMethodReconnect(mapper.readTree(StratumJsonMethodReconnect.TEST_PATT));
            StratumJson s4 = new StratumJsonMethodSetDifficulty(mapper.readTree(StratumJsonMethodSetDifficulty.TEST_PATT));
            StratumJson s5 = new StratumJsonMethodShowMessage(mapper.readTree(StratumJsonMethodShowMessage.TEST_PATT));
            StratumJson s6 = new StratumJsonResultStandard(mapper.readTree(StratumJsonResultStandard.TEST_PATT));
            StratumJson s7 = new StratumJsonResultSubscribe(mapper.readTree(StratumJsonResultSubscribe.TEST_PATT));
            return;
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MinyaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public long login(String user, String password) throws IOException {
        long id;
        synchronized (this) {
            id = this._id;
            this._id++;
        }
        this._tx.write("{\"id\": " + id + ", \"method\": \"login\", \"params\": {\"login\":\"" + user + "\", \"pass\": \"" + password + "\"]}\n");
        this._tx.flush();
        return id;
    }

    public StratumJson recvStratumJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jn = mapper.readTree(this._rx.readLine());
        //parse method
        try {
            return new StratumJsonMethodGetVersion(jn);
        } catch (MinyaException e) {
        }
        try {
            return new StratumJsonMethodMiningNotify(jn);
        } catch (MinyaException e) {
        }
        try {
            return new StratumJsonMethodReconnect(jn);
        } catch (MinyaException e) {
        }
        try {
            return new StratumJsonMethodSetDifficulty(jn);
        } catch (MinyaException e) {
        }
        try {
            return new StratumJsonMethodShowMessage(jn);
        } catch (MinyaException e) {
        }
        //parse result(複雑なものから順にね！)
        try {
            return new StratumJsonResultSubscribe(jn);
        } catch (MinyaException e) {
        }
        try {
            return new StratumJsonResultStandard(jn);
        } catch (MinyaException e) {
        }
        return null;
    }

    public long submit(String userId, String jobId, String nonce, String result) throws IOException {
        long id;
        synchronized (this) {
            id = this._id;
            this._id++;
        }
        String s = "{\"id\": " + id + ", \"method\": \"submit\", \"params\": {" +
                "\"id\": " + userId + "\"," +
                "\"job_id\": \"" + jobId + "\"," +
                "\"nonce\": \"" + nonce + "\",\"" +
                "\"result\": \"" + result + "\"}\n";
        this._tx.write(s);
        this._tx.flush();
        return id;
    }

    private class LoggingReader extends BufferedReader {
        public LoggingReader(Reader arg0) {
            super(arg0);
        }

        public String readLine() throws IOException {
            String s = super.readLine();
            MinyaLog.debug("RX<" + s);
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
            MinyaLog.debug("TX>" + str);
        }
    }
}
