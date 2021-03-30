package sample;
import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class FileLoader {
    private String filename;
    private Map<String, Integer> warnings;
    private int warningCount;

    public FileLoader(String filename){
        this.filename = filename;
        warnings = new TreeMap<>();
        this.warningCount = 0;
    }

    public Map<String, Integer> getWarnings() {
        return this.warnings;
    }

    public int getWarningCount() {
        return this.warningCount;
    }

    public void loadFile(){
        String line = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.filename));
            while ((line = br.readLine())!=null) {
                String[] columns = line.split(",");
                String warning = columns[5];
                if (warnings.containsKey(warning)) {
                    int previousCount = warnings.get(warning);
                    warnings.put(warning, previousCount + 1);
                } else {
                    warnings.put(warning, 1);
                }
                warningCount++;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
