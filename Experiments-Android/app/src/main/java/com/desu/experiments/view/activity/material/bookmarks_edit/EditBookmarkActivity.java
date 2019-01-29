package com.desu.experiments.view.activity.material.bookmarks_edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.desu.experiments.R;
import com.desu.experiments.model.GoogleApi.Item;
import com.desu.experiments.view.widget.OverScrollView;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditBookmarkActivity extends AppCompatActivity {


    @Bind(R.id.textview_title)
    TextView textViewTitle;
    @Bind(R.id.textview_domain)
    TextView textViewDomain;
    @Bind(R.id.textview_full_url)
    TextView textViewFullUrl;
    @Bind(R.id.textview_date_saved)
    TextView textViewDateSaved;

    @Bind(R.id.button_done)
    Button textViewDone;
    @Bind(R.id.button_cancel)
    Button textViewCancel;
    @Bind(R.id.button_delete)
    Button textViewDelete;

    @Bind(R.id.overscroll_view)
    OverScrollView scrollView;

    @Bind(R.id.drawee_view)
    SimpleDraweeView simpleDraweeView;

    public static final String INTENT_BOOKMARK = "INTENT_BOOKMARK";
    public static final String BUNDLE_ITEM = "BUNDLE_ITEM";
    public static final String INTENT_BUNDLE_ITEM = "INTENT_BUNDLE_ITEM";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bookmark);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int pos = intent.getIntExtra(INTENT_BOOKMARK, 0);

        Bundle bundle = intent.getBundleExtra(INTENT_BUNDLE_ITEM);
        Item item = (Item) bundle.getSerializable(BUNDLE_ITEM);

        System.out.println(pos);

        setupCardView(item);

        int translationThreshold = 50;
        scrollView.setOverScrollListener((yDistance, isReleased) -> {
            if (Math.abs(yDistance) > translationThreshold) if (isReleased) {
                onBackPressed();
                return true;
            }
            return false;
        });
    }

    private void setupCardView(Item item) {
        //Item item = Singleton.getInstance().getItems().items.get(pos);
        textViewTitle.setText(item.title);
        textViewDomain.setText(item.title);
        textViewFullUrl.setText(item.displayLink);
        textViewDateSaved.setText(item.kind);

        Uri uri = null;
        try {
            uri = Uri.parse(item.image.thumbnailLink);
        } catch (Exception e) {
            e.printStackTrace();
        }
        simpleDraweeView.setImageURI(uri);
        textViewDone.setOnClickListener(v -> onBackPressed());
        textViewCancel.setOnClickListener(v -> onBackPressed());
        textViewDelete.setOnClickListener(v -> onBackPressed());
    }

}
