package com.ccooy.app3.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.ccooy.app3.base.AppConstants
import com.ccooy.app3.base.AppConstants.BASE_URL
import com.ccooy.app3.base.AppConstants.TIME_OUT_SECONDS
import com.ccooy.app3.base.BaseApplication
import com.ccooy.app3.base.loadings.CommonLoadingViewModel
import com.ccooy.app3.base.loadings.ILoadingDelegate
import com.ccooy.app3.binding.FabAnimateViewModel
import com.ccooy.app3.data.local.db.AppDatabase
import com.ccooy.app3.data.local.db.repo.LoginDatabaseRepository
import com.ccooy.app3.data.local.prefs.PreferencesRepository
import com.ccooy.app3.util.net.BasicAuthInterceptor
import com.ccooy.app3.data.remote.GithubService
import com.ccooy.app3.di.DatabaseInfo
import com.ccooy.app3.di.PreferenceInfo
import com.ccooy.app3.ui.splash.ISplashLocalDataSource
import com.ccooy.app3.ui.splash.SplashLocalDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, RepositoryModule::class])
class AppModule {

    /**
     * 全局
     */
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideGson(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Provides
    @Singleton
    internal fun provideFabViewModel(): FabAnimateViewModel = FabAnimateViewModel()

    @Provides
    @Singleton
    internal fun provideLoadingViewModel(): CommonLoadingViewModel = CommonLoadingViewModel.instance()

    /**
     * 数据库部分
     */
    @Provides
    @Singleton
    internal fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration().build()

    @Provides
    @DatabaseInfo
    internal fun provideDatabaseName(): String = AppConstants.DB_NAME

    @Provides
    @Singleton
    internal fun provideLoginRepository(appDatabase: AppDatabase): LoginDatabaseRepository =
        LoginDatabaseRepository(appDatabase)

    /**
     * SharedPreferences部分
     */
    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String = AppConstants.PREF_NAME

    @Provides
    internal fun providePreference(@PreferenceInfo spName: String): SharedPreferences =
        BaseApplication.INSTANCE.getSharedPreferences(spName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun providePreferencesHelper(sharedPreferences: SharedPreferences): PreferencesRepository =
        PreferencesRepository(sharedPreferences)

    /**
     * 网络连接部分
     */
    @Provides
    @Singleton
    internal fun authInterceptor(): BasicAuthInterceptor = BasicAuthInterceptor()

    @Provides
    @Singleton
    internal fun httpClient(basicAuthInterceptor: BasicAuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(basicAuthInterceptor)
            .build()

    @Provides
    @Singleton
    internal fun retrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    internal fun githubService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)

//    @Provides
//    @Singleton
//    internal fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
//        return CalligraphyConfig.Builder()
//            .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
//            .setFontAttrId(R.attr.fontPath)
//            .build()
//    }


//    @Provides
//    @Singleton
//    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
//        return appDataManager
//    }


//    @Provides
//    @Singleton
//    internal fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper {
//        return appApiHelper
//    }
//
//    @Provides
//    @ApiInfo
//    internal fun provideApiKey(): String {
//        return BuildConfig.API_KEY
//    }

//    @Provides
//    @Singleton
//    internal fun provideProtectedApiHeader(
//        @ApiInfo apiKey: String,
//        preferencesHelper: PreferencesHelper
//    ): ApiHeader.ProtectedApiHeader {
//        return ApiHeader.ProtectedApiHeader(
//            apiKey,
//            preferencesHelper.getCurrentUserId(),
//            preferencesHelper.getAccessToken()
//        )
//    }

}