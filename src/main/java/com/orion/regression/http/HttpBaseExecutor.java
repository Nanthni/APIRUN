package com.orion.regression.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.orion.exception.OrionRegressionException;

public abstract class HttpBaseExecutor {
	private static final Logger log = Logger.getLogger ( HttpBaseExecutor.class.getName () );
	String restUrl = null;
	String tokenId = null;

	static CloseableHttpClient httpClient = null;

	public HttpBaseExecutor(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpBaseExecutor() {
	}

	public abstract HttpRequestBase getHttpRequest(String restUrl);

	public abstract void addMoreHeaders(HttpRequestBase httpReq);

	public String execute() throws OrionRegressionException {
		try {
			/* Prepare Http request */
			HttpRequestBase httpReq = getHttpRequest ( restUrl );
			/* Add headers to get request */
			httpReq.setHeader ( "AUTHORIZATION", "Token token=" + tokenId );
			httpReq.setHeader ( "Content-Type", "application/json" );
			httpReq.setHeader ( "Accept", "application/json" );
			addMoreHeaders ( httpReq );
			/* Response handler for after request execution */

			ResponseHandler <String> responseHandler = httpResponse -> {
				/* Get status code */
				int httpResponseCode = httpResponse.getStatusLine ().getStatusCode ();
				log.log ( Level.INFO, "Response code: " + httpResponseCode );
				if (httpResponseCode >= 200 && httpResponseCode < 300) {
					/* Convert response to String */
					HttpEntity entity = httpResponse.getEntity ();
					return entity != null ? EntityUtils.toString ( entity ) : null;
				} else {
					//
					System.out.println ( "Unexpected response status: " + httpResponseCode );
					if (httpResponseCode == 400) {

						HttpEntity entity = httpResponse.getEntity ();
						return entity != null ? EntityUtils.toString ( entity ) : null;
					}
					return null;/*
					 * throw new ClientProtocolException("Unexpected response status: " +
					 * httpResponseCode);
					 */
				}
			};
			/* Execute URL and attach after execution response handler */
			String strResponse = httpClient.execute ( httpReq, responseHandler );
			/* Print the response */
			log.log ( Level.INFO, "Response: " + strResponse );
			return strResponse;
		} catch (IOException e) {
			throw new OrionRegressionException ( e );
		}
	}
}





