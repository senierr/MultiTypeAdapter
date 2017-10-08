# RVAdapter

> 将`RecyclerView`比作一本书，`ViewHolder`是**书页**，`Data`则是**内容**（例如：图片、文字等）。
>
> `RecyclerView`的**展现过程**即是一本书的**形成过程**：**工人**（`ViewHolderWrapper`）会创建**书页**（`ViewHolder`），并将**内容**（`Data`）写上去，然后交给**工厂**（`RVAdapter`）将其装订成册。


## 架包引入

### 1. 添加仓库
```java
maven { url 'https://jitpack.io' }
```

### 2. 添加依赖
```java
compile 'com.github.senierr:RVAdapter:RELEASE_VERSION'
```
#### 注意：

`RVAdapter`内部依赖了:
```jaba
compile 'com.android.support:support-annotations:25.3.1'
compile 'com.android.support:recyclerview-v7:25.3.1'
```
依赖关系如下：
```java
+--- com.github.senierr:RVAdapter:RELEASE_VERSION
|    |    +--- com.android.support:support-annotations:25.3.1
|    |    +--- com.android.support:recyclerview-v7:25.3.1
```
如不需要，可通过以下方式关闭**传递性依赖**：
```java
compile('com.github.senierr:RVAdapter:RELEASE_VERSION', {
    transitive = false
})
或者
compile 'com.github.senierr:RVAdapter:RELEASE_VERSION@jar'
```

## 基本使用步骤

### 1. 创建工厂、工人、数据
```java
RVAdapter rvAdapter = new RVAdapter();
ViewHolderWrapper viewHolderWrapper = new ViewHolderWrapper()
List<Object> list = new ArrayList<>();
```

### 2. 分配数据给工人处理
```java
// 分配给单个工人处理
rvAdapter.assign(Data.class)
    .to(new ViewHolderWrapper());
// 分配给多个工人处理
rvAdapter.assign(Data.class)
    .to(viewHolderWrapper...)
    .by(new OneToManyLink() {...});
```

### 3. 添加数据，开始装订
```java
rvAdapter.setItems(list);
recyclerView.setAdapter(rvAdapter);
```



## 点击事件
> **ViewWrapper**提供了基础的点击事件以及更加灵活的自定义。

### 1. 列表项点击事件
```java
public void setOnItemClickListener(OnItemClickListener onItemClickListener)
```

### 2. 子控件点击事件
```java
public void setOnItemChildClickListener(int childId, OnItemChildClickListener onItemChildClickListener)
```

### 3. 自定义事件
> 通过重写`onViewHolderCreate`获取创建的`ViewHolder`，实现灵活的自定义事件。

```java
@Override
public void onViewHolderCreate(@NonNull RVHolder holder) {
    ......
}
```

## 单元格占据列数
> 重写`getSpanSize`，返回自定义占据列数，默认为1。

```java
@Override
public int getSpanSize(T item) {
    return 1;
}
```
