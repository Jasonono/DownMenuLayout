package com.jason.downmenulayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DownMenuLayout extends LinearLayout {

    private Context mContext;
    private View mShadowView;
    private FrameLayout mContentLayout;
    private FrameLayout mContainerView;
    private LinearLayout mMenuTabLayout;
    private BaseMenuAdapter mAdapter;
    private int mMenuContainerHeight;
    private int mCurrentTabPosition = -1;
    private int mAnimatorDuration = 1000;//ms
    private boolean mAnimatorExecute;

    public DownMenuLayout(Context context) {
        this(context, null);
    }

    public DownMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DownMenuLayout);

        array.recycle();
        initLayout();
    }

    private void initLayout() {
        //初始化tabs
        mMenuTabLayout = new LinearLayout(mContext);
        mMenuTabLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        mMenuTabLayout.setOrientation(HORIZONTAL);

        //创建菜单和阴影布局容器
        mContentLayout = new FrameLayout(mContext);
        mContentLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        //阴影布局
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(Color.parseColor("#66CCCCCC"));
        mShadowView.setAlpha(0f);
        mShadowView.setVisibility(GONE);
        setShadowClick(mShadowView);

        //菜单容器布局
        mContainerView = new FrameLayout(mContext);
        mContainerView.setBackgroundColor(Color.WHITE);

        mContentLayout.addView(mShadowView);
        mContentLayout.addView(mContainerView);

        setOrientation(VERTICAL);
        addView(mMenuTabLayout);

        View dividerView = new View(mContext);
        dividerView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, DisplayUtils.dp2px
                (mContext, 1)));
        dividerView.setBackgroundColor(Color.GRAY);
        addView(dividerView);

        addView(mContentLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mMenuContainerHeight = (int) (height * 0.75f);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContainerView.getLayoutParams();
        layoutParams.height = mMenuContainerHeight;
        mContainerView.setLayoutParams(layoutParams);
        mContainerView.setTranslationY(-mMenuContainerHeight);
    }

    public void setAdapter(BaseMenuAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter cannot be null");
        }
        this.mAdapter = adapter;

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View tabView = mAdapter.getTabView(i, mMenuTabLayout);
            mMenuTabLayout.addView(tabView);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tabView.getLayoutParams();
            layoutParams.weight = 1;
            tabView.setLayoutParams(layoutParams);

            //设置tab点击事件
            setTabClick(tabView, i);

            View menuView = mAdapter.getMenuView(i, mContainerView);
            menuView.setVisibility(GONE);
            mContainerView.addView(menuView);
        }
    }

    /**
     * 设置tab item 点击事件
     *
     * @param tabView
     * @param position
     */
    private void setTabClick(final View tabView, final int position) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTabPosition == -1) {
                    openMenu(position, tabView);
                } else {
                    closeMenu();
                }
            }
        });
    }

    private void setShadowClick(View shadowView) {
        shadowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
    }

    /**
     * 关闭菜单
     */
    private  void closeMenu() {
        if (mAnimatorExecute) return;
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mContainerView,
                "translationY", 0, -mMenuContainerHeight);
        translationAnimator.setDuration(mAnimatorDuration);
        translationAnimator.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,
                "alpha", 1f, 0f);
        alphaAnimator.setDuration(mAnimatorDuration);

        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View menuView = mContainerView.getChildAt(mCurrentTabPosition);
                menuView.setVisibility(GONE);
                mShadowView.setVisibility(GONE);
                mCurrentTabPosition = -1;
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
            }
        });
        alphaAnimator.start();
    }

    /**
     * 打开菜单
     *
     * @param position
     * @param tabView
     */
    private  void openMenu(final int position, View tabView) {
        if (mAnimatorExecute) return;
        mShadowView.setVisibility(VISIBLE);
        View menuView = mContainerView.getChildAt(position);
        menuView.setVisibility(VISIBLE);

        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mContainerView,
                "translationY", -mMenuContainerHeight, 0f);
        translationAnimator.setDuration(mAnimatorDuration);
        translationAnimator.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,
                "alpha", 0f, 1f);
        alphaAnimator.setDuration(mAnimatorDuration);

        alphaAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentTabPosition = position;
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
            }
        });
        alphaAnimator.start();
    }


}
