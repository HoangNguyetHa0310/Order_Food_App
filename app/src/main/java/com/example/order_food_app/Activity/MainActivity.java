package com.example.order_food_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.order_food_app.Adapter.CategoryAdapter;
import com.example.order_food_app.Adapter.SliderAdapter;
import com.example.order_food_app.Domain.Category;
import com.example.order_food_app.Domain.Foods;
import com.example.order_food_app.Domain.SliderItems;
import com.example.order_food_app.R;
import com.example.order_food_app.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    private ArrayAdapter<String> searchAdapter;
    private List<Foods> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCategory();
        initBanner();
        setVariable();
        initFoodList();
        searchFood(); // Khởi tạo chức năng tìm kiếm
    }

    private void searchFood() {
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        binding.searchTxt.setAdapter(searchAdapter);

        // Xử lý sự kiện khi chọn món ăn từ dropdown
        binding.searchTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Foods selectedFood = foodList.get(position);
                // TODO: Xử lý khi chọn món ăn (ví dụ: chuyển sang màn hình chi tiết món ăn)
                // Ví dụ:
                Intent intent = new Intent(MainActivity.this, ListFoodActivity.class);
                intent.putExtra("foodId", selectedFood.getId()); // Thay "foodId" bằng key bạn muốn sử dụng
                startActivity(intent);
            }
        });


        // Cập nhật danh sách gợi ý khi người dùng nhập liệu
        binding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoodList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý gì ở đây
            }
        });
    }


    private void filterFoodList(String text) {
        List<String> filteredFoodNames = new ArrayList<>();
        if (text.isEmpty()) {
            // Hiển thị tất cả món ăn khi ô tìm kiếm trống
            for (Foods food : foodList) {
                filteredFoodNames.add(food.getTitle());
            }
        } else {
            // Lọc danh sách món ăn dựa trên text
            for (Foods food : foodList) {
                if (food.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredFoodNames.add(food.getTitle());
                }
            }
        }

        // Cập nhật adapter
        searchAdapter.clear();
        searchAdapter.addAll(filteredFoodNames);
        searchAdapter.notifyDataSetChanged();
    }


    private void initFoodList() {
        DatabaseReference foodsRef = database.getReference("Foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                List<String> foodNames = new ArrayList<>();

                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Foods food = foodSnapshot.getValue(Foods.class);
                    if (food != null) {
                        foodList.add(food);
                        foodNames.add(food.getTitle());
                    }
                }

                searchAdapter.clear();
                searchAdapter.addAll(foodNames);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }


    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPager2.setPageTransformer(compositePageTransformer);

    }

    private void setVariable() {
        binding.bottomMenu.setItemSelected(R.id.home, true);
        binding.bottomMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.cart) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                }
            }
        });

    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Category.class));
                    }
                    if (list.size() > 0) {
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                        binding.categoryView.setAdapter(new CategoryAdapter(list));
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}