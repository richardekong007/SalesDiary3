
package com.example.adminstrator.salesdiary3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private List<Product> products = new ArrayList<>();
    private List<Product> filteredProductNames;
    private ProductViewHolder productViewHolder;
    private ProductDetailClickListener productDetailClickListener;

    public ProductAdapter(List<Product> products) {
        this.products = products;
        this.filteredProductNames = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productcatalogfields, parent, false);
        productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        final Product product = filteredProductNames.get(position);
        holder.description.setText(product.getDescription());
        holder.price.setText(String.format(Locale.US, "%.2f", product.getCostPrice()));
        holder.image.setImageBitmap(BitmapLoader.loadBitmap(product.getImagePath(), 100, 100));
        holder.stock.setText(String.format(Locale.US, "%d", product.getStock()));

        holder.view.setOnLongClickListener((view) -> {
            productDetailClickListener.onDetailClick(product, view);
            return true;
        });

    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? products : this.products;
    }


    @Override
    public int getItemCount() {
        return filteredProductNames.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchedString = constraint.toString();
                if (searchedString.isEmpty()) {
                    filteredProductNames = products;
                } else {
                    List<Product> searchedProducts = new ArrayList<>();
                    for (Product product : products) {
                        if (product.getDescription().toLowerCase().contains(searchedString)) {
                            searchedProducts.add(product);
                        }
                    }
                    filteredProductNames = searchedProducts;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredProductNames;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredProductNames = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setProductDetailClickListener(ProductDetailClickListener productDetailClickListener) {
        this.productDetailClickListener = productDetailClickListener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.cost_price)
        TextView price;
        @BindView(R.id.stock_value)
        TextView stock;

        private View view;

        public ProductViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, this.view);
        }

        public ProductViewHolder getInstance() {
            return new ProductViewHolder(this.view);
        }

    }

    public interface ProductDetailClickListener {
        void onDetailClick(Product product, View view);
    }
}
