package com.hetero.heteroiconnect.changepassword;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 

@RestController
@RequestMapping("/hr")
public class HRLoginController {

	private HRLoginService hRLoginService;
	public HRLoginController(HRLoginService hRLoginService) {
		this.hRLoginService=hRLoginService;
	}

	@PostMapping(value="/changepassword", produces="application/json")
	public Object passwordChange(@RequestBody ResetPassword resetPassword) {
		return hRLoginService.passwordChange(resetPassword);
	}
}
