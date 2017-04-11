package com.example.jkd.biyeshengguanlixitong;

/**
 * Created by jkd on 2015/9/16.
 */

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;

public class CompletionRegister extends Fragment implements View.OnClickListener {

    private ShowView mCallback;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.completion_register, container, false);

        EditText et = (EditText) rootView.findViewById(R.id.enterpassword);
        et.addTextChangedListener(watcher);

        rootView.findViewById(R.id.RegistrationComplete).setOnClickListener(this);

        return rootView;
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EditText et = (EditText) rootView.findViewById(R.id.password);
            Button bt = (Button) rootView.findViewById(R.id.RegistrationComplete);
            String st = et.getText().toString();
            if (st.equals(s.toString())) {
                rootView.findViewById(R.id.RegistrationComplete).setEnabled(true);
                bt.setText("注册完成");
            } else {
                rootView.findViewById(R.id.RegistrationComplete).setEnabled(false);
                bt.setText("请输入两遍相同的密码");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ShowView) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RegistrationComplete:
                if (mCallback != null) {
                    EditText et = (EditText) rootView.findViewById(R.id.password);
                    mCallback.CompletionRegisterCallbake(et.getText().toString());
//                    final Data zczt = (Data) getActivity().getApplication();
//                    System.out.println(zczt.GetZCZT());
//                    if(zczt.Getzczt().equals("1")) {
//                        mCallback.showMainFragment();
//                    }else {
//                        Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
//                    }
//                    mCallback.showMainFragment();
                }
        }

    }
}
