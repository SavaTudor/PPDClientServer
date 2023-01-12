import java.net.Socket;

public class DTO{
    public int state;
    public Programare programare;
    public Socket socket;

    public DTO(int state, Programare programare, Socket socket){
        this.state = state;
        this.programare = programare;
        this.socket = socket;
    }
}
