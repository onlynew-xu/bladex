package com.steelman.iot.platform.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpWebUtils {
    /**
     * 连接超时时间
     * 单位：毫秒
     */
    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    /**
     * socket超时时间
     * 单位：毫秒
     */
    private static final int SO_TIMEOUT = 10 * 1000;

    public static final String JsonContentType = "application/json";
    public static final String formUrlencodedContentType = "application/x-www-form-urlencoded";


    /**
     * 默认构造
     */
    private HttpWebUtils() {
    }

    /**
     * HTTP GET请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String get(String url, Map<String, String> paramMap) throws Exception {
        return request(HttpWebRequestMethod.Get, url, paramMap, null, null, SO_TIMEOUT, null);
    }

    public static String post(String url, String json) throws Exception {
        return request(HttpWebRequestMethod.Post, url, null, json, JsonContentType, SO_TIMEOUT, null);
    }

    public static String postFormUrl(String url, Map<String, String> paramMap)throws Exception {
        return request(HttpWebRequestMethod.Post, url, paramMap, null,formUrlencodedContentType, SO_TIMEOUT, null);
    }


    public static String request(String method, String url, Map<String, String> paramMap, String httpEntityData, String
            contentType, int soTimeout, String authorization) throws Exception {

        String result = "";

        // 处理url
        url = processUrl(url, paramMap);
        HttpClient httpClient = getHttpClient(url);
        HttpUriRequest request = createUriRequest(method, url);
        HttpResponse response = null;
        try {
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);


            if (contentType != null) {
                request.addHeader(HTTP.CONTENT_TYPE, contentType);
            }
            if (authorization != null) {
                request.addHeader("Authorization", authorization);
            }

            // 设置HttpEntity
            if (httpEntityData != null) {
                method = method.toUpperCase();
                if (HttpWebRequestMethod.Post.equals(method) || HttpWebRequestMethod.Put.equals(method)) {
                    StringEntity httpEntity = new StringEntity(httpEntityData, "UTF-8");
                    ((HttpEntityEnclosingRequestBase) request).setEntity(httpEntity);
                }
            }
            response = httpClient.execute(request);
            if (response != null && HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
            }
        } catch (Exception e) {
            if (!request.isAborted()) {
                request.abort();
            }
            throw e;
        } finally {
            if (response != null) {
                response.getEntity().getContent();
            }
            if (!request.isAborted()) {
                request.abort();
            }

            httpClient.getConnectionManager().closeExpiredConnections();
            httpClient.getConnectionManager().closeIdleConnections(5, TimeUnit.SECONDS);
            httpClient.getConnectionManager().shutdown();
        }

        return result;
    }

    private static HttpUriRequest createUriRequest(String method, String url) {
        if (HttpWebRequestMethod.Get.equals(method)) {
            return new HttpGet(url);
        }

        if (HttpWebRequestMethod.Post.equals(method)) {
            return new HttpPost(url);
        }

        if (HttpWebRequestMethod.Put.equals(method)) {
            return new HttpPut(url);
        }

        if (HttpWebRequestMethod.Delete.equals(method)) {
            return new HttpDelete(url);
        }

        return new HttpPost(url);
    }

    public static String processUrl(String url, Map<String, String> paramMap) throws Exception {
        String params = prepareParam(paramMap);
        if (!StringUtils.isEmpty(params)) {
            if (url.indexOf("?") == -1 && url.indexOf("&") == -1) {
                url += "?" + params;
            } else {
                url += "&" + params;
            }
        }
        return url;
    }

    private static String prepareParam(Map<String, String> paramMap) throws Exception {
        StringBuilder buffer = new StringBuilder();
        if (paramMap == null) {
            return buffer.toString();
        }

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            if (buffer.length() > 0) {
                buffer.append("&");
            }
            buffer.append(URLEncoder.encode(entry.getKey(), HTTP.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), HTTP.UTF_8));
        }

        return buffer.toString();
    }

    private static HttpClient getHttpClient(String url) {
        url = url.toLowerCase();
        if (url.startsWith("https")) {
            try {
                return getHttpsClient();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ClientConnectionManager ccm = new ThreadSafeClientConnManager();
        return new DefaultHttpClient(ccm);
    }

    /**
     * 获取一个针对https的HttpClient
     */
    private static HttpClient getHttpsClient() throws KeyManagementException, NoSuchAlgorithmException {

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{tm}, null);

        SSLSocketFactory ssf = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, ssf));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);
        return new DefaultHttpClient(ccm);
    }

    private static X509TrustManager tm = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] xcs, String string)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] xcs, String string)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    interface HttpWebRequestMethod {
        String Get = "GET";
        String Post = "POST";
        String Put = "PUT";
        String Delete = "DELETE";
    }
}
