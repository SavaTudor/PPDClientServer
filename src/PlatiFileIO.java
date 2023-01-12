import java.io.*;
import java.util.Vector;

public class PlatiFileIO {

    private String filePath;

    public PlatiFileIO(String filePath) {
        try {
            this.filePath = filePath;
            FileOutputStream fileOut = new FileOutputStream(filePath);
            fileOut.write("".getBytes());
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void makePlata(Plata plata){
        try {
            FileOutputStream fos = new FileOutputStream(filePath, true);
            fos.write(plata.toFileString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Vector<String> getAllPlati() {
        Vector<String> result = new Vector<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
