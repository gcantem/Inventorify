package co.edu.unal.inventorify.activities.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import co.edu.unal.inventorify.R;
import co.edu.unal.inventorify.barcodes.ZXingActivity;
import co.edu.unal.inventorify.models.Product;

public class NewProductActivity extends AppCompatActivity {

    public static EditText barcodeInfo;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ImageView productImageView;
    private String productKey;

    private String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        label = intent.getStringExtra(ListProductsActivity.EXTRA_OPERATION);
        ((Button)findViewById(R.id.newProductButton)).setText(label);

        barcodeInfo = (EditText) findViewById(R.id.productBarcodeEditText);
        productImageView = (ImageView) findViewById(R.id.productImageView);
    }

    public void saveProduct(View view){
        if(label.equals(getString(R.string.new_label))) {
            productKey = mDatabase.child("products").push().getKey();
        }
        saveImage();
    }

    private void persist(String imageURL){
        String productName = ((EditText)findViewById(R.id.productNameEditText)).getText().toString();
        String productBarcode = ((EditText)findViewById(R.id.productBarcodeEditText)).getText().toString();
        int productQuantity = Integer.parseInt(((EditText)findViewById(R.id.productQuantityEditText)).getText().toString());
        int productPrice = Integer.parseInt(((EditText)findViewById(R.id.productSalePriceEditText)).getText().toString());
        String productPriceCurrency = (String) ((Spinner) findViewById(R.id.productSalePriceSpinner)).getSelectedItem();
        int productCost = Integer.parseInt(((EditText)findViewById(R.id.productCostEditText)).getText().toString());
        String productCostCurrency = (String) ((Spinner) findViewById(R.id.productCostSpinner)).getSelectedItem();
        int productGuarantee = Integer.parseInt(((EditText)findViewById(R.id.productGuaranteeEditText)).getText().toString());
        String productGuaranteePeriod = (String) ((Spinner) findViewById(R.id.productGuaranteeSpinner)).getSelectedItem();
        Product product = new Product(productKey);
        product.setName(productName);
        product.setBarcode(productBarcode);
        product.setQuantity(productQuantity);
        product.setPrice(productPrice);
        product.setPriceCurrency(productPriceCurrency);
        product.setCost(productCost);
        product.setCostCurrency(productCostCurrency);
        product.setGuarantee(productGuarantee);
        product.setGuaranteePeriod(productGuaranteePeriod);
        product.setImageURL(imageURL);
        mDatabase.child("products").child(productKey).setValue(product);
        Toast.makeText(getApplicationContext(), productName + " was inserted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveImage(){
        StorageReference ref = mStorage.child("products/"+productKey);

        productImageView.setDrawingCacheEnabled(true);
        productImageView.buildDrawingCache();
        Bitmap bitmap = productImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {}
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                persist(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }

    public void launchBarcodeScanner(View view){
        startActivity(new Intent(this, ZXingActivity.class));
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            productImageView.setImageBitmap(imageBitmap);
        }
    }

}
