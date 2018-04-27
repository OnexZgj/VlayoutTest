
按照自己之前写简书的习惯，先上效果图：(图片素材来自于淘宝手机客户端)
![vLayout.gif](http://upload-images.jianshu.io/upload_images/5249989-4876269fc5e08144.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


在研究具体的使用的时候，先看看官方对于vLayout 的介绍和使用方法：
vLayout的官方地址：  https://github.com/alibaba/vlayout
## 官方的介绍：
通过定制化的LayoutManager，接管整个RecyclerView的布局逻辑；LayoutManager管理了一系列LayoutHelper，LayoutHelper负责具体布局逻辑实现的地方；每一个LayoutHelper负责页面某一个范围内的组件布局；不同的LayoutHelper可以做不同的布局逻辑，因此可以在一个RecyclerView页面里提供异构的布局结构，这就能比系统自带的LinearLayoutManager、aridLayoutManager等提供更加丰富的能力。同时支持扩展LayoutHelper来提供更多的布局能力。
#### 主要功能

 * 默认通用布局实现，解耦所有的View和布局之间的关系: Linear, Grid, 吸顶, 浮动, 固定位置等。
	* LinearLayoutHelper: 线性布局
	* GridLayoutHelper:  Grid布局， 支持横向的colspan
	* FixLayoutHelper: 固定布局，始终在屏幕固定位置显示
	* ScrollFixLayoutHelper: 固定布局，但之后当页面滑动到该图片区域才显示, 可以用来做返回顶部或其他书签等
	* FloatLayoutHelper: 浮动布局，可以固定显示在屏幕上，但用户可以拖拽其位置
	* ColumnLayoutHelper: 栏格布局，都布局在一排，可以配置不同列之间的宽度比值
	* SingleLayoutHelper: 通栏布局，只会显示一个组件View
	* OnePlusNLayoutHelper: 一拖N布局，可以配置1-5个子元素
	* StickyLayoutHelper: stikcy布局， 可以配置吸顶或者吸底
	* StaggeredGridLayoutHelper: 瀑布流布局，可配置间隔高度/宽度
 * 上述默认实现里可以大致分为两类：一是非fix类型布局，像线性、Grid、栏格等，它们的特点是布局在整个页面流里，随页面滚动而滚动；另一类就是fix类型的布局，它们的子节点往往不随页面滚动而滚动。
 * 所有除布局外的组件复用，VirtualLayout将用来管理大的模块布局组合，扩展了RecyclerView，使得同一RecyclerView内的组件可以复用，减少View的创建和销毁过程。



#### 如何使用

版本请参考mvn repository上的最新版本（目前最新版本是1.2.6），最新的 aar 都会发布到 jcenter 和 MavenCentral 上，确保配置了这两个仓库源，然后引入aar依赖：

``` gradle 
compile ('com.alibaba.android:vlayout:1.2.6@aar') {
	transitive = true
}
```

或者maven:  
pom.xml
``` xml
<dependency>
  <groupId>com.alibaba.android</groupId>
  <artifactId>vlayout</artifactId>
  <version>1.2.6</version>
  <type>aar</type>
</dependency>
```

初始化```LayoutManager```

``` java
final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
final VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);

recyclerView.setLayoutManager(layoutManager);
```

设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）：

``` java
RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
recyclerView.setRecycledViewPool(viewPool);
viewPool.setMaxRecycledViews(0, 10);

```

**注意：上述示例代码里只针对type=0的item设置了复用池的大小，如果你的页面有多种type，需要为每一种类型的分别调整复用池大小参数。**

我相信看到这里，还是一脸懵逼，接下来带你一步步的实现上面的复杂效果，我们按照上面的功能顺序一步步的去介绍如何实现？
#### 1. LinearLayoutHelper  线性布局

![linear.gif](http://upload-images.jianshu.io/upload_images/5249989-27a5fc73b3e69e9c.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看出和传统的RecycleView的LinearLayoutManager是没有区别的，也就是传统的线性布局管理器。
由于Adapter和正常的RecycleView的Adapter几乎相似，所以只在LinearLayoutHelper  这个的实现去贴出来，其他的代码已上传Github,查看完整代码。

核心代码实现：
```
  DelegateAdapter adapters = new DelegateAdapter(layoutManager, true);
//Linear 布局
  LinearLayoutHelper linearHelper = new LinearLayoutHelper(10);
  adapters.addAdapter(new LinearAdapter(this,lists, linearHelper));
```
LinearAdapter的实现：
```
public class LinearAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutHelper mHelper;
    private List<String> mData;

    public LinearAdapter(Context context, List<String> mData, LayoutHelper helper) {
        this.mContext=context;
        this.mData = mData;
        this.mHelper=helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_linear_layout, parent, false);
        return new RecyclerViewItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RecyclerViewItemHolder recyclerViewHolder = (RecyclerViewItemHolder) holder;
        recyclerViewHolder.tv_name.setText(mData.get(position) );
        recyclerViewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "position:" +position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 正常条目的item的ViewHolder
     */
    private class RecyclerViewItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;

        public RecyclerViewItemHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
```


#### 2. GridLayoutHelper Grid布局， 支持横向的colspan

![grid.gif](http://upload-images.jianshu.io/upload_images/5249989-d91d17f61c4ccae4.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里只需要看上面的网格布局中的东西，即：
![image.png](http://upload-images.jianshu.io/upload_images/5249989-9715005f3fe1cb28.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)这部分的内容。当然，这里和使用GridLayoutManager的效果是一样不做过多的解释。相信大家都看得懂。
核心代码实现：
```
//构造中传入相应的列的数量      
  GridLayoutHelper gridHelper = new GridLayoutHelper(5);
        gridHelper.setMarginTop(30);
//        gridHelper.setWeights(new float[]{20.0f,20.0f,20.0f,20.0f,20.0f});
        //设置垂直方向条目的间隔
        gridHelper.setVGap(5);
        //设置水平方向条目的间隔
        gridHelper.setHGap(5);
        gridHelper.setMarginLeft(30);
        gridHelper.setMarginBottom(30);
        //自动填充满布局，在设置完权重，若没有占满，自动填充满布局
        gridHelper.setAutoExpand(true);
        adapters.addAdapter(new GridHelperAdapter(imgSrc, gridHelper));
```



#### 3. FixLayoutHelper 固定布局，始终在屏幕固定位置显示

![fix
](http://upload-images.jianshu.io/upload_images/5249989-14895746ffbb1df5.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在Demo中，最上面的搜索框使用FixLayoutHelper实现的，但是如果只使用FixLayoutHelper的话，这里会将正常的item挡住一部分，所以介绍一种解决方案，即下面提到的ColumnLayoutHelper 
![fix.png](http://upload-images.jianshu.io/upload_images/5249989-c26df6769405ac84.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
核心代码实现：
```
//FixLayoutHelper,构造传入要固定的位置
        FixLayoutHelper fixHelper=new FixLayoutHelper(0,0);
        adapters.addAdapter(new FixLayoutAdapter(this,fixHelper));
```


#### 4. ScrollFixLayoutHelper固定布局，但之后当页面滑动到该图片区域才显示, 可以用来做返回顶部或其他书签等
这个效果类似于FixLayoutHelper ,所以介绍一下相关属性：

SHOW_ALWAYS：与FixLayoutHelper的行为一致，固定在某个位置；
SHOW_ON_ENTER：默认不显示视图，当页面滚动到这个视图的位置的时候，才显示；
SHOW_ON_LEAVE：默认不显示视图，当页面滚出这个视图的位置的时候显示；

#### 5. FloatLayoutHelper 浮动布局，可以固定显示在屏幕上，但用户可以拖拽其位置
悬浮按钮可以任意拖拽，但是会停留在屏幕的边上，暂时还没有找到可以任意拖动的api,后期更新,在使用的过程中如果添加了点击事件，会出现有时候拖动不了的bug，和adapters中初始化的位置和顺序有关系，建议早加入到adapters集合中，优先初始化。

![float.gif](http://upload-images.jianshu.io/upload_images/5249989-6200e32e3a712265.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

代码实现：
```
        //floatLayoutHelper
        FloatLayoutHelper layoutHelper = new FloatLayoutHelper();
        layoutHelper.setAlignType(FixLayoutHelper.BOTTOM_RIGHT);
        layoutHelper.setDefaultLocation(100, 400);

        adapters.addAdapter(new FloatLayoutAdapter(this,layoutHelper));
```


#### 6. ColumnLayoutHelper  栏格布局，都布局在一排，可以配置不同列之间的宽度比值
![Column.png](http://upload-images.jianshu.io/upload_images/5249989-c26df6769405ac84.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

代码实现：
```
//        //ColumnLayoutHelper
        ColumnLayoutHelper columnLayoutHelper=new ColumnLayoutHelper();
        adapters.addAdapter(new FixLayoutAdapter(this,columnLayoutHelper));
```

#### 7. SingleLayoutHelper 通栏布局，只会显示一个组件View

![single.gif](http://upload-images.jianshu.io/upload_images/5249989-bebd72e70bd15392.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在这个演示图中，我们使用的Banner是使用的SingleLayoutHelper 去实现。


#### 8. OnePlusNLayoutHelper 一拖N布局，可以配置1-5个子元素


![one3.gif](http://upload-images.jianshu.io/upload_images/5249989-279dc432efe08f8c.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这个Helper的实现偶尔会出现图片错位的bug，个人感觉和缓存池有关系（带后期更新）
代码实现：
```
        //onePlusNHelper
        OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
        helper.setBgColor(R.color.colorPrimary);
        helper.setPadding(5, 5, 5, 5);
//设置相关的图片展示权重，最好总和加起来是1
        helper.setColWeights(new float[]{50f});
        helper.setMargin(10, 20, 10, 10);
        adapters.addAdapter(new OneToNAdapter(goodSrc.subList(0, 2), helper));
```


#### 9. StickyLayoutHelper 可以配置吸顶或者吸底

![stick.gif](http://upload-images.jianshu.io/upload_images/5249989-e05821f7d109e567.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

即：![women](http://upload-images.jianshu.io/upload_images/5249989-20d608e31cadd9c9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)这里的悬浮的tablayout使用的是StickyLayoutHelper ，去实现的这种效果。
代码实现：
```
        //吸顶的Helper
        StickyLayoutHelper stickyHelper = new StickyLayoutHelper();
        adapters.addAdapter(new StickyLayoutAdapter(stickyHelper));
```


#### 10. StaggeredGridLayoutHelper 瀑布流布局，可配置间隔高度/宽度
![stag.png](http://upload-images.jianshu.io/upload_images/5249989-e048441204683c4e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
代码实现：
```
        //StaggerGridLayoutHelper
        initStagData();
        StaggeredGridLayoutHelper stagHelp=new StaggeredGridLayoutHelper(2);
        stagHelp.setHGap(5);
        stagHelp.setVGap(5);
        adapters.addAdapter(new StaggeredGridLayoutAdapter(this,stagSrc,stagHelp));
```
好了，到这里vLayout的使用介绍完毕，相信你也有一个大致的了解和认识，代码已上传到Github https://github.com/OnexZgj/VlayoutTest 如果你有相关的问题可以留言，愿与你一起探讨！




