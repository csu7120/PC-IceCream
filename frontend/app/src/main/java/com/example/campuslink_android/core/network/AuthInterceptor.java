package com.example.campuslink_android.core.network;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final com.example.campuslink_android.core.network.TokenStore store;
    public AuthInterceptor(com.example.campuslink_android.core.network.TokenStore store){ this.store = store; }

    @Override public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        String token = store.get();
        if (token != null) {
            req = req.newBuilder().addHeader("Authorization", "Bearer " + token).build();
        }
        return chain.proceed(req);
    }
}
