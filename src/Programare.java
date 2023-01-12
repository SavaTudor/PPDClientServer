

public class Programare {

    String nume;
    String cnp;
    String data;
    int locatieTratament;
    int tipTratament;
    String dataTratament;
    String oraTratament;

    public Programare(String string) {
        if(string.charAt(0) == '(') fromFileString(string);
        else fromString(string);
    }

    public Programare(String nume, String cnp, String data, int locatieTratament, int tipTratament, String dataTratament, String oraTratament) {
        this.nume = nume;
        this.cnp = cnp;
        this.data = data;
        this.locatieTratament = locatieTratament;
        this.tipTratament = tipTratament;
        this.dataTratament = dataTratament;
        this.oraTratament = oraTratament;
    }

    @Override
    public String toString() {
        return "[Programare]{" +
                "nume=" + nume +
                ", cnp=" + cnp +
                ", data=" + data +
                ", locatieTratament=" + locatieTratament +
                ", tipTratament=" + tipTratament +
                ", dataTratament=" + dataTratament +
                ", oraTratament=" + oraTratament +
                '}';
    }

    public String toFileString(){
        return "(" + nume + "," + cnp + "," + data + "," + locatieTratament + "," + tipTratament + "," + dataTratament + "," + oraTratament + ")\n";
    }

    public void fromFileString(String s){
        s = s.substring(1, s.length()-1);
        String[] parts = s.split(",");
        nume = parts[0];
        cnp = parts[1];
        data = parts[2];
        locatieTratament = Integer.parseInt(parts[3]);
        tipTratament = Integer.parseInt(parts[4]);
        dataTratament = parts[5];
        oraTratament = parts[6];
    }

    private String get_param(String s){
        int i = s.indexOf("=");
        return s.substring(i+1);
    }

    public void fromString(String s){
        //get the string between "{" and  "}"
        s = s.substring(s.indexOf("{")+1,s.indexOf("}"));
        String[] parts = s.split(",");
        //wrap all the parts in get_param
        for(int i=0;i<parts.length;i++){ parts[i] = get_param(parts[i]); }
        this.nume = parts[0];
        this.cnp = parts[1];
        this.data = parts[2];
        this.locatieTratament = Integer.parseInt(parts[3]);
        this.tipTratament = Integer.parseInt(parts[4]);
        this.dataTratament = parts[5];
        this.oraTratament = parts[6];
    }

    public String getNume() {
        return nume;
    }

    public String getCnp() {
        return cnp;
    }

    public String getData() {
        return data;
    }

    public int getLocatieTratament() {
        return locatieTratament;
    }

    public int getTipTratament() {
        return tipTratament;
    }

    public String getDataTratament() {
        return dataTratament;
    }

    public String getOraTratament() {
        return oraTratament;
    }
}
