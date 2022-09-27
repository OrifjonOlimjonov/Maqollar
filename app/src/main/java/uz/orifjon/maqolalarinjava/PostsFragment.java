package uz.orifjon.maqolalarinjava;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import uz.orifjon.maqolalarinjava.adapters.RecyclerViewAdapterPost;
import uz.orifjon.maqolalarinjava.databinding.FragmentPostsBinding;
import uz.orifjon.maqolalarinjava.models.ImageData;
import uz.orifjon.maqolalarinjava.models.Maqollar;
import uz.orifjon.maqolalarinjava.singleton.MyGson;
import uz.orifjon.maqolalarinjava.singleton.MySharedPreference;

public class PostsFragment extends Fragment {

    public PostsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move);
        setSharedElementEnterTransition(transition);
        setSharedElementReturnTransition(transition);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private FragmentPostsBinding binding;
    private ImageData object;
    private Map<String, List<Maqollar>> map;
    private String map1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(inflater, container, false);
        Bundle arguments = getArguments();

        if (arguments != null) {
            object = (ImageData) arguments.getSerializable("object");
            map1 = arguments.getString("map");
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Maqollar>>>() {
            }.getType();
            map = gson.fromJson(map1, type);
        }

        

        binding.setImage.setImageResource(object.getImage());
        binding.postName1.setText(object.getName());
        binding.countPost1.setText(object.getCount());

        List<Maqollar> strings = map.get(object.getName());
        RecyclerViewAdapterPost adapterPost = new RecyclerViewAdapterPost(strings, (maqol, position,list) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("maqol",list.get(position));
            bundle.putSerializable("image",object);
            bundle.putString("map",map1);
            binding.imgSearch.onActionViewCollapsed();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.infoFragment, bundle);
        });
        binding.rv.setAdapter(adapterPost);
        AlphaInAnimationAdapter adapter1 = new AlphaInAnimationAdapter(adapterPost);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter1) ;
        scaleInAnimationAdapter.setDuration(500);
        //scaleInAnimationAdapter.setHasStableIds(false);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(.100f));
        binding.rv.setAdapter(scaleInAnimationAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rv.setLayoutManager(layoutManager);
        binding.rv.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        binding.countPost.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).navigate(R.id.homeFragment));
        ViewGroup.LayoutParams layoutParams = binding.imgSearch.getLayoutParams();
        binding.imgSearch.setOnSearchClickListener(view -> {
            binding.imgSearch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.postName.setVisibility(View.GONE);
            binding.imgSearch.setBackground(null);
            binding.countPost.setVisibility(View.GONE);
        });


        binding.imgSearch.setOnCloseListener(() -> {
            binding.imgSearch.setLayoutParams(layoutParams);
            binding.postName.setVisibility(View.VISIBLE);
            binding.imgSearch.setBackgroundResource(R.drawable.search);
            binding.countPost.setVisibility(View.VISIBLE);
            return false;
        });

        binding.imgSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterPost.getFilter().filter(newText);
                return false;
            }
        });
        return binding.getRoot();
    }
}