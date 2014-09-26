package com.pearson.ed.solr.component;

import com.pearson.ed.solr.SolrServerTester;
import com.pearson.ed.solr.SolrServerTester.CoreType;

import java.util.logging.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DomainSchemaIT {
    private final static Logger log = Logger.getLogger(DomainSchemaIT.class.toString());

    @Before
    public void setUp() throws Exception{
        SolrServerTester.runOnCoreType(new SolrServerTester() {
            @Override
            public void runTest() throws Exception {
                Result result = this.makeUpdateRequest("domain", createXmlToAddDocuments());
                log.finer("Update request to " + result.getURI() + " resulted in status: " + result.getStatusCode());
                Assert.assertEquals(200, result.getStatusCode());
            }
        }, CoreType.MASTER);
    }

    private String createXmlToAddDocuments() {
        StringBuilder xml = new StringBuilder();
        xml.append("<add><doc>");
        xml.append("<field name=\"Id\">11111</field>");
        xml.append("<field name=\"ExternalIdPId\">powerschool-2</field>");
        xml.append("<field name=\"Category\">PowerSchool</field>");
        xml.append("<field name=\"AuthorizationUrl\">powerschool.com/login</field>");
        xml.append("<field name=\"OrganizationName\">IT Domain A</field>");
        xml.append("<field name=\"PostalCodes\">12345</field>");
        xml.append("<field name=\"PostalCodes\">67890</field>");
        xml.append("</doc><doc>");
        xml.append("<field name=\"Id\">22222</field>");
        xml.append("<field name=\"ExternalIdPId\">blackboard-7</field>");
        xml.append("<field name=\"Category\">Blackboard</field>");
        xml.append("<field name=\"AuthorizationUrl\">blackboard.com/login</field>");
        xml.append("<field name=\"OrganizationName\">IT Domain B</field>");
        xml.append("<field name=\"OrganizationName\">IT Domain C</field>");
        xml.append("<field name=\"PostalCodes\">67890</field>");
        xml.append("</doc></add>");
        return xml.toString();
    }

    @Test
    public void testDomainSchema() throws Exception{
        SolrServerTester.runOnMasterSlave(new SolrServerTester() {
            @Override
            public void runTest() throws Exception {
                Result result = this.makeGetRequest("domain", "/select?q=11111");
                log.finer("Request to " + result.getURI() + " resulted in status: " + result.getStatusCode());
                Assert.assertEquals(200, result.getStatusCode());
                String body = result.getBody();
                if (result.getURI().getURI().contains("master")) {
                    assertBodyContains(body, "<result name=\"response\" numFound=\"1\" start=\"0\">");
                    assertBodyContains(body, "<str name=\"Id\">11111</str>");
                    assertBodyContains(body, "<str name=\"ExternalIdPId\">powerschool-2</str>");
                    assertBodyContains(body, "<str name=\"Category\">PowerSchool</str>");
                    assertBodyContains(body, "<str name=\"AuthorizationUrl\">powerschool.com/login</str>");
                    assertBodyContains(body, "<arr name=\"OrganizationName\">");
                    assertBodyContains(body, "<str>IT Domain A</str>");
                    assertBodyContains(body, "<arr name=\"PostalCodes\">");
                    assertBodyContains(body, "<str>12345</str>");
                    assertBodyContains(body, "<str>67890</str>");
                }
                else {
                    // TODO: Figure out replication from master to slave so we can get the two results.
                	// TODO: Replication is set up but needs to be triggered and completed to ensure this record is copied.
                }
            }
        });
    }

    @Test
    public void testQueryOnPostalCode() throws Exception{
        SolrServerTester.runOnMasterSlave(new SolrServerTester() {
            @Override
            public void runTest() throws Exception {
                Result result = this.makeGetRequest("domain", "/select?q=67890");
                log.finer("Request to " + result.getURI() + " resulted in status: " + result.getStatusCode());
                Assert.assertEquals(200, result.getStatusCode());
                String body = result.getBody();
                if (result.getURI().getURI().contains("master")) {
                    assertBodyContains(body, "<result name=\"response\" numFound=\"2\" start=\"0\">");
                    assertBodyContains(body, "<str name=\"Id\">11111</str>");
                    assertBodyContains(body, "<str name=\"Id\">22222</str>");
                }
                else {
                    // TODO: Figure out replication from master to slave so we can get the two results.
                    assertBodyContains(body, "<result name=\"response\" numFound=\"0\" start=\"0\"/>");
                }
            }
        });
    }

    private void assertBodyContains(String body, String searchString) {
        Assert.assertTrue("Expected body to contain: " + searchString, body.contains(searchString));
    }
}
