package com.example.cuongdvph20635asm.ui.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.data.model.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Data> imageUrls=new ArrayList<>();

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Data> list){
        imageUrls=list;
        notifyDataSetChanged();
    }

    // Hàm kiểm tra xem chuỗi có dạng URL hay không
    private boolean isUrl(String input) {
        return input != null && input.startsWith("http");
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position).getUrl();
        // Kiểm tra nếu imageSource là URL
        if (isUrl(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.baseline_hide_image_24)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        }
        // Nếu không, giả sử imageUrl là chuỗi Base64
        else {
            byte[] imageBytes = Base64.decode(imageUrl, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imageView.setImageBitmap(decodedImage);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

