package com.app.dir.service.oauth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author toantran Interface meant to be used for all different types of OAuth
 *         signature generation and signature verifications
 */
public interface OAuthService {

	/**
	 * @param oAuthParammap
	 *            Map of OAuth parameters
	 * @param baseUrlString
	 *            The root URL used for the GET call
	 * @param tokenUrlString
	 *            The query parameter passed with GET call
	 * @return The generated string
	 * @throws UnsupportedEncodingException
	 *             Thrown when encoding fails
	 * @throws NoSuchAlgorithmException
	 *             Thrown when algorithm given is incorrect
	 * @throws InvalidKeyException
	 *             Given secret key was invalid
	 */
	public String generateSignature(Map<String, String> oAuthParammap,
			String baseUrlString, String tokenUrlString)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException;

	/**
	 * @param authorizationHeader
	 *            Authorization header with all necessary OAuth 1.0 parameters
	 * @param baseUrlString
	 *            Root URL
	 * @param tokenUrlString
	 *            Token URL
	 * @return Boolean of whether the generated signature matches with the given
	 *         signature
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public boolean verifySignature(String authorizationHeader,
			String baseUrlString, String tokenUrlString)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException;
}
