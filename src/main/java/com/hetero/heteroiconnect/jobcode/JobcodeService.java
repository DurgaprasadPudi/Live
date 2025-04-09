package com.hetero.heteroiconnect.jobcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JobcodeService {
	@Autowired
	private JobcodeDao jobcodeDao;
	private final Logger logger = LoggerFactory.getLogger(JobcodeService.class);

	public ResponseEntity<String> uploadFile(String jcode, String eid, MultipartFile file) {
		int i = jobcodeDao.uploadFile(jcode, eid, file);
		if (i > 0) {
			return new ResponseEntity<>("File Uploaded",HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Error While File Uploading",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> checkJobcode(String jcode) {
		if (jobcodeDao.checkJobcode(jcode)!=null) {
			logger.info("Jobcode exists");
			return new ResponseEntity<>("Jobcode Exists",HttpStatus.OK);
		} else {
			logger.info("Jobcode " + jcode + " not exists");
			return new ResponseEntity<>("Jobcode Not Exists",HttpStatus.NO_CONTENT);
		}
	}

	public ResponseEntity<Object> checkJobcodeGetData(String eid) {
		Object data = jobcodeDao.checkJobcodeGetData(eid);
		if (data != null) {
			return new ResponseEntity<>(data, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(data, HttpStatus.NO_CONTENT);
		}
	}
	
	public ResponseEntity<String> checkFile(String file) {
		if (jobcodeDao.checkFile(file)!=null) {
			logger.info("File exists");
			return new ResponseEntity<>("File exists",HttpStatus.OK);
		} else {
			logger.info("File Not exists");
			return new ResponseEntity<>("File Not Exists",HttpStatus.NO_CONTENT);
		}
	}
}
