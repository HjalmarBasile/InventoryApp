package com.hjalmar.android.inventoryapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hjalmar.android.inventoryapp.data.ProductContract.ProductEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.edit_product_name)
    EditText mProductNameEditText;

    @BindView(R.id.edit_product_price)
    EditText mProductPriceEditText;

    @BindView(R.id.edit_product_quantity)
    EditText mProductQuantityEditText;

    @BindView(R.id.edit_supplier_name)
    EditText mSupplierNameEditText;

    @BindView(R.id.edit_supplier_phone_number)
    EditText mSupplierPhoneNumberEditText;

    private static final int EDIT_PRODUCT_LOADER = 2;

    private Uri mIntentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mIntentUri = getIntent().getData();

        // Set Activity label
        if (isAddIntent()) {
            setTitle(R.string.activity_detail_title_new_product);
        } else {
            setTitle(R.string.activity_detail_title_edit_product);
            getSupportLoaderManager().initLoader(EDIT_PRODUCT_LOADER, null, this);
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
                saveProduct();
                finish();
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

    /**
     * Get user input from editor and save product on db
     */
    private void saveProduct() {
        String productName = mProductNameEditText.getText().toString().trim();
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumber = mSupplierPhoneNumberEditText.getText().toString().trim();

        Integer productPrice;
        Integer productQuantity;
        try {
            productPrice = Integer.parseInt(mProductPriceEditText.getText().toString().trim());
            productQuantity = Integer.parseInt(mProductQuantityEditText.getText().toString().trim());
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.detail_save_product_failure), Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        try {
            String msg;
            if (isAddIntent()) {
                Uri resultUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
                if (resultUri != null) {
                    msg = getString(R.string.detail_save_product_success);
                } else {
                    msg = getString(R.string.detail_save_product_failure);
                }
            } else {
                int rowsUpdated = getContentResolver().update(mIntentUri, values, null, null);
                if (rowsUpdated > 0) {
                    msg = getString(R.string.detail_save_product_success);
                } else {
                    msg = getString(R.string.detail_save_product_failure);
                }
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // No need to check for input validation, the provider will take care of it,
            // we just have to catch the exception and notify the user
            Toast.makeText(this, getString(R.string.detail_save_product_failure), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, mIntentUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() != 1) {
            return;
        }

        if (data.moveToFirst()) {
            mProductNameEditText.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME)));
            mProductPriceEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE))));
            mProductQuantityEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY))));
            mSupplierNameEditText.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME)));
            mSupplierPhoneNumberEditText.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER)));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mProductPriceEditText.setText("");
        mProductQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
    }

}
