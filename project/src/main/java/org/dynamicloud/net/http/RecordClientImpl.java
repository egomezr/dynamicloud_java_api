package org.dynamicloud.net.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.dynamicloud.exception.DynamiCloudServiceException;
import org.dynamicloud.logger.LoggerTool;
import org.dynamicloud.util.ConfigurationProperties;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * This class represents a http client like a browser
 * to execute request on <a href="https://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class RecordClientImpl implements RecordClient {
    public static final int MINUTES_TIMEOUT = 10 * 1000;
    public static final String DYNAMICLOUD_CLIENT = "Dynamicloud Client";
    private static final LoggerTool log = LoggerTool.getLogger(RecordClient.class);
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
     * Execute a request using form parameters
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @param method method to use
     * @return response from <a href="https://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    public String executeRequest(URI uri, Map<String, String> params, HttpMethod method) throws DynamiCloudServiceException {
        HttpClient httpclient;
        if (uri.getScheme().equalsIgnoreCase("https")) {
            SSLContext sslcontext;
            try {
                sslcontext = SSLContexts.custom()
                        .loadTrustMaterial(null, new TrustStrategy() {
                            public boolean isTrusted(final X509Certificate[] chain, final String authType)
                                    throws CertificateException {
                                return true;
                            }
                        }).build();
            } catch (NoSuchAlgorithmException e) {
                throw new DynamiCloudServiceException(e.getMessage());
            } catch (KeyManagementException e) {
                throw new DynamiCloudServiceException(e.getMessage());
            } catch (KeyStoreException e) {
                throw new DynamiCloudServiceException(e.getMessage());
            }

            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            RequestConfig config = RequestConfig.custom().setConnectTimeout(MINUTES_TIMEOUT).setSocketTimeout(MINUTES_TIMEOUT).build();
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config).build();
        } else {
            httpclient = HttpClients.createDefault();
        }

        Form form = Form.form();
        if (method != HttpMethod.DELETE && method != HttpMethod.GET) {
            for (String key : params.keySet()) {
                form.add(key, params.get(key));
            }
        }

        HttpUriRequest req = getMethod(method, uri);

        req.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        req.addHeader("Origin", "Dynamicloud://API");
        req.addHeader("Accept-Encoding", "gzip, deflate");
        req.addHeader("Dynamicloud-API", "Java");
        req.addHeader("User-Agent", DYNAMICLOUD_CLIENT);
        req.addHeader("API-Version", ConfigurationProperties.getInstance().
                getProperty(ConfigurationProperties.VERSION));

        if (method != HttpMethod.DELETE && method != HttpMethod.GET) {
            HttpEntityEnclosingRequestBase reqWithEntity = (HttpEntityEnclosingRequestBase) req;
            reqWithEntity.setEntity(new UrlEncodedFormEntity(form.build(), Charset.forName("UTF-8")));
        }

        try {
            return httpclient.execute(req, new ResponseHandler<String>() {
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
        } catch (IOException e) {
            throw new DynamiCloudServiceException(e.getMessage());
        }
    }

    private HttpUriRequest getMethod(HttpMethod method, URI uri) {
        HttpUriRequest request;

        if (method == HttpMethod.GET) {
            request = new HttpGet(uri);
        } else if (method == HttpMethod.DELETE) {
            request = new HttpDelete(uri);
        } else if (method == HttpMethod.PUT) {
            request = new HttpPut(uri);
        } else {
            request = new HttpPost(uri);
        }

        return request;
    }

    /**
     * Execute a request using form parameters
     * Default method POST
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @return response from <a href="https://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    @Override
    public String executeRequest(URI uri, Map<String, String> params) throws DynamiCloudServiceException {
        return executeRequest(uri, params, HttpMethod.POST);
    }
}