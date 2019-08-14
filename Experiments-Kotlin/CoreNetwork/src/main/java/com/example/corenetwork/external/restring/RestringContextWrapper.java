package com.example.corenetwork.external.restring;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import com.example.corenetwork.external.restring.RestringResources;
import com.example.corenetwork.external.restring.StringRepository;

/**
 * Main Restring context wrapper which wraps the context for providing another layout inflater & resources.
 */
class RestringContextWrapper extends ContextWrapper {

    private RestringLayoutInflater layoutInflater;
    private ViewTransformerManager viewTransformerManager;

    public static RestringContextWrapper wrap(Context context,
                                              com.example.corenetwork.external.restring.StringRepository stringRepository,
                                              ViewTransformerManager viewTransformerManager) {
        return new RestringContextWrapper(context, stringRepository, viewTransformerManager);
    }

    private RestringContextWrapper(Context base,
                                   com.example.corenetwork.external.restring.StringRepository stringRepository,
                                   ViewTransformerManager viewTransformerManager) {
        super(
                new CustomResourcesContextWrapper(
                        base,
                        new com.example.corenetwork.external.restring.RestringResources(base.getResources(), stringRepository))
        );
        this.viewTransformerManager = viewTransformerManager;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (layoutInflater == null) {
                layoutInflater = new RestringLayoutInflater(LayoutInflater.from(getBaseContext()), this, viewTransformerManager, true);
            }
            return layoutInflater;
        }

        return super.getSystemService(name);
    }
}