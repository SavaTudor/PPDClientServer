import java.io.*;
import java.util.Vector;

public class ProgramariFileIO {

    private String filePath;

    public ProgramariFileIO(String filePath) {
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

    public synchronized void writeProgramare(Programare programare){
        try {
            FileOutputStream fos = new FileOutputStream(filePath, true);
            fos.write(programare.toFileString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteProgramare(Programare programare){
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            String sp = programare.toFileString();
            sp = sp.substring(0, sp.length() - 1);

            while ((line = file.readLine()) != null) {
                if(!line.equals(sp)) {
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
            }
            file.close();

            FileOutputStream fileOut = new FileOutputStream(filePath);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public synchronized Vector<String> getAllProgramari() {
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
