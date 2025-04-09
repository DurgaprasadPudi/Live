package com.hetero.heteroiconnect.worksheet.utility;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageBundleSource {

	private MessageSource messagesource;

	public MessageBundleSource(@Qualifier("centralMessageSource") MessageSource messagesource) {
		this.messagesource = messagesource;
	}

	public String getmessagebycode(String messageproperty, Object[] obj) {

		return messagesource.getMessage(messageproperty, obj, Locale.getDefault());
	}

}
