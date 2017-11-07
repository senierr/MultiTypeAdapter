# SeAdapter

[![](https://jitpack.io/v/senierr/SeAdapter.svg)](https://jitpack.io/#senierr/SeAdapter)
[![](https://img.shields.io/travis/rust-lang/rust.svg)](https://github.com/senierr/SeHttp)

## 目前支持

* 多类型数据
* 多类型视图
* 数据与视图一对一组合
* 数据与视图一对多组合
* 列表项及子控件点击长按事件
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
compile 'com.github.senierr:SeAdapter:<release_version>'
```

#### 注意：
`SeAdapter`内部依赖了:
```jaba
compile 'com.android.support:support-annotations:25.3.1'
compile 'com.android.support:recyclerview-v7:25.3.1'
```
依赖关系如下：
```java
+--- com.github.senierr:MultiTypeAdapter:<release_version>
|    |    +--- com.android.support:support-annotations:25.3.1
|    |    +--- com.android.support:recyclerview-v7:25.3.1
```
如不需要，可通过以下方式关闭**传递性依赖**：
```java
compile ('com.github.senierr:SeAdapter:<release_version>', {
    transitive = false
})
或者
compile 'com.github.senierr:SeAdapter:RELEASE_VERSION@aar'
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

SeAdapter seAdapter = new SeAdapter();
seAdapter.bind(DataBean.class, new FirstWrapper())
seAdapter.setDataList(list);
recyclerView.setAdapter(seAdapter);
```

## 进阶使用

### 1. 点击长按事件
```java
// 1. 列表项点击长按事件
setOnItemClickListener(OnItemClickListener onItemClickListener)

// 2. 子控件点击长按事件
setOnItemChildClickListener(int childId, OnItemChildClickListener onItemChildClickListener)
```

### 2. 自定义占据列数

```java
/**
 * 自定义占据列数
 *
 * 默认返回1。
 */
@Override
public int getSpanSize(T item) {
    return 1;
}
```

### 3. 协同/一对多

> **协同/一对多**：即共同处理相同指定类型数据；
> 例如：聊天列表界面，相同的聊天数据（ChatBean），对应不同的布局（**当前用户**和**其他用户**）。

```java
/**
 * 注册多种处理处理器时，指定数据绑定方式
 */
seAdapter.bind(DataBean.class, firstWrapper, secondWrapper)
            .with(new DataBinder<DataBean>() {
                @Override
                public int onBindIndex(@NonNull DataBean item) {
                    if (item.getId() % 2 == 0) {
                        return 0;   // 返回注册的处理器的索引
                    }
                    return 1;
                }
            });
```

## 其他

`头部/底部`、`状态显示`、`加载更多`等，本质上都是一种`数据类型`，以及对应的`ViewHolderWrapper`。

`SeAdapter`内部提供了简便的处理方式，详见`support`包。

### 1. 头部/底部

只要实现`ViewHolderWrapper`并重写`getSpanSize()`，返回总列数即可占满全宽。

### 2. 状态显示

`BaseStateWrapper`内部支持`正在加载`、`空数据`、`加载错误`、`没有网络`及`自定义状态`状态显示。

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
            case StateBean.STATE_LOADING:
                ......
                break;
            case StateBean.STATE_EMPTY:
                ......
                break;
            case StateBean.STATE_ERROR:
                ......
                break;
            case StateBean.STATE_NO_NETWORK:
                ......
                break;
        }
    }
}

seAdapter.bind(StateBean.class, stateWrapper);
stateWrapper.showLoading();         // 正在加载
stateWrapper.showEmpty();           // 空数据
stateWrapper.showError();           // 加载错误
stateWrapper.showNoNetwork();       // 没有网络
stateWrapper.show(state);           // 自定义状态，state必须大于等于0
stateWrapper.hide()                 // 隐藏状态页
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
            case LoadMoreBean.STATUS_LOADING_COMPLETED:
                ......
                break;
            case LoadMoreBean.STATUS_LOAD_NO_MORE:
                ......
                break;
            case LoadMoreBean.STATUS_LOAD_FAILURE:
                ......
                break;
        }
    }
}

setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
seAdapter.bind(LoadMoreBean.class, loadMoreWrapper);
list.add(loadMoreWrapper.getLoadMoreBean());
```

## 混淆

```java
-dontwarn com.senierr.seadapter.**
-keep class com.senierr.seadapter.** { *; }
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