package com.pysun.flowlayout;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class FlowLayout extends ViewGroup {
    private int horizontalSpace;
    private int verticalSpace;
    private Integer lines;


    private List<Row> rows;
    private Row curRow;
    private int preElementBottom;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        horizontalSpace = typed.getDimensionPixelOffset(R.styleable.FlowLayout_flHorizontalSpacing, 20);
        verticalSpace = typed.getDimensionPixelOffset(R.styleable.FlowLayout_flVerticalSpacing, 15);
        lines = typed.getInteger(R.styleable.FlowLayout_flLines, Integer.MAX_VALUE);
        typed.recycle();

        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int paddingRight = getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int useWidth = width - paddingLeft - paddingRight;
        int useHeight = height - paddingTop - getPaddingBottom();
        //开始遍历所有的子控件
        int childCount = getChildCount();
        rows.clear();
        curRow.width = 10000;
        curRow.height = -verticalSpace;
        preElementBottom = paddingTop;
        int flowWidth = paddingLeft;
        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            //获取子控件的尺寸，与测量模式
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(useWidth, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(useHeight, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            //测量子控件
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int childWidth = childView.getMeasuredWidth();//子控件的宽度
            int childHeight = childView.getMeasuredHeight();//子控件的高度


            if (curRow.width + childWidth - paddingLeft > useWidth) {//新加一行

                if (rows.size() >= lines) {

                    break;
                }
                preElementBottom += (curRow.height + verticalSpace);
                if (curRow.width != 10000) {
                    curRow.width -= horizontalSpace;//上一行的宽度的horizontalSpace
                    flowWidth = Math.max(flowWidth, curRow.width);
                }
                rows.add(curRow);
                curRow = new Row(paddingLeft, 0);

            }
            curRow.addElement(new Element(childView, curRow.width, preElementBottom, curRow.width + childWidth, childHeight + preElementBottom));
            curRow.width += (childWidth + horizontalSpace);
            curRow.height = Math.max(curRow.height, childHeight);//行内元素最大高度作为行高
//            if (i == childCount - 1) {//如果最后一行只有一个元素，计算FlowLayout的最终高度
//                preElementBottom += curRow.height;
//                curRow.width -= horizontalSpace;//最后一行的宽度的horizontalSpace
//                rows.add(curRow);
//                rows.remove(0);//移除默认添加的第一行
//                flowWidth = Math.max(flowWidth, curRow.width);//最后一行
//
//            }

        }


        preElementBottom += curRow.height;//最后一行底部Y坐标
        curRow.width -= horizontalSpace;//最后一行的宽度的horizontalSpace
        rows.add(curRow);
        rows.remove(0);//移除默认添加的第一行
        flowWidth = Math.max(flowWidth, curRow.width);//最后一行


        setMeasuredDimension(flowWidth + getPaddingRight(), preElementBottom +

                getPaddingBottom());
    }


    public void init() {
        rows = new ArrayList<>();
        curRow = new Row(10000, -verticalSpace);//默认添加的第一行
        preElementBottom = 0;

        addDefaultDisappearingAnim();

    }

    public void addDefaultDisappearingAnim() {
        LayoutTransition mLayoutTransition = new LayoutTransition();
        PropertyValuesHolder disappearingTranslationY = PropertyValuesHolder.ofFloat("translationY", 10, 0, 10, 0);
        PropertyValuesHolder disappearingTranslationX = PropertyValuesHolder.ofFloat("translationX", 10, 0, 10, 0);
        ObjectAnimator mAnimatorDisappearing = ObjectAnimator.ofPropertyValuesHolder((Object) null, disappearingTranslationY, disappearingTranslationX);
        mAnimatorDisappearing.setDuration(300);
        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, mAnimatorDisappearing);
        setLayoutTransition(mLayoutTransition);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (Row row : rows) {

            for (Element element : row.elements) {
                element.layout();
                element.toString();
            }
        }

    }

    public class Row {
        List<Element> elements;
        int width;
        int height;

        Row(int width, int height) {
            elements = new ArrayList<>();
            this.width = width;
            this.height = height;
        }

        void addElement(Element element) {
            elements.add(element);
        }
    }

    public void setLines(Integer lines) {
        this.lines = lines;
        invalidate();
    }

    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
        invalidate();
    }

    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
        invalidate();
    }

    public class Element {
        private View view;
        private int top, left, right, bottom;

        Element(View view, int top, int left, int right, int bottom) {
            this.view = view;
            this.top = top;
            this.left = left;
            this.right = right;
            this.bottom = bottom;
        }

        public void layout() {
            view.layout(top, left, right, bottom);
        }

        @Override
        public String toString() {
            return "top = " + top + "left = " + left + " right = " + right + " bottom = " + bottom;
        }
    }
}
