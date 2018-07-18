package com.pysun.flowlayout.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pysun.flowlayout.DefaultFlowLabel;
import com.pysun.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DefaultFlowLabel.Callback<DefaultFlowLabel.Label> {


    private Button btnEidt;
    private FlowLayout flLabel;
    List<DefaultFlowLabel> defaultFlowLabels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEidt = findViewById(R.id.btn_edit);
        flLabel = findViewById(R.id.fl_label);
        btnEidt.setOnClickListener(this);
        DefaultFlowLabel defaultFlowLabel;
        for (int i = 0; i < 25; i++) {
            defaultFlowLabel = new DefaultFlowLabel(this, flLabel);
            defaultFlowLabel.setLabel(new DefaultFlowLabel.Label("你好," + i, i + ""));
            defaultFlowLabel.setLabelCallback(this);
            defaultFlowLabels.add(defaultFlowLabel);

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
