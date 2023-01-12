public class Confirmare {
    boolean acceptat;

    public Confirmare(boolean acceptat) {
        this.acceptat = acceptat;
    }

    public Confirmare(String string) {
        fromString(string);
    }

    @Override
    public String toString() {
        return "[Confirmare]{" +
                "acceptat=" + acceptat +
                '}';
    }

    private String get_param(String s) {
        int i = s.indexOf("=");
        return s.substring(i + 1);
    }

    public void fromString(String s) {
        s = s.substring(s.indexOf("{") + 1, s.indexOf("}"));
        String[] parts = s.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = get_param(parts[i]);
        }
        this.acceptat = Boolean.parseBoolean(parts[0]);
    }

    public boolean getAcceptat() {
        return acceptat;
    }
}
