package com.pysun.flowlayout.demo;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pysun.flowlayout.DefaultFlowLabel;
import com.pysun.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DefaultFlowLabel.Callback<DefaultFlowLabel.Label> {


    private Button btnEidt;
    private FlowLayout flLabel,flLabelSpread,flLabelSpreadInside;
    List<DefaultFlowLabel> defaultFlowLabels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEidt = findViewById(R.id.btn_edit);
        flLabel = findViewById(R.id.fl_label_edit);
        btnEidt.setOnClickListener(this);
        DefaultFlowLabel defaultFlowLabel;
        for (int i = 0; i < 15; i++) {
            defaultFlowLabel = new DefaultFlowLabel(this, flLabel);
            defaultFlowLabel.setLabel(new DefaultFlowLabel.Label("你好," + i, i + ""));
            defaultFlowLabel.setLabelCallback(this);
            defaultFlowLabels.add(defaultFlowLabel);

        }


        flLabelSpread = findViewById(R.id.fl_label_spread);
        flLabelSpread.setChainType(FlowLayout.SPREAD);
        flLabelSpread.setHorizontalSpace(20);
        flLabelSpread.setVerticalSpace(20);

        for (int i = 0; i < 30; i++) {
            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.BLUE);
            textView.setText("你好," +i + "");
            flLabelSpread.addView(textView);

        }



        flLabelSpreadInside = findViewById(R.id.fl_label_spread_inside);
        flLabelSpreadInside.setChainType(FlowLayout.SPREAD_INSIDE);
        flLabelSpreadInside.setHorizontalSpace(40);
        flLabelSpreadInside.setVerticalSpace(20);

        for (int i = 0; i < 20; i++) {
            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.BLUE);
            textView.setText("你好," +i + "");
            flLabelSpreadInside.addView(textView);

        }
    }

    @Override
    public void onClick(View v) {
        btnEidt.setSelected(!btnEidt.isSelected());
        boolean isEdit = btnEidt.isSelected();

        btnEidt.setText(isEdit ? "完成" : "编辑");


        for (DefaultFlowLabel defaultFlowLabel : defaultFlowLabels) {
            defaultFlowLabel.setEditable(isEdit);
        }


    }

    @Override
    public void onSelected(DefaultFlowLabel.Label label) {

        Toast.makeText(this, label.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemove(DefaultFlowLabel.Label label) {
//        Toast.makeText(this,label.name,Toast.LENGTH_SHORT).show();

    }
}
