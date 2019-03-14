package br.com.fielo.padiolaJ;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JavarestApi {

	private static final Logger logger = LoggerFactory.getLogger(JavarestApi.class);

	private String tokenUrl = "https://login.salesforce.com/services/oauth2/token";

	private String username  = "rogerio.rs@gmail.com";
	private String password = "p9vANcICMVAQMOpMVkMvm2xDl";//"#Masterkey2019";
	private String consumerKey = "3MVG9KsVczVNcM8woUMnplYhNxdlZbZgNA1pH9VgjJl7PE9QK.Pbs82g1RcxyVS2UkxEU_3hklA==";
	private String consumerSecret = "A6C9C0349DCABB2D480E464F67278CA418204C12FE253F8A009AE718C702DBA1";
	
	//p9vANcICMVAQMOpMVkMvm2xDl

	public void execute() throws URISyntaxException {

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
		} catch (IOException e) {
			logger.error("IOException ", e);
		}

	}
}
