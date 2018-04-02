
package com.example.adminstrator.salesdiary3;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;




public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products = new ArrayList<>();

    private ProductDetailClickListener productDetailClickListener;
    public ProductAdapter(List<Product>products){
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productcatalogfields,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        final Product product = products.get(position);
        holder.description.setText(product.getDescription());
        holder.price.setText(String.format(Locale.US,"%.2f",product.getCostPrice()));
        holder.image.setImageBitmap(BitmapLoader.loadBitmap(product.getImagePath(),
                100,100));
        holder.stock.setText(String.format(Locale.US,"%d",product.getStock()));

        holder.container.setOnLongClickListener((view)-> {
            productDetailClickListener.onDetailClick(product);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProductDetailClickListener(ProductDetailClickListener productDetailClickListener){
        this.productDetailClickListener = productDetailClickListener;
    }





    public class ProductViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.description) TextView description;
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.cost_price) TextView price;
        @BindView(R.id.stock_value) TextView stock;
        @BindView(R.id.container) CardView container;

        public ProductViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface ProductDetailClickListener {
        void onDetailClick(Product product);
    }
}
