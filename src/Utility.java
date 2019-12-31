import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Utility {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Scanner inputReader = new Scanner(System.in);

    protected Object convertValue(String value, String valueType) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        Object convertedValue = null;
        if(valueType.equals("json")) {
            try {
                convertedValue = mapper.readValue(value, new TypeReference<Map<String, String>>(){});
            } catch (IOException e) {
                throw e;
            }
        } else {
            convertedValue = value;
        }
        return convertedValue;
    }

    protected String convertValue(Map<String, Object> dataMap, String valueType) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        return dataMap
                .entrySet()
                .parallelStream()
                .map(entry -> {
                    String data = null;
                    try {
                        String value = null;
                        if("json".equals(valueType)) {
                            value = mapper.writeValueAsString(entry.getValue());
                        } else {
                            value = String.valueOf(entry.getValue());
                        }
                        data = entry.getKey() + "=" + value;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return data;
                })
                .collect(Collectors.joining("\n"));
    }

    protected String readKey() throws Throwable {
        Boolean pass = false;
        String key = null;
        do {
            try {
                System.out.println("Enter the key : ");
                key = in.readLine();
                if(!isValidKeyLength(key)) continue;
                pass = true;
            }catch (Throwable e) {
                if(e.getClass().equals(InputMismatchException.class)){
                    System.out.println("Please enter only numbers as option");
                    inputReader.next();
                }
                continue;
            }
        } while (pass != true);
        return key;
    }

    protected Boolean isValidKeyLength(String key) throws Throwable{
        if(key.length() > 32) {
            System.out.println("Key length should be the maximum of 32 characters");
            return false;
        }
        return true;
    }

    protected Boolean isValidValueLength(String value) throws Throwable {
        if(value.getBytes("UTF-8").length > 16000) {
            System.out.println("Value length should be the maximum of 16kb");
            return false;
        }
        return true;
    }

    protected String getCategory(Map<String, Object> existingDataMap, Map<String, Object> timeToLiveMap,
                                 String type, Boolean byPassOption) throws Throwable {
        String key = null;
        String displayOption = type.equals("read") ? "Fetch" : type.equals("delete") ? "Delete" : "Create";
        Integer option = 1;
        do {
            try {
                if (!byPassOption) {
                    System.out.println("1. " + displayOption + "\t 2.Back");
                    option = inputReader.nextInt();
                } else {
                    byPassOption = false;
                }

                switch (option) {
                    case 1:
                        System.out.println("Enter the Category : ");
                        key = in.readLine();
                        if(!isValidKeyLength(key)) {
                            continue;
                        }
                       // System.out.println("type : " + type);
                        if (type.equals("write")) {
                            if (existingDataMap.get(key) != null) {
                                System.out.println("Already there is a data associated with this key, use different category");
                                continue;
                            }
                        } else {
//                            System.out.println("inside else case");
//                            System.out.println("datamap : " + existingDataMap);
                            Boolean isKeyExpired = isKeyExpired(key, timeToLiveMap);

                            if(existingDataMap.get(key) == null || isKeyExpired) {
                                System.out.println("No data is associated with this category, use different category");
                                continue;
                            }
                        }
                        option = 2;
                        break;
                    case 2:
                        key = null;
                }
            } catch (Throwable e) {
               // e.printStackTrace();
                if(e.getClass().equals(InputMismatchException.class)){
                    System.out.println("Please enter only numbers as option");
                    inputReader.next();
                }
                continue;
            }
        } while (option != 2);
        return key;
    }

    protected Boolean isKeyExpired(String key, Map<String, Object> timeToLiveMap) throws Throwable {
       Long expiryTime =  timeToLiveMap.get(key) != null ? Long.valueOf(String.valueOf(timeToLiveMap.get(key))) : null;
       if(expiryTime != null && System.currentTimeMillis() > expiryTime) {
           System.out.println("expired time : " + new Date(expiryTime));
           return  true;
       }
       return false;
    }

    protected String readValue() throws Throwable {
        Boolean pass = false;
        String value = null;
        do {
            System.out.println("Enter the value : ");
            value = in.readLine();
            if(!isValidValueLength(value)) {
                continue;
            }
            pass = true;
        } while (pass != true);
        return value;
    }

    protected Map<String, Object> getInputDataFromUser() throws Throwable {
        Map<String, Object> map = null;
        Boolean pass = false;
        do{
            try{
                System.out.println("Enter the number of data going to be entered : ");
                Integer count = inputReader.nextInt();
                map = new HashMap<>(count);
                for (int i = 0; i < count; i++) {
                    String key = readKey();
                    String value = readValue();
                    map.put(key, value);
                }
                pass = true;
            } catch (Throwable e) {
                if(e.getClass().equals(InputMismatchException.class)){
                    System.out.println("Please enter only numbers as option");
                    inputReader.next();
                }
                continue;
            }
        } while (pass != true);
        return map;
    }
}
