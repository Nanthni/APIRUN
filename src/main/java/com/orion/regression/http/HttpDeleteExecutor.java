package com.orion.regression.http;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * HTTP utlity class to perform "Delete" based REST call
 * 
 * @author Vijay Anand
 *
 */
public class HttpDeleteExecutor extends HttpBaseExecutor{
	
	public static HttpDeleteExecutor getInstance(CloseableHttpClient httpClient, String restUrl, String tokenId) {
		return new HttpDeleteExecutor(httpClient, restUrl, tokenId);
	}	

	private HttpDeleteExecutor(CloseableHttpClient httpClient, String restUrl, String tokenId) {
		super(httpClient);
		this.restUrl = restUrl;
		this.tokenId = tokenId;
	}
	
	public HttpRequestBase getHttpRequest(String restURL) {
		return new HttpDelete(restURL);
	}
	
	public void addMoreHeaders(HttpRequestBase httpReq) {}
	   
}