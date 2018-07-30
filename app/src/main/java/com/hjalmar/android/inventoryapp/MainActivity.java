package com.hjalmar.android.inventoryapp;

import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hjalmar.android.inventoryapp.data.ProductContract.ProductEntry;
import com.hjalmar.android.inventoryapp.loader.QueryCursorLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.db_info)
    ListView productListView;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @BindView(R.id.action_button)
    FloatingActionButton fab;

    private final QueryCursorLoader mQueryCursorLoader = new QueryCursorLoader(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        productListView.setAdapter(mQueryCursorLoader.getProductCursorAdapter());
        productListView.setEmptyView(emptyView);
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.setData(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id));
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(QueryCursorLoader.PRODUCTS_LOADER_ID, null, mQueryCursorLoader);
    }

}
