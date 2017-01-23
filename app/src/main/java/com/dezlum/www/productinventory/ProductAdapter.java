package com.dezlum.www.productinventory;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dezlum.www.productinventory.data.ProductDbHelper;

import java.util.ArrayList;

/**
 * Created by saurabh on 1/23/2017.
 */

public class ProductAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> products = new ArrayList<>();

    public ProductAdapter(Activity context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    ProductDbHelper db = new ProductDbHelper(getContext());

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        final Product currentProduct = getItem(position);

        Button btnSell = (Button) listItemView.findViewById(R.id.list_sell_btn);

        TextView ProductName = (TextView) listItemView.findViewById(R.id.list_product_name);
        ProductName.setText(currentProduct.getName());

        final TextView InStock = (TextView) listItemView.findViewById(R.id.list_product_stock);
        InStock.setText(Integer.toString(currentProduct.getStock()));

        TextView ProductPrice = (TextView) listItemView.findViewById(R.id.list_product_price);
        ProductPrice.setText("$" + Float.toString(currentProduct.getPrice()));

        final TextView ProductSold = (TextView) listItemView.findViewById(R.id.list_product_sold);
        ProductSold.setText(Integer.toString(currentProduct.getSales()));

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stock = Integer.parseInt(InStock.getText().toString());
                int sold = Integer.parseInt(ProductSold.getText().toString());

                if (stock > 0) {
                    stock--;
                    Log.d("btnSell stock: ", String.valueOf(stock));
                    sold++;
                    Log.d("btnSell sales: ", String.valueOf(sold));
                    currentProduct.setSales(sold);
                    currentProduct.setStock(stock);
                    InStock.setText(Integer.toString(stock));
                    ProductSold.setText(Integer.toString(sold));
                    db.updateProduct(currentProduct);
                }

            }
        });

        return listItemView;
    }
}
