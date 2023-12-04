package br.com.m4rc310.auth.graphql;



/**
 * The Enum MEnumError.
 */
public enum MEnumError {
	
	/** The access unauthorized. */
	ACCESS_UNAUTHORIZED(401, MConst.ERROR$access_unauthorized);
	
	/** The code. */
	private int code;
	
	/** The message. */
	private String message;

	/**
	 * Instantiates a new m enum error.
	 *
	 * @param code    the code
	 * @param message the message
	 */
	private MEnumError(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
