package com.dineplan.dinefly.core.api;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.in.ApiError;
import com.dineplan.dinefly.core.api.service.DineplanService;

import eu.livotov.labs.android.robotools.network.RTNetwork;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by dlivotov on 17/08/2016.
 */

public class DineplanApiClient {

    private final static long SIZE_OF_CACHE = 100 * 1024 * 1024; // 100 MiB
    private final String deviceId;
    private final String endpointUrl;
    private final boolean debugMode;
    private String userId;
    private final String http_cache_control_header_in = "public, max-age=10800, max-stale=2419200";
    private final String http_cache_control_header_out_online = "public, max-age=10800";
    private final String http_cache_control_header_out_offline = "public, only-if-cached, max-stale=2419200";
    private DineplanService service;
    private Retrofit retrofit;
    private Cache cache;

    public DineplanApiClient(@NonNull String url, @NonNull String deviceId, @Nullable String userId) {
        this.deviceId = deviceId.replace("http://", "");
        this.endpointUrl = url.toLowerCase().startsWith("http") ? url : ("http://" + url);
        this.debugMode = App.isDebuggable();
        this.userId = userId;
        rebuildAdapters();
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    private void rebuildAdapters() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        if (debugMode) {
            httpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return appendHttpHeaders(chain);
            }
        });

        httpClientBuilder.retryOnConnectionFailure(true);
        httpClientBuilder.connectTimeout(1, TimeUnit.MINUTES);
        httpClientBuilder.readTimeout(5, TimeUnit.MINUTES);
        httpClientBuilder.writeTimeout(5, TimeUnit.MINUTES);

        retrofit = new Retrofit.Builder().baseUrl(endpointUrl).client(httpClientBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(DineplanService.class);
    }

    private Response appendHttpHeaders(Interceptor.Chain chain) throws IOException {
        final Request request = chain.request();
        Request.Builder bulder = request.newBuilder();

        if (!TextUtils.isEmpty(deviceId)) {
            bulder.addHeader(Headers.DeviceID, deviceId);
        }

        if (!TextUtils.isEmpty(userId)) {
            bulder.addHeader(Headers.UserID, userId);
        }

        if (debugMode) {
            bulder.addHeader("Accept-Encoding", "");
        }

        return chain.proceed(bulder.build());
    }

    public ApiError getError(retrofit2.Response<?> response) {
        retrofit2.Converter<okhttp3.ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (Throwable e) {
            error = new ApiError();
            error.setCode(response.code());
            error.setDescription(response.message());
        }

        return error;
    }

    public DineplanService getService() {
        return service;
    }

    public class Headers {

        public final static String DeviceID = "DeviceId";
        public final static String UserID = "UserId";
    }
}
