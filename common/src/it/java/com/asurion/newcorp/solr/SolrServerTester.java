package com.pearson.ed.solr;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * Test utility class which knows how to make basic HTTP requests to master/slave solr cores running within maven
 * for integration tests.  We have a custom solr.xml file that creates a master and slave version of each solr core
 * we define - master cores run with the prefix "master-" and slave cores run with the prefix "slave".
 * (e.g. "master-domainuser" and "slave-domainuser")
 * 
 *
 */
public abstract class SolrServerTester {
	public enum CoreType {
		MASTER("master"), SLAVE("slave");

		private String prefix;
		
		private CoreType(String prefix) {
			this.prefix = prefix;
		}
		
		String getPrefix() { return this.prefix; }
	}
	
	private final static Logger log = Logger.getLogger(SolrServerTester.class.toString());

	private static final String SOLR_ADMIN = "solradmin";
	private static final String AUTH_REALM = "Search";
	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final String JETTY_PORT = System.getProperty("jetty.server.port");
	private static final String LOOPBACK_BASE_URL = "http://" + SERVER_ADDRESS + ":"+JETTY_PORT+"/search/";
		
	protected HttpClient client;
	protected String url;

	protected CoreType coreType;
	
	private void setClient(HttpClient client) {
		this.client = client;
	}

	private void setUrl(String url) {
		this.url = url;
	}

	public static void runOnCoreType(SolrServerTester tester, CoreType coreType) throws Exception {
		HttpClient client = new HttpClient();
		Credentials solradminCredentials = new UsernamePasswordCredentials(SOLR_ADMIN, SOLR_ADMIN);
		client.getState().setCredentials(new AuthScope(SERVER_ADDRESS,  Integer.parseInt(JETTY_PORT), AUTH_REALM), solradminCredentials);
		tester.setClient(client);
		String endpoint = LOOPBACK_BASE_URL + coreType.getPrefix();
		log.info("Running test using base url " + endpoint);
		tester.setUrl(endpoint);
		tester.setCoreType(coreType);
		tester.runTest();
	}

	/**
	 * Run the created tester instance against both the master and slave cores running in maven.
	 */
	public static void runOnMasterSlave(SolrServerTester tester) throws Exception {

		for (CoreType coreType : CoreType.values()) {
			runOnCoreType(tester, coreType);
		}
	};
	
	private void setCoreType(CoreType coreType) {
		this.coreType = coreType;
	}

	/**
	 * Create a GET request against a given solr core given an endpoint
	 * @param 	coreName the name of the solr core (e.g. domainuser)
	 * @param 	endpoint an string representing the name of the endpoint to call plus parameters to it
	 * 			(e.g. "/select?q=foo"). 
	 */
	protected Result makeGetRequest(String coreName, String endpoint) throws HttpException, IOException {
		GetMethod method = new GetMethod(this.url + "-" + coreName + endpoint);
		log.info("Making GET request to " + method.getURI());

		Result result = new Result(method.getURI(), client.executeMethod(method), method.getResponseBodyAsString());
		method.releaseConnection();
		return result;
	}

	/**
	 * Creates a POST request to write xml data to solr /update endpoint.
	 */
	protected Result makeUpdateRequest(String coreName, String xmlData) throws IOException {
		PostMethod method = new PostMethod(this.url + "-" + coreName  + "/update?commit=true");
		method.setRequestEntity(new StringRequestEntity(xmlData, "text/xml", "UTF-8"));
		log.info("Making POST request to " + method.getURI());

		Result result = new Result(method.getURI(), client.executeMethod(method), method.getResponseBodyAsString());
		method.releaseConnection();
		return result;
	}

	/**
	 * Abstract method that will contain the specific test logic to run
	 */
	public abstract void runTest() throws Exception;		

	/**
	 * Helper class with result of HTTP request.  Contains the http status code returned
	 * and the body of the response.  Has helper method to return the body as a XML document
	 * for parsing using XPath.
	 * 
	 *
	 */
	public class Result {
		public URI getURI() {
			return this.uri;
		}
		public int getStatusCode() {
			return statusCode;
		}

		public String getBody() {
			return body;
		}

		private URI uri;
		private int statusCode;
		private String body;

		public Result(URI uri, int statusCode, String body) {
			this.uri = uri;
			this.statusCode = statusCode;
			this.body = body;
		}
		public Document getBodyAsXMLDocument() throws DocumentException {
			return DocumentHelper.parseText(this.body);
		}
	}
}
