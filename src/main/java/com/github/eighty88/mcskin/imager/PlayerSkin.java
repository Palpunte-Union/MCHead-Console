package com.github.eighty88.mcskin.imager;

import com.github.eighty88.mcskin.imager.renderer.shapes.ObjectGroup;
import com.github.eighty88.mcskin.imager.renderer.shapes.Tetrahedron;

import java.awt.image.BufferedImage;

public class PlayerSkin {
    public enum SkinConfig {STEVE, ALEX}
    public enum Facing {FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM}
    public enum Layer {BASE, OVERLAY, BOTH}

    private final BufferedImage skinFile;

    private static final double DEFAULT_OVERLAY_SCALE = 1.125;

    public PlayerSkin(String uuid) {
        this.skinFile = PlayerSkinGrabber.getSkin(uuid);

        if(this.skinFile==null)
            System.out.println("Player " + uuid + " not found!");
    }

    public BufferedImage get(Facing side, Layer layers) {
        return getHead(side, layers);
    }

    private BufferedImage getHead(Facing side, Layer layers) {
        BufferedImage image;
        int x;
        int y;
        int width = 8;
        int height = 8;

        switch(side) {
            case FRONT:
                x = 8;
                y = 8;
                break;
            case BACK:
                x = 24;
                y = 8;
                break;
            case LEFT:
                x = 16;
                y = 8;
                break;
            case RIGHT:
                x = 0;
                y = 8;
                break;
            case TOP:
                x = 8;
                y = 0;
                break;
            case BOTTOM:
                x = 16;
                y = 0;
                break;
            default:
                return null;
        }

        if(layers == Layer.OVERLAY) {
            x+=32;
        }

        image = skinFile.getSubimage(x, y, width, height);
        return image;
    }

    public ObjectGroup getModel(double scale, Layer layers, double overlayScale) {
        boolean hasAlpha = false;
        if(layers == Layer.BOTH) {
            return getModel(scale, Layer.BASE, overlayScale).merge(getModel(scale, Layer.OVERLAY, overlayScale));
        } else if (layers == Layer.OVERLAY) {
            hasAlpha = true;
            scale*=overlayScale;
        }

        double xScale = get(Facing.FRONT, layers).getWidth()/2.0;
        double yScale = get(Facing.TOP, layers).getHeight()/2.0;
        double zScale = get(Facing.FRONT, layers).getHeight()/2.0;

        Tetrahedron front =  ImageConverter.imageToTetrahedron(get(Facing.FRONT, 	layers), scale, hasAlpha);
        Tetrahedron back  =  ImageConverter.imageToTetrahedron(get(Facing.BACK, 	layers), scale, hasAlpha);
        Tetrahedron left  =	 ImageConverter.imageToTetrahedron(get(Facing.LEFT, 	layers), scale, hasAlpha);
        Tetrahedron right =  ImageConverter.imageToTetrahedron(get(Facing.RIGHT, 	layers), scale, hasAlpha);
        Tetrahedron top   =	 ImageConverter.imageToTetrahedron(get(Facing.TOP, 	layers), scale, hasAlpha);
        Tetrahedron bottom = ImageConverter.imageToTetrahedron(get(Facing.BOTTOM, layers), scale, hasAlpha);

        front.rotate(true, 270, 0, 0, null);
        back.rotate(true, 90, 180, 0, null);
        left.rotate(true, 270, 0, 270, null);
        right.rotate(true, 270, 0, 90, null);
        top.rotate(true, 0, 0, 0, null);
        bottom.rotate(true, 0, 0, 0, null);

        front.setLocation(0, scale*yScale, 0);
        back.setLocation(0, -scale*yScale, 0);
        left.setLocation(scale*xScale, 0, 0);
        right.setLocation(-scale*xScale, 0, 0);
        top.setLocation(0, 0, scale*zScale);
        bottom.setLocation(0, 0, -scale*zScale);

        return new ObjectGroup(front, back, left, right, top, bottom);

    }

    public ObjectGroup getFigure(double scale, double overlayScale, Float headPitch, Float headYaw, SkinPose skinPose) {
        Layer layer = Layer.BOTH;

        if(skinPose == null)
            skinPose = SkinPose.standing();

        double[][][] pose = skinPose.getValues();

        ObjectGroup head = getModel(scale, layer, overlayScale);

        scale*=4;

        head.setRotation(pose[SkinPose.HEAD][SkinPose.ROTATION][0], pose[SkinPose.HEAD][SkinPose.ROTATION][1], pose[SkinPose.HEAD][SkinPose.ROTATION][2]);


        head.setLocation(pose[SkinPose.HEAD][SkinPose.LOCATION][0]*scale, pose[SkinPose.HEAD][SkinPose.LOCATION][1]*scale, pose[SkinPose.HEAD][SkinPose.LOCATION][2]*scale);

        if(headPitch != null || headYaw != null) {
            head.setLocation(0, 0, scale/2);
            head.setRotation((headPitch==null? 0 : headPitch), 0, 0);
            head.rotate(true, 0, 0, (headYaw==null? 0 : headYaw), null);
            head.setLocation(pose[SkinPose.HEAD][SkinPose.LOCATION][0]*scale, pose[SkinPose.HEAD][SkinPose.LOCATION][1]*scale, pose[SkinPose.HEAD][SkinPose.LOCATION][2]*scale);
        }

        head.identifier = "HEAD";

        ObjectGroup output = new ObjectGroup(head);
        output.identifier = "FIGURE";
        return output;
    }

    public ObjectGroup getFigure(double scale, Float headPitch, Float headYaw, SkinPose skinPose) {
        return getFigure(scale, DEFAULT_OVERLAY_SCALE, headPitch, headYaw, skinPose);
    }

}
