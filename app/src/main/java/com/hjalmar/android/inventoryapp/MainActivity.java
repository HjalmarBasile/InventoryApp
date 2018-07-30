package com.hjalmar.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        productListView.setAdapter(mQueryCursorLoader.getProductCursorAdapter());
        productListView.setEmptyView(emptyView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(QueryCursorLoader.PRODUCTS_LOADER_ID, null, mQueryCursorLoader);

        //insertDummyProducts();
    }

    /**
     * Temporary helper method to insert dummy data. For debugging purposes only.
     */
    private void insertDummyProducts() {

        ContentValues values1 = new ContentValues();
        values1.put(ProductEntry.COLUMN_PRODUCT_NAME, "DummyProductName1");
        values1.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1000);
        values1.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 7);
        values1.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "DummySupplierName1");
        values1.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "+393333333333");

        Uri resultUri1 = getContentResolver().insert(ProductEntry.CONTENT_URI, values1);
        if (resultUri1 == null) {
            Toast.makeText(this, "Failed to insert new data", Toast.LENGTH_SHORT).show();
        }

        long newRowId1 = ContentUris.parseId(resultUri1);
        if (newRowId1 != -1) {
            Toast.makeText(this, "Record successfully created with _id " + newRowId1, Toast.LENGTH_SHORT).show();
        }

        ContentValues values2 = new ContentValues();
        values2.put(ProductEntry.COLUMN_PRODUCT_NAME, "DummyProductName2");
        values2.put(ProductEntry.COLUMN_PRODUCT_PRICE, 2000);
        values2.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 5);
        values2.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "DummySupplierName2");
        values2.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, "+394444444444");

        Uri resultUri2 = getContentResolver().insert(ProductEntry.CONTENT_URI, values2);
        if (resultUri2 == null) {
            Toast.makeText(this, "Failed to insert new data", Toast.LENGTH_SHORT).show();
        }

        long newRowId2 = ContentUris.parseId(resultUri2);
        if (newRowId2 != -1) {
            Toast.makeText(this, "Record successfully created with _id " + newRowId2, Toast.LENGTH_SHORT).show();
        }
    }

}
