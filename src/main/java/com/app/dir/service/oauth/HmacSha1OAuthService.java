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
public class HmacSha1OAuthService implements OAuthService{
	private Properties prop = new Properties();
	final static private String ENC = "UTF-8";
	final static private String HMAC_SHA1 = "HmacSHA1";

	private static final Logger log = LoggerFactory
			.getLogger(HmacSha1OAuthService.class);

	/**
	 * Default constructor. Loads properties file
	 * 
	 * @throws IOException
	 */
	public HmacSha1OAuthService() throws IOException {
		prop.load(getClass().getResourceAsStream("/consumer.properties"));
	}

	
	/* (non-Javadoc)
	 * @see com.app.dir.service.oauth.OAuthService#generateSignature(java.util.Map, java.lang.String, java.lang.String)
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
				prop.getProperty("CONSUMER_SECRET"), ENC) + '&';

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

	
	/* (non-Javadoc)
	 * @see com.app.dir.service.oauth.OAuthService#verifySignature(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean verifySignature(String authorizationHeader,
			String baseUrlString, String tokenUrlString)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException {
		log.debug("verifySignature method");
		String serverConsumerKey = prop.getProperty("CONSUMER_KEY");

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
			log.debug("SIZEOFMAP: " + oAuthMap.size());

			return generateSignature(oAuthMap, baseUrlString, tokenUrlString)
					.equals(oAuthMap.get("oauth_signature"));
		}
	}
}
