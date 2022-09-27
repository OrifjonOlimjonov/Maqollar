package uz.orifjon.maqolalarinjava.adapters;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;
import uz.orifjon.maqolalarinjava.databinding.ItemBinding;
import uz.orifjon.maqolalarinjava.models.ImageData;
import uz.orifjon.maqolalarinjava.singleton.MyGson;
import uz.orifjon.maqolalarinjava.singleton.MySharedPreference;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable, AnimateViewHolder {
    private List<ImageData> list;
    private List<ImageData> isSorted;
    private OnClickListenerItem onClickListener;
    public RecyclerViewAdapter(List<ImageData> list, OnClickListenerItem onClickListener) {
        this.list = list;
        this.isSorted = list;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.itemPhoto.setImageResource(isSorted.get(position).getImage());
        holder.binding.countPost.setText(isSorted.get(position).getCount());
        holder.binding.postName.setText(isSorted.get(position).getName());
        holder.binding.itemPhoto.setTransitionName("image_big" + position);
        holder.itemView.setOnClickListener(view -> {
            onClickListener.onClickItem(isSorted.get(position), position, isSorted,holder.binding.itemPhoto);
        });
    }

    @Override
    public int getItemCount() {
        return isSorted.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String str = charSequence.toString();
                if (str.isEmpty()) {
                    isSorted = list;
                } else {
                    List<ImageData> listFilter = new ArrayList<>();
                    for (ImageData imageData : list) {
                        if (imageData.getName().substring(0, str.length()).equalsIgnoreCase(str)) {
                            listFilter.add(imageData);
                        }
                    }
                    isSorted = listFilter;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = isSorted;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                isSorted = (List<ImageData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void animateAddImpl(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull Animator.AnimatorListener animatorListener) {
        viewHolder.itemView.animate().translationY(0f).alpha(1f).setDuration(300).setListener(animatorListener);
    }

    @Override
    public void animateRemoveImpl(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull Animator.AnimatorListener animatorListener) {
        viewHolder.itemView.animate().translationY(-viewHolder.itemView.getHeight() * 0.3f).alpha(0f).setDuration(300).setListener(animatorListener);
    }

    @Override
    public void preAnimateAddImpl(@NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setTranslationY(-viewHolder.itemView.getHeight() * 0.3f);
        viewHolder.itemView.setAlpha(0f);
    }

    @Override
    public void preAnimateRemoveImpl(@NonNull RecyclerView.ViewHolder viewHolder) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBinding binding;

        public MyViewHolder(@NonNull ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListenerItem {
        void onClickItem(ImageData imageData, int position, List<ImageData> list,ImageView imageView);
    }
}
