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

import jp.nyatla.minya.HexArray;
import jp.nyatla.minya.MiningWork;
import jp.nyatla.minya.MinyaException;
import jp.nyatla.minya.StratumMiningWork;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class StratumWorkBuilder {
    private HexArray _coinbase;
    private double _difficulty = Double.NEGATIVE_INFINITY;
    private StratumJsonResultLogin _login = null;
    private HexArray _merkle_loot;
    private HexArray _xnonce2;

    public StratumWorkBuilder(StratumJsonResultLogin login) {
        this._login = login;
    }

    private static HexArray diff2target(double diff) {
        long m;
        int k;
        byte[] target = new byte[8 * 4];
        for (k = 6; k > 0 && diff > 1.0; k--) {
            diff /= 4294967296.0;
        }
        m = (long) (4294901760.0 / diff);
        if (m == 0 && k == 6) {
            Arrays.fill(target, (byte) 0xff);
        } else {
            Arrays.fill(target, (byte) 0);
            for (int i = 0; i < 8; i++) {
                target[k * 4 + i] = (byte) ((m >> (i * 8)) & 0xff);
            }
        }
        return new HexArray(target);
    }

    /**
     * MiningWorkを生成します。
     *
     * @return 生成できない場合はNULLです。
     * @throws MinyaException
     */
    public MiningWork buildMiningWork() {
        if (this._notify == null || this._login == null || this._difficulty < 0) {
            return null;
        }
        //Increment extranonce2
        HexArray xnonce2 = this._xnonce2;
        String xnonce2_str = xnonce2.getStr();
        for (int i = 0; i < xnonce2.getLength() && (0 == (++xnonce2.refHex()[i])); i++) ;

        //Assemble block header
        HexArray work_data = new HexArray(this._notify.version);
        work_data.append(this._notify.prev_hash);
        work_data.append(this._merkle_loot, 0, 32);
        work_data.append(this._notify.ntime);
        work_data.append(this._notify.nbit);
        work_data.append(new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, (byte) 0x80});
        work_data.append(new byte[40]);
        work_data.append(new byte[]{
                (byte) 0x80, 0x02, 0x00, 0x00});
        return new StratumMiningWork(work_data, diff2target(this._difficulty / 65536.0), this._notify.job_id, xnonce2_str);

    }

    public HexArray refXnonce2() {
        return this._xnonce2;
    }

    public void setDiff(StratumJsonMethodSetDifficulty dif) {
        this._difficulty = dif.difficulty;
    }

    private byte[] sha256d(byte[] i_s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(md.digest(i_s));
    }


}