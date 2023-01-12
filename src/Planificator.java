import java.util.Vector;

public class Planificator {
    int pret;
    int locuri;
    int durata;
    Vector<Integer> ferestreProgramare = new Vector<>();

    public Planificator(int pret, int locuri, int durata) {
        this.pret = pret;
        this.locuri = locuri;
        this.durata = durata;

        int nrFerestre = (18 - 10) * 60 / durata;
        for (int i = 0; i < nrFerestre; i++) {
            ferestreProgramare.add(0);
        }

    }

    public synchronized boolean incearcaRezervare(String oraString) {
        int index = computeFereastra(oraString);
        if (ferestreProgramare.get(index) < locuri) {
            ferestreProgramare.set(index, ferestreProgramare.get(index) + 1);
            return true;
        }
        return false;
    }

    public synchronized void anuleazaRezervare(String oraString) {
        int index = computeFereastra(oraString);
        ferestreProgramare.set(index, ferestreProgramare.get(index) - 1);
    }

    public int getPret() {
        return pret;
    }

    public int getLocuri() {
        return locuri;
    }

    public int computeFereastra(String oraString) {
        int ora = Integer.parseInt(oraString.substring(0, 2));
        int minut = Integer.parseInt(oraString.substring(3, 5));


        return (int) Math.floor(((ora - 10) * 60 + minut) / (float) durata);
    }
}
