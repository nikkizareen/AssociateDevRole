package select.users;

public class Data {
    private int a;
    public Data(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }

    @Override
    public String toString() {
        return "Data{" +
                "a=" + a +
                '}';
    }
}
