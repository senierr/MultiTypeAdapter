# MultiTypeAdapter

[![](https://img.shields.io/badge/release-v2.4.0-blue.svg)](https://github.com/senierr/MultiTypeAdapter)
[![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/senierr/MultiTypeAdapter)
[![](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## 目前支持

* 多类型数据
* 多类型视图
* 数据与视图一对一组合
* 数据与视图一对多组合
* 自定义视图类型匹配
* 列表项点击长按事件
* 子控件点击长按事件
* 自定义事件
* 自定义列表项所占列数
* 其他
    * 头部/底部
    * 正在加载/空数据/加载错误/没有网络/自定义状态
    * 加载更多

**[更新日志](CHANGE_LOG.md)**

## 架包引入

#### Gradle

```
maven { url "https://www.jitpack.io" }
```

```
implementation 'com.github.senierr:MultiTypeAdapter:2.4.0'
```

**注意：`MultiTypeAdapter`内部依赖了:**

```
implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10'
implementation 'androidx.recyclerview:recyclerview:1.2.1'
```

## 基本使用

```
// 1. 定义视图包装器
class DefaultWrapper : ViewHolderWrapper<T>(R.layout.item_layout)

// 2. 将包装器注册至适配器
multiTypeAdapter.register(DefaultWrapper())
```

## 进阶使用

### 1. 点击长按事件

```
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

```
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

```
multiTypeAdapter.register(firstWrapper, secondWrapper) {
            return@register if (item.id == 0) FirstWrapper::class.java else SecondWrapper::class.java
        }
```

### 4. 自定义视图类型匹配

**注意：**使用自定义视图类型匹配会使自动匹配失效！

```
multiTypeAdapter.register(
    listOf(data1Wrapper, data2Wrapper, loadMoreWrapper, stateWrapper),
    object : ViewTypeLinker {
        override fun getItemViewType(item: Any): Int {
            ...
        }

        override fun getViewHolderWrapper(itemViewType: Int): ViewHolderWrapper<*> {
            return when (itemViewType) {
                ...
            }
        }
    })
```

## 其他

`头部/底部`、`状态显示`、`加载更多`等，本质上都是一种`数据类型`，以及对应的`ViewHolderWrapper`。

`MultiTypeAdapter`内部提供了简便的处理方式，详见`support`包。

### 1. 头部/底部

只要实现`ViewHolderWrapper`并重写`getSpanSize()`，返回总列数即可占满全宽。

### 2. 状态显示

`BaseStateWrapper`支持`自定义状态`显示。

**注意：**调用`stateWrapper.show...()`时，会清空`MultiTypeAdapter`内部数据，并增加一条新数据`stateBean`，重新加载数据时记得先**clear()、clear()、clear()**!

```
class StateWrapper : BaseStateWrapper(R.layout.item_state) {

    override fun onBindViewHolder(holder: ViewHolder, item: StateBean) {
        when (item.state) {
            STATE_DEFAULT -> { ... }
        }
    }

    companion object {
        const val STATE_DEFAULT = 0
    }
}

multiTypeAdapter.register(StateBean.class, stateWrapper);
stateWrapper.setState(StateWrapper.STATE_DEFAULT);
```

### 3. 加载更多

**注意：** 要在列表数据末添加`加载更多（LoadMoreBean）`类型数据。

```
class LoadMoreWrapper : BaseLoadMoreWrapper(R.layout.item_load_more) {

    override fun onBindViewHolder(holder: ViewHolder, item: LoadMoreBean) {
        val textView = holder.findView<TextView>(R.id.tv_text)
        val progressBar = holder.findView<ProgressBar>(R.id.pb_bar)
        when (item.loadState) {
            LoadMoreBean.STATUS_LOADING -> {
                textView?.setText(R.string.loading)
                progressBar?.visibility = View.VISIBLE
            }
            LoadMoreBean.STATUS_COMPLETED -> {
                textView?.setText(R.string.completed)
                progressBar?.visibility = View.GONE
            }
            LoadMoreBean.STATUS_NO_MORE -> {
                textView?.setText(R.string.no_more)
                progressBar?.visibility = View.GONE
            }
            LoadMoreBean.STATUS_FAILURE -> {
                textView?.setText(R.string.failure)
                progressBar?.visibility = View.GONE
            }
        }
    }
}

setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
multiTypeAdapter.register(LoadMoreBean.class, loadMoreWrapper);
list.add(loadMoreWrapper.getLoadMoreBean());
```

## 混淆

```
-dontwarn com.senierr.adapter.**
-keep class com.senierr.adapter.** { *; }
```

## License
```
Copyright 2022 senierr

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
