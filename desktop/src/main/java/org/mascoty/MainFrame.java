package org.mascoty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static javax.swing.BoxLayout.PAGE_AXIS;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainFrame extends JFrame {
    private JPanel pane;
    private Picture picture;
    public static final int PORT = 50051;

    private final Action exitAction = new AbstractAction("Exit") {
        {
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    private final Action changeColorAction = new AbstractAction("Change Background Color") {
        {
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);


        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JColorChooser chooser = new JColorChooser();
            Color chosen = JColorChooser.showDialog(picture, "Select New Background Color", Color.GREEN);
            picture.setBackground(chosen);
        }
    };

    private final JPopupMenu jPopupMenu = new JPopupMenu();

    {
        jPopupMenu.add(new JMenuItem(exitAction));
        jPopupMenu.add(new JMenuItem(changeColorAction));

        // To avoid popup cutting by main window shape forbid light-weight popups
        jPopupMenu.setLightWeightPopupEnabled(false);
    }

    private final MouseAdapter mouseListener = new MouseAdapter() {

        int x, y;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                x = e.getX();
                y = e.getY();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                jPopupMenu.show(getContentPane(), e.getX(), e.getY());
            }
        }
    };


    public JTextArea ip, port, time;
    JRadioButton second;
    JButton start, top;
    // Уже бывавшие в использовании сервера закрытые порты.
    private final ArrayList<Integer> usedPorts = new ArrayList<>();

    public MainFrame() {
        super("ALPHA MASCOTY");
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        setAlwaysOnTop(true);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), PAGE_AXIS));
        // Добавление скрола по вертикали и горизонтали.

//        add(new JScrollPane(pane = new JPanel(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        setMinimumSize(new Dimension(700, 900));
//        pane.removeAll();
//        pane.setLayout(new BoxLayout(pane, PAGE_AXIS));
//        pane.setBackground(new Color(0,0,0,0));
        add(picture = new Picture(this));

        JButton button = new JButton("REFRESH CONNECTION");

        button.addActionListener(e -> {
            picture = new Picture(this);
        });
        add(button);


        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Exception while sleep");
                    }

                    ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", PORT).usePlaintext().build();

                    MascotGrpc.MascotBlockingStub stub = MascotGrpc.newBlockingStub(channel);
                    MascotRequest request = MascotRequest.newBuilder().build();
                    System.out.println(request.getAllFields());

                    try {
                        MascotResponse response = stub.getMascot(request);
                        Mascot.emotion = response.getEmotion();
                        Mascot.blink = response.getBlink();
                        Mascot.lips = response.getLips();
                        Mascot.voice = response.getVoice();
                    } catch (StatusRuntimeException e) {
                        System.out.println(e.getStatus());
                    }

                    picture.repaint();
                    try {
                        Thread.sleep(10);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }.start();
        repaint();
        build();
    }

    public void build() {
        setVisible(true);

        pack();
        setLocationRelativeTo(null);

//        JLabel timeLabel;
//
//        cons.anchor = GridBagConstraints.NORTHWEST;
//        cons.insets = new Insets(5, 10, 5, 5);
//        cons.gridx = 1;
//        add(new JLabel("IP:   "), cons);
//
//        cons.gridx = 2;
//        add(ip = new JTextArea(), cons);
//        ip.setText("server is not running");
//        ip.setEnabled(false);
//
//        cons.gridx = 1;
//        cons.gridy = 1;
//        add(new JLabel("Port: "), cons);
//
//        cons.gridx = 2;
//        add(port = new JTextArea(), cons);
//        port.setText("6969");
//
//        port.setPreferredSize(new Dimension(300, 25));
//        ip.setPreferredSize(new Dimension(300, 25));
//
//        cons.gridx = 0;
//        cons.gridy = 0;
//        cons.gridheight = 4;
//        top = new JButton("/icons/top");
//        top.setPreferredSize(new Dimension(50, 50));
//        add(top, cons);
//        top.setToolTipText("Get all recordings from database");
//
//
//        cons.gridx = 0;
//        cons.gridy = 3;
//        cons.gridheight = 4;
//        start = new JButton("/icons/start");
//        start.setPreferredSize(new Dimension(50, 50));
//        add(start, cons);
//        start.setToolTipText("Start server");
//        start.addActionListener(e -> {
//            // Обработка контента текстовых полей.
//            int truePort, trueTime;
//            try {
//                truePort = Integer.parseInt(port.getText());
//            } catch (Exception ex) {
//                showMessageDialog(null, "Port must be a number", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            if (truePort < 1) {
//                showMessageDialog(null, "Port must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            try {
//                trueTime = Integer.parseInt(time.getText());
//            } catch (Exception ex) {
//                showMessageDialog(null, "Time must be a number", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            if (trueTime < 1) {
//                showMessageDialog(null, "Time must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            if (usedPorts.contains(truePort) && usedPorts.get(usedPorts.size() - 1) != truePort) {
//                showMessageDialog(null, "Too late, I've already closed that port\nTry restarting server", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            // Начало работы серверного потока.
//            usedPorts.add(truePort);
//
//            start.setEnabled(false);
//            time.setEnabled(false);
//            port.setEnabled(false);
//            second.setEnabled(false);
//        });
//
//        cons.gridheight = 1;
//        cons.gridx = 1;
//        cons.gridy = 4;
//        second = new JRadioButton("second player");
//        add(second, cons);
//
//
//        cons.gridy = 3;
//        time = new JTextArea();
//        time.setText("5");
//        time.setPreferredSize(new Dimension(300, 25));
//
//        timeLabel = new JLabel("Session time: ");
//        add(timeLabel, cons);
//        cons.gridx = 2;
//        add(time, cons);
//


    }
}
