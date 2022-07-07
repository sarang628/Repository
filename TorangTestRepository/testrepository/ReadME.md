# MyReviewItemUiState 진행상황

## 쿼리작성
MyReviewDao

## 테스트 데이터 입력
- SeedDatabaseWorker

## 테스트 데이터 파일
- my_reviews.json
- my_reviews1.json
- review_image.json

## TODO
1. 내 리뷰 요청 시 빈값 전달
2. 내 리뷰 요청 시 오류정의하기
3. 내 리뷰 요청 시 요청 타임아웃 테스트

## 테스트 저장소 설정을 위한 가이드
내 리뷰화면의 내리뷰 항목들은 MyReviewsRepository에서 관리합니다.
테스트 저장소에서 MyReviewsRepository를 주입 할 수 있도록 설정해줘야합니다.

## 공통 데이터 베이스 의존성 주입 설정 방법
app_root_package/com.example.torang_core.di/DatabaseModule.kt
```
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    /** 로컬 데이터베이스 제공 */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        //TODO::Build Variants로 변경 할 수 있게 설정하기
        //return AppDatabase.getInstance(context) // 운영
        return TestAppDatabase.getInstance(context) // 테스트
    }
}
```
## 내 리뷰 저장소 의존성 주입 방법
app_root_package/com.example.torang_core.di/RepositoryModule.kt
```
@Module
@InstallIn(SingletonComponent::class)
abstract class MyReviewsRepositoryProvider() {
    @Binds
    abstract fun provideMyReviewsRepository(
        // TODO::Build Variants로 변경 할 수 있게 수정하기 
        // myReviewsRepositoryImpl: MyReviewsRepositoryImpl // 운영 
        myReviewsRepositoryImpl: TestMyReviewsRepository // 테스트
    ): MyReviewsRepository
}
```