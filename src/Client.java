import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class Client {
    private String nume;
    private String cnp;
    private String data;

    public Client() {
        this.nume = randomString(10);
        this.cnp = randomStringNumber(10);
        this.data = "1/8/2023";

        TimerTask task = new TimerTask() {
            public void run() {
//                System.out.println("Sending Programare");
                SendProgramare();
            }
        };

        new Timer().scheduleAtFixedRate(task, 0, 2000);

    }

    private String randomOra() {
        String s = "";
        int ora = (int) (Math.random() * 8) + 10;
        int minute = (int) (Math.random() * 60);
        if (ora < 10) s += "0";
        s += ora;
        s += ":";
        if (minute < 10) s += "0";
        s += minute;
        return s;
    }

    private String randomStringNumber(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s += (int) (Math.random() * 10);
        }
        return s;
    }

    public static String randomString(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s += (char) (Math.random() * 26 + 'a');
        }
        return s;
    }

    public void SendProgramare() {
        try {
            Socket socket = new Socket("localhost", 8080);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF(generateProgramare().toString());
            out.flush();
            Confirmare c = new Confirmare(in.readUTF());
            if (!c.getAcceptat()) {
                System.out.println("Programare nereusita");
            } else {
                System.out.println("Programare reusita");
                out.writeUTF((new Confirmare(true)).toString());
                out.flush();
                c = new Confirmare(in.readUTF());
                boolean anulat = false;
                if (Math.random() < 0.5) anulat = true;
                out.writeUTF((new Confirmare(anulat)).toString());
                out.flush();
            }
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Socket deja inchis");
            }

        } catch (IOException e) {
//            e.printStackTrace();

        }
    }

    public Programare generateProgramare() {
        return new Programare(nume, cnp, data, (int) (Math.random() * 5), (int) (Math.random() * 5), data, randomOra());
    }

}

