package com.github.miniwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddCategoryActivity extends Activity {
    public static final int OK = 0;
    public static final int CANCEL = 1;
    public static final String CATEGORY = "CATEGORY";

    @InjectView(R.id.newCategory)
    TextView productName;
    @InjectView(R.id.addCategoryEditText)
    EditText category;
    @InjectView(R.id.addCategoryOK)
    Button okButton;
    @InjectView(R.id.addCategoryCancel)
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_category);

        ButterKnife.inject(this);

    }
    @OnClick(R.id.addCategoryOK)
    public void onClickOK() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(CATEGORY, category.getText().toString());
        setResult(OK, returnIntent);
        finish();
    }

    @OnClick(R.id.addCategoryCancel)
    public void onClickCancel() {
        setResult(CANCEL, new Intent());
        finish();
    }
}
