package com.hetero.heteroiconnect.allowance;
public class ApiResponse {
    private String status;
    private String message;
    private String statuscode;
    private Object data;

    public ApiResponse() {}

    public ApiResponse(String status, String message, String statuscode, Object data) {
        this.status = status;
        this.message = message;
        this.statuscode=statuscode;
        this.data = data;
    }

    public ApiResponse(String string, String message2) {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "ApiResponse [status=" + status + ", message=" + message + ", statuscode=" + statuscode + ", data="
				+ data + "]";
	}
    
}
