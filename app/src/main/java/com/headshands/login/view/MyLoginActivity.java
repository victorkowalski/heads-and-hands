package com.headshands.login.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.headshands.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class MyLoginActivity extends AppCompatActivity {
    EditText et_name, et_password;
    TextView tv_status;
    Button btn_login;

    Observable<Boolean> observable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //AppPrefs.init(this);
        //AppPrefs.setUserName("ayusch");
        //AppPrefs.setPassword("pass12");

        init();

    }

    private void init() {

        et_name = findViewById(R.id.editText_email);
        et_password = findViewById(R.id.editText_password);
        btn_login = findViewById(R.id.button_login);
        //tv_status = findViewById(R.id.tv_status);

        Observable<String> nameObservable = RxTextView.textChanges(et_name).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });
        Observable<String> passwordObservable = RxTextView.textChanges(et_password).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });

        observable = Observable.combineLatest(nameObservable, passwordObservable, new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String s, String s2) throws Exception {
                return isValidForm(s, s2);
            }
        });

        observable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                updateButton(aBoolean);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void updateButton(boolean valid) {
        if (valid)
            btn_login.setEnabled(true);
    }

    public boolean isValidForm(String name, String password) {
        boolean validName = name.length() > 3;

        if (!validName) {
            et_name.setError("Please enter valid name");
        }
        /*&& password.equals(AppPrefs.getPassword()*/
        boolean validPass = password.length() > 3;
        if (!validPass) {
            et_password.setError("Incorrect password");
        }
        return validName && validPass;
    }
}