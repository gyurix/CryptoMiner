package gyurix.cryptominer.stratum;

import gyurix.cryptominer.cryptohash.Keccak256;

public class Miner {
    public Keccak256 keccak = new Keccak256();

    public static void main(String[] args) {
        Miner miner = new Miner();
        System.out.println(new String(miner.mine("elek")));

    }

    public byte[] doKeccak(byte[] data) {
        keccak.reset();
        keccak.update(data);
        return keccak.digest();
    }

    public byte[] mine(String hash) {
        //byte[] out=hash.getBytes();
        //out= doKeccak(out);
        System.out.println(new String(hexToByte("656c656b".getBytes())));

        return new byte[0];
    }
}
