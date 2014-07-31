package com.app.dir.service.oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author toantran
 * 
 *         Handles OAuth signature generation for GET methods and also verifies
 *         OAuth signatures
 *
 */
public class OAuthService {
	private Properties prop = new Properties();
	final static private String ENC = "UTF-8";
	final static private String HMAC_SHA1 = "HmacSHA1";

	private static final Logger log = LoggerFactory
			.getLogger(OAuthService.class);

	/**
	 * Default constructor. Loads properties file
	 * 
	 * @throws IOException
	 */
	public OAuthService() throws IOException {
		prop.load(getClass().getResourceAsStream("/consumer.properties"));
	}

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
			InvalidKeyException {

		log.debug("generateSignature method");

		StringBuilder baseString = new StringBuilder();

		baseString
				.append("GET&"
						+ URLEncoder.encode(baseUrlString, ENC)
						+ "&oauth_consumer_key%3D"
						+ oAuthParammap.get("OAuth oauth_consumer_key")
						+ "%26oauth_nonce%3D"
						+ oAuthParammap.get("oauth_nonce")
						+ "%26oauth_signature_method%3D"
						+ oAuthParammap.get("oauth_signature_method")
						+ "%26oauth_timestamp%3D"
						+ oAuthParammap.get("oauth_timestamp")
						+ "%26oauth_version%3D"
						+ oAuthParammap.get("oauth_version")
						+ "%26url%3D"
						+ URLEncoder.encode(
								URLEncoder.encode(tokenUrlString, ENC), ENC));

		String keyString = URLEncoder.encode(
				prop.getProperty("consumer-secret"), ENC) + '&';

		byte[] keyBytes = keyString.getBytes(ENC);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);
		mac.init(key);
		byte[] text = baseString.toString().getBytes(ENC);
		byte[] signBy = mac.doFinal(text);
		Base64 base64 = new Base64();
		String newSignature = new String(base64.encode(signBy));

		return URLEncoder.encode(newSignature, ENC);
	}

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
			NoSuchAlgorithmException {
		log.debug("verifySignature method");
		String serverConsumerKey = prop.getProperty("consumer-key");

		Map<String, String> oAuthMap = new HashMap<String, String>();
		String[] authArray = authorizationHeader.split(",");

		for (String keyValue : authArray) {
			String[] param = keyValue.split("\"");

			oAuthMap.put(param[0].replace("=", "").trim(), param[1].trim());
		}

		if (!(serverConsumerKey
				.equals(oAuthMap.get("OAuth oauth_consumer_key")))) {
			return false;
		} else {
//			generateSignature(oAuthMap, baseUrlString, tokenUrlString).equals(
//					oAuthMap.get("oauth_signature"));
			
			log.debug("SIZEOFMAP: " + oAuthMap.size());
			
			String generated = generateSignature(oAuthMap, baseUrlString, tokenUrlString);
			String given = oAuthMap.get("oauth_signature");
			log.debug("GENERATED!!!!:" + generated);
			log.debug("GIVEN!!!!: " + given);
			return generated.equals(given);
		}
	}
}
