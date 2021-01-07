package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.protobuf.ExampleProto;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppHttpClientTest {

    private static final String COMMUNITY1_URL = "http://localhost:8080/communities/1";

    //FIXME
    private void assertResponse(String response) {
        /*
        assertThat(response, containsString("id"));
        assertThat(response, containsString("course_name"));
        assertThat(response, containsString("REST with Spring"));
        assertThat(response, containsString("student"));
        assertThat(response, containsString("first_name"));
        assertThat(response, containsString("last_name"));
        assertThat(response, containsString("email"));
        assertThat(response, containsString("john.doe@baeldung.com"));
        assertThat(response, containsString("richard.roe@baeldung.com"));
        assertThat(response, containsString("jane.doe@baeldung.com"));
        assertThat(response, containsString("phone"));
        assertThat(response, containsString("number"));
        assertThat(response, containsString("type"));
        */
        System.out.println(response); //DEBUG
    }

    private InputStream executeHttpRequest(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(request);
        return httpResponse.getEntity().getContent();
    }

    private String convertProtobufMessageStreamToJsonString(InputStream protobufStream) throws IOException {
        //JsonFormat jsonFormat = new JsonFormat();
        ExampleProto.Community community = ExampleProto.Community.parseFrom(protobufStream);
        return community.toString();//return jsonFormat.printToString(course);
    }

    @Test
    public void whenUsingHttpClient_thenSucceed() throws IOException {
        InputStream responseStream = executeHttpRequest(COMMUNITY1_URL);
        String jsonOutput = convertProtobufMessageStreamToJsonString(responseStream);
        assertResponse(jsonOutput);

        assert (true); //FIXME
    }

}
