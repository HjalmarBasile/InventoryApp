package com.hjalmar.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hjalmar.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by hjalmar
 * On 16/07/2018.
 */
public class ProductDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "store.db";

    ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCTS_TABLE = buildSchema();
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    /**
     * Create a String that contains the SQL statement to create the products table
     */
    private String buildSchema() {
        String schema = "CREATE TABLE ";

        schema += ProductEntry.TABLE_NAME;
        schema += " (";
        schema += ProductEntry._ID;
        schema += " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        schema += ProductEntry.COLUMN_PRODUCT_NAME;
        schema += " TEXT NOT NULL, ";
        schema += ProductEntry.COLUMN_PRODUCT_PRICE;
        schema += " INTEGER NOT NULL, ";
        schema += ProductEntry.COLUMN_PRODUCT_QUANTITY;
        schema += " INTEGER NOT NULL DEFAULT 0, ";
        schema += ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME;
        schema += " TEXT NOT NULL, ";
        schema += ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER;
        schema += " TEXT);";

        return schema;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing for now
    }

}
