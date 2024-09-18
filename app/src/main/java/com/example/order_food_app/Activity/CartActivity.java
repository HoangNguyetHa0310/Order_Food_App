package com.example.order_food_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.order_food_app.Adapter.CartAdapter;
import com.example.order_food_app.Adapter.CategoryAdapter;
import com.example.order_food_app.Helper.ChangeNumberItemsListener;
import com.example.order_food_app.Helper.ManagmentCart;
import com.example.order_food_app.R;
import com.example.order_food_app.databinding.ActivityCartBinding;
import com.example.order_food_app.databinding.ActivityDetailBinding;
import com.example.order_food_app.databinding.ActivityListFoodBinding;

public class CartActivity extends BaseActivity {

    ActivityCartBinding binding;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initCartList();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), managmentCart, () -> calculateCart()));
    }

    private void calculateCart() {
        double percentTax = 0.02; // thuế thêm 2% cho mỗi đơn hàng
        double delivery = 1; // Mỗi đơn hết 10 nghìn đồng
        double tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax +  delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText(itemTotal + " VND");
        binding.taxTxt.setText(tax + " VND");
        binding.deliveryTxt.setText(delivery + " VND");
        binding.totalTxt.setText(total + " VND");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> startActivity(new Intent(CartActivity.this, MainActivity.class)));
    }
}