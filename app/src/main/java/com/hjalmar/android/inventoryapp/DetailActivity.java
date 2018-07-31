package com.hjalmar.android.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

    private View.OnTouchListener mOnTouchListener;

    private static final String PRODUCT_EDITED_KEY = "PRODUCT_EDITED_KEY";
    private boolean mProductHasBeenEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // Restore mProductHasBeenEdited variable value
        if (savedInstanceState != null) {
            mProductHasBeenEdited = savedInstanceState.getBoolean(PRODUCT_EDITED_KEY);
        } else {
            mProductHasBeenEdited = false;
        }

        mIntentUri = getIntent().getData();

        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mProductHasBeenEdited = true;
                return false;
            }
        };

        mProductNameEditText.setOnTouchListener(mOnTouchListener);
        mProductPriceEditText.setOnTouchListener(mOnTouchListener);
        mProductQuantityEditText.setOnTouchListener(mOnTouchListener);
        mSupplierNameEditText.setOnTouchListener(mOnTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mOnTouchListener);

        // Set Activity label
        if (isAddIntent()) {
            setTitle(R.string.activity_detail_title_new_product);
        } else {
            setTitle(R.string.activity_detail_title_edit_product);
            getSupportLoaderManager().initLoader(EDIT_PRODUCT_LOADER, null, this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(PRODUCT_EDITED_KEY, mProductHasBeenEdited);
        super.onSaveInstanceState(outState);
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
            MenuItem itemDelete = menu.findItem(R.id.action_delete);
            MenuItem itemBuy = menu.findItem(R.id.action_buy);
            itemDelete.setVisible(false);
            itemBuy.setVisible(false);
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
            case R.id.action_buy:
                updateQuantityOfDelta(-1);
                finish();
                break;
            case R.id.action_delete:
                confirmDeletion();
                break;
            case android.R.id.home: {
                if (mProductHasBeenEdited) {
                    showUnsavedChangesDialog(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NavUtils.navigateUpFromSameTask(DetailActivity.this);
                        }
                    });
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
            }
            return true;
            default:
                throw new IllegalArgumentException("Unexpected action id: " + item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mProductHasBeenEdited) {
            showUnsavedChangesDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        } else {
            super.onBackPressed();
        }
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

    private void updateQuantityOfDelta(int quantityDelta) {
        Integer currentQuantity;
        try {
            currentQuantity = Integer.parseInt(mProductQuantityEditText.getText().toString().trim());
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.detail_change_quantity_product_failure), Toast.LENGTH_SHORT).show();
            return;
        }

        int finalQuantity = currentQuantity + quantityDelta;
        if (finalQuantity < 0) {
            Toast.makeText(this, getString(R.string.sale_not_in_stock), Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, finalQuantity);

            int rowsUpdated = getContentResolver().update(mIntentUri, values, null, null);
            String msg;
            if (rowsUpdated == 0) {
                msg = getString(R.string.detail_change_quantity_product_failure);
            } else {
                msg = getString(R.string.detail_change_quantity_product_success);
            }

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show an alert dialog to confirm product deletion from user
     */
    private void confirmDeletion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_deletion);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
                finish();
            }
        });
        builder.setNegativeButton(R.string.return_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    /**
     * Get user input from editor and delete product from db
     */
    private void deleteProduct() {
        if (mIntentUri == null) {
            throw new IllegalStateException("Single item deletion was requested, but no uri was found");
        }

        try {
            String msg;
            int rowsDeleted = getContentResolver().delete(mIntentUri, null, null);
            if (rowsDeleted > 0) {
                msg = getString(R.string.detail_delete_product_success);
            } else {
                msg = getString(R.string.detail_delete_product_failure);
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.detail_delete_product_failure), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);

        builder.setPositiveButton(R.string.discard, discardButtonOnClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
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
