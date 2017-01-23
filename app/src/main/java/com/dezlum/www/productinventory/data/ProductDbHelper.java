package com.dezlum.www.productinventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dezlum.www.productinventory.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saurabh on 1/23/2017.
 */

public class ProductDbHelper extends SQLiteOpenHelper {
    private Context mContext;

    public ProductDbHelper(Context context) {
        super(context,ProductDbContract.DATABASE_NAME,null,ProductDbContract.DATABASE_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String TEXT_TYPE = " TEXT";
        final String COMMA_SEP = ",";
        final String INTEGER_TYPE = " INTEGER";
        final String FLOAT_TYPE = " REAL";
        String CREATE_TABLE = "CREATE TABLE " + ProductDbContract.ProductEntry.TABLE_NAME + "("
                + ProductDbContract.ProductEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY,"
                + ProductDbContract.ProductEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + COMMA_SEP
                + ProductDbContract.ProductEntry.COLUMN_PRODUCT_IMAGE + TEXT_TYPE + COMMA_SEP
                + ProductDbContract.ProductEntry.COLUMN_PRODUCT_PRICE + FLOAT_TYPE + COMMA_SEP
                + ProductDbContract.ProductEntry.COLUMN_PRODUCT_STOCK + INTEGER_TYPE + COMMA_SEP
                + ProductDbContract.ProductEntry.COLUMN_PRODUCT_SALES + INTEGER_TYPE + COMMA_SEP
                + ProductDbContract.ProductEntry.COLUMN_SUPPLIER_CONTACT + TEXT_TYPE + ")";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductDbContract.ProductEntry.TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_STOCK, product.getStock());
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_SALES, product.getSales());
        values.put(ProductDbContract.ProductEntry.COLUMN_SUPPLIER_CONTACT, product.getSupplier());

        // Inserting Row
        db.insert(ProductDbContract.ProductEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting one product
    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProductDbContract.ProductEntry.TABLE_NAME,
                new String[]{
                        ProductDbContract.ProductEntry.COLUMN_PRODUCT_ID,
                        ProductDbContract.ProductEntry.COLUMN_PRODUCT_NAME,
                        ProductDbContract.ProductEntry.COLUMN_PRODUCT_IMAGE,
                        ProductDbContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                        ProductDbContract.ProductEntry.COLUMN_PRODUCT_STOCK,
                        ProductDbContract.ProductEntry.COLUMN_PRODUCT_SALES,
                        ProductDbContract.ProductEntry.COLUMN_SUPPLIER_CONTACT
                },
                ProductDbContract.ProductEntry.COLUMN_PRODUCT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    // Getting All Products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + ProductDbContract.ProductEntry.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setImage(cursor.getString(2));
                product.setPrice(cursor.getFloat(3));
                product.setStock(Integer.parseInt(cursor.getString(4)));
                product.setSales(Integer.parseInt(cursor.getString(5)));
                product.setSupplier(cursor.getString(6));

                productList.add(product);
            } while (cursor.moveToNext());
        }

        return productList;
    }

    // Getting products Count
    public int getProductsCount() {
        String countQuery = "SELECT * FROM " + ProductDbContract.ProductEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    // Updating a single product
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_STOCK, product.getStock());
        values.put(ProductDbContract.ProductEntry.COLUMN_PRODUCT_SALES, product.getSales());

        // updating row
        return db.update(ProductDbContract.ProductEntry.TABLE_NAME,
                values,
                ProductDbContract.ProductEntry.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    // Deleting a product
    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductDbContract.ProductEntry.TABLE_NAME, ProductDbContract.ProductEntry.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }

    //Delete all products
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductDbContract.ProductEntry.TABLE_NAME, null, null);
        db.execSQL("delete  from " + ProductDbContract.ProductEntry.TABLE_NAME);
        db.close();
    }

    //Delete the database file
    public void deleteDatabase() {

        mContext.deleteDatabase(ProductDbContract.DATABASE_NAME);
    }
}
