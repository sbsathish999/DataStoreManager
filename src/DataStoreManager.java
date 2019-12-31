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
    static String appBehaviour = "single threaded way";
    public static void main(String args[]){
        System.out.println("Hi, Welcome to Data Store Manager");
        System.out.println();
        System.out.println();
        int option = 0;
        FileHandler fileHandler = new FileHandler();
        DataStoreHelper dataStoreHelper = new DataStoreHelper();
        do {
            try {
                //System.out.println("Note: By default the APP will work as a single threaded way. \nAPP behaviour can be changed with \"Change APP behaviour menu\".");
                System.out.println();
                System.out.println("1. Choose File Path,  2. Create Entry,  "
                                         + "3. Fetch Entry,  4. Delete Entry,  5.Exit");///* 6. Change APP behaviour
                System.out.print("Enter the option no. : ");
                option = inputReader.nextInt();
                switch (option) {
                    case 1:
                        if(!existingDataMap.isEmpty()) {
                            fileHandler.addDataIntoFile(dataStoreFile, existingDataMap, "json");
                            fileHandler.addDataIntoFile(timeToLiveFile, timeToLiveMap, "string");
                        }
                        dataStoreFile = fileHandler.chooseFile("dataStoreFile.txt", false);
                        System.out.println("Choosen file : " + dataStoreFile.getAbsolutePath());
                        timeToLiveFile = fileHandler.chooseFile("timeToLiveFile.txt", true);
                        fileHandler.loadFileData(dataStoreFile, existingDataMap, "json");
                        fileHandler.loadFileData(timeToLiveFile, timeToLiveMap, "long");
                        break;
                    case 2:
                        dataStoreHelper.createDataStoreEntry(dataStoreFile, existingDataMap, timeToLiveMap);
                        break;
                    case 3:
                        Object entryJson  = dataStoreHelper.fetchEntry(dataStoreFile, existingDataMap, timeToLiveMap);
                        dataStoreHelper.displayEntry(entryJson);
                        break;
                    case 4:
                        dataStoreHelper.deleteEntry(dataStoreFile, existingDataMap, timeToLiveMap);
                        break;
                    case 5:
                        fileHandler.addDataIntoFile(dataStoreFile, existingDataMap, "json");
                        fileHandler.addDataIntoFile(timeToLiveFile, timeToLiveMap, "string");
                        break;
                    case 6:
                        appBehaviour = dataStoreHelper.changeAPPBehaviour();
                }
            } catch (Throwable e) {
                System.out.println("Error Message  : " + e);
                //e.printStackTrace();
                if(e.getClass().equals(InputMismatchException.class)){
                    System.out.println("Please enter only numbers as option");
                    inputReader.next();
                }
                continue;
            }
        } while (option != 5);
    }
}
