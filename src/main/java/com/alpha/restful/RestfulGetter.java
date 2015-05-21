package com.alpha.restful;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Alpha on May/20/15.
 */
public class RestfulGetter {

	private static final String HOST = "http://10.10.68.144";
	private static final String PATH = "/v3/products/160626";
	public static void jersey(String host, String path) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(UriBuilder.fromUri(host).build());
		String response = service.path(path).accept(MediaType.APPLICATION_JSON).get(String.class);

		UnitInfoDto dto = fromJson(response, UnitInfoDto.class);

		System.out.println(dto);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
	}

	public static void main(String[] args) {
		jersey(HOST, PATH);
	}



	String APP_ID = "XXX";
	String REST_API_KEY = "YYY";

	String header = "-H \"X-Parse-Application-Id: " + APP_ID
		+ "\" -H \"X-Parse-REST-API-Key: " + REST_API_KEY
		+ "\" -H \"Content-Type: application/zip\" ";

	public void post2Parse(String fileName) {

		String outputString;

		String command = "curl -X POST " + header + "--data-binary '@"
			+ fileName + "' https://api.parse.com/1/files/" + fileName;

		System.out.println(command);

		Process curlProc;
		try {
			curlProc = Runtime.getRuntime().exec(command);

			DataInputStream curlIn = new DataInputStream(curlProc.getInputStream());

			while ((outputString = curlIn.readLine()) != null) {
				System.out.println(outputString);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
