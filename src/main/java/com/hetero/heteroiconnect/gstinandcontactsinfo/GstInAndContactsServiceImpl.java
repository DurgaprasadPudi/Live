package com.hetero.heteroiconnect.gstinandcontactsinfo;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.exception.FuelAndDriverExpensesException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class GstInAndContactsServiceImpl implements GstInAndContactsService {

	private static final Logger logger = LoggerFactory.getLogger(GstInAndContactsServiceImpl.class);

	private final GstInAndContactsRepository gstInAndContactsRepository;
	private final MessageBundleSource messageBundleSource;

	public GstInAndContactsServiceImpl(GstInAndContactsRepository gstInAndContactsRepository,
			MessageBundleSource messageBundleSource) {
		this.gstInAndContactsRepository = gstInAndContactsRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<GstInInfoDTO> getAllGstinInfo() {
		logger.info("Fetching all GSTIN information started.");
		try {
			List<GstInInfoDTO> gstinInfoList = gstInAndContactsRepository.fetchAllGstinInfo();
			logger.info("Successfully fetched {} GSTIN records.", gstinInfoList.size());
			return gstinInfoList;
		} catch (Exception e) {
			logger.error("Error occurred while fetching GSTIN information: {}", e.getMessage(), e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("gstin.info.fetching.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ContactsInfoDTO> getAllContacts() {
		logger.info("Fetching all Contacts information started.");
		try {
			List<ContactsInfoDTO> contactsList = gstInAndContactsRepository.fetchAllContacts();
			logger.info("Successfully fetched {} Contact records.", contactsList.size());
			return contactsList;
		} catch (Exception e) {
			logger.error("Error occurred while fetching Contacts information: {}", e.getMessage(), e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("contacts.info.fetching.error", new Object[] {}), e);
		}
	}
}
