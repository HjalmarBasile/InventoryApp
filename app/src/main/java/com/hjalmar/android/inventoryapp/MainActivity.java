package com.hjalmar.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjalmar.android.inventoryapp.data.ProductContract.ProductEntry;
import com.hjalmar.android.inventoryapp.model.ProductCursorAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.db_info)
    ListView productListView;

    @BindView(R.id.empty_view)
    TextView emptyView;

    private final ProductCursorAdapter mCursorAdapter = new ProductCursorAdapter(this, null);

    public static final int PRODUCTS_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        productListView.setEmptyView(emptyView);
        productListView.setAdapter(mCursorAdapter);

        getSupportLoaderManager().initLoader(PRODUCTS_LOADER_ID, null, this);
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        //String[] projection = new String[]{ProductEntry._ID, ProductEntry.COLUMN_PRODUCT_NAME, ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME};
        return new CursorLoader(this, ProductEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
