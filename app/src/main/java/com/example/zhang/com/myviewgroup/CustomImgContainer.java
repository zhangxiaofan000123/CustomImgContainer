package com.example.zhang.com.myviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 19051 on 2017/2/7.
 */

public class CustomImgContainer  extends ViewGroup{

    private static final String TAG = "CustomImgContainer";

    public CustomImgContainer(Context context) {
        super(context);
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImgContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 計算所有的ChildView的寬度和高度， 然後根據ChildView 的計算結果，設置自己的寬和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("----->", "onMeasure");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获取此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, (heightMode == MeasureSpec.UNSPECIFIED) + "," + sizeHeight
                + "," + getLayoutParams().height);

        //計算所有的childView的宽度和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content 是设置宽和高
         */

        int width = 0 ;
         int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        //用于计算左边两个childView的高度
        int lHeight = 0;
        //用于计算右边两个childView的高度，最终高度取二者之间的大值
        int rHeight = 0;

        //用于计算上边两个childView的宽度
        int tWidth = 0;
        //用于计算下面两个childView的宽度，最终宽度取二者之间的大值
        int bWidht = 0;

        /**
         * 根据childview 计算出的宽和高，以及设置margin计算容器的宽和高，主要用于容器是wrap_content时
         *
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth(); //子控件的宽度
            cHeight = childView.getMeasuredHeight(); //子控件的高度
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            //上面的两个childView
            if (i == 0 || i == 1) {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if(i == 2 || i == 3){
                bWidht = cWidth +cParams.leftMargin+cParams.rightMargin;
            }

            if (i == 0 || i == 2) {
                lHeight = cHeight +cParams.topMargin+cParams.bottomMargin;
            }
            if(i == 1 || i ==3){
                rHeight = cHeight +cParams.topMargin+cParams.bottomMargin;
            }

        }
        width = Math.max(tWidth, bWidht);
        height = Math.max(lHeight, rHeight);

        /**
         * 如果是wrap_content設置為我們计算的值
         * 否则： 直接设置为父容器计算的值
         */

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY)?sizeWidth:width,
                (heightMode == MeasureSpec.EXACTLY)?sizeHeight:height);


    }

    /**
     * onLayout对其所有childView进行定位（设置childView的绘制区域）
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("----->", "onLayout");
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         *   遍歷所有的childView根据其宽度和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            int cl = 0 ,ct = 0, cr = 0 , cb = 0 ;
            switch (i) {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth()-cWidth-/*cParams.leftMargin-*/cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = getHeight() -cHeight -cParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth()-cWidth-/*cParams.leftMargin-*/cParams.rightMargin;
                    ct = getHeight()-cHeight - cParams.bottomMargin;
                    break;
            }
            cr = cl+cWidth;
            cb = ct+cHeight;
            childView.layout(cl,ct,cr,cb);
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        Log.e(TAG, "generateDefaultLayoutParams");
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p)
    {
        Log.e(TAG, "generateLayoutParams p");
        return new MarginLayoutParams(p);
    }
}
