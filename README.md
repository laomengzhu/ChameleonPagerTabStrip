# ChameleonPagerTabStrip

一款支持Tab随用户滑动，由未选中状态渐变到选中状态的ViewPager选项卡控件，支持Android2.0+

![Screenshot](https://raw.githubusercontent.com/xiaolifan/ChameleonPagerTabStrip/master/ScreenShot/demo.gif)

## 使用（详见app目录）

### 1、ChameleonPagerTabStrip属性介绍

ChameleonPagerTabStrip有3个属性：
``` xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="ChameleonPagerTabStrip">
		<!--选项卡文字未选中时的字体颜色-->
        <attr name="normalColor" format="color|reference" />
		<!--选项卡文字选中时的字体颜色-->
        <attr name="focusColor" format="color|reference" />
		<!--选项卡文字大小-->
        <attr name="tabTextSize" format="dimension|reference" />
    </declare-styleable>
</resources>
```

### 2、xml中定义控件

``` xml
 <com.fan.chameleonpagertabstrip.ChameleonPagerTabStrip
        android:id="@+id/viewPagerTabStrip"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="#333333"
        app:focusColor="@color/text_color_focus"
        app:normalColor="@color/text_color_normal"
        app:tabTextSize="16sp" />
```

### 3、绑定ViewPager

``` java
MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
viewPager.setAdapter(adapter);
//必需在给ViewPager绑定适配器（setAdapter）之后，才能调用绑定函数，否则无效。
tabScript.setViewPager(viewPager);
```

### 4、IconTabProvider

如果需要在Tab上显示图片，需要你的PagerAdapter实现IconTabProvider接口，并返回图标的资源ID。
``` java
private static final int[] ICONS = new int[] {R.drawable.icon_document, R.drawable.icon_music, R.drawable.icon_movie};
.
.
.
.
@Override
public int getPageIconResId(int position) {
    return ICONS[position];
}
```

## License

    Mozilla Public License, version 2.0