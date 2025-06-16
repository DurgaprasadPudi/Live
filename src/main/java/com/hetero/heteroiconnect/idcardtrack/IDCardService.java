package com.hetero.heteroiconnect.idcardtrack;

import java.util.List;

public interface IDCardService {
	
	ApiResponse addIDCardIssuedDetails(UploadDetails uploadDetails);
	 
	List<FetchDetails> getAllIDCardIssuedDetails(int status);
 
	ApiResponse updateIDDetails(UploadDetails uploadDetails);
 
	List<ReissuedDetailsDTO> getReissuedDetails(String trackingId);

}
