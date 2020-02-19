# ShadowBanner

***年轻人的第一个 Android Banner***

### 在项目中添加依赖

#### build.gradle(project)

```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

#### build.gradle(app)

```
implementation 'com.github.BarefootBKK:ShadowBanner:v1.0.0-beta.02'
```

### 快速开始

#### 在布局文件中添加

```
<com.bkk.mybanner.ShadowBannerView
        android:id="@+id/bannerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintDimensionRatio="16:9" />
```
