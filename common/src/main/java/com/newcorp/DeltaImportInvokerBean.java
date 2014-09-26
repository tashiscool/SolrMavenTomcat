package com.newcorp;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;


public class DeltaImportInvokerBean 
{
	Logger log = Logger.getLogger(getClass());
	
	@Value("${hostname:localhost}")
	private String hostname;

	@Value("${solradmin:solradmin}")
	private String solradmin;
	@Value("${password:password}")
	private String password;

	private final String DELTA_IMPORT_URL = "https://$HOSTNAME/search/product/dataimport?command=delta-import&commit=false";
	private CredentialsProvider credsProvider = new BasicCredentialsProvider();
	private UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(solradmin, password);
	private AuthScope authScope = new AuthScope("localhost", 443);
	
	
	@Scheduled(cron = "* * */1 * *") //do this every day  
	void doSomethingWith() throws ClientProtocolException, IOException{  
		String url = DELTA_IMPORT_URL.replace("$HOSTNAME", hostname);
		credsProvider.setCredentials(
				authScope,
				usernamePasswordCredentials);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		try {
			
			HttpGet httpget = new HttpGet(url);
			log.debug("executing request" + httpget.getRequestLine()+"\n");
			CloseableHttpResponse response = httpclient.execute(httpget);
			
			try {
				HttpEntity entity = response.getEntity();
				log.debug("----------------------------------------\n");
				log.debug(response.getStatusLine()+"\n");
				if (entity != null) {
					log.debug("Response content length: " + entity.getContentLength());
				}
				log.debug(EntityUtils.toString(entity));
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	} 
}
