package com.hetero.heteroiconnect.gstinandcontactsinfo;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gstinandcontacts")
public class GstInAndContactsController {

	private final GstInAndContactsService gstInAndContactsService;

	public GstInAndContactsController(GstInAndContactsService gstInAndContactsService) {
		this.gstInAndContactsService = gstInAndContactsService;
	}

	@GetMapping("/gstininfo")
	public List<GstInInfoDTO> getAllGstinInfo() {
		return gstInAndContactsService.getAllGstinInfo();
	}

	@GetMapping("/contactsinfo")
	public List<ContactsInfoDTO> getAllContactsInfo() {
		return gstInAndContactsService.getAllContacts();
	}
}
