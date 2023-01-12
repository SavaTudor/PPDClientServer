import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Serviciu {

    Planificator[][] planificator;
    AtomicInteger nrAnulari = new AtomicInteger(0);
    Lock lock;
    Condition faraAnulari;
    ProgramariFileIO prio;
    PlatiFileIO plio;
    Verificator verificator;


    public Serviciu() {
        int preturi[] = {50, 20, 40, 100, 30};
        int durate[] = {120, 20, 30, 60, 30};
        int nrMax[] = {3, 1, 1, 2, 1};
        verificator = new Verificator(preturi, durate, nrMax);

        planificator = new Planificator[5][5];
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                planificator[i][j] = new Planificator(preturi[j],nrMax[j]*(i+1), durate[j]);
            }
        }
        lock = new ReentrantLock();
        faraAnulari = lock.newCondition();
        prio = new ProgramariFileIO("programari.txt");
        plio = new PlatiFileIO("plati.txt");

        TimerTask task = new TimerTask() {
            public void run() {
                verifica();
            }
        };
        long time = 1000*5;
        new Timer().schedule(task, time, time);
    }

    public boolean incearcaRezervare(Programare programare) {
        int locatie = programare.getLocatieTratament();
        int tip = programare.getTipTratament();
        String ora = programare.getOraTratament();
//        System.out.println("Clientul " + programare.getNume() + " a cerut o programare la " + locatie + " la ora " + ora + " pentru tratamentul " + tip);

        if (planificator[locatie][tip].incearcaRezervare(ora)) {
            prio.writeProgramare(programare);
            return true;
        }

        return false;
    }

    public void plateste(Programare programare){

        String data = programare.getData();
        String cnp = programare.getCnp();

        int locatie = programare.getLocatieTratament();
        int tip = programare.getTipTratament();
        int suma = planificator[locatie][tip].getPret();

        Plata plata = new Plata(data, cnp, suma);
        plio.makePlata(plata);
    }

    public void anuleazaPlata(Programare programare){
        lock.lock();
        nrAnulari.incrementAndGet();
        lock.unlock();

        String data = programare.getData();
        String cnp = programare.getCnp();
        String ora = programare.getOraTratament();
        int locatie = programare.getLocatieTratament();
        int tip = programare.getTipTratament();
        int suma = planificator[locatie][tip].getPret();

        planificator[locatie][tip].anuleazaRezervare(ora);

        prio.deleteProgramare(programare);
        Plata plata = new Plata(data, cnp, -suma);
        plio.makePlata(plata);

        lock.lock();
        if(nrAnulari.decrementAndGet() == 0) faraAnulari.signalAll();
        lock.unlock();
    }

    private void verifica(){
        try {
            lock.lock();
            while(nrAnulari.get() > 0) faraAnulari.await();

            Vector<String> programari = prio.getAllProgramari();
            Vector<String> plati = plio.getAllPlati();

            verificator.verifica(programari, plati);

            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
