package com.example.corenetwork.external.restring;

import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import androidx.annotation.NonNull;

/**
 * This is the wrapped resources which will be provided by Restring.
 * For getting strings and texts, it checks the strings repository first and if there's a new string
 * that will be returned, otherwise it will fallback to the original resource strings.
 */
public class RestringResources extends Resources {

    private final com.example.corenetwork.external.restring.StringRepository stringRepository;

    RestringResources(@NonNull final Resources res,
                      @NonNull final com.example.corenetwork.external.restring.StringRepository stringRepository) {
        super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
        this.stringRepository = stringRepository;
    }

    @NonNull
    @Override
    public String getString(int id) throws NotFoundException {
        String value = getStringFromRepository(id);
        if (value != null) {
            return value;
        }
        return super.getString(id);
    }

    @NonNull
    @Override
    public String getString(int id, Object... formatArgs) throws NotFoundException {
        String value = getStringFromRepository(id);
        if (value != null) {
            return String.format(value, formatArgs);
        }
        return super.getString(id, formatArgs);
    }

    @NonNull
    @Override
    public CharSequence getText(int id) throws NotFoundException {
        String value = getStringFromRepository(id);
        if (value != null) {
            return fromHtml(value);
        }
        return super.getText(id);
    }

    @Override
    public CharSequence getText(int id, CharSequence def) {
        String value = getStringFromRepository(id);
        if (value != null) {
            return fromHtml(value);
        }
        return super.getText(id, def);
    }

    private String getStringFromRepository(int id) {
        try {
            String stringKey = getResourceEntryName(id);
            return stringRepository.getString(com.example.corenetwork.external.restring.RestringUtil.getCurrentLanguage(), stringKey);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    private CharSequence fromHtml(String source) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //noinspection deprecation
            return Html.fromHtml(source);
        } else {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        }
    }
}
