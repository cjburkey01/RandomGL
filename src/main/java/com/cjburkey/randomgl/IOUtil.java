package com.cjburkey.randomgl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public final class IOUtil {
    
    public static String readFile(String fileName) {
        // Make sure the file name is not null or empty
        if (fileName == null || (fileName = fileName.trim()).isEmpty()) {
            return null;
        }
        
        // Sanitize file name
        fileName = fileName.replaceAll(Pattern.quote("\\"), "/").replaceAll(Pattern.quote("//"), "/");
        while (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        fileName = "/" + fileName;
        
        // Read the file
        BufferedReader reader = null;
        try {
            // Initialize the reader
            reader = new BufferedReader(new InputStreamReader(RandomGL.class.getResourceAsStream(fileName)));
            StringBuilder output = new StringBuilder();
            String line = "";
            
            // Keep reading lines until they don't exist anymore
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append('\n');
            }
            
            // Return the output if no errors occur
            return output.toString();
        } catch (Exception e) {
            Debug.error("Failed to read file: {}", fileName);
        } finally {
            // Cleanup the reader
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e1) {
                Debug.error("Failed to close reader for file: {}", fileName);
            }
        }
        return null;
    }
    
}