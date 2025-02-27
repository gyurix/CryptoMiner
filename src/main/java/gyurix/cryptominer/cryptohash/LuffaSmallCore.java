// $Id: LuffaSmallCore.java 240 2010-06-21 14:58:28Z tp $

package gyurix.cryptominer.cryptohash;

/**
 * This class implements Luffa-224 and Luffa-256.
 * <p>
 * <pre>
 * ==========================(LICENSE BEGIN)============================
 *
 * Copyright (c) 2007-2010  Projet RNRT SAPHIR
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * ===========================(LICENSE END)=============================
 * </pre>
 *
 * @author Thomas Pornin &lt;thomas.pornin@cryptolog.com&gt;
 * @version $Revision: 240 $
 */

abstract class LuffaSmallCore extends DigestEngine {

    private static final int[] IV = {
            0x6d251e69, 0x44b051e0, 0x4eaa6fb4, 0xdbf78465,
            0x6e292011, 0x90152df4, 0xee058139, 0xdef610bb,
            0xc3b44b95, 0xd9d2f256, 0x70eee9a0, 0xde099fa3,
            0x5d9b0557, 0x8fc944b3, 0xcf1ccf0e, 0x746cd581,
            0xf7efc89d, 0x5dba5781, 0x04016ce5, 0xad659c05,
            0x0306194f, 0x666d1836, 0x24aa230a, 0x8b264ae7
    };

    private static final int[] RC00 = {
            0x303994a6, 0xc0e65299, 0x6cc33a12, 0xdc56983e,
            0x1e00108f, 0x7800423d, 0x8f5b7882, 0x96e1db12
    };

    private static final int[] RC04 = {
            0xe0337818, 0x441ba90d, 0x7f34d442, 0x9389217f,
            0xe5a8bce6, 0x5274baf4, 0x26889ba7, 0x9a226e9d
    };

    private static final int[] RC10 = {
            0xb6de10ed, 0x70f47aae, 0x0707a3d4, 0x1c1e8f51,
            0x707a3d45, 0xaeb28562, 0xbaca1589, 0x40a46f3e
    };

    private static final int[] RC14 = {
            0x01685f3d, 0x05a17cf4, 0xbd09caca, 0xf4272b28,
            0x144ae5cc, 0xfaa7ae2b, 0x2e48f1c1, 0xb923c704
    };

    private static final int[] RC20 = {
            0xfc20d9d2, 0x34552e25, 0x7ad8818f, 0x8438764a,
            0xbb6de032, 0xedb780c8, 0xd9847356, 0xa2c78434
    };

    private static final int[] RC24 = {
            0xe25e72c1, 0xe623bb72, 0x5c58a4a4, 0x1e38e2e7,
            0x78e38b9d, 0x27586719, 0x36eda57f, 0x703aace7
    };

    private int V00, V01, V02, V03, V04, V05, V06, V07;
    private int V10, V11, V12, V13, V14, V15, V16, V17;
    private int V20, V21, V22, V23, V24, V25, V26, V27;
    private byte[] tmpBuf;

    /**
     * Create the object.
     */
    LuffaSmallCore() {
    }

    /**
     * Decode a 32-bit big-endian word from the array {@code buf}
     * at offset {@code off}.
     *
     * @param buf the source buffer
     * @param off the source offset
     * @return the decoded value
     */
    private static final int decodeBEInt(byte[] buf, int off) {
        return ((buf[off] & 0xFF) << 24)
                | ((buf[off + 1] & 0xFF) << 16)
                | ((buf[off + 2] & 0xFF) << 8)
                | (buf[off + 3] & 0xFF);
    }

    /**
     * Encode the 32-bit word {@code val} into the array
     * {@code buf} at offset {@code off}, in big-endian
     * convention (most significant byte first).
     *
     * @param val the value to encode
     * @param buf the destination buffer
     * @param off the destination offset
     */
    private static final void encodeBEInt(int val, byte[] buf, int off) {
        buf[off + 0] = (byte) (val >>> 24);
        buf[off + 1] = (byte) (val >>> 16);
        buf[off + 2] = (byte) (val >>> 8);
        buf[off + 3] = (byte) val;
    }

    /**
     * @see DigestEngine
     */
    protected Digest copyState(LuffaSmallCore dst) {
        dst.V00 = V00;
        dst.V01 = V01;
        dst.V02 = V02;
        dst.V03 = V03;
        dst.V04 = V04;
        dst.V05 = V05;
        dst.V06 = V06;
        dst.V07 = V07;
        dst.V10 = V10;
        dst.V11 = V11;
        dst.V12 = V12;
        dst.V13 = V13;
        dst.V14 = V14;
        dst.V15 = V15;
        dst.V16 = V16;
        dst.V17 = V17;
        dst.V20 = V20;
        dst.V21 = V21;
        dst.V22 = V22;
        dst.V23 = V23;
        dst.V24 = V24;
        dst.V25 = V25;
        dst.V26 = V26;
        dst.V27 = V27;
        return super.copyState(dst);
    }

    /**
     * @see DigestEngine
     */
    protected void doInit() {
        tmpBuf = new byte[32];
        engineReset();
    }

    /**
     * @see DigestEngine
     */
    protected void doPadding(byte[] output, int outputOffset) {
        int ptr = flush();
        tmpBuf[ptr] = (byte) 0x80;
        for (int i = ptr + 1; i < 32; i++)
            tmpBuf[i] = 0x00;
        update(tmpBuf, ptr, 32 - ptr);
        for (int i = 0; i < ptr + 1; i++)
            tmpBuf[i] = 0x00;
        update(tmpBuf, 0, 32);
        encodeBEInt(V00 ^ V10 ^ V20, output, outputOffset + 0);
        encodeBEInt(V01 ^ V11 ^ V21, output, outputOffset + 4);
        encodeBEInt(V02 ^ V12 ^ V22, output, outputOffset + 8);
        encodeBEInt(V03 ^ V13 ^ V23, output, outputOffset + 12);
        encodeBEInt(V04 ^ V14 ^ V24, output, outputOffset + 16);
        encodeBEInt(V05 ^ V15 ^ V25, output, outputOffset + 20);
        encodeBEInt(V06 ^ V16 ^ V26, output, outputOffset + 24);
        if (getDigestLength() == 32)
            encodeBEInt(V07 ^ V17 ^ V27, output, outputOffset + 28);
    }

    /**
     * @see DigestEngine
     */
    protected void engineReset() {
        V00 = IV[0];
        V01 = IV[1];
        V02 = IV[2];
        V03 = IV[3];
        V04 = IV[4];
        V05 = IV[5];
        V06 = IV[6];
        V07 = IV[7];
        V10 = IV[8];
        V11 = IV[9];
        V12 = IV[10];
        V13 = IV[11];
        V14 = IV[12];
        V15 = IV[13];
        V16 = IV[14];
        V17 = IV[15];
        V20 = IV[16];
        V21 = IV[17];
        V22 = IV[18];
        V23 = IV[19];
        V24 = IV[20];
        V25 = IV[21];
        V26 = IV[22];
        V27 = IV[23];
    }

    /**
     * @see DigestEngine
     */
    public int getInternalBlockLength() {
        return 32;
    }

    /**
     * @see DigestEngine
     */
    protected void processBlock(byte[] data) {
        int tmp;
        int a0, a1, a2, a3, a4, a5, a6, a7;
        int M0 = decodeBEInt(data, 0);
        int M1 = decodeBEInt(data, 4);
        int M2 = decodeBEInt(data, 8);
        int M3 = decodeBEInt(data, 12);
        int M4 = decodeBEInt(data, 16);
        int M5 = decodeBEInt(data, 20);
        int M6 = decodeBEInt(data, 24);
        int M7 = decodeBEInt(data, 28);
        a0 = V00 ^ V10;
        a1 = V01 ^ V11;
        a2 = V02 ^ V12;
        a3 = V03 ^ V13;
        a4 = V04 ^ V14;
        a5 = V05 ^ V15;
        a6 = V06 ^ V16;
        a7 = V07 ^ V17;
        a0 = a0 ^ V20;
        a1 = a1 ^ V21;
        a2 = a2 ^ V22;
        a3 = a3 ^ V23;
        a4 = a4 ^ V24;
        a5 = a5 ^ V25;
        a6 = a6 ^ V26;
        a7 = a7 ^ V27;
        tmp = a7;
        a7 = a6;
        a6 = a5;
        a5 = a4;
        a4 = a3 ^ tmp;
        a3 = a2 ^ tmp;
        a2 = a1;
        a1 = a0 ^ tmp;
        a0 = tmp;
        V00 = a0 ^ V00;
        V01 = a1 ^ V01;
        V02 = a2 ^ V02;
        V03 = a3 ^ V03;
        V04 = a4 ^ V04;
        V05 = a5 ^ V05;
        V06 = a6 ^ V06;
        V07 = a7 ^ V07;
        V00 = M0 ^ V00;
        V01 = M1 ^ V01;
        V02 = M2 ^ V02;
        V03 = M3 ^ V03;
        V04 = M4 ^ V04;
        V05 = M5 ^ V05;
        V06 = M6 ^ V06;
        V07 = M7 ^ V07;
        tmp = M7;
        M7 = M6;
        M6 = M5;
        M5 = M4;
        M4 = M3 ^ tmp;
        M3 = M2 ^ tmp;
        M2 = M1;
        M1 = M0 ^ tmp;
        M0 = tmp;
        V10 = a0 ^ V10;
        V11 = a1 ^ V11;
        V12 = a2 ^ V12;
        V13 = a3 ^ V13;
        V14 = a4 ^ V14;
        V15 = a5 ^ V15;
        V16 = a6 ^ V16;
        V17 = a7 ^ V17;
        V10 = M0 ^ V10;
        V11 = M1 ^ V11;
        V12 = M2 ^ V12;
        V13 = M3 ^ V13;
        V14 = M4 ^ V14;
        V15 = M5 ^ V15;
        V16 = M6 ^ V16;
        V17 = M7 ^ V17;
        tmp = M7;
        M7 = M6;
        M6 = M5;
        M5 = M4;
        M4 = M3 ^ tmp;
        M3 = M2 ^ tmp;
        M2 = M1;
        M1 = M0 ^ tmp;
        M0 = tmp;
        V20 = a0 ^ V20;
        V21 = a1 ^ V21;
        V22 = a2 ^ V22;
        V23 = a3 ^ V23;
        V24 = a4 ^ V24;
        V25 = a5 ^ V25;
        V26 = a6 ^ V26;
        V27 = a7 ^ V27;
        V20 = M0 ^ V20;
        V21 = M1 ^ V21;
        V22 = M2 ^ V22;
        V23 = M3 ^ V23;
        V24 = M4 ^ V24;
        V25 = M5 ^ V25;
        V26 = M6 ^ V26;
        V27 = M7 ^ V27;
        V14 = (V14 << 1) | (V14 >>> 31);
        V15 = (V15 << 1) | (V15 >>> 31);
        V16 = (V16 << 1) | (V16 >>> 31);
        V17 = (V17 << 1) | (V17 >>> 31);
        V24 = (V24 << 2) | (V24 >>> 30);
        V25 = (V25 << 2) | (V25 >>> 30);
        V26 = (V26 << 2) | (V26 >>> 30);
        V27 = (V27 << 2) | (V27 >>> 30);
        for (int r = 0; r < 8; r++) {
            tmp = V00;
            V00 |= V01;
            V02 ^= V03;
            V01 = ~V01;
            V00 ^= V03;
            V03 &= tmp;
            V01 ^= V03;
            V03 ^= V02;
            V02 &= V00;
            V00 = ~V00;
            V02 ^= V01;
            V01 |= V03;
            tmp ^= V01;
            V03 ^= V02;
            V02 &= V01;
            V01 ^= V00;
            V00 = tmp;
            tmp = V05;
            V05 |= V06;
            V07 ^= V04;
            V06 = ~V06;
            V05 ^= V04;
            V04 &= tmp;
            V06 ^= V04;
            V04 ^= V07;
            V07 &= V05;
            V05 = ~V05;
            V07 ^= V06;
            V06 |= V04;
            tmp ^= V06;
            V04 ^= V07;
            V07 &= V06;
            V06 ^= V05;
            V05 = tmp;
            V04 ^= V00;
            V00 = ((V00 << 2) | (V00 >>> 30)) ^ V04;
            V04 = ((V04 << 14) | (V04 >>> 18)) ^ V00;
            V00 = ((V00 << 10) | (V00 >>> 22)) ^ V04;
            V04 = (V04 << 1) | (V04 >>> 31);
            V05 ^= V01;
            V01 = ((V01 << 2) | (V01 >>> 30)) ^ V05;
            V05 = ((V05 << 14) | (V05 >>> 18)) ^ V01;
            V01 = ((V01 << 10) | (V01 >>> 22)) ^ V05;
            V05 = (V05 << 1) | (V05 >>> 31);
            V06 ^= V02;
            V02 = ((V02 << 2) | (V02 >>> 30)) ^ V06;
            V06 = ((V06 << 14) | (V06 >>> 18)) ^ V02;
            V02 = ((V02 << 10) | (V02 >>> 22)) ^ V06;
            V06 = (V06 << 1) | (V06 >>> 31);
            V07 ^= V03;
            V03 = ((V03 << 2) | (V03 >>> 30)) ^ V07;
            V07 = ((V07 << 14) | (V07 >>> 18)) ^ V03;
            V03 = ((V03 << 10) | (V03 >>> 22)) ^ V07;
            V07 = (V07 << 1) | (V07 >>> 31);
            V00 ^= RC00[r];
            V04 ^= RC04[r];
        }
        for (int r = 0; r < 8; r++) {
            tmp = V10;
            V10 |= V11;
            V12 ^= V13;
            V11 = ~V11;
            V10 ^= V13;
            V13 &= tmp;
            V11 ^= V13;
            V13 ^= V12;
            V12 &= V10;
            V10 = ~V10;
            V12 ^= V11;
            V11 |= V13;
            tmp ^= V11;
            V13 ^= V12;
            V12 &= V11;
            V11 ^= V10;
            V10 = tmp;
            tmp = V15;
            V15 |= V16;
            V17 ^= V14;
            V16 = ~V16;
            V15 ^= V14;
            V14 &= tmp;
            V16 ^= V14;
            V14 ^= V17;
            V17 &= V15;
            V15 = ~V15;
            V17 ^= V16;
            V16 |= V14;
            tmp ^= V16;
            V14 ^= V17;
            V17 &= V16;
            V16 ^= V15;
            V15 = tmp;
            V14 ^= V10;
            V10 = ((V10 << 2) | (V10 >>> 30)) ^ V14;
            V14 = ((V14 << 14) | (V14 >>> 18)) ^ V10;
            V10 = ((V10 << 10) | (V10 >>> 22)) ^ V14;
            V14 = (V14 << 1) | (V14 >>> 31);
            V15 ^= V11;
            V11 = ((V11 << 2) | (V11 >>> 30)) ^ V15;
            V15 = ((V15 << 14) | (V15 >>> 18)) ^ V11;
            V11 = ((V11 << 10) | (V11 >>> 22)) ^ V15;
            V15 = (V15 << 1) | (V15 >>> 31);
            V16 ^= V12;
            V12 = ((V12 << 2) | (V12 >>> 30)) ^ V16;
            V16 = ((V16 << 14) | (V16 >>> 18)) ^ V12;
            V12 = ((V12 << 10) | (V12 >>> 22)) ^ V16;
            V16 = (V16 << 1) | (V16 >>> 31);
            V17 ^= V13;
            V13 = ((V13 << 2) | (V13 >>> 30)) ^ V17;
            V17 = ((V17 << 14) | (V17 >>> 18)) ^ V13;
            V13 = ((V13 << 10) | (V13 >>> 22)) ^ V17;
            V17 = (V17 << 1) | (V17 >>> 31);
            V10 ^= RC10[r];
            V14 ^= RC14[r];
        }
        for (int r = 0; r < 8; r++) {
            tmp = V20;
            V20 |= V21;
            V22 ^= V23;
            V21 = ~V21;
            V20 ^= V23;
            V23 &= tmp;
            V21 ^= V23;
            V23 ^= V22;
            V22 &= V20;
            V20 = ~V20;
            V22 ^= V21;
            V21 |= V23;
            tmp ^= V21;
            V23 ^= V22;
            V22 &= V21;
            V21 ^= V20;
            V20 = tmp;
            tmp = V25;
            V25 |= V26;
            V27 ^= V24;
            V26 = ~V26;
            V25 ^= V24;
            V24 &= tmp;
            V26 ^= V24;
            V24 ^= V27;
            V27 &= V25;
            V25 = ~V25;
            V27 ^= V26;
            V26 |= V24;
            tmp ^= V26;
            V24 ^= V27;
            V27 &= V26;
            V26 ^= V25;
            V25 = tmp;
            V24 ^= V20;
            V20 = ((V20 << 2) | (V20 >>> 30)) ^ V24;
            V24 = ((V24 << 14) | (V24 >>> 18)) ^ V20;
            V20 = ((V20 << 10) | (V20 >>> 22)) ^ V24;
            V24 = (V24 << 1) | (V24 >>> 31);
            V25 ^= V21;
            V21 = ((V21 << 2) | (V21 >>> 30)) ^ V25;
            V25 = ((V25 << 14) | (V25 >>> 18)) ^ V21;
            V21 = ((V21 << 10) | (V21 >>> 22)) ^ V25;
            V25 = (V25 << 1) | (V25 >>> 31);
            V26 ^= V22;
            V22 = ((V22 << 2) | (V22 >>> 30)) ^ V26;
            V26 = ((V26 << 14) | (V26 >>> 18)) ^ V22;
            V22 = ((V22 << 10) | (V22 >>> 22)) ^ V26;
            V26 = (V26 << 1) | (V26 >>> 31);
            V27 ^= V23;
            V23 = ((V23 << 2) | (V23 >>> 30)) ^ V27;
            V27 = ((V27 << 14) | (V27 >>> 18)) ^ V23;
            V23 = ((V23 << 10) | (V23 >>> 22)) ^ V27;
            V27 = (V27 << 1) | (V27 >>> 31);
            V20 ^= RC20[r];
            V24 ^= RC24[r];
        }
    }

    /**
     * @see Digest
     */
    public int getBlockLength() {
        /*
         * Private communication from Luffa designer Watanabe Dai:
         *
         * << I think that there is no problem to use the same
         *    setting as CubeHash, namely B = 256*ceil(k / 256). >>
         */
        return -32;
    }

    /**
     * @see Digest
     */
    public String toString() {
        return "Luffa-" + (getDigestLength() << 3);
    }
}
