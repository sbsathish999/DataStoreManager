import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String args[]) throws IOException {
        Map<String, Object> sizeCheckingMap = new HashMap<>();
        sizeCheckingMap.put("sdfsd", 1);
        for(int i=1; i<100000; i++) {
            sizeCheckingMap.put("sdfsd" + i, i);
        }
         System.out.println("Index Size: " + sizeCheckingMap.size());
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(sizeCheckingMap);
        oos.close();
        System.out.println("Data Size: " + baos.size());
    }
}
