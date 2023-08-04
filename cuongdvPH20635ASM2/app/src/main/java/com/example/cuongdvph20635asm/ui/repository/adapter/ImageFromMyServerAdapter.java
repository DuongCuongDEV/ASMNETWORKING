package com.example.cuongdvph20635asm.ui.repository.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuongdvph20635asm.R;
import com.example.cuongdvph20635asm.data.model.Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageFromMyServerAdapter extends RecyclerView.Adapter<ImageFromMyServerAdapter.ImageViewHolder> {
    private Context context;
    private List<Data> imageUrls=new ArrayList<>();
    private OnClickItem onClickItem;

    public ImageFromMyServerAdapter(Context context,OnClickItem onClickItem) {
        this.context = context;
        this.onClickItem=onClickItem;
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
        View view=LayoutInflater.from(context).inflate(R.layout.item_image_server,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Data data=imageUrls.get(position);
        String imageUrl = data.getHdurl();
        holder.tv_title.setText(data.getTitle());
        if (data.getCopyright()==null || data.getCopyright().isEmpty()) {
            holder.tv_copyright.setText("Noname");
        } else {

            holder.tv_copyright.setText(data.getCopyright().replace("\n",""));
        }
        holder.tv_date.setText(data.getDate());

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
            if(imageUrl!=null) {
                byte[] imageBytes = Base64.decode(imageUrl, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.imageView.setImageBitmap(decodedImage);
            }
        }
        holder.layout_item_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onclickItem(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_item_product;
        ImageView imageView;
        TextView tv_title, tv_copyright, tv_date;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_item_product=itemView.findViewById(R.id.layout_item_product);
            imageView = itemView.findViewById(R.id.image);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_copyright=itemView.findViewById(R.id.tv_copyright);
            tv_date=itemView.findViewById(R.id.tv_time);
        }
    }
    public interface OnClickItem{
        void onclickItem(Data data);
    }
}