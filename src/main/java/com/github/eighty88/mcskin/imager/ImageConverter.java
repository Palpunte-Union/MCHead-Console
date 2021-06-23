package com.github.eighty88.mcskin.imager;

import com.github.eighty88.mcskin.imager.renderer.LightingControl;
import com.github.eighty88.mcskin.imager.renderer.PointLight;
import com.github.eighty88.mcskin.imager.renderer.point.Point3d;
import com.github.eighty88.mcskin.imager.renderer.point.PointConverter;
import com.github.eighty88.mcskin.imager.renderer.shapes.Polygon3d;
import com.github.eighty88.mcskin.imager.renderer.shapes.Tetrahedron;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageConverter {
    public static BufferedImage renderSkin(PlayerSkin playerSkin, SkinPose skinPose, Float headPitch, Float headYaw, ArrayList<PointLight> lights, double xRotation, double yRotation, double zRotation, int subdivisions, int width, int height) {
        PointConverter.WIDTH = width;
        PointConverter.HEIGHT = height;
        PointConverter.CAM_DISTANCE = 85000.0*Math.pow(PointConverter.SCALE, 0.25)/height-205;
        PointConverter.CAM_DISTANCE /= (250.0/PointConverter.FOV_SCALE);

        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2D = bImage.createGraphics();
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        Rectangle2D.Double rect = new Rectangle2D.Double(0,0,bImage.getWidth(),bImage.getHeight());
        g2D.fill(rect);
        g2D.setPaintMode();

        if(skinPose == null) {
            skinPose = SkinPose.standing();
        }

        Tetrahedron playerModel = playerSkin.getFigure(10, headPitch, headYaw, skinPose).mergeAll();
        playerModel.rotate(true, 0, 0, zRotation, LightingControl.lightVector);
        playerModel.rotate(true, 0, yRotation, 0, LightingControl.lightVector);
        playerModel.rotate(true, xRotation, 0, 0, LightingControl.lightVector);

        playerModel.renderLighting(bImage.getGraphics(), subdivisions, lights);

        playerModel.delete();

        System.gc();

        return bImage;

    }

    public static Tetrahedron imageToTetrahedron(BufferedImage image, double scale, boolean hasAlpha) {
        List<Polygon3d> polygons = new ArrayList<>();
        double startX = -image.getWidth()*scale/2;
        double startY = image.getHeight()*scale/2;


        Color color;
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                color = new Color(image.getRGB(x, y), hasAlpha);

                polygons.add(
                        new Polygon3d(color,
                                new Point3d(startX+(x*scale), startY+(y*scale), 0),
                                new Point3d(startX+((x+1)*scale), startY+(y*scale), 0),
                                new Point3d(startX+((x+1)*scale), startY+((y+1)*scale), 0),
                                new Point3d(startX+(x*scale), startY+((y+1)*scale), 0)
                        )
                );
            }
        }

        return new Tetrahedron(polygons);
    }
}

