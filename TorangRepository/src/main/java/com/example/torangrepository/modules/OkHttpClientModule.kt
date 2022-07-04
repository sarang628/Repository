package com.example.torangrepository.modules

import android.content.Context
import com.example.torang_core.util.Logger
import com.example.torangrepository.TorangPreference
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface TorangOkhttpClient {
    fun getHttpClient(): OkHttpClient
}

@Singleton
class TorangOkHttpClientImpl @Inject constructor(@ApplicationContext val context: Context) :
    TorangOkhttpClient {

    override fun getHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.HEADERS
        logger.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logger)
        httpClient.writeTimeout(10, TimeUnit.SECONDS)
        httpClient.connectTimeout(10, TimeUnit.SECONDS)
        httpClient.writeTimeout(10, TimeUnit.SECONDS)
        httpClient.readTimeout(10, TimeUnit.SECONDS)

        /*httpClient.hostnameVerifier(object : HostnameVerifier{
            override fun verify(p0: String?, p1: SSLSession?): Boolean {
                Logger.d("!!!!!$p0")
                Logger.d( "!!!!!${p1?.cipherSuite} \n" +
                        "${p1?.creationTime} \n" +
                        "${p1?.peerHost} \n" +
                        "${p1?.peerPort} \n" +
                        "${p1?.protocol} \n" +
                        "")
                return p0.equals(p1?.peerHost)
            }
        })*/

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("User-Agent", "android")
                .header(
                    "accessToken",
                    TorangPreference().getAccessToken(context)!!
                )
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
//        return getUnsafeOkHttpClient()
    }

    fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                        Logger.d("checkClientTrusted $chain , $authType")
                        Logger.d("chain ${chain?.get(0)}")
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                        Logger.d("checkServerTrusted $chain , $authType")
                        Logger.d("chain ${chain?.get(0)}")
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        Logger.d("getAcceptedIssuers")
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class OkHttpClientModule {
    @Binds
    abstract fun provideOkhttpClientModule(torangOkHttpClientImpl: TorangOkHttpClientImpl): TorangOkhttpClient
}