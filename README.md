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

#### 在代码中（以Activity为例）
ShadowBanner支持自定义banner item的布局，也可以使用默认布局

以默认布局举例：

```
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化
        ShadowBannerView myBanner = findViewById(R.id.bannerView);
        myBanner.setBannerAdapter(new SimpleBannerAdapter(getCells()));
        // 注意：build()方法请一定要在最后使用
        myBanner.build();
    }

    /**
     * 构建Cell list，根据个人需要定义
     * @return
     */
    private List<ShadowBannerCell> getCells() {
        String url = "http://img5.imgtn.bdimg.com/it/u=1274659812,975303251&fm=26&gp=0.jpg";
        List<ShadowBannerCell> bannerBeans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShadowBannerCell bannerBean = new ShadowBannerCell();
            bannerBean.setTitle("标题" + (i + 1));
            bannerBean.setContent("内容" + (i + 1));
            bannerBean.setImageUrl(url);
            bannerBeans.add(bannerBean);
        }
        return bannerBeans;
    }
}
```

**如果要使用自动轮播**

```
myBanner.setAutoScroll(true);
```

### 高级

**如果你不喜欢默认的布局，可以新建一个布局文件，如：**

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:id="@+id/bannerImage"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

**接着实现自定义的BannerAdapter，如：**

```
public class MyBannerAdapter extends ShadowBannerAdapter<String> {

    public MyBannerAdapter(List<String> list, int layout) {
        super(list, layout);
    }

    @Override
    public void onItemCreate(Context context, ViewHolder holder, String item, int position) {
        ImageView imageView = holder.getView(R.id.bannerImage);
        Glide.with(context)
                .load(item)
                .into(imageView);
    }
}
```

**使用时将自定义的Adapter传入：**

```
MyBannerAdapter bannerAdapter = new MyBannerAdapter(你的list, 你创建的item的布局文件);
myBanner.setBannerAdapter(bannerAdapter);
```
