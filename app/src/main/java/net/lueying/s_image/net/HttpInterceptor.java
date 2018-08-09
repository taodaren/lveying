package com.gongyujia.app.api;

import android.text.TextUtils;
import android.util.Log;

import com.gongyujia.app.BuildConfig;
import com.gongyujia.app.constant.UserConstant;
import com.gongyujia.app.core.App;
import com.gongyujia.app.util.CyptoUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by jt on 2016/10/26.
 */
public class HttpInterceptor implements Interceptor {

    private static final String TAG = "HttpClient";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private boolean isDebug = (BuildConfig.BUILD_TYPE == "debug");
    public final static String PRIVATE_TOKEN_KEY = "private-token";

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = App.getApplication().getConfig(UserConstant.TOKEN);
        if (token != null && !TextUtils.isEmpty(token)) {
            token = CyptoUtil.decode(UserConstant.TOKEN, token);
        } else {
            token = "";
        }
        Request request = chain.request();
        if (request.body() instanceof FormBody) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();

            //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
            for (int i = 0; i < formBody.size(); i++) {
                bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
            }

            formBody = bodyBuilder
                    .addEncoded(UserConstant.APP_ID, "0008")
                    .addEncoded(UserConstant.TOKEN, token)
                    .addEncoded(UserConstant.CITY_ID, App.getApplication().getCurrentCitiId() + "")
                    .addEncoded(UserConstant.SIGN, CyptoUtil.encodeByMd5("0008GYJANDRIODPWD"))
                    .addEncoded(UserConstant.VERSION, "10")
                    .addEncoded(UserConstant.UDID, App.getApplication().getAppId())
                    .build();

            request = request.newBuilder().post(formBody).build();
        }

        if (!isDebug) {
            return chain.proceed(request);
        }

        //the request url
        String url = request.url().toString();
        //the request method
        String method = request.method();
        long t1 = System.nanoTime();
        Log.d(TAG, String.format(Locale.getDefault(), "Sending %s request [url = %s]", method, url));
        //the request body
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            StringBuilder sb = new StringBuilder("Request Body [");
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset));
                sb.append(" (Content-Type = ").append(contentType.toString()).append(",")
                        .append(requestBody.contentLength()).append("-byte body)");
            } else {
                sb.append(" (Content-Type = ").append(contentType.toString())
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
            sb.append("]");
            Log.d(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        //the response time
        Log.d(TAG, String.format(Locale.getDefault(), "Received response for [url = %s] in %.1fms", url, (t2 - t1) / 1e6d));

        //the response state
        Log.d(TAG, String.format(Locale.CHINA, "Received response is %s ,message[%s],code[%d]", response.isSuccessful() ? "success" : "fail", response.message(), response.code()));

        //the response data
        ResponseBody body = response.body();

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.defaultCharset();
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String bodyString = buffer.clone().readString(charset);
        Log.d(TAG, String.format("Received response json string [%s]", bodyString));

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public static String getUserAgent() {
        StringBuilder ua = new StringBuilder("yopark.com");
        ua.append("/appVersion=" + App.getApplication().getPackageInfo().versionName + '_' + App.getApplication().getPackageInfo().versionCode);//App版本
        ua.append("/os=Android");
        ua.append("/version=" + android.os.Build.VERSION.RELEASE);//手机系统版本
        ua.append("/model=" + android.os.Build.MODEL); //手机型号
        ua.append("/appId=" + App.getApplication().getAppId());//客户端唯一标识
        return ua.toString();
    }
}
