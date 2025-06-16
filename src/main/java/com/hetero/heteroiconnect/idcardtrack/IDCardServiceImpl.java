package com.hetero.heteroiconnect.idcardtrack;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetFetchingException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class IDCardServiceImpl implements IDCardService {
	private static final Logger logger = LoggerFactory.getLogger(IDCardServiceImpl.class);

	private IdCardRepository idCardRepository;
	private MessageBundleSource messageBundleSource;

	public IDCardServiceImpl(IdCardRepository idCardRepository, MessageBundleSource messageBundleSource) {
		this.idCardRepository = idCardRepository;
		this.messageBundleSource = messageBundleSource;
	}
	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse addIDCardIssuedDetails(UploadDetails uploadDetails) {
		try {
			return idCardRepository.addIDCardIssuedDetails(uploadDetails);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("id.card.upload.error", new Object[] {}), e);
		}
	}
 
	public List<FetchDetails> getAllIDCardIssuedDetails(int status) {
		try {
			return idCardRepository.getAllIDCardIssuedDetails(status);
		} catch (Exception e) {
			throw new UserWorkSheetFetchingException(
					messageBundleSource.getmessagebycode("id.card.list.fetching.error", new Object[] {}));
 
		}
	}
 
	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse updateIDDetails(UploadDetails uploadDetails) {
		try {
			return idCardRepository.updateIDDetails(uploadDetails);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("id.card.update.error", new Object[] {}), e);
		}
	}
 
	@Transactional(rollbackFor = Throwable.class)
	public List<ReissuedDetailsDTO> getReissuedDetails(String trackingId) {
		try {
			return idCardRepository.getReissuedDetails(trackingId);
		} catch (Exception e) {
			throw new UserWorkSheetFetchingException(
					messageBundleSource.getmessagebycode("id.card.reissued.details.fetching.error", new Object[] {}));
		}
	}

}
