package com.hetero.heteroiconnect.contractdetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    private FileUtil() {
        // private constructor to prevent instantiation
    }

    public static byte[] getFileContentAsBytes(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return new byte[0];
        }
        Path path = Paths.get(filePath);
        try {
            return Files.readAllBytes(path);
        } catch (NoSuchFileException e) {
            return new byte[0];
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
