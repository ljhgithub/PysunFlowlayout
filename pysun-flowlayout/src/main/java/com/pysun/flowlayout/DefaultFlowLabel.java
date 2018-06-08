package com.pysun.flowlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DefaultFlowLabel implements View.OnClickListener {

    private TextView tvLabelName;
    private ImageView ivLabelClose;
    private FrameLayout labelView;
    private FlowLayout flowLayout;
    private boolean editable = false;
    private Callback<Label> mLabelCallback;
    private Label mLabel;

    public DefaultFlowLabel(Context context, FlowLayout flowLayout) {
        this(context, flowLayout, false);
    }

    public DefaultFlowLabel(Context context, FlowLayout flowLayout, boolean mode) {
        this.flowLayout = flowLayout;
        this.labelView = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.default_label_view, null, false);
        flowLayout.addView(labelView);
        this.flowLayout.setClipChildren(false);
        this.flowLayout.setClipToPadding(false);
        this.tvLabelName = this.labelView.findViewById(R.id.label_name);
        this.ivLabelClose = this.labelView.findViewById(R.id.label_close);
        this.ivLabelClose.setVisibility(mode ? View.VISIBLE : View.GONE);
        this.labelView.setOnClickListener(this);

    }

    public void setLabel(Label label) {
        if (null == label) return;
        this.mLabel = label;
        this.tvLabelName.setText(mLabel.name);
        this.tvLabelName.setTag(mLabel.id);
    }

    public Label getLabel() {
        return mLabel;
    }

    public void setLabelCallback(Callback<Label> labelCallback) {
        this.mLabelCallback = labelCallback;
    }

    public FrameLayout getLabelView() {
        return labelView;
    }


    public void setEditable(boolean editable) {
        this.editable = editable;
        this.ivLabelClose.setVisibility(editable ? View.VISIBLE : View.GONE);
    }

    public boolean getEditable() {
        return editable;
    }

    @Override
    public void onClick(View v) {
        if (editable) {
            flowLayout.removeView(labelView);
            if (null != mLabelCallback) {
                mLabelCallback.onRemove(mLabel);
            }
        } else {
            if (null != mLabelCallback) {
                mLabelCallback.onSelected(mLabel);
            }
        }
    }


    public interface Callback<T> {

        void onSelected(T t);

        void onRemove(T t);

    }

    public static class Label {
        public String name;
        public String id;

        public Label(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }
}
