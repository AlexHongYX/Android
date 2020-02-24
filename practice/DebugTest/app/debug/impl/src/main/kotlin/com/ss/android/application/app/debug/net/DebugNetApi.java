package com.ss.android.application.app.debug.net;

import com.bytedance.retrofit2.Call;
import com.bytedance.retrofit2.http.GET;
import com.bytedance.retrofit2.http.Path;
import com.bytedance.retrofit2.http.QueryMap;

import java.util.Map;


/**
 * Created by yihuaqi on 5/21/17.
 */

public interface DebugNetApi {
    @GET("/network/get_network/")
    Call<String> getNetwork();

    @GET("/api/{api_version}/search/hot_words")
    Call<String> getSearchTrending(@Path("api_version") int version, @QueryMap Map<String, Object> queryMap);
}
