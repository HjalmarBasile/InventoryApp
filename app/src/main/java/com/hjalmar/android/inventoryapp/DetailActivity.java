package com.hjalmar.android.inventoryapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    private Uri mIntentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIntentUri = getIntent().getData();

        // Set Activity label
        if (isAddIntent()) {
            setTitle(R.string.activity_detail_title_new_product);
        } else {
            setTitle(R.string.activity_detail_title_edit_product);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (isAddIntent()) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(false);
        }

        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                break;
            case R.id.action_delete:
                break;
            case android.R.id.home:
                break;
            default:
                throw new IllegalArgumentException("Unexpected action id: " + item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isAddIntent() {
        return mIntentUri == null;
    }

}
