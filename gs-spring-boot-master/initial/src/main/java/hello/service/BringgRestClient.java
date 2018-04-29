package hello.service;/*
 *
 * A free Java sample program 
 * to POST to a HTTPS secure SSL website
 *
 * @author William Alexander
 * free for use as long as this comment is included 
 * in the program as it is
 * 
 * More Free Java programs available for download 
 * at http://www.java-samples.com
 *
 */


import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Map;

public class BringgRestClient {

    private HmacUtils hmacUtils;
    private final String accessToken;
    private final String secretKey;

    public BringgRestClient(String accessToken, String secretKey) {
        this.accessToken = accessToken;
        this.secretKey = secretKey;

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public JSONObject sendPostRequest(Map<String, String> parameters, String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        //add request header
        con.setRequestMethod("POST");

        // build url parameters
        String urlParameters = buildRequest(parameters);

        // Send post request
        sendPostParameters(con, urlParameters);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("BringgRestClient parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        //print result
        System.out.println(response.toString());

        return new JSONObject(response.toString());
    }

    private String buildRequest(Map<String, String> parameters) {
        Map<String, String> properties = buildProperties(parameters);
        String urlParameters = processRequestParameters(properties);
        return urlParameters;
    }

    private Map<String, String> buildProperties(Map<String, String> parameters) {
        parameters.put("access_token", accessToken);
        parameters.put("timestamp", String.valueOf(Instant.now().getEpochSecond()));
        String signature = calculateSignature(parameters);
        parameters.put("signature", signature);
        return parameters;
    }

    private String calculateSignature(Map<String, String> parameters) {
        String urlParameters = processRequestParameters(parameters);
        hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, secretKey);
        return hmacUtils.hmacHex(urlParameters);
    }


    /**
     * Convert given Map of parameters to URL-encoded string
     *
     * @param parameters request parameters
     * @return URL-encoded parameters string
     */
    private static String processRequestParameters(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (String parameterName : parameters.keySet()) {
            sb.append(parameterName).append('=').append(urlEncode(parameters.get(parameterName))).append('&');
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Encode given String with URLEncoder in UTF-8
     *
     * @param s string to encode
     * @return URL-encoded string
     */
    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This is impossible, UTF-8 is always supported according to the java standard
            throw new RuntimeException(e);
        }
    }

    /**
     * Send POST parameters to given connection
     *
     * @param con           connection to set parameters on
     * @param urlParameters encoded URL POST parameters
     * @throws IOException
     */
    private static void sendPostParameters(URLConnection con, String urlParameters) throws IOException {
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
    }

    public JSONObject sendGetRequest(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        //add request header
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "authKey");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return new JSONObject(response.toString());
    }
}