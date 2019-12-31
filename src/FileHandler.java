import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileHandler {
    Scanner inputReader = new Scanner(System.in);
    Utility utility = new Utility();

    protected File chooseFile(String filePath, Boolean skipCustomFile) throws Throwable {
        System.out.println("Note: If the file is not found in the path you specified, it will create a new one with the file name you provided");
        if(!skipCustomFile) {
            System.out.println("Choose path of the file:\n1.Use default location\n2.Specify path location");
            System.out.println("Enter the option");
            Integer option = inputReader.nextInt();
            if(option.equals(2)) {
                System.out.println("Sample Input formats:\n1.Windows : \"C:\\\\directoryA\\\\directoryB\\\\fileName.txt\"\n2.Mac : \"/Users/directoryA/directoryB/fileName.txt\"");
                System.out.println("Enter the path of the file without quotes: " );
                filePath = inputReader.next();
            }
        }
        File file = new File(filePath);
        try {
        if(!file.exists()) {
            file.createNewFile();
        }
        } catch (Throwable e) {
            System.out.println("Incorrect path/ Access denied to the give path. Please choose different path to proceed.");
            throw e;
        }
        return file;
    }

    protected void loadFileData(File file, Map<String, Object> dataMap, String valueType) throws Throwable{
        Scanner fileReader = new Scanner(file);
        BufferedReader inputStreamREader = new BufferedReader(new FileReader(file));
        dataMap.putAll(inputStreamREader.lines().parallel().map(s-> s.split("=")).collect(Collectors.toMap(s-> s[0], s-> {
            Object value = null;
            try {
                value = utility.convertValue(s[1], valueType);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return value;
        })));
//        while(fileReader.hasNext()) {
//            String line = fileReader.next();
//            String lineArray[] = line.split("=");
//            dataMap.put(lineArray[0], utility.convertValue(lineArray[1], valueType));
//        }
        fileReader.close();
    }

    protected void addDataIntoFile(File file, Map<String, Object> dataMap, String valueType) throws Throwable {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        String dataString = utility.convertValue(dataMap, valueType);
        fileWriter.write(dataString);
        fileWriter.close();
    }

    public static void validateFileSize(Map<String, Object> dataMap) throws Throwable{
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(dataMap);
        oos.close();
        if(baos.size() > 1073741824) {
            throw new Exception("The file can contains only the size of 1GB. Try choosing different file to proceed.");
        }
    }

    protected void checkFileAvailability(File file) throws Throwable {
        if(file == null) throw new Exception("File has not found / choosen. Please Choose file to proceed.");
    }
}
