package me.thienbao860.android.horsegameapp;

import android.content.Context;
import android.widget.Toast;

import java.util.Random;

public class Utils {

    public static void sendToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String randomHexColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public static double generateRandom(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }
}
