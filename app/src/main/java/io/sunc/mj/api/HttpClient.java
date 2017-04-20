package io.sunc.mj.api;

import android.util.Log;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 匿名 SSL HttpClient
 */
public class HttpClient {

    public static OkHttpClient getDefaultHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(loggingInterceptor);

        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .body(new DebugResponseBody(request, response.body()))
                        .build();
            }
        });

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
            builder.hostnameVerifier(new TrustAnyHostnameVerifier());
        } catch (KeyManagementException ignored) {
        } catch (NoSuchAlgorithmException ignored) {
        }
        return builder.build();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 打印请求内容
     */
    private static class DebugResponseBody extends ResponseBody {
        private final Request request;
        private final ResponseBody responseBody;
        private BufferedSource bufferedSource;
        private Buffer buffer = new Buffer();
        private long count = 0;

        DebugResponseBody(Request request, ResponseBody responseBody) {
            this.request = request;
            this.responseBody = responseBody;
            Log.d("Http", "Http start: " + request.toString());
            Log.d("Http", "Http headers: " + request.headers());
            if (request.method().equals("POST")) {
                StringBuilder params = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    int postParamsSize = ((FormBody) request.body()).size();
                    for (int i = 0; i < postParamsSize; i++) {
                        params.append(((FormBody) request.body()).encodedName(i) + "=" + ((FormBody) request.body()).encodedValue(i) + "\n");
                    }
                    Log.d("Http", "Http post params: " + params.toString());
                }
            }
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        @Override
        public void close() {
            String data = buffer.readByteString().utf8();
            Log.d("Http", "Http data byte length:" + data.getBytes().length);
            Log.d("Http", "Http data string length:" + data.length());
            Log.d("Http", "Http end: [" + request.url().toString() + "]" + " Data: " + data);
            buffer.close();
            super.close();
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead;
                    bytesRead = super.read(sink, byteCount);
                    if (bytesRead > 0) {
                        sink.copyTo(buffer, count, bytesRead);
                        count += bytesRead;
                    }
                    return bytesRead;
                }
            };
        }
    }
}
