package com.w4lle.library;

import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.agp.components.ScrollView;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.components.AttrSet;
import ohos.agp.components.ComponentContainer;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.animation.AnimatorValue.ValueUpdateListener;
import ohos.agp.components.Component.LayoutRefreshedListener;
import ohos.agp.components.Component;
import ohos.agp.components.Component.DrawTask;
import ohos.agp.render.Canvas;
import ohos.agp.components.Component.TouchEventListener;
import ohos.multimodalinput.event.TouchEvent;
import ohos.agp.components.Component.ScrolledListener;
import com.hmos.compact.utils.AttrUtils;

/**
 * Created by w4lle on 15-9-9.
 * Copyright (c) 2015 Boohee, Inc. All rights reserved.
 */
public class PullDownView extends ScrollView implements LayoutRefreshedListener, DrawTask, TouchEventListener, ScrolledListener {

    private static final float DEFAULTPULLDOWNHEIGHT = 50;

    private static final float DEFAULTPULLUPHEIGHT = 50;

    private int touchSlop;

    private int screenHeight;

    private Component topView;

    private int topViewHeight;

    private Component containerView;

    private float origY;

    private float distanceY;

    private boolean isShowTopView;

    private boolean isonce;

    private ComponentContainer.LayoutConfig layoutParams;

    private int oldTopMargin;

    private boolean isTouchOne;

    private boolean isTop;

    private OnPullChangeListerner onPullChangeListerner;

    private boolean isChangeSpeed;

    private float pullDownHeight;

    private float pullUpHeight;

    public PullDownView(Context context) {
        this(context, null);
        setLayoutRefreshedListener(this);
        addDrawTask(this);
        setTouchEventListener(this);
        setScrolledListener(this);
    }

    public PullDownView(Context context, AttrSet attrs) {
        this(context, attrs, "");
        setLayoutRefreshedListener(this);
        addDrawTask(this);
        setTouchEventListener(this);
        setScrolledListener(this);
    }

    public PullDownView(Context context, AttrSet attrs, java.lang.String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setLayoutRefreshedListener(this);
        addDrawTask(this);
        setTouchEventListener(this);
        setScrolledListener(this);
    }

    private void init(Context context, AttrSet attrs) {
        DisplayAttributes metrics = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        screenHeight = metrics.height;
        topViewHeight = screenHeight;
        touchSlop = 22;
        getAttrs(attrs);
    }

    private void getAttrs(AttrSet attrs) {
        isChangeSpeed = AttrUtils.getBooleanFromAttr(attrs, "is_change_speed", false);
        pullDownHeight = AttrUtils.getDimensionFromAttr(attrs, "pull_down_height", DEFAULTPULLDOWNHEIGHT);
        pullUpHeight = AttrUtils.getDimensionFromAttr(attrs, "pull_up_height", DEFAULTPULLUPHEIGHT);
    }

    @Override
    public void onRefreshed(Component component) {
        if (!isonce) {
            ComponentContainer parentView = (ComponentContainer) this.getComponentAt(0);
            topView = parentView.getComponentAt(0);
            containerView = parentView.getComponentAt(1);
            topView.getLayoutConfig().height = topViewHeight;
            isonce = true;
            layoutParams = containerView.getLayoutConfig();
            oldTopMargin = layoutParams.getMarginTop();
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        // onDraw method
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent ev) {
        switch(ev.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                break;
            case TouchEvent.POINT_MOVE:
                doPointMoveAction(ev);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                doPointUpAction();
                break;
        }
        return true;
    }

    private void doPointMoveAction(TouchEvent ev) {
        if (!isTouchOne) {
            origY = ev.getPointerScreenPosition(0).getY();
            isTouchOne = true;
            isTop = getScrollValue(Component.AXIS_Y) == 0;
        }
        distanceY = ev.getPointerScreenPosition(0).getY() - origY;
        if (distanceY < touchSlop && isShowTopView) {
            return;
        }
        if (distanceY < touchSlop) {
            return;
        }
        if (isTop && distanceY > 0 && !isShowTopView) {
            moveDown((int) distanceY / 2);
        }
    }

    private void doPointUpAction() {
        isTouchOne = false;
        if (isTop && distanceY > pullDownHeight && !isShowTopView) {
            pullDown();
        } else if (distanceY > 0 && distanceY < pullUpHeight && !isShowTopView) {
            pullUp();
        } else if (distanceY < 0 && isShowTopView) {
            pullUp();
            if (onPullChangeListerner != null) {
                onPullChangeListerner.onPullUp();
            }
        }
    }

    private void pullDown() {
        animate(layoutParams.getMarginTop(), screenHeight);
        isShowTopView = true;
        if (onPullChangeListerner != null) {
            onPullChangeListerner.onPullDown();
        }
    }

    private void pullUp() {
        animate(layoutParams.getMarginTop(), oldTopMargin);
        isShowTopView = false;
    }

    private void moveDown(int distance) {
        layoutParams.setMarginTop(distance + oldTopMargin);
        containerView.setLayoutConfig(layoutParams);
    }

    private void animate(int start, int end) {
        AnimatorValue valueAnimator = new AnimatorValue();
        valueAnimator.setDuration(300);
        valueAnimator.setCurveType(ohos.agp.animation.Animator.CurveType.ACCELERATE_DECELERATE);
        valueAnimator.start();
        valueAnimator.setValueUpdateListener(new ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animator, float value) {
                float val = value * (end - start) + start;
                layoutParams.setMarginTop((int) val);
                containerView.setLayoutConfig(layoutParams);
            }
        });
    }

    @Override
    public void onContentScrolled(Component component, int l, int t, int oldl, int oldt) {
        if (t <= topViewHeight && t >= 0 && !isShowTopView && isChangeSpeed) {
            topView.setTranslationY(t / 2);
        }
    }

    public void setOnPullChangeListerner(OnPullChangeListerner onPullChangeListerner) {
        this.onPullChangeListerner = onPullChangeListerner;
    }

    public interface OnPullChangeListerner {

        void onPullDown();

        void onPullUp();
    }
}
