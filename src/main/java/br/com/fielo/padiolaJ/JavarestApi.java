package br.com.fielo.padiolaJ;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class JavarestApi {
	
	 private static final Logger logger = LoggerFactory.getLogger(JavarestApi.class);
	
	 @Value("${security.oauth2.client.access-token-urivalue.from.file}")
	 private String tokenUrl;

	 private String username;
	 private String password;
	 private String consumerKey;
	 private String consumerSecret;
	 
	 public void execute() throws URISyntaxException {
		 
	            System.out.print("Salesforce Username: ");
	            username = System.console().readLine();

	            System.out.print("Salesforce Password: ");
	            password = new String(System.console().readPassword());

	            System.out.print("Salesforce Consumer Key: ");
	            consumerKey = System.console().readLine();

	            System.out.print("Salesforce Consumer Secret: ");
	            consumerSecret = new String(System.console().readPassword());
	        

	        try {
	            // login
	            final CloseableHttpClient httpclient = HttpClients.createDefault();

	            final List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
	            loginParams.add(new BasicNameValuePair("client_id", consumerKey));
	            loginParams.add(new BasicNameValuePair("client_secret", consumerSecret));
	            loginParams.add(new BasicNameValuePair("grant_type", "password"));
	            loginParams.add(new BasicNameValuePair("username", username));
	            loginParams.add(new BasicNameValuePair("password", password));

	            final HttpPost post = new HttpPost(tokenUrl);
	            post.setEntity(new UrlEncodedFormEntity(loginParams));

	            final HttpResponse loginResponse = httpclient.execute(post);

	            // parse
	            final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	            final JsonNode loginResult = mapper.readValue(loginResponse.getEntity().getContent(), JsonNode.class);
	            final String accessToken = loginResult.get("access_token").asText();
	            final String instanceUrl = loginResult.get("instance_url").asText();

	            // query contacts
	            final URIBuilder builder = new URIBuilder(instanceUrl);
	            builder.setPath("/services/data/v39.0/query/").setParameter("q", "SELECT Id, Name FROM Contact");

	            final HttpGet get = new HttpGet(builder.build());
	            get.setHeader("Authorization", "Bearer " + accessToken);

	            final HttpResponse queryResponse = httpclient.execute(get);

	            final JsonNode queryResults = mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

	            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults));
	        }
	        catch (IOException e) {
	        	logger.error("IOException ",e);
	        }
	    
	 }}
