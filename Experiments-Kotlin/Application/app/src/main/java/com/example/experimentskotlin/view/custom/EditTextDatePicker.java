package com.example.experimentskotlin.view.custom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.experimentskotlin.R;
import com.example.experimentskotlin.UIConstants;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Кнопка для выбора даты
 */
public class EditTextDatePicker extends TextInputEditText implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private EditTextDatePickerListener mListener;
    private Runnable dateChangedListener;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar = new GregorianCalendar();
    private DatePickerDialog dialog;
    private Date defauldDate = new Date();

    EditText getEditText() {
        return this;
    }

    public interface EditTextDatePickerListener {
        void didSelectDate(View view, Date date);

        void willShowDatePicker();
    }

    public EditTextDatePicker(Context context) {
        super(context);
        setOnClickListener(this);
        getEditText().setFocusable(false);
        getEditText().setTextIsSelectable(false);
        getEditText().setInputType(InputType.TYPE_NULL);
    }

    public EditTextDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
        setOnClickListener(this);
        getEditText().setFocusable(false);
        getEditText().setTextIsSelectable(false);
        getEditText().setInputType(InputType.TYPE_NULL);
    }

    public EditTextDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context, attrs);
        setOnClickListener(this);
        getEditText().setFocusable(false);
        getEditText().setTextIsSelectable(false);
        getEditText().setInputType(InputType.TYPE_NULL);
    }

    public void setUpdateListener(EditTextDatePickerListener listener) {
        mListener = listener;
    }


    private void setDateChangedListener(Runnable listener) {
        dateChangedListener = listener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        setDate(date);
        if (mListener != null)
            mListener.didSelectDate(this, date);
        if (dateChangedListener != null)
            dateChangedListener.run();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null)
            mListener.willShowDatePicker();
        Date date;
        if (minMaxDateRange == null) {
            date = getDate();
            if (date == null)
                date = defauldDate;
        } else {
            date = getDate();
            if (date == null)
                date = defauldDate;
        }
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        try {
            dialog.dismiss();
        } catch (Exception ignored) {

        }

        dialog = new DatePickerDialog(getContext(), R.style.AppTheme_AlertDialogTheme, this, year, month, day);
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getContext().getString(R.string.ok), dialog);
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getContext().getString(R.string.cancel), dialog);
        dialog.setCancelable(false);

        if (minMaxDateRange != null) {
            calendar.setTime(minMaxDateRange.first);
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.setTime(minMaxDateRange.second);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        }


        dialog.show();
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditTextDatePicker,
                0, 0);

        try {
            String hint = context.getString(a.getResourceId(R.styleable.EditTextDatePicker_hint_date_picker, R.string.blank_string));
            String dateFormat = a.getString(R.styleable.EditTextDatePicker_dateFormat);
            if (dateFormat == null) dateFormat = UIConstants.DATE_FORMAT;
            simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
            setHint(hint);
        } finally {
            a.recycle();
        }
    }

    public String getDateFormat() {
        return simpleDateFormat.toPattern();
    }

    public void setDefauldDate(Date date) {
        defauldDate = date;
    }

    public void setDate(Date date) {
        if (date != null)
            setText(simpleDateFormat.format(date));
        else
            setText(null);
    }

    public Date getDate() {
        try {
            //noinspection ConstantConditions
            return simpleDateFormat.parse(getText().toString());
        } catch (Throwable e) {
            return null;
        }
    }

    @Nullable
    Pair<Date, Date> minMaxDateRange;

    public void setDateRange(@Nullable Pair<Date, Date> minMaxDateRange) {
        this.minMaxDateRange = minMaxDateRange;
    }


    @BindingAdapter("date_range_edit_text_date_picker")
    public static void setDateRange(EditTextDatePicker view, Pair<Date, Date> range) {
        view.setDateRange(range);
    }

    @BindingAdapter("date_edit_text_date_picker")
    public static void setDate(EditTextDatePicker view, Date date) {
        view.setDate(date);
    }

    @InverseBindingAdapter(attribute = "date_edit_text_date_picker")
    public static Date getDate(EditTextDatePicker view) {
        return view.getDate();
    }

    @BindingAdapter("date_edit_text_date_pickerAttrChanged")
    public static void setDateChangedListener(EditTextDatePicker view, final InverseBindingListener inverseListener) {
        view.setDateChangedListener(new Runnable() {
            @Override
            public void run() {
                inverseListener.onChange();
            }
        });
    }

}

