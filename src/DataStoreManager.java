import java.io.File;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class DataStoreManager {
    static Scanner inputReader = new Scanner(System.in);
    static File dataStoreFile = null;
    static File timeToLiveFile = null;
    static Map<String, Object> existingDataMap = new ConcurrentHashMap<String, Object>() {};
    static Map<String, Object> timeToLiveMap = new ConcurrentHashMap<String, Object>() {};

    public static void main(String args[]){
        System.out.println("Hi, Welcome to Data Store Manager");
        System.out.println();
        System.out.println();
        int option = 0;
        FileHandler fileHandler = new FileHandler();
        DataStoreHelper dataStoreHelper = new DataStoreHelper();
        do {
            try {
                System.out.println("1. Choose File Path,  2. Create Entry,  "
                                         + "3. Fetch Entry,  4. Delete Entry,  5.Exit");
                System.out.print("Enter the option no. : ");
                option = inputReader.nextInt();
                switch (option) {
                    case 1:
                        //The below condition take place if user changes the file in between for any cases like memory adjustment/ wanted to work with different file.
                        if(!existingDataMap.isEmpty()) {
                            fileHandler.addDataIntoFile(dataStoreFile, existingDataMap, "json");
                            fileHandler.addDataIntoFile(timeToLiveFile, timeToLiveMap, "string");
                            existingDataMap.clear();
                            timeToLiveMap.clear();
                        }
                        dataStoreFile = fileHandler.chooseFile("dataStoreFile.txt", false);
                        System.out.println("Choosen file : " + dataStoreFile.getAbsolutePath());
                        timeToLiveFile = fileHandler.chooseFile("timeToLiveFile.txt", true);

                        fileHandler.loadFileData(dataStoreFile, existingDataMap, "json");
                        fileHandler.loadFileData(timeToLiveFile, timeToLiveMap, "long");
                        //System.out.println(existingDataMap + "/n " + timeToLiveMap);
                        break;
                    case 2:
                        dataStoreHelper.createDataStoreEntry(dataStoreFile, existingDataMap, timeToLiveMap);
                        break;
                    case 3:
                        Object entryJson  = dataStoreHelper.fetchEntry(dataStoreFile, existingDataMap, timeToLiveMap);
                        if(entryJson!= null) dataStoreHelper.displayEntry(entryJson);
                        break;
                    case 4:
                        dataStoreHelper.deleteEntry(dataStoreFile, existingDataMap, timeToLiveMap);
                        break;
                    case 5:
                        fileHandler.addDataIntoFile(dataStoreFile, existingDataMap, "json");
                        fileHandler.addDataIntoFile(timeToLiveFile, timeToLiveMap, "string");
                }
            } catch (Throwable e) {
                System.out.println("Error Message  : " + e);
                if(e.getClass().equals(InputMismatchException.class)){
                    System.out.println("Please enter only numbers as option");
                    inputReader.next();
                }
                continue;
            }
        } while (option != 5);
    }
}
