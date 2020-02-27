# ShadowBanner

***年轻人的第一个 Android Banner.***

## 在项目中添加依赖

#### build.gradle (project)

```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

#### build.gradle (app)

```
implementation 'com.github.BarefootBKK:ShadowBanner:1.1.0'
```

## 快速开始

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

**以默认布局举例：**

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

**设置Item点击Listener**

```
myBanner.setOnCellClickListener(new ShadowBannerAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(Object item, int position) {
        // 转换为自己的item类型
        // ShadowBannerCell cellBanner = (ShadowBannerCell) item;
    }
});
```

**开启自动轮播**

```
myBanner.setAutoScroll(true);
```

## 高级

如果你不喜欢默认的布局，可以使用自定义布局

**Step 1: 新建布局文件，如：**

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

**Step 2: 实现自定义的BannerAdapter**

```
public class MyBannerAdapter extends ShadowBannerAdapter<String> {

    public MyBannerAdapter(List<String> list, int layout) {
        super(list, layout);
    }

    @Override
    public void onItemCreate(ViewHolder holder, BannerItem item, int position) {
        ImageView imageView = holder.getView(R.id.bannerImage);
        Glide.with(holder.getContext())
                .load(item)
                .into(imageView);
    }
}
```

**Step 3: 使用时将自定义的Adapter传入**

```
MyBannerAdapter bannerAdapter = new MyBannerAdapter(你的list, 你创建的item的布局文件);

myBanner.setBannerAdapter(bannerAdapter);
```

#### 自定义小圆点样式

如果你不喜欢默认的小圆点样式，可以使用：

```
myBanner.setPointRes(选中时的样式，未选中时的样式);
```
也可以设置颜色（未设置样式时才起作用）：
```
myBanner.setPointsColor(选中时的颜色，未选中时的颜色);
```
移除样式：
```
myBanner.removePointRes();
```

*更多功能可在使用中发现*
