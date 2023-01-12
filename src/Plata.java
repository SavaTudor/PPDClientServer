public class Plata {

    String data;
    String cnp;
    int suma;

    public Plata(String string) {
        if(string.charAt(0) == '(') fromFileString(string);
        else fromString(string);
    }

    public Plata(String data, String cnp, int suma) {
        this.data = data;
        this.cnp = cnp;
        this.suma = suma;
    }

    @Override
    public String toString() {
        return "[Plata]{" +
                "data='" + data + '\'' +
                ", cnp=" + cnp +
                ", suma=" + suma +
                '}';
    }

    public String toFileString(){
        // just make a string with all the fields separated by commas and return it
        return "(" + data + "," + cnp + "," + suma + ")\n";
    }

    public void fromFileString(String s){
        // remove the first and last character (the parentheses)
        s = s.substring(1, s.length()-2);
        // split the string by commas
        String[] parts = s.split(",");
        // assign the fields
        data = parts[0];
        cnp = parts[1];
        suma = Integer.parseInt(parts[2]);
    }

    private String get_param(String s){
        int i = s.indexOf("=");
        return s.substring(i+1);
    }

    public void fromString(String s){
        s = s.substring(s.indexOf("{")+1,s.indexOf("}"));
        String[] parts = s.split(",");
        for(int i=0;i<parts.length;i++){ parts[i] = get_param(parts[i]); }
        this.data = parts[0];
        this.cnp = parts[1];
        this.suma = Integer.parseInt(parts[2]);
    }

    public String getData() {
        return data;
    }

    public String getCnp() {
        return cnp;
    }

    public int getSuma() {
        return suma;
    }
}
