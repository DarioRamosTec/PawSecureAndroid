package com.example.pawsecure.implementation;

import com.example.pawsecure.R;

public class PawSecureResource {
    public static int getIdIconPet (PawSecureActivity pawSecureActivity, int icon) {
        switch (icon) {
            default:
                return R.drawable.icon_animal;
            case 1:
                return R.drawable.pet_cat_1;
            case 2:
                return R.drawable.pet_cat_2;
            case 3:
                return R.drawable.pet_cat_3;
            case 4:
                return R.drawable.pet_cat_4;
            case 5:
                return R.drawable.pet_rabbit_1;
            case 6:
                return R.drawable.pet_rabbit_2;
            case 7:
                return R.drawable.pet_rabbit_3;
            case 8:
                return R.drawable.pet_rabbit_4;
            case 9:
                return R.drawable.pet_dog_1;
            case 10:
                return R.drawable.pet_dog_2;
            case 11:
                return R.drawable.pet_dog_3;
            case 12:
                return R.drawable.pet_dog_4;
        }
    }
}
