import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Worker {

    private static Serviciu serviciu = new Serviciu();

    static DTO faProgramare(Socket clientSocket) {
        System.out.println("Clientul " + clientSocket.getPort() + " a cerut o programare");
        boolean accepted = true;
        Programare programare = null;
        try {
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());


            programare = new Programare(in.readUTF());
            accepted = serviciu.incearcaRezervare(programare);


            out.writeUTF((new Confirmare(accepted)).toString());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return new DTO(-1, null, null);
        }

//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new DTO(accepted ? 1 : 0, programare, clientSocket);
    }

    static DTO plateste(DTO dto) {
//        System.out.println("Clientul " + dto.socket.getPort() + " a cerut plata");
        if (dto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(dto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(dto.socket.getInputStream());

                in.readUTF();
                serviciu.plateste(dto.programare);

                out.writeUTF((new Confirmare(true)).toString());
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
                return new DTO(-1, null, null);
            }
        }

        return dto;
    }

    static void anuleazaPlata(DTO dto) {
        if (dto.state == 1) {
            try {
                DataOutputStream out = new DataOutputStream(dto.socket.getOutputStream());
                DataInputStream in = new DataInputStream(dto.socket.getInputStream());

                Confirmare confirmare = new Confirmare(in.readUTF());
                if (confirmare.getAcceptat()) serviciu.anuleazaPlata(dto.programare);

                out.writeUTF((new Confirmare(true)).toString());
                out.flush();
                dto.socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dto.state == 0) {
            try {
                dto.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("Clientul " + dto.socket.getPort()  + " a terminat cu: " + dto.state);


    }

}
