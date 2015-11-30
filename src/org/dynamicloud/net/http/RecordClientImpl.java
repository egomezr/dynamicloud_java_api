package org.dynamicloud.net.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dynamicloud.logger.LoggerTool;
import org.dynamicloud.util.ConfigurationProperties;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * This class represents a http client like a browser
 * to execute request on <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class RecordClientImpl implements RecordClient {
    public static final int MINUTES_TIMEOUT = 10 * 1000;
    private static final LoggerTool log = LoggerTool.getLogger(RecordClient.class);
    public static final String DYNAMICLOUD_CLIENT = "Dynamicloud Client";
    private static RecordClient instance;

    protected static RecordClient getInstance() {
        if (instance == null) {
            instance = new RecordClientImpl();
        }

        return instance;
    }

    /**
     * This method returns the returned string from DymaiCloud servers.
     *
     * @param is stream object
     * @return String object
     */
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    /**
     * @param uri     uri target
     * @param params  parameters that will be sent in request
     * @param destiny file to store download content
     * @param method  method to use
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     * @throws IOException
     */
    public String downloadFile(URI uri, File destiny, Map<String, String> params, HttpMethod method) throws IOException {
        JSONObject json = new JSONObject();

        try {
            Request req = getMethod(method, uri);

            Form form = Form.form();
            if (method != HttpMethod.DELETE && method != HttpMethod.GET) {
                for (String key : params.keySet()) {
                    form.add(key, params.get(key));
                }
            }

            if (method != HttpMethod.DELETE && method != HttpMethod.GET) {
                req.bodyForm(form.build());
            }
            req.addHeader("User-Agent", DYNAMICLOUD_CLIENT).
                    addHeader("Version", ConfigurationProperties.getInstance().
                            getProperty(ConfigurationProperties.VERSION)).
                    addHeader("Language", "Java");

            req.execute().saveContent(destiny);

            json.put("status", 200);

            return json.toString();
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    /**
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @param file   file to upload
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     * @throws IOException
     */
    public String uploadFile(URI uri, File file, Map<String, String> params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (params != null) {
            for (String key : params.keySet()) {
                builder.addTextBody(key, params.get(key));
            }
        }

        builder.addBinaryBody("files[]", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        httpPost.addHeader("User-Agent", DYNAMICLOUD_CLIENT);
        httpPost.addHeader("Version", ConfigurationProperties.getInstance().getProperty(ConfigurationProperties.VERSION));
        httpPost.addHeader("Language", "Java");

        CloseableHttpResponse response = client.execute(httpPost);

        InputStream content = response.getEntity().getContent();
        String stringFromInputStream = getStringFromInputStream(content);

        client.close();

        if (response.getStatusLine().getStatusCode() == 200) {
            return stringFromInputStream;
        }

        /**
         * Something wrong in DynamiCloud servers
         */
        String message;
        try {
            JSONObject j = new JSONObject(stringFromInputStream);

            message = j.getString("message");

            if (message == null) {
                throw new RuntimeException("Fatal error executing request.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Fatal error executing request (" + e.getMessage() + ").");
        }

        throw new RuntimeException(message);
    }

    /**
     * Execute a request using form parameters
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @param method method to use
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    public String executeRequest(URI uri, Map<String, String> params, HttpMethod method) throws IOException {
        Form form = Form.form();
        if (method != HttpMethod.DELETE && method != HttpMethod.GET) {
            for (String key : params.keySet()) {
                form.add(key, params.get(key));
            }
        }

        Request req = getMethod(method, uri);

        req.version(HttpVersion.HTTP_1_1).connectTimeout(MINUTES_TIMEOUT).
                addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").
                addHeader("Origin", "Dynamicloud://API").
                addHeader("Accept-Encoding", "gzip, deflate").
                addHeader("Dynamicloud_API", "Java").
                addHeader("User-Agent", DYNAMICLOUD_CLIENT).
                addHeader("API_Version", ConfigurationProperties.getInstance().
                        getProperty(ConfigurationProperties.VERSION));

        if (method != HttpMethod.DELETE && method != HttpMethod.GET) {
            req.bodyForm(form.build(), Charset.forName("UTF-8"));
        }

        return req.execute().handleResponse(new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws IOException {
                InputStream content = httpResponse.getEntity().getContent();
                String stringFromInputStream = getStringFromInputStream(content);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    return stringFromInputStream;
                }

                /**
                 * Something wrong in Dynamicloud servers
                 */
                String message;
                try {
                    JSONObject j = new JSONObject(stringFromInputStream);

                    message = j.getString("message");

                    if (message == null) {
                        throw new RuntimeException("Fatal error executing request.");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Fatal error executing request (" + e.getMessage() + ").");
                }

                throw new RuntimeException(message);
            }
        });
    }

    private Request getMethod(HttpMethod method, URI uri) {
        if (method == HttpMethod.GET) {
            return Request.Get(uri);
        } else if (method == HttpMethod.DELETE) {
            return Request.Delete(uri);
        } else if (method == HttpMethod.PUT) {
            return Request.Put(uri);
        }

        return Request.Post(uri);
    }

    /**
     * Execute a request using form parameters
     * Default method POST
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    @Override
    public String executeRequest(URI uri, Map<String, String> params) throws IOException {
        return executeRequest(uri, params, HttpMethod.POST);
    }
}