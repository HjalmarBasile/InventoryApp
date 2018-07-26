package com.hjalmar.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hjalmar
 * On 16/07/2018.
 */
public final class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.hjalmar.android.inventoryapp";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCTS = ProductEntry.TABLE_NAME;

    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        /**
         * The content URI to access the product data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "-" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "-" + PATH_PRODUCTS;

        /**
         * Name of the database table for products.
         */
        public static final String TABLE_NAME = "products";

        /**
         * Name of the product.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Price of the product in cents.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_PRICE = "product_price";

        /**
         * Quantity of the orders for the product.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";

        /**
         * Name of the supplier of the product.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";

        /**
         * Phone number of the supplier of the product.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }

}
