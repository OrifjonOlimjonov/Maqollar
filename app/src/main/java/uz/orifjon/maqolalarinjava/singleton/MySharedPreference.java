package uz.orifjon.maqolalarinjava.singleton;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    private static MySharedPreference mySharedPreference = new MySharedPreference();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private MySharedPreference() {
    }

    public static MySharedPreference getInstance(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("movies",Context.MODE_PRIVATE);
        }
        return mySharedPreference;
    }

    public String get(){
        return sharedPreferences.getString("movies","");
    }
    public void set( String str){
        editor = sharedPreferences.edit();
        editor.putString("movies",str).commit();
    }

    public void clear(){
        editor.clear();
    }
}
