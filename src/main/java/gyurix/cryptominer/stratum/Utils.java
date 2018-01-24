package gyurix.cryptominer.stratum;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

public class Utils {
    @Getter
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private static byte byteToHex(byte data) {
        return (byte) (data > 9 ? data - 10 + 'a' : '0' + data);
    }

    private static byte[] byteToHex(byte[] data) {
        int len = data.length;
        byte[] buf = new byte[len * 2];
        for (int i = 0; i < len; i++) {
            buf[i * 2] = byteToHex((byte) (data[i] >> 4));
            buf[i * 2 + 1] = byteToHex((byte) (data[i] & 15));
        }
        return buf;
    }

    private static byte[] hexToByte(byte[] data) {
        int blen = data.length / 2;
        byte[] buf = new byte[blen];
        for (int i = 0; i < blen; i++)
            buf[i] = (byte) ((hexToByte(data[i * 2]) << 4) + hexToByte(data[i * 2 + 1]));
        return buf;
    }

    private static byte hexToByte(byte c) {
        return (byte) (c >= 'a' ? c - 'a' + 10 : c - '0');
    }
}
