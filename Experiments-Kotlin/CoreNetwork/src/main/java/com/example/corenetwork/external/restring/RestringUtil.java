package com.example.corenetwork.external.restring;

import java.util.Locale;

class RestringUtil {

    static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }
}
