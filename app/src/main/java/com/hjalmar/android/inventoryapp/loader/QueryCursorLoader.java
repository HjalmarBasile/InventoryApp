package com.hjalmar.android.inventoryapp.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hjalmar.android.inventoryapp.data.ProductContract;
import com.hjalmar.android.inventoryapp.model.ProductCursorAdapter;

/**
 * Created by hjalmar
 * On 30/07/2018.
 */
public class QueryCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Context mContext;

    private final ProductCursorAdapter mProductCursorAdapter;

    public static final int PRODUCTS_LOADER_ID = 1;

    public QueryCursorLoader(Context context) {
        mContext = context;
        mProductCursorAdapter = new ProductCursorAdapter(context, null);
    }

    public ProductCursorAdapter getProductCursorAdapter() {
        return mProductCursorAdapter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(mContext, ProductContract.ProductEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mProductCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mProductCursorAdapter.swapCursor(null);
    }

}
