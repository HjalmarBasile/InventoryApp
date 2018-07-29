package com.hjalmar.android.inventoryapp.model;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = view.findViewById(R.id.item_product_name);
        TextView priceView = view.findViewById(R.id.item_product_price);
        TextView quantityView = view.findViewById(R.id.item_product_quantity);

        String currentProductName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        Integer currentProductPrice = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE));
        Integer currentProductQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));

        nameView.setText(currentProductName);
        priceView.setText(context.getString(R.string.tools_text_product_price, currentProductPrice));
        quantityView.setText(context.getString(R.string.tools_text_product_quantity, currentProductQuantity));
    }

}
