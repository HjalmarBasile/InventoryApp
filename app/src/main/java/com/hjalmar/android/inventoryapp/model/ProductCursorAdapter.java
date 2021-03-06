package com.hjalmar.android.inventoryapp.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hjalmar.android.inventoryapp.R;
import com.hjalmar.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by hjalmar
 * On 26/07/2018.
 */
public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameView = view.findViewById(R.id.item_product_name);
        TextView priceView = view.findViewById(R.id.item_product_price);
        TextView quantityView = view.findViewById(R.id.item_product_quantity);
        Button saleButton = view.findViewById(R.id.item_button_sale);

        final Integer id = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        String currentProductName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        final Integer currentProductPrice = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        final Integer currentProductQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProductQuantity <= 0) {
                    Toast.makeText(v.getContext(), context.getString(R.string.sale_not_in_stock), Toast.LENGTH_SHORT).show();
                } else {
                    Uri itemUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);

                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, currentProductQuantity - 1);

                    int rowsUpdated = context.getContentResolver().update(itemUri, values, null, null);

                    String msg;
                    if (rowsUpdated == 0) {
                        msg = context.getString(R.string.sale_failure);
                    } else {
                        msg = context.getString(R.string.sale_success);
                    }

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        nameView.setText(currentProductName);
        priceView.setText(context.getString(R.string.tools_text_product_price, currentProductPrice));
        quantityView.setText(context.getString(R.string.tools_text_product_quantity, currentProductQuantity));
    }

}
