import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sda.code.model.WeatherModel;

import java.io.IOException;

public class Main   {
    public static void main(String[] args) throws IOException {
        System.out.println("pogoda");

        final String apiKey = "284903ed5e2c6152346bdeffd9a80fc4";
        final String baseUrl = "http://api.openweathermap.org/data/2.5/weather";

        // System.out.println(new WeatherModel());

        final String url = new StringBuilder()
                .append(baseUrl)
                .append("?appid=").append(apiKey)
                .append("&q=").append("Lodz")
                .append("&units=").append("metric")
                .append("&lang=").append("pl")
                .toString();

        // przeklejone
        String responseBody;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
        Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        WeatherModel weather = gson.fromJson(responseBody, WeatherModel.class);

        System.out.println(weather.getName());
        System.out.println(weather.getMain().getTemp() + " C");
    }
}
