import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataStoreHelper {

    FileHandler fileHandler = new FileHandler();
    Utility utility = new Utility();
    Scanner inputReader = new Scanner(System.in);

    protected void createDataStoreEntry(File file, Map<String, Object> existingDataMap, Map<String, Object> timeToLiveMap) throws Throwable {
        fileHandler.checkFileAvailability(file);
        String keyCategory = utility.getCategory(existingDataMap, timeToLiveMap,"write", true);
        if(keyCategory != null) {
            while(true) {
                System.out.println("Do you want to set expiry time to this key (y/n) ?");
                String setExpiry = inputReader.next();
                if (setExpiry.equalsIgnoreCase("y")) {
                    System.out.println("Enter the expiry time in number of seconds");
                    int seconds = inputReader.nextInt();
                    Long currentTime = System.currentTimeMillis();
                    Long milliSecondsToBeAdded = Long.valueOf(String.valueOf(seconds)) * 1000;
                    Long expiryTimeInMillis = currentTime + milliSecondsToBeAdded;
                    timeToLiveMap.put(keyCategory, expiryTimeInMillis);
                }
                if(setExpiry.equalsIgnoreCase("y") || setExpiry.equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("Invalid option. Please entery (y/n) to proceed.");
                }
            }

            Map<String, Object> dataMap = utility.getInputDataFromUser();
            Map<String, Object> sizeCheckingMap = new HashMap<>();
            sizeCheckingMap.putAll(existingDataMap);
            sizeCheckingMap.put(keyCategory, dataMap);
            fileHandler.validateFileSize(sizeCheckingMap);
            existingDataMap.put(keyCategory, dataMap);
            System.out.println("Stored successfully");
        }
    }

    protected Object fetchEntry(File file, Map<String, Object> existingDataMap, Map<String, Object> timeToLiveMap) throws Throwable {
        fileHandler.checkFileAvailability(file);
        String keyCategory = utility.getCategory(existingDataMap, timeToLiveMap, "read", true);
        //System.out.println("keycategory : " + keyCategory);
        if(keyCategory != null) {
            return existingDataMap.get(keyCategory);
        }
        return null;
    }

    protected void displayEntry(Object  data) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
    }

    protected void deleteEntry(File file, Map<String, Object> existingDataMap, Map<String, Object> timeToLiveMap) throws Throwable {
        fileHandler.checkFileAvailability(file);
        String keyCategory = utility.getCategory(existingDataMap, timeToLiveMap, "delete", true);
        if(keyCategory != null) {
            existingDataMap.remove(keyCategory);
            System.out.println("Data is deleted with this category : " + keyCategory);
        }
    }
}
