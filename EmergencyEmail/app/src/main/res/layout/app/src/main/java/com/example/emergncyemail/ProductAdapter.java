package com.example.emergncyemail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewAdapter> {

    public Context context;
    public List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.cardview,null);
        ProductViewAdapter productViewAdapter=new ProductViewAdapter(view);
        return productViewAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewAdapter productViewAdapter, int i) {
        Product product=productList.get(i);
        productViewAdapter.start.setText(product.getStart());
        productViewAdapter.end.setText(product.getEnd());
        productViewAdapter.weekday.setText(product.getWeekday());
        String saa[]=product.getStart().split(" ");
        String sr[]=saa[0].split(":");

        int sa=Integer.parseInt(sr[0]);
        int sb=Integer.parseInt(sr[1]);
        long curr=(sa*3600)+(sb*60);
        int a=18000;
        int b=43200;
        int c=64800;

        Log.d("------------------",curr+"");
        if(a<curr && curr<b){
            productViewAdapter.imge.setBackgroundResource(R.drawable.x);
        }else if(b<curr && curr<c){
            productViewAdapter.imge.setBackgroundResource(R.drawable.z);
        }else{
            productViewAdapter.imge.setBackgroundResource(R.drawable.y);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewAdapter extends RecyclerView.ViewHolder{

        TextView imge,start,end,weekday;

        public ProductViewAdapter(@NonNull View itemView) {
            super(itemView);

            imge=itemView.findViewById(R.id.textView14);
            start=itemView.findViewById(R.id.textView16);
            end=itemView.findViewById(R.id.textView20);
            weekday=itemView.findViewById(R.id.textView22);

        }
    }
}
