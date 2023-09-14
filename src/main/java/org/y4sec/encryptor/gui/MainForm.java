package org.y4sec.encryptor.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.y4sec.encryptor.core.Constants;
import org.y4sec.encryptor.core.PatchHelper;
import org.y4sec.encryptor.util.JNIUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainForm implements Constants {
    private JPanel masterPanel;
    private JPanel rootPanel;
    private JTextField jarText;
    private JButton choseFileButton;
    private JTextField packageText;
    private JButton encryptJarFileButton;
    private JButton exportAgentLibButton;
    private JPanel logPanel;
    private JScrollPane scroll;
    private JTextArea logArea;
    private JLabel jarLabel;
    private JLabel packageLabel;
    private JLabel operationLabel;
    private JPanel operationPanel;
    private JLabel keyLabel;
    private JTextField keyText;
    private JButton showCommandButton;
    private static String pack;
    private static String globalKey;
    private static String libAbs;
    private static String jarAbs;

    public static void start() {
        JFrame frame = new JFrame("code-encryptor-plus");
        frame.setContentPane(new MainForm().masterPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void log(String data) {
        String msg = String.format("[log] %s\n", data);
        logArea.append(msg);
        logArea.setCaretPosition(logArea.getText().length());
    }

    public MainForm() {
        log("start code encryptor success");
        choseFileButton.addActionListener(e -> {
            log("chose jar file operation");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JAR", "jar");
            fileChooser.addChoosableFileFilter(filter);
            int option = fileChooser.showOpenDialog(new JFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String absPath = file.getAbsolutePath();
                jarText.setText(absPath);
            }
        });

        encryptJarFileButton.addActionListener(e -> {
            String packageName = packageText.getText();
            if (packageName == null || packageName.isEmpty()) {
                log("package is null");
                return;
            }
            String jar = jarText.getText();
            if (jar == null || jar.isEmpty()) {
                log("jar is null");
                return;
            }
            String key = keyText.getText();
            if (key == null || key.isEmpty()) {
                log("jar is null");
                return;
            }
            key = key.trim();
            if (key.getBytes().length != 16) {
                JOptionPane.showMessageDialog(rootPanel, "KEY LENGTH MUST BE 16");
                return;
            }
            byte[] keyBytes = key.getBytes();

            String finalKey = key;
            new Thread(() -> {
                JNIUtil.extractDllSo(EncryptorDLL, null, true);
                Path libPath = Paths.get(TempDir).resolve(EncryptorDLL);
                Path jarPath = Paths.get(jar);

                PatchHelper.patchJar(jarPath, libPath, packageName, keyBytes);

                String srcName = jarPath.getFileName().toString();
                jarAbs = Paths.get(String.format("%s_%s.jar", srcName.substring(0,
                        srcName.lastIndexOf(".")), NewFileSuffix)).toAbsolutePath().toString();
                log("final key: " + finalKey);
                log("package: " + packageName);
                globalKey = finalKey;
                pack = packageName;
            }).start();
        });

        exportAgentLibButton.addActionListener(e -> {
            JNIUtil.extractDllSo(DecrypterDLL, null, false);
            String sb = "File: " +
                    TempDir +
                    "/" +
                    "decrypter.dll";
            libAbs = Paths.get(TempDir).resolve("decrypter.dll").toAbsolutePath().toString();
            JOptionPane.showMessageDialog(rootPanel, sb);
        });

        showCommandButton.addActionListener(e -> {
            if (jarAbs.isEmpty() || pack.isEmpty() || libAbs.isEmpty() || globalKey.isEmpty()) {
                JOptionPane.showMessageDialog(rootPanel, "NOT ALLOWED");
                return;
            }
            JFrame frame = new JFrame("Show Command");
            frame.setContentPane(new Show(jarAbs, libAbs, pack, globalKey).rootPanel);
            frame.pack();
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        masterPanel = new JPanel();
        masterPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 3), -1, -1));
        masterPanel.add(rootPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        jarLabel = new JLabel();
        jarLabel.setText("Jar File");
        rootPanel.add(jarLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        jarText = new JTextField();
        rootPanel.add(jarText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        choseFileButton = new JButton();
        choseFileButton.setText("Chose File");
        rootPanel.add(choseFileButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        packageLabel = new JLabel();
        packageLabel.setText("Package Name");
        rootPanel.add(packageLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        packageText = new JTextField();
        rootPanel.add(packageText, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        operationLabel = new JLabel();
        operationLabel.setText("Operation");
        rootPanel.add(operationLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        operationPanel = new JPanel();
        operationPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(operationPanel, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        encryptJarFileButton = new JButton();
        encryptJarFileButton.setText("Encrypt Jar File");
        operationPanel.add(encryptJarFileButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportAgentLibButton = new JButton();
        exportAgentLibButton.setText("Export Lib");
        operationPanel.add(exportAgentLibButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showCommandButton = new JButton();
        showCommandButton.setText("Show Command");
        operationPanel.add(showCommandButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keyLabel = new JLabel();
        keyLabel.setText("Your KEY");
        rootPanel.add(keyLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        keyText = new JTextField();
        rootPanel.add(keyText, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        logPanel = new JPanel();
        logPanel.setLayout(new GridLayoutManager(1, 1, new Insets(3, 3, 3, 3), -1, -1));
        masterPanel.add(logPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(500, 200), new Dimension(500, 200), new Dimension(500, 200), 0, false));
        scroll = new JScrollPane();
        logPanel.add(scroll, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setForeground(new Color(-16722419));
        scroll.setViewportView(logArea);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return masterPanel;
    }

}
