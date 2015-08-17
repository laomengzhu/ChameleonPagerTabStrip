package com.fan.chameleonpagertabstrip;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ChameleonPagerTabStrip extends HorizontalScrollView implements OnPageChangeListener {

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    private OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private boolean checkedTabWidths = false;

    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int tabBackgroundResId = 0;

    /**
     * min margin between tabs
     */
    private int minMargin = 10;
    private int tabTextSize = 14;
    private int focusTextColor, normalTextColor;

    public ChameleonPagerTabStrip(Context context) {
        this(context, null);
    }

    public ChameleonPagerTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChameleonPagerTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
        setWillNotDraw(false);

        int resId;
        Resources resources = context.getResources();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChameleonPagerTabStrip);

        //get tabTextSize from xml
        resId = ta.getResourceId(R.styleable.ChameleonPagerTabStrip_tabTextSize, -1);
        if (resId > 0) {
            tabTextSize = resources.getDimensionPixelSize(resId);
        } else {
            tabTextSize = ta.getDimensionPixelSize(R.styleable.ChameleonPagerTabStrip_tabTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm));
        }

        //get tabNormalTextColor from xml
        resId = ta.getResourceId(R.styleable.ChameleonPagerTabStrip_normalColor, -1);
        if (resId > 0) {
            normalTextColor = resources.getColor(resId);
        } else {
            normalTextColor = ta.getColor(R.styleable.ChameleonPagerTabStrip_normalColor, 0x4cffffff);
        }

        //get tabFocusTextColor from xml
        resId = ta.getResourceId(R.styleable.ChameleonPagerTabStrip_focusColor, -1);
        if (resId > 0) {
            focusTextColor = resources.getColor(resId);
        } else {
            focusTextColor = ta.getColor(R.styleable.ChameleonPagerTabStrip_focusColor, 0xccffffff);
        }

        ta.recycle();

        //add a linearLayout as tab container
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setGravity(Gravity.CENTER_VERTICAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        minMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minMargin, dm);
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        if (pager == null || pager.getAdapter() == null) {
            return;
        }

        tabsContainer.removeAllViews();

        int tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            int iconID = 0;
            if (pager.getAdapter() instanceof IconTabProvider) {
                iconID = ((IconTabProvider) pager.getAdapter()).getPageIconResId(i);
            }
            CharSequence cs = pager.getAdapter().getPageTitle(i);
            if (cs != null || iconID > 0) {
                addTab(i, cs.toString(), iconID);
            }
        }

        checkedTabWidths = false;
    }

    private void addTab(int position, String title, int iconID) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ChameleonTextView tab = new ChameleonTextView(getContext());
        tab.setText(title);
        tab.setFocusable(true);
        tab.setTextColor(Color.argb(255, Color.red(focusTextColor), Color.green(focusTextColor), Color.blue(focusTextColor)));
        tab.setSingleLine();
        tab.setLayoutParams(lp);
        tab.setBackgroundResource(tabBackgroundResId);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
        tab.setCompoundDrawablesWithIntrinsicBounds(0, iconID, 0, 0);
        tabsContainer.addView(tab);

        tab.setTag(position);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem((int) v.getTag(), true);
            }
        });

    }

    private void updateTabStyles() {
        int tabCount = pager == null ? 0 : pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            if (v instanceof ChameleonTextView) {
                ChameleonTextView tab = (ChameleonTextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTextColor(Color.argb(255, Color.red(focusTextColor), Color.green(focusTextColor), Color.blue(focusTextColor)));
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setBackgroundResource(tabBackgroundResId);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        if (checkedTabWidths) {
            return;
        }

        //calculator suitable tab margin
        int myWidth = getMeasuredWidth();
        int childWidthWithMargin = 0;
        int childWidth = 0;
        int tabCount = pager == null ? 0 : pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            int width = tabsContainer.getChildAt(i).getMeasuredWidth();
            childWidth += width;
            childWidthWithMargin += width + 2 * minMargin;
        }

        if (childWidth > 0 && myWidth > 0) {
            if (childWidthWithMargin < myWidth) {
                minMargin = (myWidth - childWidth) / (tabCount * 2);
            }

            for (int i = 0; i < tabCount; i++) {
                final View child = tabsContainer.getChildAt(i);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                lp.leftMargin = minMargin;
                lp.rightMargin = minMargin;
                child.setLayoutParams(lp);
            }
            checkedTabWidths = true;
        }
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setFocusTextColor(int focusTextColor) {
        this.focusTextColor = focusTextColor;
        updateTabStyles();
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        updateTabStyles();
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float progress = positionOffset;

        if (progress > 0.99f) {
            progress = 1f;
        } else if (progress < 0.1f) {
            progress = 0.0f;
        }

        int tabCount = pager == null ? 0 : pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            if (v instanceof ChameleonTextView) {
                ChameleonTextView tab = (ChameleonTextView) v;
                if (i == position) {
                    tab.freshTranslateData(normalTextColor, focusTextColor, progress);
                } else if (i == position + 1) {
                    tab.freshTranslateData(focusTextColor, normalTextColor, progress);
                } else {
                    tab.freshTranslateData(normalTextColor, normalTextColor, progress);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        int childWidth = 0;
        for (int i = 0; i <= position; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth() + 2 * minMargin;
        }
        if (childWidth > getMeasuredWidth()) {
            smoothScrollBy(childWidth - getMeasuredWidth(), 0);
        } else if (childWidth < getMeasuredWidth() && getScrollX() != 0) {
            smoothScrollBy(-getScrollX(), 0);
        }

        if (delegatePageListener != null) {
            delegatePageListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (delegatePageListener != null) {
            delegatePageListener.onPageScrollStateChanged(state);
        }
    }

}
