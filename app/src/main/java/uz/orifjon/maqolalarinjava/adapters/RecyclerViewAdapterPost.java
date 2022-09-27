package uz.orifjon.maqolalarinjava.adapters;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;
import uz.orifjon.maqolalarinjava.databinding.ItemPostBinding;
import uz.orifjon.maqolalarinjava.models.ImageData;
import uz.orifjon.maqolalarinjava.models.Maqollar;

public class RecyclerViewAdapterPost extends RecyclerView.Adapter<RecyclerViewAdapterPost.MyHolderView> implements Filterable, AnimateViewHolder {

    private List<Maqollar> list;
    private List<Maqollar> isSorted;
    private OnClickListenerItem1 onClickListenerItem;

    public RecyclerViewAdapterPost(List<Maqollar> list, OnClickListenerItem1 onClickListenerItem) {
        this.list = list;
        this.isSorted = list;
        this.onClickListenerItem = onClickListenerItem;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolderView(ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        holder.binding.postText.setText(isSorted.get(position).getPostUz());
        holder.itemView.setOnClickListener(view -> onClickListenerItem.onClickItem(isSorted.get(position), position, isSorted));
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
                    List<Maqollar> listFilter = new ArrayList<>();
                    for (Maqollar maqol : list) {
                        if (maqol.getPostUz().substring(0, str.length()).equalsIgnoreCase(str)) {
                            listFilter.add(maqol);
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
                isSorted = (List<Maqollar>) filterResults.values;
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

    class MyHolderView extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        public MyHolderView(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListenerItem1 {
        void onClickItem(Maqollar maqol, int position, List<Maqollar> isSorted);
    }

}
