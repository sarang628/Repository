# 안드로이드 저장소 테스트 환경 설정하기
[TOC]
## 환경설정하기
### 의존성 추가하기

```
    /** HILT */
    implementation "com.google.dagger:hilt-android:$hiltVersion"
    kapt "com.google.dagger:hilt-android-compiler:$hiltVersion"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:$hiltViewModelVersion"
    kapt "androidx.hilt:hilt-compiler:$hiltViewModelVersion"
    annotationProcessor "androidx.hilt:hilt-compiler:$hiltViewModelVersion"

    // For Robolectric tests.
    //testImplementation("com.google.dagger:hilt-android-testing:2.38.1")
    // ...with Kotlin.
    //kaptTest("com.google.dagger:hilt-android-compiler:2.38.1")
    // ...with Java.
    //testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.38.1")
    
    // For instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38.1")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.38.1")
    // ...with Java.
    //androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.38.1")
```

### build.gradle TestRunner 설정하기

androidTest 폴더에 아래 파일 추가하기

```
// A custom runner to set up the instrumented application class for tests.
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
```

```
    defaultConfig {
        ...
        testInstrumentationRunner "해당파일_패키지명.CustomTestRunner"
    }
```

## 로그인 상태 만들기
```
@get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Inject
    lateinit var loggedInUserDao: LoggedInUserDao


    @Test
    fun login() {
        runBlocking {
            loggedInUserDao.insert(
                LoggedInUserData(
                    userId = 4
                )
            )
        }
    }
```
