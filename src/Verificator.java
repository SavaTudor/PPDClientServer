import java.io.FileOutputStream;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Verificator {

    int preturi[];
    int durate[];
    int nrMax[];
    int verificationCounter = 0;
    String filePath = "verificare";

    public Verificator(int preturi[], int durate[], int nrMax[]) {
        this.preturi = preturi;
        this.durate = durate;
        this.nrMax = nrMax;
    }

    public void verifica(Vector<String> programari, Vector<String> plati) {

        Vector<Vector<Vector<Integer>>> calupeLoactii = new Vector<>();

        for (int i = 0; i < 5; i++) {
            Vector<Vector<Integer>> calupe = new Vector<>();
            for (int j = 0; j < 5; j++) {
                int nrFerestre = (18 - 10) * 60 / durate[j];
                Vector<Integer> ferestre = new Vector<>();
                for (int k = 0; k < nrFerestre; k++) {
                    ferestre.add(0);
                }
                calupe.add(ferestre);
            }
            calupeLoactii.add(calupe);
        }
        Vector<Integer> ferestreProgramare = new Vector<>();


        var time = java.time.LocalTime.now();

        HashMap<String, Queue<Programare>> posibilNeplatite = new HashMap<>();
        HashMap<String, Integer> platiNeanulate = new HashMap<>();

        Vector<Vector<Programare>> neplatite = new Vector<Vector<Programare>>(5);

        for (int i = 0; i < 5; i++) {
            neplatite.add(new Vector<Programare>());
        }

        int solduri[] = new int[5];
        for (int i = 0; i < 5; i++) {
            solduri[i] = 0;
        }
        for (String programare : programari) {
            Programare p = new Programare(programare);
            int idx = computeFereastra(p.getOraTratament(), durate[p.getTipTratament()]);
            int nr = calupeLoactii.get(p.getLocatieTratament()).get(p.getTipTratament()).get(idx);
            calupeLoactii.get(p.locatieTratament).get(p.tipTratament).set(idx, nr + 1);

            if (!posibilNeplatite.containsKey(p.getCnp())) posibilNeplatite.put(p.getCnp(), new LinkedList<>());
            posibilNeplatite.get(p.getCnp()).add(p);
        }

        for (String plata : plati) {
            Plata p = new Plata(plata);
            if (!platiNeanulate.containsKey(p.getCnp())) platiNeanulate.put(p.getCnp(), 0);
            if (p.getSuma() > 0) platiNeanulate.put(p.getCnp(), platiNeanulate.get(p.getCnp()) + 1);
            else platiNeanulate.put(p.getCnp(), platiNeanulate.get(p.getCnp()) - 1);
        }

        for (String cnp : posibilNeplatite.keySet()) {
            int nrPlati = 0;
            if (platiNeanulate.containsKey(cnp)) nrPlati = platiNeanulate.get(cnp);
            for (int i = 0; i < nrPlati; i++) {
                Programare p = posibilNeplatite.get(cnp).poll();
                solduri[p.getLocatieTratament()] += preturi[p.getTipTratament()];
            }
            while (!posibilNeplatite.get(cnp).isEmpty()) {
                Programare p = posibilNeplatite.get(cnp).poll();
                neplatite.get(p.getLocatieTratament()).add(p);
            }
        }

        try {
            FileOutputStream vf = new FileOutputStream(filePath + verificationCounter + ".txt");
            verificationCounter++;
            vf.write((time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "\n").getBytes());
            for (int i = 0; i < 5; i++) {
                vf.write(("Locatia " + i + " are soldul " + solduri[i] + "\n").getBytes());
                vf.write("Tratamentele neplatite sunt:\n".getBytes());
                for (Programare p : neplatite.get(i)) {
                    vf.write(p.toFileString().getBytes());
                }

                for (int j = 0; j < 5; j++) {
                    LocalTime timeOfAppointment = LocalTime.of(8, 0);

                    vf.write(("Tratamentul " + j + " are urmatoarele programari libere :\n").getBytes());
                    for (int k = 0; k < calupeLoactii.get(i).get(j).size(); k++) {
                        vf.write(timeOfAppointment.toString().getBytes());
                        vf.write(" - max: ".getBytes());
                        vf.write(String.valueOf(nrMax[j]).getBytes());
                        vf.write(" - act: ".getBytes());
                        vf.write((calupeLoactii.get(i).get(j).get(k)).toString().getBytes());
                        if (calupeLoactii.get(i).get(j).get(k) > nrMax[j] * (i + 1)) vf.write((" OVERFLOW").getBytes());
                        vf.write("\n".getBytes());
                        timeOfAppointment = timeOfAppointment.plusMinutes(durate[j]);

                    }
                }
                vf.write("\n".getBytes());
            }

            vf.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public int computeFereastra(String oraString, int durata) {
        int ora = Integer.parseInt(oraString.substring(0, 2));
        int minut = Integer.parseInt(oraString.substring(3, 5));


        return (int) Math.floor(((ora - 10) * 60 + minut) / (float) durata);
    }
}
