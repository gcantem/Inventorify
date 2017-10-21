package co.edu.unal.inventorify.activities.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.unal.inventorify.R;
import co.edu.unal.inventorify.models.Product;

public class ListProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecycler;
    private DatabaseReference mDatabase;
    private DatabaseReference products;
    private FirebaseRecyclerAdapter<Product, ProductHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        productsRecycler = (RecyclerView)findViewById(R.id.productListRecycler);
        productsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("products");

        adapter = new FirebaseRecyclerAdapter<Product, ProductHolder>(
            Product.class,
            //android.R.layout.two_line_list_item,
            R.layout.product_layout,
            ProductHolder.class,
            mDatabase) {

                @Override
                protected void populateViewHolder(ProductHolder viewHolder, Product model, int position) {
                    viewHolder.setName(model.getName()+" ("+model.getQuantity()+") units");
                    Glide.with(getApplicationContext()).load(model.getImageURL()).into(viewHolder.productImage);
                }
            };
        productsRecycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }


    public void newProduct(View view){
        startActivity(new Intent(this, NewProductActivity.class));
    }

    public static class ProductHolder extends RecyclerView.ViewHolder {

        private final TextView productName;
        private final ImageView productImage;

        public ProductHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }

        public void setName(String name) {
            productName.setText(name);
        }
    }
}
