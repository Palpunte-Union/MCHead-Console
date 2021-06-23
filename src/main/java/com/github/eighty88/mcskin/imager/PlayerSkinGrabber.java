package com.github.eighty88.mcskin.imager;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class PlayerSkinGrabber {

    private static final String SKIN_URL_PREFIX = "https://crafatar.com/skins/";

    private static final String DEFAULT_SKIN_STEVE = "https://cdn.discordapp.com/attachments/774125506210693121/780156711281098762/default_steve.png";
    private static final String DEFAULT_SKIN_ALEX = "https://cdn.discordapp.com/attachments/774125506210693121/780156699133870090/default_alex.png";


    public static BufferedImage getSkin(String uuid) {
        BufferedImage img = retrieveImage(SKIN_URL_PREFIX + uuid);

        if (img == null) {
            img = getDefaultSkin(PlayerSkin.SkinConfig.STEVE);
        }

        return img;
    }

    public static BufferedImage getDefaultSkin(PlayerSkin.SkinConfig skinType) {
        switch (skinType) {
            case STEVE:
                return retrieveImage(DEFAULT_SKIN_STEVE);
            case ALEX:
                return retrieveImage(DEFAULT_SKIN_ALEX);
            default:
                return null;
        }
    }

    private static BufferedImage retrieveImage(String urlAddress) {
        try {
            URL url = new URL(urlAddress);
            return ImageIO.read(url);
        } catch (Exception e) {
            return null;
        }
    }

}
