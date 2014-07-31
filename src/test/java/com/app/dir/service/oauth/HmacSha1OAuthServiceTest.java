package com.app.dir.service.oauth;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author toantran
 *
 *         Class unit tests HmacSha1OAuthService
 */
public class HmacSha1OAuthServiceTest {

	/**
	 * Tests verifySignature method with authorization header params match the
	 * generated key
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void verifySignature_CorrectMatchingParams_True()
			throws IOException, InvalidKeyException, NoSuchAlgorithmException {
		HmacSha1OAuthService oservice = new HmacSha1OAuthService();

		String authorizationHeader = "OAuth oauth_consumer_key=\"appdirectintegrationchallenge-11272\", oauth_nonce=\"-5601359236458923633\", oauth_signature=\"GUml0HI0CSWFK90pjjjSij38HbE%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1406700844\", oauth_version=\"1.0\"";
		String baseUrlString = "http://sleepy-mountain-3452.herokuapp.com/create";
		String tokenUrlString = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";

		assertTrue("Signature should have been verified",
				oservice.verifySignature(authorizationHeader, baseUrlString,
						tokenUrlString));
	}

	/**
	 * Test verifySignature method when the given key vs the system consumer
	 * keys don't match
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void verifySignature_WrongKey_True() throws IOException,
			InvalidKeyException, NoSuchAlgorithmException {
		HmacSha1OAuthService oservice = new HmacSha1OAuthService();

		String authorizationHeader = "OAuth oauth_consumer_key=\"appdirectintegrationchallenge-11273\", oauth_nonce=\"-5601359236458923633\", oauth_signature=\"GUml0HI0CSWFK90pjjjSij38HbE%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1406700844\", oauth_version=\"1.0\"";
		String baseUrlString = "http://sleepy-mountain-3452.herokuapp.com/create";
		String tokenUrlString = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";

		assertFalse("Signature should have been verified",
				oservice.verifySignature(authorizationHeader, baseUrlString,
						tokenUrlString));
	}

	/**
	 * Test verifySignature method when the given signature vs the system's
	 * generated signature don't match
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void verifySignature_WrongGivenSignature_True() throws IOException,
			InvalidKeyException, NoSuchAlgorithmException {
		HmacSha1OAuthService oservice = new HmacSha1OAuthService();

		String authorizationHeader = "OAuth oauth_consumer_key=\"appdirectintegrationchallenge-11272\", oauth_nonce=\"-5601359236458923633\", oauth_signature=\"XUml0HI0CSWFK90pjjjSij38HbE%3D\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"1406700844\", oauth_version=\"1.0\"";
		String baseUrlString = "http://sleepy-mountain-3452.herokuapp.com/create";
		String tokenUrlString = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";

		assertFalse("Signature should have been verified",
				oservice.verifySignature(authorizationHeader, baseUrlString,
						tokenUrlString));
	}

	/**
	 * 
	 * Tests generateSignature method with canned inputs in a map.  Method should produce an output
	 * 
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	public void generateSignature_OAuthMap_OAuthSignature() throws IOException,
			InvalidKeyException, NoSuchAlgorithmException {
		HmacSha1OAuthService oservice = new HmacSha1OAuthService();
		Map<String, String> oAuthMap = new HashMap<String, String>();
		String baseUrlString = "http://sleepy-mountain-3452.herokuapp.com/create";
		String tokenUrlString = "https://www.appdirect.com/api/integration/v1/events/6b06dd61-dd50-4afc-974f-5e6fa7757437";

		oAuthMap.put("oauth_signature", "lfse4W3myUUbyOALre9wcAVZIj4%3D");
		oAuthMap.put("oauth_version", "1.0");
		oAuthMap.put("oauth_nonce", "-5601359236458923633");
		oAuthMap.put("oauth_signature_method", "HMAC-SHA1");
		oAuthMap.put("oauth_consumer_key",
				"appdirectintegrationchallenge-11272");
		oAuthMap.put("oauth_timestamp", "1406700844");

		String expected = "lfse4W3myUUbyOALre9wcAVZIj4%3D";
		String actual = oservice.generateSignature(oAuthMap, baseUrlString,
				tokenUrlString);
		assertEquals("Generated signature is not correct", expected, actual);
	}

}
