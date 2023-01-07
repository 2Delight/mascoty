package org.mascoty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Picture extends JPanel {
    private MainFrame gui;

    private BufferedImage face, eyes_opened, eyes_closed, mouth_opened, mouth_closed;

    public Picture(MainFrame gui) {
        super();
        this.gui = gui;
        setMinimumSize(new Dimension(90, 90));
//        setBackground(Color.GREEN);
        setBackground(new Color(0, 0, 0, 0));

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("face.png");
            face = ImageIO.read(is);

            is = classloader.getResourceAsStream("eyes_o.png");
            eyes_opened = ImageIO.read(is);

            is = classloader.getResourceAsStream("eyes_c.png");
            eyes_closed = ImageIO.read(is);

            is = classloader.getResourceAsStream("mouth_o.png");
            mouth_opened = ImageIO.read(is);

            is = classloader.getResourceAsStream("mouth_c.png");
            mouth_closed = ImageIO.read(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        //setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        //setMinimumSize(new Dimension(100,100));
        //setPreferredSize(new Dimension(600,600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ui != null) {
            ColorUIResource color = (ColorUIResource) UIManager.get("ComboBox.background");
            ColorUIResource fontColor = (ColorUIResource) UIManager.get("ComboBox.foreground");
            Graphics scratchGraphics = (g == null) ? null : g.create();
            try {
                assert g != null;

                Graphics2D g2 = (Graphics2D) g.create();

                RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHints(qualityHints);


//                g2.setPaint(new GradientPaint(new Point(0, 0), new Color(0, 0,
//                        0,0), new Point(0, getHeight()), new Color(0, 0,
//                        0,0)));
//                g2.setColor(new Color(0,0,0,0));
//                g2.clearRect(0,0,500,500);

                // first // second
                // third // forurth


//                g2.fillRoundRect(2, 2, getWidth()-2, getHeight()-2, 20, 20);


                g2.setPaint(new GradientPaint(new Point(0, 0), new Color(fontColor.getRed(), fontColor.getGreen(),
                        fontColor.getBlue()), new Point(0, getHeight()), new Color(fontColor.getRed(), fontColor.getGreen(),
                        fontColor.getBlue())));


                g2.drawImage(face, 0, Mascot.voice, null);


                if (Mascot.lips) {
                    g2.drawImage(mouth_opened, 0, Mascot.voice, null);
                } else {
                    g2.drawImage(mouth_closed, 0, Mascot.voice, null);
                }

                if (!Mascot.blink) {
                    g2.drawImage(eyes_opened, 0, Mascot.voice, null);
                } else {
                    g2.drawImage(eyes_closed, 0, Mascot.voice, null);
                }


//				for (int i = 0; i < gui.skeletonList.nodes.size(); i++) {
//					if (gui.skeletonList.nodes.get(i).toString().contains("Tracker")) {
//						continue;
//					}
//					int x = (int) Math.round(Double.parseDouble(gui.skeletonList.nodes.get(i).x.getText().replace(',', '.')) / 2 * (first.stopX*scale.getValue()*1.0/100) + first.stopX * 1.0 / 2);
////					if (x > first.startX + first.stopX / 2) {
////						x = (int) Math.round(x * (1 - 1.0 * scale.getValue() / 100));
////					} else {
////						x = (int) Math.round(x * (1+1.0 * scale.getValue() / 100));
////					}
//
//					int y = (int) Math.round((first.stopY*scale.getValue()*1.0/100) - Double.parseDouble(gui.skeletonList.nodes.get(i).y.getText().replace(',', '.')) / 2 * (first.stopY*scale.getValue()*1.0/100));
////					if (y > first.startY + first.stopY / 2) {
////						y = (int) Math.round(y * (1 - 1.0 * scale.getValue() / 100));
////					} else {
////						y = (int) Math.round(y * (1+1.0 * scale.getValue() / 100));
////					}
//
//					int z = (int) Math.round(Double.parseDouble(gui.skeletonList.nodes.get(i).z.getText().replace(',', '.')) * resizeMultiplier.getValue()) + 5;
//					if (!resizePoints.isSelected()) {
//						z = 5;
//					}
//					if (showPoints.isSelected()) {
//
//						g2.drawOval(first.startX + x - z / 2, first.startY + y - z / 2, z, z);
//					}
//					if (showNames.isSelected()) {
//						g2.drawString(gui.skeletonList.nodes.get(i).toString(), first.startX + x, first.startY + y);
//					}
//				}
//				g2.drawString("Front View:", first.startX + 10, first.startY + 20);
//
//
//				for (int i = 0; i < gui.skeletonList.nodes.size(); i++) {
//					if (gui.skeletonList.nodes.get(i).toString().contains("Tracker")) {
//						continue;
//					}
//
//					int x = (int) Math.round(Double.parseDouble(gui.skeletonList.nodes.get(i).z.getText().replace(',', '.')) / 2 * (second.stopX*scale.getValue()*1.0/100) + second.stopX * 1.0 / 2);
//					int y = (int) Math.round((second.stopY*scale.getValue()*1.0/100) - Double.parseDouble(gui.skeletonList.nodes.get(i).y.getText().replace(',', '.')) / 2 * (second.stopY*scale.getValue()*1.0/100));
//					int z = (int) Math.round(Double.parseDouble(gui.skeletonList.nodes.get(i).x.getText().replace(',', '.')) * resizeMultiplier.getValue()) + 5;
//					if (!resizePoints.isSelected()) {
//						z = 5;
//					}
//					if (showPoints.isSelected()) {
//						g2.drawOval(second.startX + x - z / 2, second.startY + y - z / 2, z, z);
//					}
//					if (showNames.isSelected()) {
//						g2.drawString(gui.skeletonList.nodes.get(i).toString(), second.startX + x, second.startY + y);
//					}
//				}
//				g2.drawString("Side View:", second.startX + 10, second.startY + 20);
//
//
//				for (int i = 0; i < gui.skeletonList.nodes.size(); i++) {
//					if (gui.skeletonList.nodes.get(i).toString().contains("Tracker")) {
//						continue;
//					}
//
//					int x = (int) Math.round(Double.parseDouble(gui.skeletonList.nodes.get(i).x.getText().replace(',', '.')) / 2 * (third.stopX*scale.getValue()*1.0/100) + third.stopX * 1.0 / 2);
//					int y = (int) Math.round(third.stopY - Double.parseDouble(gui.skeletonList.nodes.get(i).z.getText().replace(',', '.')) / 2 * (third.stopY*scale.getValue()*1.0/100));
//					int z = (int) Math.round(Double.parseDouble(gui.skeletonList.nodes.get(i).y.getText().replace(',', '.')) * resizeMultiplier.getValue() / 10) + 5;
//					if (!resizePoints.isSelected()) {
//						z = 5;
//					}
//					if (showPoints.isSelected()) {
//						g2.drawOval(third.startX + x - z / 2, third.startY / 2 + y - z / 2, z, z);
//					}
//					if (showNames.isSelected()) {
//						g2.drawString(gui.skeletonList.nodes.get(i).toString(), third.startX + x, third.startY / 2 + y);
//					}
//				}

                g2.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                assert scratchGraphics != null;
                scratchGraphics.dispose();
            }
            //System.out.println("ui drawn");
        }
    }
}
