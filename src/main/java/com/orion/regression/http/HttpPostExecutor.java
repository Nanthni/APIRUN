package com.orion.regression.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * HTTP utlity class to perform "Post" based REST call
 * 
 * @author Vijay Anand
 *
 */
public class HttpPostExecutor extends HttpBaseExecutor {
	String entity = null;

	public static HttpPostExecutor getInstance(CloseableHttpClient httpClient, String restUrl, String tokenId) {
		return new HttpPostExecutor(httpClient, restUrl, tokenId);
	}

	public static HttpPostExecutor getInstance(CloseableHttpClient httpClient, String restUrl, String tokenId, String entity) {
		return new HttpPostExecutor(httpClient, restUrl, tokenId, entity);
	}

	private HttpPostExecutor(CloseableHttpClient httpClient, String restUrl, String tokenId) {
		super(httpClient);
		this.restUrl = restUrl;
		this.tokenId = tokenId;
	}

	private HttpPostExecutor(CloseableHttpClient httpClient, String restUrl, String tokenId, String entity) {
		super(httpClient);
		this.restUrl = restUrl;
		this.tokenId = tokenId;
		this.entity = entity;
	}

	public HttpRequestBase getHttpRequest(String restURL) {
		return new HttpPost(restURL);
	}

	public void addMoreHeaders(HttpRequestBase httpReq) {
		if (entity != null && !"".equals(entity)) {
			HttpPost httpPost = (HttpPost)httpReq;
			try {
				httpPost.setEntity(new StringEntity(entity));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

}