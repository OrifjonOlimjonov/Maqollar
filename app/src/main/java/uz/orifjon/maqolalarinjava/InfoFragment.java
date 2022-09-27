package uz.orifjon.maqolalarinjava;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import uz.orifjon.maqolalarinjava.databinding.FragmentInfoBinding;
import uz.orifjon.maqolalarinjava.models.ImageData;
import uz.orifjon.maqolalarinjava.models.Maqollar;

public class InfoFragment extends Fragment {


    public InfoFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();
        Maqollar maqol = (Maqollar) arguments.getSerializable("maqol");
        ImageData object = (ImageData) arguments.getSerializable("image");

        binding.uzText.setText(maqol.getPostUz());
        binding.ruText.setText(maqol.getPostRu());
        binding.enText.setText(maqol.getPostEng());
        binding.themesPost.setText(object.getName());


        binding.back.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("object", object);
            bundle.putString("map", bundle.getString("map"));
            FragmentManager parentFragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = parentFragmentManager.beginTransaction();
            fragmentTransaction.remove(this);
            parentFragmentManager.popBackStack();
            fragmentTransaction.commit();

        });
        return binding.getRoot();
    }


}