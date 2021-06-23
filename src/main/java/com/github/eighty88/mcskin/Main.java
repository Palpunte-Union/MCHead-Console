package com.github.eighty88.mcskin;

import com.github.eighty88.mcskin.imager.ImageConverter;
import com.github.eighty88.mcskin.imager.PlayerSkin;
import com.github.eighty88.mcskin.imager.SkinPose;
import com.github.eighty88.mcskin.imager.renderer.PointLight;
import com.github.eighty88.mcskin.imager.renderer.point.Point3d;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        a(args[0]);
    }

    public static void a(String str) {
        BufferedImage image;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + str);

            ArrayList<PointLight> lights = new ArrayList<>();
            lights.add(new PointLight(new Point3d(0, 150, 125), 5000, 3));
            JSONObject object = new JSONObject(Objects.requireNonNull(readURL(url)));
            String uuidString = (String) object.get("id");
            image = ImageConverter.renderSkin(
                    new PlayerSkin(uuidString),
                    new SkinPose(SkinPose.standing().getValues()),
                    0f,
                    0f,
                    lights,
                    0,
                    0,
                    0,
                    1,
                    1000,
                    1000
            );
            image = image.getSubimage(367, 76, 264, 264);
            ImageIO.write(image, "png", new File(str + ".png"));
        } catch (Exception e) {
            System.out.println("Player Not Found!");
        }
    }

    private static String readURL(URL url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder builder = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                builder.append(chars, 0, read);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
