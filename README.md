# MultiTypeAdapter

#### 灵活、便捷的创建多类型视图解决方案。

## 前言

> 如果将`RecyclerView`比作一本书，那么`ViewHolder`就是**书页**，`Data`则是**内容**（例如：图片、文字等）。
>
> `RecyclerView`的**展现过程**即是一本书的**形成过程**：**工人**（`ViewHolderWrapper`）创建**书页**（`ViewHolder`），并将**内容**（`Data`）写上去，然后交给**工厂**（`MultiTypeAdapter`）将其装订成册。

`MultiTypeAdapter`的目标：**创建任意内容的书。**

## 目前支持

* 多类型数据
* 多类型视图
* 数据与视图一对一组合
* 数据与视图一对多组合
* 列表项及子控件点击长按事件
* 自定义事件
* 自定义ViewHolder
* 自定义列表项所占列数
* 头部/底部
* 正在加载/空数据/加载错误/没有网络/自定义状态`
* 加载更多
* 其他

## 架包引入

### 1. 添加仓库
```java
maven { url 'https://jitpack.io' }
```

### 2. 添加依赖
```java
compile 'com.github.senierr:MultiTypeAdapter:RELEASE_VERSION'
```

#### 注意：
`MultiTypeAdapter`内部依赖了:
```jaba
compile 'com.android.support:support-annotations:25.3.1'
compile 'com.android.support:recyclerview-v7:25.3.1'
```
依赖关系如下：
```java
+--- com.github.senierr:MultiTypeAdapter:RELEASE_VERSION
|    |    +--- com.android.support:support-annotations:25.3.1
|    |    +--- com.android.support:recyclerview-v7:25.3.1
```
如不需要，可通过以下方式关闭**传递性依赖**：
```java
compile('com.github.senierr:MultiTypeAdapter:RELEASE_VERSION', {
    transitive = false
})
或者
compile 'com.github.senierr:MultiTypeAdapter:RELEASE_VERSION@jar'
```

## 基本使用
```java
// 1. 定义工人
public class FirstWrapper extends ViewHolderWrapper<DataBean> {

    public FirstWrapper() {
        /**
         * 构造函数，指定工人的技能：能处理的数据类型和布局类型
         *
         * 注：一名工人只负责一类数据和一类布局的处理，细分职责粒度，方便重用。
         */
        super(DataBean.class, R.layout.item_first);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        // 定义如何排版，数据如何写入
        ......
    }
}

// 2. 创建工厂
MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter();
// 3. 雇佣工人
multiTypeAdapter.register(new FirstWrapper())
// 4. 设置数据
multiTypeAdapter.setDataList(list);
// 5. 开始装订
recyclerView.setAdapter(multiTypeAdapter);
```

## 进阶使用

### 1. 点击长按事件
```java
// 1. 列表项点击长按事件
setOnItemClickListener(OnItemClickListener onItemClickListener)

// 2. 子控件点击长按事件
setOnItemChildClickListener(int childId, OnItemChildClickListener onItemChildClickListener)
```

### 2. 自定义ViewHolder

```java
/**
 * 通过重写onCreateViewHolder函数，获取父类创建的RVHolder，实现灵活的自定义事件；
 * 或者返回创建的自定义RVHolder。
 */
@Override @NonNull
public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    RVHolder rvHolder = super.onCreateViewHolder(parent);
    ......
    return rvHolder;
}
```

### 3. 自定义占据列数

```java
/**
 * 重写getSpanSize函数，返回自定义占据列数，默认为1。
 */
@Override
public int getSpanSize(T item) {
    return 1;
}
```

### 4. 协同/一对多

> **协同/一对多**：即一种数据类型分配给多种工人共同处理；
> 例如：聊天列表界面，相同的聊天数据（ChatBean），对应不同的布局（**当前用户**和**其他用户**）。

```java
/**
 * 协同处理相同类型数据的工人都会收到询问：是否接受分配数据onAcceptAssignment()。
 * 重写onAcceptAssignment()函数，选择是否接受分配，默认接受true。
 *
 * 原理为：轮询拦截机制，所以必须保证一个数据只有一个处理，否则数据会分配给优先添加的工人（ViewHolderWrapper）。
 */
@Override
public boolean onAcceptAssignment(T item) {
    ......
    return true;
}
```

## 其他

`头部/底部`、`状态显示`、`加载更多`等，本质上都是一种`数据类型`，以及对应的`ViewHolderWrapper`。

`MultiTypeAdapter`内部提供了简便的处理方式，详见`support`包下。

### 1. 头部/底部

只要实现`ViewHolderWrapper`并重写`getSpanSize()`，返回总列数即可占满全宽。

### 2. 状态显示

`MultiTypeAdapter`内部支持`正在加载`、`空数据`、`加载错误`、`没有网络`及`自定义状态`状态显示。

**注意：**`状态显示`时，会清空`MultiTypeAdapter`内部数据，并增加一条数据`stateBean`，重新加载数据时记得先**clear()、clear()、clear()**!

```java
public class StateWrapper extends BaseStateWrapper {
    public StateWrapper() {
        super(R.layout.item_state);
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

multiTypeAdapter.register(stateWrapper);
stateWrapper.showLoading();         // 正在加载
stateWrapper.showEmpty();           // 空数据
stateWrapper.showError();           // 加载错误
stateWrapper.showNoNetwork();       // 没有网络
stateWrapper.refreshView(state);    // 自定义状态
```

### 3. 加载更多

**注意：** 要在列表数据末添加`加载更多`类型数据。

```java
public class LoadMoreWrapper extends BaseLoadMoreWrapper {
    public LoadMoreWrapper() {
        super(R.layout.item_third);
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
multiTypeAdapter.register(loadMoreWrapper);
list.add(loadMoreWrapper.getLoadMoreBean());
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