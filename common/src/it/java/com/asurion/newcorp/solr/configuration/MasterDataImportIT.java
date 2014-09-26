package com.pearson.ed.solr.configuration;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpException;
import org.apache.solr.client.solrj.SolrServerException;
import org.dom4j.DocumentException;
import org.junit.Test;

import com.pearson.ed.solr.SolrServerTester;

public class MasterDataImportIT {

	private static String[] cores_both = {
		"product",
		"user"
	};
	
	
	private final static Logger log = Logger.getLogger(MasterDataImportIT.class.toString());
	
	@Test
	/**
	 * See if the component fires when sent a generic query - using debug flag to see details of
	 * what happened in solr.
	 */
	public void testDataImportFull() throws Exception {
		SolrServerTester.runOnMasterSlave(new SolrServerTester() {
			private static final String DATA_IMPORT_ENDPOINT_PREFIX =
					"/dataimport?verbose=on&debug=on&start=0&rows=1&debugQuery=on&command=";

			@Override
			public void runTest() throws SolrServerException, IOException, InterruptedException, DocumentException {
				if (CoreType.MASTER.equals(this.coreType)) {
					for (String core : cores_both) {
						runDataImportTest(core);							
					}					
					if (this.makeGetRequest(cores_search2[0],"/admin/ping").getStatusCode() == 200) {
						for (String core : cores_search2) {
							runDataImportTest(core);							
						}
					}
				}
			}

			private void runDataImportTest(String core) throws HttpException, IOException, DocumentException {
//				assertEquals(core + " isn't running", 
//						200,
//						this.makeGetRequest(core, "/admin/ping").getStatusCode());
				Result result;
				result = this.makeGetRequest(core, DATA_IMPORT_ENDPOINT_PREFIX+"full-import");
				log.info("body:");
				log.info(result.getBody());
				assertTrue("Request to " + result.getURI().getURI() + " resulted in status " + result.getStatusCode(), result.getStatusCode() == 200);				
				assertFalse("Unable to perform full import on core " + core, result.getBody().contains("Indexing failed"));
				result = this.makeGetRequest(core, DATA_IMPORT_ENDPOINT_PREFIX+"delta-import");
				log.info("body:");
				log.info(result.getBody());
				assertTrue("Request to " + result.getURI().getURI() + " resulted in status " + result.getStatusCode(), result.getStatusCode() == 200);				
				assertFalse("Unable to perform delta import on core " + core, result.getBody().contains("Indexing failed"));
//				org.dom4j.Document document = result.getBodyAsXMLDocument();
//				assertNotNull(document.selectNodes("/response/lst[@name='debug']/lst[@name='timing']/lst[@name='prepare']/lst[@name='com.pearson.ed.solr.component.RemoveFieldsSearchComponent']"));
			}			
		});
	}
}
