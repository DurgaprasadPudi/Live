package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileToByteConverter {
	private static final Logger logger = LoggerFactory.getLogger(FileToByteConverter.class);

	public byte[] getFileAsByteArray(String filePath) {
		byte[] fileData = null;
		Path path = Paths.get(filePath);

		try {
			fileData = Files.readAllBytes(path);
		} catch (IOException e) {
			logger.warn("Failed to read file at path: {}. Error: {}", filePath, e.getMessage());
			logger.warn("File not found or inaccessible at path: {}", filePath);
			throw new RuntimeException(e);
		}

		return fileData;
	}
}
