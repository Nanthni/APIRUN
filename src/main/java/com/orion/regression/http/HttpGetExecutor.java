package com.orion.regression.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * HTTP utlity class to perform "Get" based REST call
 * 
 * @author Vijay Anand
 *
 */
public class HttpGetExecutor extends HttpBaseExecutor{
	
	public static HttpGetExecutor getInstance(CloseableHttpClient httpClient, String restUrl, String tokenId) {
		return new HttpGetExecutor(httpClient, restUrl, tokenId);
	}	

	private HttpGetExecutor(CloseableHttpClient httpClient, String restUrl, String tokenId) {
		super(httpClient);
		this.restUrl = restUrl;
		this.tokenId = tokenId;		
	}	
		
	public HttpRequestBase getHttpRequest(String restURL) {
		return new HttpGet(restURL);
	}
	
	public void addMoreHeaders(HttpRequestBase httpReq) {}
	  
}