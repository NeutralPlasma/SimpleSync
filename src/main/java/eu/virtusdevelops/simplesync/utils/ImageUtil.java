package eu.virtusdevelops.simplesync.utils;

import eu.virtusdevelops.simplesync.db.entity.LinkedPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class ImageUtil {

    public static BufferedImage generateImage(LinkedPlayer linkedPlayer, BufferedImage template) throws IOException {
        Graphics2D g2d = template.createGraphics();
        g2d.setPaint(new GradientPaint(0, 0, Color.CYAN, 1080, 540, Color.PINK));
//        g2d.setColor(Color.BLUE);
        //g2d.fillRect(0, 0, 1080, 540);


        g2d.setColor(Color.WHITE);


        URL url = new URL("https://mc-heads.net/head/" + linkedPlayer.getUsername() + "/nohead");
        BufferedImage image = ImageIO.read(url);


        g2d.drawImage(image, 10, 3, 50, 50,null);
        g2d.drawString(linkedPlayer.getUsername(), 74, 25);

        // if player online blabla add check
        g2d.setColor(Color.GREEN);
        g2d.fillOval(9, 42, 15, 15);


        g2d.dispose();
        return template;
    }



    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }
}
