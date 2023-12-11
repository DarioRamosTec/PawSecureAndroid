package com.example.pawsecure.singletone;

import com.example.pawsecure.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePetManager {

    static List<ImagePet> petsList = null;

    public static List<ImagePet> getList () {
        if (petsList == null) {
            petsList = new ArrayList<>();
            petsList.add(0, new ImagePet() {{ this.icon = R.drawable.pet_cat_1; this.animal = 1; }});
            petsList.add(1, new ImagePet() {{ this.icon = R.drawable.pet_cat_2; this.animal = 1; }});
            petsList.add(2, new ImagePet() {{ this.icon = R.drawable.pet_cat_3; this.animal = 1; }});
            petsList.add(3, new ImagePet() {{ this.icon = R.drawable.pet_cat_4; this.animal = 1; }});
            petsList.add(4, new ImagePet() {{ this.icon = R.drawable.pet_dog_1; this.animal = 2; }});
            petsList.add(5, new ImagePet() {{ this.icon = R.drawable.pet_dog_2; this.animal = 2; }});
            petsList.add(6, new ImagePet() {{ this.icon = R.drawable.pet_dog_3; this.animal = 2; }});
            petsList.add(7, new ImagePet() {{ this.icon = R.drawable.pet_dog_4; this.animal = 2; }});
            petsList.add(8, new ImagePet() {{ this.icon = R.drawable.pet_rabbit_1; this.animal = 3; }});
            petsList.add(9, new ImagePet() {{ this.icon = R.drawable.pet_rabbit_2; this.animal = 3; }});
            petsList.add(10, new ImagePet() {{ this.icon = R.drawable.pet_rabbit_3; this.animal = 3; }});
            petsList.add(11, new ImagePet() {{ this.icon = R.drawable.pet_rabbit_4; this.animal = 3; }});
            petsList.add(12, new ImagePet() {{ this.icon = R.drawable.icon_animal; this.animal = 1; }});
        }
        return petsList;
    }

    public static int getIdIconPet (int icon) {
        if (icon <= -1) {
            return R.drawable.icon_new;
        } else if (icon == 0 || icon >= getList().size()) {
            return R.drawable.icon_animal;
        }
        return getList().get(icon).icon;
    }

    public static class ImagePet {
        public int icon;
        public int animal;
    }
}
