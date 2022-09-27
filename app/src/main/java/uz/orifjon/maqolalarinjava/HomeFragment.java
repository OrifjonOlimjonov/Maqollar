package uz.orifjon.maqolalarinjava;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.FragmentNavigatorExtrasKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import kotlin.Pair;
import uz.orifjon.maqolalarinjava.adapters.RecyclerViewAdapter;
import uz.orifjon.maqolalarinjava.databinding.FragmentHomeBinding;
import uz.orifjon.maqolalarinjava.databinding.ItemBinding;
import uz.orifjon.maqolalarinjava.models.ImageData;
import uz.orifjon.maqolalarinjava.models.Maqollar;
import uz.orifjon.maqolalarinjava.singleton.MyGson;
import uz.orifjon.maqolalarinjava.singleton.MySharedPreference;

public class HomeFragment extends Fragment {

    public HomeFragment() {  }
    private FragmentHomeBinding binding;
    private List<ImageData> list;
    private Map<String, List<Maqollar>> map;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        loadData();
        Gson gson = new Gson();
        String s1 = gson.toJson(map);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, (imageData, position, list1, imageView) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            bundle.putSerializable("object", imageData);
            bundle.putString("map", s1);
            binding.searchView.onActionViewCollapsed();
            //Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_postsFragment, bundle);
            //String transitionName = binding.rv.findViewById(R.id.itemPhoto).getTransitionName();
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(imageView, "image_big")
                    .build();
            Navigation.findNavController(binding.getRoot()).navigate(
                    R.id.action_homeFragment_to_postsFragment,
                    bundle,
                    null,
                    extras
            );

        });
        AlphaInAnimationAdapter adapter1 = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter1) ;
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setHasStableIds(false);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(.100f));
        binding.rv.setAdapter(scaleInAnimationAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rv.setLayoutManager(layoutManager);
        binding.rv.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        binding.rv.setNestedScrollingEnabled(false);
        binding.searchView.setOnSearchClickListener(view -> {
            binding.post.setVisibility(View.GONE);
            binding.searchView.setBackground(null);
        });


        binding.searchView.setOnCloseListener(() -> {
            binding.post.setVisibility(View.VISIBLE);
            binding.searchView.setBackgroundResource(R.drawable.ic_search_icon);
            return false;
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadData() {
        list = new ArrayList<>();
        List<Maqollar> posts = new ArrayList<>();
        map = new HashMap<>();
        list.add(new ImageData(R.drawable.image1, "46 ta", "Adolat, tenglik haqida"));
        posts.add(new Maqollar("Johilda kuch ko‘p bo‘lar, Kuchim degan tez o‘lar.", "Заставь дурака богу молиться — они лоб расшибет.", "Zeal is fit only for wise men, but is found mostly in fools."));
        posts.add(new Maqollar("Qaysi butaga o‘t tushsa, o‘sha buta kuyar.", "Один пирог два раза не съешь.", "You cannot sell the cow and drink the milk."));
        posts.add(new Maqollar("Ishlamagan — tishlamas, Ishyoqmasga kun kulmas.", "Кто не работает, тот не ест.", "Work shall not eat,He that will not."));
        posts.add(new Maqollar("Suvning ishi — o‘pirmoq, O‘tning ishi — kuydirmoq.", "Ha воде шутки плохи.", "Worse things happen at sea."));
        posts.add(new Maqollar("Yomon bilan talashsang, qadring ketar.", "Куда ни кинь — всюду клин.", "Make the worst of both worlds."));
        posts.add(new Maqollar("Vaqtida ogohlantirmoq — do‘stning ishi.", "Свое временное предупреждение — спасенье.", "A word in season."));
        posts.add(new Maqollar("Olomondan qochgan qutulmas.", "Народу тьма.", "All the world and his wife."));
        posts.add(new Maqollar("Tili bilan suyar, Dili bilan so‘yar.", "Красивые слова иногда прикрывают неприглядные поступки.", "Fine words dress ill deeds."));
        posts.add(new Maqollar("Bo‘ri qarisa, itga kulgi bo‘lar.", "Нам не страшен серый волк.", "Who's afraid of the big bad wolf?"));
        posts.add(new Maqollar("Qo‘shning ko‘r bo‘lsa, ko‘zingni qis.", "С волками жить — по-волчьи выть.", "One must howl with the wolves."));
        map.put(list.get(0).getName(), posts);
        list.add(new ImageData(R.drawable.image2, "186 ta", "Baxt, sevgi va omad"));
        posts = new ArrayList<>();
        posts.add(new Maqollar("Qarib quyilmagan, Achib suyulmas.", "Ha седину беспадок.", "Young saint, old devil."));
        posts.add(new Maqollar("Umr o‘zar, husn to‘zar.", "Такова жизнь.", "So goes the world."));
        posts.add(new Maqollar("Ayolning nozidan qo‘rq, Ahmoqning — so‘zidan.", "Женский обычай не мытьем, так катаньем, а свое возьмет.", "Women must have their wills while they live, because they make none when they die."));
        posts.add(new Maqollar("Bo‘ri bor bo‘lsin desang, qo‘y but bo‘lmas.", "Пустить козла в огород.", "Set the wolf to keep the sheep."));
        posts.add(new Maqollar("Mushuk yo‘g‘ida sichqon tepaga chiqar.", "Кошка из дому — мышка на стол.", "When the cat is away the mice will play."));
        posts.add(new Maqollar("Moysiz arava o‘q yer.", "He подмажешь — не поедешь.", "Grease the wheels."));
        posts.add(new Maqollar("Yoshlik — beboshlik.", "Молодо — зелено, погулять велено.", "То sow one's, wild oats."));
        posts.add(new Maqollar("Yurib-yurib yo‘l ozdim, Yo‘llarda quduq qazdim. O‘zim qazgan quduqqa Yiqilib o‘layozdim.", "За худо примись, а худо — за тебя.", "Sow the wind and reap the whirlwind."));
        posts.add(new Maqollar("Zamonaning zindoni ham, xandoni ham bor.", "Превратности судьбы.", "The whirligig of time."));
        posts.add(new Maqollar("Ishongan tog‘da kiyik yotmas.", "Ha воде век вековать, на воде его и покончить.", "The way to be safe is never to be secure."));
        map.put(list.get(1).getName(), posts);
        list.add(new ImageData(R.drawable.image3, "147 ta", "Boylik va faqirlik haqida"));
        posts = new ArrayList<>();
        posts.add(new Maqollar("Bo‘rining o‘zi to‘ysa ham, ko‘zi to‘ymas.", "Сколько собаке не хватать, а сытой не бывать.", "Greedy as а wolf."));
        posts.add(new Maqollar("Vafosizga berilma, Vafolidan ajrilma.", "Дай бог — с кем венчаться, с тем и кончаться.", "Wedlock is a padlock."));
        posts.add(new Maqollar("Ochko‘zni tuproq to‘ydirar.", "Дай бог много, а захочется и побольше.", "What's yours is mine, and what's mine is my own."));
        posts.add(new Maqollar(" Boyning xo‘rozi ham tuxum qilar.", "Денег куры не клюют.", "Roll in wealth."));
        posts.add(new Maqollar(" Xayr qilsang, butun qil.", "He от росы урожай, а от поту.", "What is worth doing is worth doing well."));
        posts.add(new Maqollar("Pulni ko‘p deb shopirma, Yo‘q vaqti zor bo‘lasan.", "Мотовство до нужды доведет.", "Waste not, want net."));
        posts.add(new Maqollar("Johilda kuch ko‘p bo‘lar, Kuchim degan tez o‘lar.", "Заставь дурака богу молиться — они лоб расшибет.", "Zeal is fit only for wise men, but is found mostly in fools."));
        posts.add(new Maqollar("Tinch oqar suvning tagi teran bo’lar.", "Тихие воды глубоки.", "Still waters have deep bottoms."));
        posts.add(new Maqollar("Urush — xunrezlik.", "Война кровь любит.", "Wars bring scars."));
        posts.add(new Maqollar("Bir daraxtdan bog‘ bo‘lmas.", "Один в поле не воин.", "The voice of one man is the voice of no one."));
        map.put(list.get(2).getName(), posts);
        list.add(new ImageData(R.drawable.image4, "162 ta", "Botirlik mardlik haqida"));
        posts = new ArrayList<>();
        posts.add(new Maqollar("Usta, ishni qilar yuzta.", "Дело мастера боится.", "The workman is known by his work."));
        posts.add(new Maqollar("Donoga ishora kifoya.", "Умный понимает с полуслова.", "A word to the wise."));
        posts.add(new Maqollar("Ishlamagan — tishlamas, Ishyoqmasga kun kulmas.", "Кто не работает, тот не ест.", "Work shall not eat,He that will not."));
        posts.add(new Maqollar("So‘z aytganda o‘ylab ayt, Oqibatini bilib ayt.", "Быть хозяином своего слова.", "One’s word is one's bond."));
        posts.add(new Maqollar("Vaqtida ogohlantirmoq — do‘stning ishi.", "Свое временное предупреждение — спасенье.", "A word in season."));
        posts.add(new Maqollar("Bilmaganning bilagi tolmas.", "Усердие не по разуму.", "Zeal without knowledge is fire without light."));
        posts.add(new Maqollar("Johilda kuch ko‘p bo‘lar, Kuchim degan tez o‘lar.", "Заставь дурака богу молиться — они лоб расшибет.", "Zeal is fit only for wise men, but is found mostly in fools."));
        posts.add(new Maqollar("Dono so‘ziga bino qo‘yar, Nodon o‘ziga bino qo‘yar.", "Умный взвешивает слова — дурак говорит не думая.", "Words are the wise man's counters and the fool's money."));
        posts.add(new Maqollar("Ayolning nozidan qo‘rq, Ahmoqning — so‘zidan.", "Женский обычай не мытьем, так катаньем, а свое возьмет.", "Women must have their wills while they live, because they make none when they die."));
        posts.add(new Maqollar("Dunyoni ahmoq so‘rar, Oqil uning hayronasi.", "Ну и чудеса!", "Wonders will never cease."));
        map.put(list.get(3).getName(), posts);
        list.add(new ImageData(R.drawable.image5, "150 ta", "Boshqalar"));
        posts = new ArrayList<>();
        posts.add(new Maqollar("Qaysi butaga o‘t tushsa, o‘sha buta kuyar.", "Один пирог два раза не съешь.", "You cannot sell the cow and drink the milk."));
        posts.add(new Maqollar("Johilda kuch ko‘p bo‘lar, Kuchim degan tez o‘lar.", "Заставь дурака богу молиться — они лоб расшибет.", "Zeal is fit only for wise men, but is found mostly in fools."));
        posts.add(new Maqollar("So‘z aytganda o‘ylab ayt, Oqibatini bilib ayt.", "Быть хозяином своего слова.", "One’s word is one's bond."));
        posts.add(new Maqollar("Donoga ishora kifoya.", "Умный понимает с полуслова.", "A word to the wise."));
        posts.add(new Maqollar("Suvning ishi — o‘pirmoq, O‘tning ishi — kuydirmoq.", "Ha воде шутки плохи.", "Worse things happen at sea."));
        posts.add(new Maqollar("Yomon bilan talashsang, qadring ketar.", "Куда ни кинь — всюду клин.", "Make the worst of both worlds."));
        posts.add(new Maqollar("It hurar — karvon o‘tar.", "Брань на вороту не виснет.", "Hard words break no bones."));
        posts.add(new Maqollar("Bo‘rini to‘q dema, dushmanni yo‘q dema.", "Волк в овечьей шкуре.", "A wolf in sheep's clothing."));
        posts.add(new Maqollar("Tili bilan suyar, Dili bilan so‘yar.", "Красивые слова иногда прикрывают неприглядные поступки.", "Fine words dress ill deeds."));
        posts.add(new Maqollar("Aroq ham bir, sharob ham bir ichganga.", "Вином горя не зальешь.", "The wine in the bottle does not quench thirst."));
        map.put(list.get(4).getName(), posts);
        list.add(new ImageData(R.drawable.image6, "34 ta", "Vatan va xalq haqida"));
        posts = new ArrayList<>();

        posts.add(new Maqollar("Maishatga ko‘p berilma, Jamiyatdan orqada qolasan.", "Гулянки да пирушки оставят без полушки.", "Wine and wenches empty men's purses."));
        posts.add(new Maqollar("Mushuk yo‘g‘ida sichqon tepaga chiqar.", "Кошка из дому — мышка на стол.", "When the cat is away the mice will play."));
        posts.add(new Maqollar("Pichir-pichirdan o‘t chiqar.", "Ложь, что ржа: тлит.", "Where there is whispering, there is lying."));
        posts.add(new Maqollar("Tinchini topmaganning boshida tegirmon toshi.", "He спавши, да беду наспал.", "Where there's а will, there's trouble."));
        posts.add(new Maqollar("Zamonaning zindoni ham, xandoni ham bor.", "Превратности судьбы.", "The whirligig of time."));
        posts.add(new Maqollar("Ko‘zing borida yo‘l tani, Esing borida el tani.", "Знать что к чему.", "To know what is what."));
        posts.add(new Maqollar("Dard kam, dahmaza kam.", "Гора с плеч.", "A great weight off one's mind."));
        posts.add(new Maqollar("Tinch oqar suvning tagi teran bo’lar.", "Тихие воды глубоки.", "Still waters have deep bottoms."));
        posts.add(new Maqollar("Urush — xunrezlik.", "Война кровь любит.", "Wars bring scars."));
        posts.add(new Maqollar("Zamoning tulki bo‘lsa, sen tozi bo‘l.", "Ловить рыбу в мутной воде.", "Waters, Fish in troubled."));

        map.put(list.get(5).getName(), posts);
        list.add(new ImageData(R.drawable.image7, "173 ta", "Donolik haqida"));
        posts = new ArrayList<>();
        posts.add(new Maqollar("Ishlab topganning — osh, Ishlamay topganing — tosh.", "Каковы дела, такова и слава.", "The labourer is worthy, of his hire."));
        posts.add(new Maqollar("Qarilikni donolik bezar, Yoshlikni — kamtarlik.", "Старые дураки глупее молодых.", "Young men think old men fools, and old men know young men to be so."));
        posts.add(new Maqollar("Bekorchidan-bemaza gap.", "Говорить на ветер.", "Waste words."));
        posts.add(new Maqollar("Yomon bilan talashsang, qadring ketar.", "Куда ни кинь — всюду клин.", "Make the worst of both worlds."));
        posts.add(new Maqollar("Vaqtida ogohlantirmoq — do‘stning ishi.", "Свое временное предупреждение — спасенье.", "A word in season."));
        posts.add(new Maqollar("Dono so‘ziga bino qo‘yar, Nodon o‘ziga bino qo‘yar.", "Умный взвешивает слова — дурак говорит не думая.", "Words are the wise man's counters and the fool's money."));
        posts.add(new Maqollar("Donoga ishora kifoya.", "Умный понимает с полуслова.", "A word to the wise."));
        posts.add(new Maqollar("So‘z o‘lchovi oz, ozning ma'nosi soz.", "Мешай дело с бездельем, проживешь век с весельем.", "All work and no play makes Jack a dull boy."));
        posts.add(new Maqollar("So‘z aytganda o‘ylab ayt, Oqibatini bilib ayt.", "Быть хозяином своего слова.", "One’s word is one's bond."));
        posts.add(new Maqollar("Bo‘rini to‘q dema, dushmanni yo‘q dema.", "Волк в овечьей шкуре.", "A wolf in sheep's clothing."));
        map.put(list.get(6).getName(), posts);
        list.add(new ImageData(R.drawable.image8, "100 ta", "Do’stlik va dushmanlik haqida"));
        posts = new ArrayList<>();

        posts.add(new Maqollar("Vaqtida ogohlantirmoq — do‘stning ishi.", "Свое временное предупреждение — спасенье.", "A word in season."));
        posts.add(new Maqollar("Dunyoni ahmoq so‘rar, Oqil uning hayronasi.", "Ну и чудеса!", "Wonders will never cease."));
        posts.add(new Maqollar("Bo‘rini to‘q dema, dushmanni yo‘q dema.", "Волк в овечьей шкуре.", "A wolf in sheep's clothing."));
        posts.add(new Maqollar("Tili bilan suyar, Dili bilan so‘yar.", "Красивые слова иногда прикрывают неприглядные поступки.", "Fine words dress ill deeds."));
        posts.add(new Maqollar("Xotin — erning vaziri.", "Муж — голова, жена — шея.", "He that has a wife has a master."));
        posts.add(new Maqollar("Dushman terisidan do‘sting uchun po‘stin bich.", "Волк в овечьей шкуре.", "Catch the whigs bathing and walk away with their clothes."));
        posts.add(new Maqollar("Mehmon izzatda, Mezbon xizmatda.", "Принес Бог гостя, дал хозяину пир.", "Welcome is the best cheer."));
        posts.add(new Maqollar("Chaqirilmagan qo‘noq — yo‘nilmagan tayoq.", "Ha незваного гостя не припасена и ложка.", "Welcome as a storm."));
        posts.add(new Maqollar("Tomoq ho‘llamoq.", "Промочить горю.", "To wet one's whistle."));
        posts.add(new Maqollar("Xayr qilsang, butun qil.", "He от росы урожай, а от поту.", "What is worth doing is worth doing well."));
        map.put(list.get(7).getName(), posts);
        list.add(new ImageData(R.drawable.image9, "186 ta", "Imkon va tushkunlik"));
        posts = new ArrayList<>();

        posts.add(new Maqollar("Ishlab topganning — osh, Ishlamay topganing — tosh.", "Каковы дела, такова и слава.", "The labourer is worthy, of his hire."));
        posts.add(new Maqollar("Chumchuqdan qo‘rqqan tariq ekmas, Chigirtkadan qo‘rqqan — ekin.", "Волков бояться — в лес не ходить.", "He that is afraid of wounds must not come near a battle."));
        posts.add(new Maqollar("Qarib quyilmagan, Achib suyulmas.", "Ha седину беспадок.", "Young saint, old devil."));
        posts.add(new Maqollar("Sabrli — chidar, Sabrsiz — yonar.", "Всякому терпению приходит конец.", "Even a worm will turn."));
        posts.add(new Maqollar("Ishlamagan — tishlamas, Ishyoqmasga kun kulmas.", "Кто не работает, тот не ест.", "Work shall not eat,He that will not."));
        posts.add(new Maqollar("Umr o‘zar, husn to‘zar.", "Такова жизнь.", "So goes the world."));
        posts.add(new Maqollar("Suvning ishi — o‘pirmoq, O‘tning ishi — kuydirmoq.", "Ha воде шутки плохи.", "Worse things happen at sea."));
        posts.add(new Maqollar("To‘ydan oldin nog‘ora chalma.", "Heговори \"ron\", пока не перескочишь.", "Don't halloo till you are out of the wood. "));
        posts.add(new Maqollar("Bo‘rkni tashlab bo‘ridan qutulib bo‘lmas.", "Сделать кого-либо козлом отпущения.", "Throw smb. to the wolves."));
        posts.add(new Maqollar("To‘r soluvdim baliqqa, Ilinib chiqdi qurbaqa.", "Пошел по шерсть, а воротился стриженый.", "Go for wool and come home shorn."));

        map.put(list.get(8).getName(), posts);
        list.add(new ImageData(R.drawable.image10, "215 ta", "Ishbilarmonlik va uddaburonlik"));

        posts = new ArrayList<>();

        posts.add(new Maqollar("Ishlab topganning — osh, Ishlamay topganing — tosh.", "Каковы дела, такова и слава.", "The labourer is worthy, of his hire."));
        posts.add(new Maqollar("Chumchuqdan qo‘rqqan tariq ekmas, Chigirtkadan qo‘rqqan — ekin.", "Волков бояться — в лес не ходить.", "He that is afraid of wounds must not come near a battle."));
        posts.add(new Maqollar("Bilmaganning bilagi tolmas.", "Усердие не по разуму.", "Zeal without knowledge is fire without light."));
        posts.add(new Maqollar("So‘z aytganda o‘ylab ayt, Oqibatini bilib ayt.", "Быть хозяином своего слова.", "One’s word is one's bond."));
        posts.add(new Maqollar("Bekorchidan-bemaza gap.", "Говорить на ветер.", "Waste words."));
        posts.add(new Maqollar("Donoga ishora kifoya.", "Умный понимает с полуслова.", "A word to the wise."));
        posts.add(new Maqollar("Dono so‘ziga bino qo‘yar, Nodon o‘ziga bino qo‘yar.", "Умный взвешивает слова — дурак говорит не думая.", "Words are the wise man's counters and the fool's money."));
        posts.add(new Maqollar("Usta lop-lop qiladi, Ishini asbob qiladi.", "Без топора не плотник.", "What is a workman without his tools?"));
        posts.add(new Maqollar("So‘z o‘lchovi oz, ozning ma'nosi soz.", "Мешай дело с бездельем, проживешь век с весельем.", "All work and no play makes Jack a dull boy."));
        posts.add(new Maqollar("Dunyo ko‘rmay, dunyo kishisi bo’lmas.", "Выйти в люди.", "Raise in the world."));

        map.put(list.get(9).getName(), posts);
        list.add(new ImageData(R.drawable.image11, "157 ta", "Mehnat va hunar"));
        posts = new ArrayList<>();

        posts.add(new Maqollar("Ishlab topganning — osh, Ishlamay topganing — tosh.", "Каковы дела, такова и слава.", "The labourer is worthy, of his hire."));
        posts.add(new Maqollar("Chumchuqdan qo‘rqqan tariq ekmas, Chigirtkadan qo‘rqqan — ekin.", "Волков бояться — в лес не ходить.", "He that is afraid of wounds must not come near a battle."));
        posts.add(new Maqollar("Yosh kelsa — ishga, Qari kelsa — oshga.", "Молодой на службу, старый на совет.", "Youth will serve."));
        posts.add(new Maqollar("Bilmaganning bilagi tolmas.", "Усердие не по разуму.", "Zeal without knowledge is fire without light."));
        posts.add(new Maqollar("Dono so‘ziga bino qo‘yar, Nodon o‘ziga bino qo‘yar.", "Умный взвешивает слова — дурак говорит не думая.", "Words are the wise man's counters and the fool's money."));
        posts.add(new Maqollar("Sabrli — chidar, Sabrsiz — yonar.", "Всякому терпению приходит конец.", "Even a worm will turn."));
        posts.add(new Maqollar("Suvning ishi — o‘pirmoq, O‘tning ishi — kuydirmoq.", "Ha воде шутки плохи.", "Worse things happen at sea."));
        posts.add(new Maqollar("Yomon bilan talashsang, qadring ketar.", "Куда ни кинь — всюду клин.", "Make the worst of both worlds."));
        posts.add(new Maqollar("Gap bilguncha ish bil.", "He по словам судят, а по делам.", "Words are but wind."));
        posts.add(new Maqollar("Ishlamagan — tishlamas, Ishyoqmasga kun kulmas.", "Кто не работает, тот не ест.", "Work shall not eat,He that will not."));
        map.put(list.get(10).getName(), posts);
        list.add(new ImageData(R.drawable.image12, "100 ta", "Mehr va oqibat haqida"));
        posts = new ArrayList<>();

        posts.add(new Maqollar("Yosh kelsa — ishga, Qari kelsa — oshga.", "Молодой на службу, старый на совет.", "Youth will serve."));
        posts.add(new Maqollar("Vaqtida ogohlantirmoq — do‘stning ishi.", "Свое временное предупреждение — спасенье.", "A word in season."));
        posts.add(new Maqollar("Bo‘ri qarisa, itga kulgi bo‘lar.", "Нам не страшен серый волк.", "Who's afraid of the big bad wolf?"));
        posts.add(new Maqollar("Ayolning nozidan qo‘rq, Ahmoqning — so‘zidan.", "Женский обычай не мытьем, так катаньем, а свое возьмет.", "Women must have their wills while they live, because they make none when they die."));
        posts.add(new Maqollar("Xotinning qaqildog‘i — tegirmonning shaqildog‘i.", "Где баба, тамрынок; где две, там базар.", "Where there are women and geese, there wants no noise."));
        posts.add(new Maqollar("Gap bilan osh pishmas.", "Разговорами сыт не будешь.", "Fine words butter no parsnips."));
        posts.add(new Maqollar("Xotin yettiga chiqsa, Yetti uyning kalitini ola chiqar.", "И дура жена мужу правды не скажет.", "A woman conceals what she knows not."));
        posts.add(new Maqollar("Maishatga ko‘p berilma, Jamiyatdan orqada qolasan.", "Гулянки да пирушки оставят без полушки.", "Wine and wenches empty men's purses."));
        posts.add(new Maqollar("Qo‘shning ko‘r bo‘lsa, ko‘zingni qis.", "С волками жить — по-волчьи выть.", "One must howl with the wolves."));
        posts.add(new Maqollar("Xotin er orqasidan — xotin, Xonim xon orqasidan — xonim.", "Жена Цезаря вне подозрений.", "Caesar's wifemust be above suspicion"));
        map.put(list.get(11).getName(), posts);
        list.add(new ImageData(R.drawable.image13, "89 ta", "Oila haqida"));

        posts = new ArrayList<>();
        posts.add(new Maqollar("Qarib quyilmagan, Achib suyulmas.", "Ha седину беспадок.", "Young saint, old devil."));
        posts.add(new Maqollar("Xotinning yomoni — umrning egovi.", "Одному с женою горе, другому вдвое.", "Women are necessary evils."));
        posts.add(new Maqollar("Xotin yettiga chiqsa, Yetti uyning kalitini ola chiqar.", "И дура жена мужу правды не скажет.", "A woman conceals what she knows not."));
        posts.add(new Maqollar("Xotinning qaqildog‘i — tegirmonning shaqildog‘i.", "Где баба, тамрынок; где две, там базар.", "Where there are women and geese, there wants no noise."));
        posts.add(new Maqollar("Ota ko‘rgan o‘t yo‘nar, Ona ko‘rgan to‘n bichar.", "He учил отец, а дядя не выучит.", "It is a wise child that knows its own father."));
        posts.add(new Maqollar("Ro‘zg‘or tushdi boshga, Qo‘l tegmadi oshga.", "Жениться — не лапоть надеть.", "It is hard to wive and thrive both in a year."));
        posts.add(new Maqollar("Qo‘shning ko‘r bo‘lsa, ko‘zingni qis.", "С волками жить — по-волчьи выть.", "One must howl with the wolves."));
        posts.add(new Maqollar("Xotinsiz o‘tish — xato, Bolasiz o‘tish — jafo.", "Без жены как без шапки.", "Wives must be had, be they good or bad."));
        posts.add(new Maqollar("Xotin — erning vaziri.", "Муж — голова, жена — шея.", "He that has a wife has a master."));
        posts.add(new Maqollar("Pichir-pichirdan o‘t chiqar.", "Ложь, что ржа: тлит.", "Where there is whispering, there is lying."));


        map.put(list.get(12).getName(), posts);
        list.add(new ImageData(R.drawable.image14, "43 ta", "Sabr qanoat haqida"));

        posts = new ArrayList<>();
        posts.add(new Maqollar("Qarib quyilmagan, Achib suyulmas.", "Ha седину беспадок.", "Young saint, old devil."));
        posts.add(new Maqollar("Ishlamagan — tishlamas, Ishyoqmasga kun kulmas.", "Кто не работает, тот не ест.", "Work shall not eat,He that will not."));
        posts.add(new Maqollar("Suvning ishi — o‘pirmoq, O‘tning ishi — kuydirmoq.", "Ha воде шутки плохи.", "Worse things happen at sea."));
        posts.add(new Maqollar("Xotinning qaqildog‘i — tegirmonning shaqildog‘i.", "Где баба, тамрынок; где две, там базар.", "Where there are women and geese, there wants no noise."));
        posts.add(new Maqollar("Xotinning yomoni — umrning egovi.", "Одному с женою горе, другому вдвое.", "Women are necessary evils."));
        posts.add(new Maqollar("To‘ydan oldin nog‘ora chalma.", "Heговори \"ron\", пока не перескочишь.", "Don't halloo till you are out of the wood."));
        posts.add(new Maqollar("Bo‘rini to‘q dema, dushmanni yo‘q dema.", "Волк в овечьей шкуре.", "A wolf in sheep's clothing."));
        posts.add(new Maqollar("Ayolning nozidan qo‘rq, Ahmoqning — so‘zidan.", "Женский обычай не мытьем, так катаньем, а свое возьмет.", "Women must have their wills while they live, because they make none when they die."));
        posts.add(new Maqollar("Ota ko‘rgan o‘t yo‘nar, Ona ko‘rgan to‘n bichar.", "He учил отец, а дядя не выучит.", "It is a wise child that knows its own father."));
        posts.add(new Maqollar("Havas bilan g‘ayrat o‘lchanar.", "Чего хочется, тому верится.", "The wish is father to the thought."));

        map.put(list.get(13).getName(), posts);
        list.add(new ImageData(R.drawable.image15, "50 ta", "Salomatlik va ozodalik"));

        posts = new ArrayList<>();
        posts.add(new Maqollar("Umr o‘zar, husn to‘zar.", "Такова жизнь.", "So goes the world."));
        posts.add(new Maqollar("Ayol husni pardozda emas.", "He наряд жен украсит — домостройство.", "The more women look in their glass the less they look to their house."));
        posts.add(new Maqollar("Xunukdan хudo bezor.", "Страшен как черт.", "As ugly as sin."));
        posts.add(new Maqollar("Ertagi nasibadan qolma, Kechki nasibaga borma.", "Утро вечера мудренее.", "Tomorrow is a new day."));
        posts.add(new Maqollar("Omon bo‘lsang, olam seniki.", "Желаю вам здравствовать долгие годы.", "May your shadow never grow less!"));
        posts.add(new Maqollar("Har go‘zalning bir aybi bor.", "Ложка дегтя в бочке мёда.", "A crumpled rose-leaf.."));
        posts.add(new Maqollar("Sihat tilasang ko‘p yema, Izzat tilasang ko‘p dema.", "He задавай вопросов, и не услышишь лжи.", "Ask no questions and you will be told no lies."));
        posts.add(new Maqollar("Dardingning vaqti o‘tsa, Tabibdan o‘pkalama.", "Береги платье снову, а здоровье смолоду.", "Prevention is better than cure."));
        posts.add(new Maqollar("Sog‘lom tanda — sog‘lom aql.", "Здоровом теле здоровый дух.", "A sound. mind in a sound body."));
        posts.add(new Maqollar("Kasallik botmonlab kelib, misqollab ketar.", "Болезнь входит пудами, а выходит золотниками.", "Mischief comes by the pound and goes away by the ounce."));

        map.put(list.get(14).getName(), posts);
        list.add(new ImageData(R.drawable.image16, "120 ta", "To’g’rilik haqida"));

        posts = new ArrayList<>();

        posts.add(new Maqollar("Qarilikni donolik bezar, Yoshlikni — kamtarlik.", "Qarilikni donolik bezar, Yoshlikni — kamtarlik.", "Young men think old men fools, and old men know young men to be so."));
        posts.add(new Maqollar("Yosh kelsa — ishga, Qari kelsa — oshga.", "Молодой на службу, старый на совет.", "Youth will serve."));
        posts.add(new Maqollar("Vaqtida ogohlantirmoq — do‘stning ishi.", "Свое временное предупреждение — спасенье.", "A word in season."));
        posts.add(new Maqollar("Ishlamagan — tishlamas, Ishyoqmasga kun kulmas.", "Кто не работает, тот не ест.", "Work shall not eat,He that will not."));
        posts.add(new Maqollar("Qallobning bolidan saxiyning zahri yaxshi.", "Пускать пыль в глаза.", "То pull the wool over a person's eyes."));
        posts.add(new Maqollar("Pichir-pichirdan o‘t chiqar.", "Ложь, что ржа: тлит.", "Where there is whispering, there is lying."));
        posts.add(new Maqollar("Ko‘zing yetmagan ishga qo‘l urma.", "He уверен — не берись.", "When in doubt, leave out."));
        posts.add(new Maqollar("Bolani yoshdan asra, Niholni boshdan asra.", "Гни дерево, пока гнется.", "As the twig is bent, so the tree is inclined."));
        posts.add(new Maqollar("Magizni bo‘lib yesa, qirq kishiga yetar.", "Тебе половинка и мне половинка.", "To make two bites of a cherry."));
        posts.add(new Maqollar("Haqiqat oltindan qimmat.", "За правду бог и добрые люди.", "Truth is the daughter of Allah."));

        map.put(list.get(15).getName(), posts);

        list.add(new ImageData(R.drawable.image17, "108 ta", "Umr va vaqt haqida"));

        posts = new ArrayList<>();
        posts.add(new Maqollar("Yoshi yetmay ishi yetmas.", "Молодо растет, а старо старится.", "Youth will have its course."));
        posts.add(new Maqollar("Yosh kelsa — ishga, Qari kelsa — oshga.", "Молодой на службу, старый на совет.", "Youth will serve."));
        posts.add(new Maqollar("Qari o‘lsa — davlat, Yosh o‘lsa — qiyomat.", "Молодые по выбор умрут, старые поголовно.", "Young men may die, old men must."));
        posts.add(new Maqollar("Qarib quyilmagan, Achib suyulmas.", "Ha седину беспадок.", "Young saint, old devil."));
        posts.add(new Maqollar("Yosh ketaman deb qo‘rqitar, Qari — o‘laman deb.", "В чем молод похвалится, в том стар покается.", "Youth and age will never agree."));
        posts.add(new Maqollar("Qarilikni donolik bezar, Yoshlikni — kamtarlik.", "Старые дураки глупее молодых.", "Young men think old men fools, and old men know young men to be so."));
        posts.add(new Maqollar("Bo‘ri qarisa, itga kulgi bo‘lar.", "Нам не страшен серый волк.", "Who's afraid of the big bad wolf?"));
        posts.add(new Maqollar("Har narsa o‘z vaqtida qiziq.", "Все приедается.", "A wonder lasts but nine days."));
        posts.add(new Maqollar("To‘ydan oldin nog‘ora chalma.", "Heговори \"ron\", пока не перескочишь.", "Don't halloo till you are out of the wood."));
        posts.add(new Maqollar("It hurar — karvon o‘tar.", "Брань на вороту не виснет.", "Hard words break no bones."));

        map.put(list.get(16).getName(), posts);

        list.add(new ImageData(R.drawable.image18, "282 ta", "Xulq va tarbiya haqida"));

        posts = new ArrayList<>();
        posts.add(new Maqollar("Johilda kuch ko‘p bo‘lar, Kuchim degan tez o‘lar.", "Заставь дурака богу молиться — они лоб расшибет.", "Zeal is fit only for wise men, but is found mostly in fools."));
        posts.add(new Maqollar("Qarib quyilmagan, Achib suyulmas.", "Ha седину беспадок.", "Young saint, old devil."));
        posts.add(new Maqollar("Yosh ketaman deb qo‘rqitar, Qari — o‘laman deb.", "В чем молод похвалится, в том стар покается.", "Youth and age will never agree."));
        posts.add(new Maqollar("Qarilikni donolik bezar, Yoshlikni — kamtarlik.", "Старые дураки глупее молодых.", "Young men think old men fools, and old men know young men to be so."));
        posts.add(new Maqollar("Yosh kelsa — ishga, Qari kelsa — oshga.", "Молодой на службу, старый на совет.", "Youth will serve."));
        posts.add(new Maqollar("Gap bilguncha ish bil.", "He по словам судят, а по делам.", "Words are but wind."));
        posts.add(new Maqollar("So‘z o‘lchovi oz, ozning ma'nosi soz.", "Мешай дело с бездельем, проживешь век с весельем.", "All work and no play makes Jack a dull boy."));
        posts.add(new Maqollar("Zahar til — qilichdan yomon.", "Злые языки — острый меч.", "Words cut more than swords."));
        posts.add(new Maqollar("So‘z aytganda o‘ylab ayt, Oqibatini bilib ayt.", "Быть хозяином своего слова.", "One’s word is one's bond."));
        posts.add(new Maqollar("Yomon bilan talashsang, qadring ketar.", "Куда ни кинь — всюду клин.", "Make the worst of both worlds."));

        map.put(list.get(17).getName(), posts);
        list.add(new ImageData(R.drawable.image19, "220 ta", "Yaxshilik va yomonlik haqida"));

        posts = new ArrayList<>();
        posts.add(new Maqollar("So‘z aytganda o‘ylab ayt, Oqibatini bilib ayt.", "Быть хозяином своего слова.", "One’s word is one's bond."));
        posts.add(new Maqollar("Zahar til — qilichdan yomon.", "Злые языки — острый меч.", "Words cut more than swords."));
        posts.add(new Maqollar("Gap bilguncha ish bil.", "He по словам судят, а по делам.", "Words are but wind."));
        posts.add(new Maqollar("Xotinning qaqildog‘i — tegirmonning shaqildog‘i.", "Где баба, тамрынок; где две, там базар.", "Where there are women and geese, there wants no noise."));
        posts.add(new Maqollar("Ayol husni pardozda emas.", "He наряд жен украсит — домостройство.", "The more women look in their glass the less they look to their house."));
        posts.add(new Maqollar("Tili bilan suyar, Dili bilan so‘yar.", "Красивые слова иногда прикрывают неприглядные поступки.", "Fine words dress ill deeds."));
        posts.add(new Maqollar("Qallobning bolidan saxiyning zahri yaxshi.", "Пускать пыль в глаза.", "То pull the wool over a person's eyes."));
        posts.add(new Maqollar("Aql aqldan quvvat olar.", "Великие души понимают друг друга.", "Good wits jump."));
        posts.add(new Maqollar(" Xayr qilsang, butun qil.", "He от росы урожай, а от поту.", "What is worth doing is worth doing well."));
        posts.add(new Maqollar("Urushning boshi — o‘yin, Oxiri — o‘lim.", "Паны дерутся, а у холопов чубы трещат.", "War is the sport of kings."));

        map.put(list.get(18).getName(), posts);
    }
}