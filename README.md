# MultiTypeAdapter

#### 灵活、快捷地创建多类型视图的解决方案。

> 如果将`RecyclerView`比作一本书，那么`ViewHolder`就是**书页**，`Data`则是**内容**（例如：图片、文字等）。
>
> `RecyclerView`的**展现过程**即是一本书的**形成过程**：**工人**（`ViewHolderWrapper`）创建**书页**（`ViewHolder`），并将**内容**（`Data`）写上去，然后交给**工厂**（`MultiTypeAdapter`）将其装订成册。

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
multiTypeAdapter.addViewHolderWrappers(new FirstWrapper())
// 4. 设置数据
multiTypeAdapter.setDataList(list);
// 5. 开始装订
recyclerView.setAdapter(multiTypeAdapter);
```

## 进阶使用

### 1. 点击事件
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

`MultiTypeAdapter`内部支持**加载更多**，详见`support`包下`BaseLoadMoreWrapper`。

**加载更多**本质上仍然是：一种特殊类型数据（LoadMoreBean），以及特殊的工人（LoadMoreWrapper），用法与其他一致。

```java
// 1. 定义加载更多工人
public class LoadMoreWrapper extends BaseLoadMoreWrapper {
    public LoadMoreWrapper() {
        super(R.layout.item_third);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull LoadMoreBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        switch (item.getLoadState()) {
            case LoadMoreBean.STATUS_LOADING:
                textView.setText("正在加载...");
                break;
            case LoadMoreBean.STATUS_LOADING_COMPLETED:
                textView.setText("加载完成");
                break;
            case LoadMoreBean.STATUS_LOAD_NO_MORE:
                textView.setText("没有更多");
                break;
            case LoadMoreBean.STATUS_LOAD_FAILURE:
                textView.setText("加载失败");
                break;
        }
    }
}

// 2. 设置加载更多回调
setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
// 3. 雇佣加载更多工人
multiTypeAdapter.addViewHolderWrapper(loadMoreWrapper);
// 4. 列表末添加加载更多类型数据
multiTypeAdapter.getDataList().add(loadMoreWrapper.getLoadMoreBean());

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