# MultiTypeAdapter

[![](https://jitpack.io/v/senierr/MultiTypeAdapter.svg)](https://jitpack.io/#senierr/MultiTypeAdapter)
[![](https://img.shields.io/travis/rust-lang/rust.svg)](https://github.com/senierr/SeHttp)

## 目前支持

* 多类型数据
* 多类型视图
* 数据与视图一对一组合
* 数据与视图一对多组合
* 列表项点击长按事件
* 子控件点击长按事件
* 自定义事件
* 自定义列表项所占列数
* 其他
    * 头部/底部
    * 正在加载/空数据/加载错误/没有网络/自定义状态
    * 加载更多

## 架包引入

### 1. 添加仓库
```java
maven { url 'https://jitpack.io' }
```

### 2. 添加依赖
```java
implementation 'com.github.senierr:MultiTypeAdapter:<release_version>'
```

#### 注意：
`MultiTypeAdapter`内部依赖了:
```jaba
implementation 'com.android.support:support-annotations:28.0.0'
implementation 'com.android.support:recyclerview-v7:28.0.0'
```
依赖关系如下：
```java
+--- com.github.senierr:MultiTypeAdapter:<release_version>
|    |    +--- com.android.support:support-annotations:28.0.0
|    |    +--- com.android.support:recyclerview-v7:28.0.0
```
如不需要，可通过以下方式关闭**传递性依赖**：
```java
implementation ('com.github.senierr:MultiTypeAdapter:<release_version>', {
    transitive = false
})
或者
implementation 'com.github.senierr:MultiTypeAdapter:RELEASE_VERSION@aar'
```

## 基本使用
```java
public class FirstWrapper extends ViewHolderWrapper<DataBean> {

    @NonNull @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.item_first);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        ......
    }
}

MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
multiTypeAdapter.register(DataBean.class, new FirstWrapper())
recyclerView.setAdapter(multiTypeAdapter);
```

## 进阶使用

### 1. 点击长按事件
```java
// 列表项点击事件
setOnItemClickListener(OnItemClickListener<T> onItemClickListener)
// 列表项长按事件
setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener)

// 子控件点击事件
setOnChildClickListener(int childId, OnChildClickListener<T> onChildClickListener)
// 子控件长按事件
setOnChildLongClickListener(int childId, OnChildLongClickListener<T> onChildLongClickListener)
```

### 2. 自定义占据列数

```java
/**
 * 获取当前项所占列数
 *
 * @param item 数据项
 * @return 默认为1，超过最大列数为全行
 */
@Override
public int getSpanSize(T item) {
    return 1;
}
```

### 3. 一对多

```java
/**
 * 当一种数据类型，包含多种布局时，可指定每项所使用的布局类型。
 */
multiTypeAdapter.register(DataBean::class.java, firstWrapper, secondWrapper)
                .with { item -> if (item.id == 0) firstWrapper else secondWrapper
        }
```

## 其他

`头部/底部`、`状态显示`、`加载更多`等，本质上都是一种`数据类型`，以及对应的`ViewHolderWrapper`。

`MultiTypeAdapter`内部提供了简便的处理方式，详见`support`包。

### 1. 头部/底部

只要实现`ViewHolderWrapper`并重写`getSpanSize()`，返回总列数即可占满全宽。

### 2. 状态显示

`BaseStateWrapper`支持`自定义状态`显示。

**注意：**调用`stateWrapper.show...()`时，会清空`MultiTypeAdapter`内部数据，并增加一条新数据`stateBean`，重新加载数据时记得先**clear()、clear()、clear()**!

```java
public class StateWrapper extends BaseStateWrapper {

    @NonNull @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.item_state);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull StateBean item) {
        switch (item.getState()) {
            case StateBean.STATE_DEFAULT:
                ......
                break;
        }
    }
}

multiTypeAdapter.register(StateBean.class, stateWrapper);
stateWrapper.setState(StateWrapper.STATE_DEFAULT);
```

### 3. 加载更多

**注意：** 要在列表数据末添加`加载更多（LoadMoreBean）`类型数据。

```java
public class LoadMoreWrapper extends BaseLoadMoreWrapper {

    @NonNull @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.item_load_more);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull LoadMoreBean item) {
        switch (item.getLoadState()) {
            case LoadMoreBean.STATUS_LOADING:
                ......
                break;
            case LoadMoreBean.STATUS_COMPLETED:
                ......
                break;
            case LoadMoreBean.STATUS_NO_MORE:
                ......
                break;
            case LoadMoreBean.STATUS_FAILURE:
                ......
                break;
        }
    }
}

setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
multiTypeAdapter.register(LoadMoreBean.class, loadMoreWrapper);
list.add(loadMoreWrapper.getLoadMoreBean());
```

## 混淆

```java
-dontwarn com.senierr.adapter.**
-keep class com.senierr.adapter.** { *; }
```

## License
```
Copyright 2017 senierr

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```