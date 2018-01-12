package gyurix.cryptominer.stratum;

public class Jsonable {
    @Override
    public String toString() {
        return Utils.getGson().toJson(this);
    }
}
