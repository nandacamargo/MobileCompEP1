package com.example.nanda.register;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by nanda on 17/04/17.
 */

public class TeacherActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_teacher);
    }

    public void clickCancel() {
        Log.i("cancel","passei aqui");
    }

    public void clickConfirm() {

    }
}
