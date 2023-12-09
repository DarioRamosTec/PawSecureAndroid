package com.example.pawsecure.singletone;

import com.example.pawsecure.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePet {

    static List<Integer> petsList = null;

    public static List<Integer> getList () {
        if (petsList == null) {
            petsList = new ArrayList<>();
            petsList.add(0, R.drawable.pet_cat_1);
            petsList.add(1, R.drawable.pet_cat_2);
            petsList.add(2, R.drawable.pet_cat_3);
            petsList.add(3, R.drawable.pet_cat_4);
            petsList.add(4, R.drawable.pet_rabbit_1);
            petsList.add(5, R.drawable.pet_rabbit_2);
            petsList.add(6, R.drawable.pet_rabbit_3);
            petsList.add(7, R.drawable.pet_rabbit_4);
            petsList.add(8, R.drawable.pet_dog_1);
            petsList.add(9, R.drawable.pet_dog_2);
            petsList.add(10, R.drawable.pet_dog_3);
            petsList.add(11, R.drawable.pet_dog_4);
        }
        return petsList;
    }

    public static int getIdIconPet (int icon) {
        if (icon <= -1) {
            return R.drawable.icon_new;
        } else if (icon == 0 || icon >= petsList.size()) {
            return R.drawable.icon_animal;
        }
        return getList().get(icon);
    }
}
