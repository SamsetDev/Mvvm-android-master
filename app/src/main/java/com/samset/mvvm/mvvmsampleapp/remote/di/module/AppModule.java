package com.samset.mvvm.mvvmsampleapp.remote.di.module;

import android.app.Application;
import android.util.Log;

import com.example.test.mvvmsampleapp.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.samset.mvvm.mvvmsampleapp.remote.di.ViewModelSubComponent;
import com.samset.mvvm.mvvmsampleapp.remote.service.repository.GitHubService;
import com.samset.mvvm.mvvmsampleapp.utils.CommonUtils;
import com.samset.mvvm.mvvmsampleapp.view.viewmodel.baseVM.ViewModelFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.common.net.HttpHeaders.CACHE_CONTROL;

@Module(subcomponents = ViewModelSubComponent.class)
public class AppModule {

    @Singleton
    @Provides
    GitHubService provideGithubService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(GitHubService.HTTPS_API_GITHUB_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GitHubService.class);
    }


    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    public StethoInterceptor getSteltho() {
        return new StethoInterceptor();
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application application, StethoInterceptor stethoInterceptor, HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(CommonUtils.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.readTimeout(CommonUtils.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(getIntercepter(application));
        if (BuildConfig.DEBUG) {
            okHttpClient.addNetworkInterceptor(stethoInterceptor);
            okHttpClient.interceptors().add(httpLoggingInterceptor);
        }

        return okHttpClient.build();
    }


    public Interceptor getIntercepter(final Application application) {

        Interceptor headerAuthorizationInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (!CommonUtils.isNetworkConnected(application)) {
                    Request request = chain.request();
                    CacheControl cacheControl = new CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build();
                    request = request.newBuilder().cacheControl(cacheControl).build();
                    String rawJson = chain.proceed(request).body().string();

                    Log.e("TAG", String.format("req response cache raw JSON response is: %s", rawJson));
                    return chain.proceed(request);
                } else {

                    CacheControl cacheControl = new CacheControl.Builder().maxAge(1, TimeUnit.HOURS).build();
                    Request.Builder request = chain.request().newBuilder();
                    request.addHeader("Accept", "application/json");
                    request.header(CACHE_CONTROL, cacheControl.toString());
                    Response response = chain.proceed(request.build());


                    return response;
                }
            }
        };
        return headerAuthorizationInterceptor;
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    /*@Singleton
    @Provides
    ProjectRepository getrepository() {
        return new ProjectRepository();
    }*/

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {

        return new ViewModelFactory(viewModelSubComponent.build());
    }

}
