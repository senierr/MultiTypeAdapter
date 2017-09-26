# RVAdapter

针对RecyclerView多类型视图的解决方案之一，主要原理为**数据驱动视图**；


## 导入架包

1. 添加仓库：
```java
maven { url 'https://jitpack.io' }
```

2. 添加依赖：
```java
compile 'com.android.support:support-annotations:VERSION'
compile 'com.android.support:recyclerview-v7:VERSION'

compile 'com.github.senierr:RVAdapter:VERSION'
```
## 使用

1. 创建ViewWrapper

2. 创建RVAdapter

3. 注册数据类型(Object)对应的布局类型(ViewWrapper)

4. 添加数据，刷新

**部分示例如下：**

```java
// 创建适配器
rvAdapter = new RVAdapter();
// 注册一对一关系
rvAdapter.register(SelectionBean.class)
        .with(new SelectionWrapper());
rvAdapter.register(FooterBean.class)
        .with(new FooterWrapper());
rvAdapter.register(HeaderBean.class)
        .with(new HeaderWrapper());
// 注册一对多关系
rvAdapter.register(NormalBean.class)
        .with(new NormalWrapper1(),
                new NormalWrapper2())
        .by(new OneToManyBinder<NormalBean>() {
            @Override
            public Class<? extends ViewWrapper<NormalBean>> onGetWrapperType(@NonNull NormalBean item) {
                if (item.getId() < 3) {
                    return NormalWrapper1.class;
                }
                return NormalWrapper2.class;
            }
        });
// 设置点击事件
rvAdapter.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(RVHolder viewHolder, int position) {
        showToast("onItemClick: " + position);
    }

    @Override
    public boolean onItemLongClick(RVHolder viewHolder, int position) {
        showToast("onItemLongClick: " + position);
        return true;
    }
});
// 设置适配器
recyclerView.setAdapter(rvAdapter);
```