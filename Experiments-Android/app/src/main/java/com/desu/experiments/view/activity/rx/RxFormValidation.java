package com.desu.experiments.view.activity.rx;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.desu.experiments.R;
import com.desu.experiments.passay.WhitespaceRuleCompat;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

import static android.text.TextUtils.isEmpty;
import static android.util.Patterns.EMAIL_ADDRESS;

public class RxFormValidation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_form_validation);
        ButterKnife.bind(this);
        formValidation();
        doubleBinding();
        createValidator();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeSubscription.clear();
        if (_subscription != null) _subscription.unsubscribe();
        if (_subscriptionDoubleBinding != null) _subscriptionDoubleBinding.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        passValidation();
    }

    @Bind(R.id.btn_demo_form_valid)
    TextView _btnValidIndicator;
    @Bind(R.id.demo_combl_email)
    EditText _email;
    @Bind(R.id.demo_combl_password)
    EditText _password;
    @Bind(R.id.demo_combl_num)
    EditText _number;

    private Observable<CharSequence> _emailChangeObservable;
    private Observable<CharSequence> _passwordChangeObservable;
    private Observable<CharSequence> _numberChangeObservable;

    private Subscription _subscription = null;

    void formValidation() {
        _emailChangeObservable = RxTextView.textChanges(_email).skip(1);
        _passwordChangeObservable = RxTextView.textChanges(_password).skip(1);
        _numberChangeObservable = RxTextView.textChanges(_number).skip(1);
        _combineLatestEvents();
    }

    private void _combineLatestEvents() {
        _subscription = Observable.combineLatest(_emailChangeObservable,
                _passwordChangeObservable,
                _numberChangeObservable,
                (newEmail, newPassword, newNumber) -> {

                    boolean emailValid = !isEmpty(newEmail) &&
                            EMAIL_ADDRESS.matcher(newEmail).matches();
                    if (!emailValid) _email.setError("Invalid Email!");

                    boolean passValid = !isEmpty(newPassword) && newPassword.length() > 8;
                    if (!passValid) _password.setError("Invalid Password!");

                    boolean numValid = !isEmpty(newNumber);
                    if (numValid) {
                        int num = Integer.parseInt(newNumber.toString());
                        numValid = num > 0 && num <= 100;
                    }
                    if (!numValid) _number.setError("Invalid Number!");

                    return emailValid && passValid && numValid;

                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.e("combineLatestEvents", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean formValid) {
                        if (formValid)
                            _btnValidIndicator.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                        else
                            _btnValidIndicator.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    }
                });
    }

    @Bind(R.id.double_binding_num1)
    EditText _number1;
    @Bind(R.id.double_binding_num2)
    EditText _number2;
    @Bind(R.id.double_binding_result)
    TextView _result;

    PublishSubject<Float> _resultEmitterSubject;
    Subscription _subscriptionDoubleBinding;

    void doubleBinding() {
        _resultEmitterSubject = PublishSubject.create();
        _subscriptionDoubleBinding = _resultEmitterSubject.asObservable().subscribe(aFloat -> {
            _result.setText(String.valueOf(aFloat));
        });
        onNumberChanged();
    }

    @OnTextChanged({R.id.double_binding_num1, R.id.double_binding_num2})
    public void onNumberChanged() {
        float num1 = 0;
        float num2 = 0;

        if (!isEmpty(_number1.getText().toString())) {
            num1 = Float.parseFloat(_number1.getText().toString());
        }

        if (!isEmpty(_number2.getText().toString())) {
            num2 = Float.parseFloat(_number2.getText().toString());
        }

        _resultEmitterSubject.onNext(num1 + num2);
    }


    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.password_input_layout)
    TextInputLayout mPasswordInputLayout;
    CompositeSubscription compositeSubscription;
    int
            minLength = 10,
            maxLength = 30,
            minDigits = 3,
            minUpperCase = 2,
            minLowerCase = 2,
            minSpecChars = 2;

    PasswordValidator validator;
    void createValidator() {
        Properties props = new Properties();
        /*
        props.setProperty("HISTORY_VIOLATION", "Password matches one of %1$s previous passwords.");
        props.setProperty("ILLEGAL_WORD", "Password contains the dictionary word '%1$s'.");
        props.setProperty("ILLEGAL_WORD_REVERSED", "Password contains the reversed dictionary word '%1$s'.");
        props.setProperty("ILLEGAL_MATCH", "Password matches the illegal pattern '%1$s'.");
        props.setProperty("ALLOWED_MATCH", "Password must match pattern '%1$s'.");
        props.setProperty("ILLEGAL_CHAR", "Password contains the illegal character '%1$s'.");
        props.setProperty("ALLOWED_CHAR", "Password contains the illegal character '%1$s'.");
        props.setProperty("ILLEGAL_SEQUENCE", "Password contains the illegal sequence '%1$s'.");
        props.setProperty("ILLEGAL_USERNAME", "Password contains the user id '%1$s'.");
        props.setProperty("ILLEGAL_USERNAME_REVERSED", "Password contains the user id '%1$s' in reverse.");
        */
        props.setProperty("ILLEGAL_WHITESPACE", getString(R.string.ILLEGAL_WHITESPACE));
        props.setProperty("INSUFFICIENT_UPPERCASE", getString(R.string.INSUFFICIENT_UPPERCASE));
        props.setProperty("INSUFFICIENT_LOWERCASE", getString(R.string.INSUFFICIENT_LOWERCASE));
        /*
        props.setProperty("INSUFFICIENT_ALPHABETICAL", "Password must contain at least %1$s alphabetical characters.");
        */
        props.setProperty("INSUFFICIENT_DIGIT", getString(R.string.INSUFFICIENT_DIGIT));
        props.setProperty("INSUFFICIENT_SPECIAL", getString(R.string.INSUFFICIENT_SPECIAL));
        /*
        props.setProperty("INSUFFICIENT_CHARACTERISTICS", "Password matches %1$s of %3$s character rules, but %2$s are required.");
        props.setProperty("SOURCE_VIOLATION", "Password cannot be the same as your %1$s password.");
        */
        props.setProperty("TOO_LONG", getString(R.string.TOO_LONG));
        props.setProperty("TOO_SHORT", getString(R.string.TOO_SHORT));
        MessageResolver resolver = new PropertiesMessageResolver(props);
        validator = new PasswordValidator(resolver, Arrays.asList(
                // length between 8 and 16 characters
                new LengthRule(minLength, maxLength),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, minUpperCase),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, minLowerCase),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, minDigits),
                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, minSpecChars),
                // no whitespace
                new WhitespaceRuleCompat()
        ));
        mPasswordView.requestFocus();
    }

    void passValidation() {
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(getValidPasswordObservable()
                .skip(1)
                .subscribe((result) -> {
                    if (result.isValid()) {
                        mPasswordInputLayout.setError(null);
                    } else {
                        String error = "";
                        for (String msg : validator.getMessages(result)) {
                            error += "\n" + msg;
                        }
                        mPasswordInputLayout.setError(error);
                    }
                }));


    }
    Observable<RuleResult> getValidPasswordObservable() {
        return RxTextView.textChangeEvents(mPasswordView)
                .map(onPasswordChangeEvent -> isPasswordValidPassSay(onPasswordChangeEvent.text().toString()));
    }
    RuleResult isPasswordValidPassSay(String password) {
        System.out.println(isPasswordValidRegex(password));
        return validator.validate(new PasswordData(password));
    }

    String isPasswordValidRegex(String password) {
        String result = "";
        String specChars = "@#$_!%^&?";
        Pattern
                TOO_SHORT = Pattern.compile(
                        "(?=^.{" + minLength + ",})^.*"
                ),
                INSUFFICIENT_DIGIT = Pattern.compile(
                        "(?=(.*\\d){" + minDigits + "})^.*"
                ),
                INSUFFICIENT_UPPERCASE = Pattern.compile(
                        "(?=(.*[A-Z]){" + minUpperCase + "})^.*"
                ),
                INSUFFICIENT_LOWERCASE = Pattern.compile(
                        "(?=(.*[a-z]){" + minLowerCase + "})^.*"
                ),
                INSUFFICIENT_SPECIAL = Pattern.compile(
                        "(?=(.*[" + specChars + "]){" + minSpecChars + "})^.*"
                ),
                ILLEGAL_WHITESPACE = Pattern.compile(
                        "(?!.*[\\s])^.*"
                );

        /*
        Pattern HAS_TWO_UPPER_AND_LOWER_CASE = Pattern.compile(
                "(?=(.*[A-Za-z]){2})^.*"
        );
        if (!HAS_TWO_UPPER_AND_LOWER_CASE.matcher(password).matches()) result +="Must HAS_TWO_UPPER_AND_LOWER_CASE\n";
        */

        if (!TOO_SHORT.matcher(password).matches())
            result += String.format(getString(R.string.TOO_SHORT), minLength) + "\n";
        if (password.length()>maxLength)
            result += String.format(getString(R.string.TOO_LONG),minLength, maxLength) + "\n";
        if (!INSUFFICIENT_DIGIT.matcher(password).matches())
            result += String.format(getString(R.string.INSUFFICIENT_DIGIT), minDigits) + "\n";
        if (!INSUFFICIENT_UPPERCASE.matcher(password).matches())
            result += String.format(getString(R.string.INSUFFICIENT_UPPERCASE), minUpperCase) + "\n";
        if (!INSUFFICIENT_LOWERCASE.matcher(password).matches())
            result += String.format(getString(R.string.INSUFFICIENT_LOWERCASE), minLowerCase) + "\n";
        if (!INSUFFICIENT_SPECIAL.matcher(password).matches())
            result += String.format(getString(R.string.INSUFFICIENT_SPECIAL), minSpecChars) + " (" + specChars + ")" + "\n";
        if (!ILLEGAL_WHITESPACE.matcher(password).matches())
            result += getString(R.string.ILLEGAL_WHITESPACE) + "\n";
        return result;
    }
}
