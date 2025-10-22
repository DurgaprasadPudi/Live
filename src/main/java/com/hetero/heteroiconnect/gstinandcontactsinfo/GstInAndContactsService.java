package com.hetero.heteroiconnect.gstinandcontactsinfo;

import java.util.List;

public interface GstInAndContactsService {
	List<GstInInfoDTO> getAllGstinInfo();

	List<ContactsInfoDTO> getAllContacts();

}
