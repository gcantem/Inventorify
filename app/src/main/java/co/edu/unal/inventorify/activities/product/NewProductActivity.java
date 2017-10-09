package co.edu.unal.inventorify.activities.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.unal.inventorify.R;
import co.edu.unal.inventorify.barcodes.ZXingActivity;
import co.edu.unal.inventorify.models.Product;

public class NewProductActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public static EditText barcodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        barcodeInfo = (EditText) findViewById(R.id.productBarcodeEditText);
    }

    public void saveProduct(View view){
        String productKey = mDatabase.child("products").push().getKey();
        Log.d("INVENTORIFY_PRODUCT", "Pkey: "+productKey);
        String productName = ((EditText)findViewById(R.id.productNameEditText)).getText().toString();
        String productBarcode = ((EditText)findViewById(R.id.productBarcodeEditText)).getText().toString();
        int productQuantity = Integer.parseInt(((EditText)findViewById(R.id.productQuantityEditText)).getText().toString());
        int productPrice = Integer.parseInt(((EditText)findViewById(R.id.productSalePriceEditText)).getText().toString());
        int productCost = Integer.parseInt(((EditText)findViewById(R.id.productCostEditText)).getText().toString());
        Product product = new Product(productKey);
        product.setName(productName);
        product.setBarcode(productBarcode);
        product.setQuantity(productQuantity);
        product.setPrice(productPrice);
        product.setCost(productCost);
        mDatabase.child("products").child(productKey).setValue(product);
        Log.i("INVENTORIFY_PRODUCT", productName + " was inserted!");
        Toast.makeText(getApplicationContext(), productName + " was inserted!", Toast.LENGTH_SHORT).show();
        clear();
    }

    public void launchBarcodeScanner(View view){
        startActivity(new Intent(this, ZXingActivity.class));
    }

    public void clear(){
        ((EditText)findViewById(R.id.productNameEditText)).setText("");
        ((EditText)findViewById(R.id.productBarcodeEditText)).setText("");
        ((EditText)findViewById(R.id.productQuantityEditText)).setText("");
        ((EditText)findViewById(R.id.productSalePriceEditText)).setText("");
        ((EditText)findViewById(R.id.productCostEditText)).setText("");
    }
}
