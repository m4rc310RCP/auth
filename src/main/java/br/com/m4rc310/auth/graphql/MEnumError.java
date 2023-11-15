package br.com.m4rc310.auth.graphql;



public enum MEnumError {
	ACCESS_UNAUTHORIZED(401, MConst.ERROR$access_unauthorized);
	
	private int code;
	private String message;

	private MEnumError(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
