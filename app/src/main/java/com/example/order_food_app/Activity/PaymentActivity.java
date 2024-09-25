package com.example.order_food_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.order_food_app.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy dữ liệu tổng tiền từ Intent
        Intent intent = getIntent();
        double total = intent.getDoubleExtra("total", 0.0);

        // Hiển thị tổng tiền trên PaymentActivity
        TextView totalFeeTxt = findViewById(R.id.totalFeeTxt);
        totalFeeTxt.setText(total + " VND");

        // Xử lý nút backBtn
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            // Quay lại CartActivity
            startActivity(new Intent(PaymentActivity.this, CartActivity.class));
        });

        // Xử lý nút xác nhận (btn_confirm)
        findViewById(R.id.btn_confirm).setOnClickListener(view -> {
            // Chuyển đến thank_you
            startActivity(new Intent(PaymentActivity.this, thank_you.class));
            // Đóng PaymentActivity
            finish();
        });
    }
}