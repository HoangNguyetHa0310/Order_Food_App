package com.example.order_food_app.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.order_food_app.Domain.Foods;
import com.example.order_food_app.Helper.ManagmentCart;
import com.example.order_food_app.R;
import com.example.order_food_app.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {

    ActivityDetailBinding binding;
    private Foods object;
    private int num = 1 ;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.backBtn.setOnClickListener(v -> finish());
        Glide.with(this)
                .load(object.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(binding.pic);
        binding.priceTxt.setText(object.getPrice() + "VND");
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingTxt.setText(object.getStar() + "Xếp hạng");
        binding.ratingBar.setRating((float)object.getStar());
        binding.totalTxt.setText(num * object.getPrice() + "VND");

        binding.plusBtn.setOnClickListener(view -> {
            num = num + 1;
            binding.numTxt.setText(num + "");
            binding.totalTxt.setText((num * object.getPrice()) + "VND");
        });

        binding.minusBtn.setOnClickListener(view -> {
            if (num > 1 ) {
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText((num * object.getPrice()) + "VND");
            }
        });

        binding.addBtn.setOnClickListener(view -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });

    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }


}