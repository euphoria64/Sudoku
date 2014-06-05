package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board {

    int[][] soln;
    JFrame mainFrame;
    JPanel mainPanel;
    ArrayList<JButton> jButtons;

    public void generateBoard(int p) {

        mainFrame = new JFrame("Sudoku");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        SolutionGenerator numbers = new SolutionGenerator(p);
        soln = numbers.solve();

        jButtons = new ArrayList<>(81);
        for (int Jbutts = 0; Jbutts < 81; Jbutts++) {
            JButton b = new JButton();
            jButtons.add(b);
        }

        class buttonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                JButton source = (JButton) event.getSource();
                ChangeNumber changeNum = new ChangeNumber();
                changeNum.Change(source);
                changeNum.setVisible();

            }
        }
        ActionListener changedBut = new buttonListener();

        class cannotChangeListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(mainFrame, "You can't change this!");
            }
        }
        ActionListener noChangedBut = new cannotChangeListener();

        JPanel panelNumbers = new JPanel();
        boolean isGreen = true;
        int row = 0;
        for (int r = 0; r < 9; r++) {
            JPanel panel = new JPanel();
            int col = 0;
            for (int c = 0; c < 9; c++) {
                int x = numbers.getNumber(r, c);
                if (x == 0) {
                    jButtons.set((r * 9) + (c), new JButton(""));
                    jButtons.get((r * 9) + (c)).addActionListener(changedBut);
                    jButtons.get((r * 9) + (c)).setPreferredSize(new Dimension(42, 27));

                } else {
                    jButtons.set((r * 9) + (c), new JButton(x + ""));
                    jButtons.get((r * 9) + (c)).addActionListener(noChangedBut);
                    jButtons.get((r * 9) + (c)).setPreferredSize(new Dimension(42, 27));
                    jButtons.get((r * 9) + (c)).setForeground(new Color(204, 0, 0));
                }
                if (isGreen) {
                    jButtons.get((r * 9) + c).setBackground(Color.YELLOW);
                } else {
                    jButtons.get((r * 9) + c).setBackground(new Color(225, 211, 255));
                }
                panel.add(jButtons.get((r * 9) + (c)));
                panel.setBackground(Color.WHITE);
                col++;
                if (col == 3) {
                    isGreen = !isGreen;
                    col = 0;
                }
            }
            isGreen = !isGreen;
            row++;
            if (row == 3) {
                isGreen = !isGreen;
                row = 0;
            }
            panelNumbers.add(panel);
        }
        panelNumbers.setBackground(Color.BLUE);

        //BottomPanel
        JPanel bottomPanel = new JPanel();

        final JButton check = new JButton("Check");
        final JButton solve = new JButton("Solve");
        JButton newGame = new JButton("New Game");

        class CheckListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {

                if (check()) {
                    JOptionPane.showMessageDialog(mainFrame, "Congratulations, you won!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Some of your numbers are wrong...");
                }

            }

            public boolean check() {
                int[][] b = new int[9][9];
                int i = 0;
                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        if (jButtons.get(i).getText().equals("")) {
                            b[x][y] = 0;
                        } else {
                            b[x][y] = Integer.parseInt(jButtons.get(i).getText());
                        }
                        i++;
                    }

                }
                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        if (!Utility.pieceIsValid(x, y, b)) {
                            return false;
                        }
                    }
                }
                solve.setEnabled(false);
                return true;
            }

        }
        ActionListener check1 = new CheckListener();
        check.addActionListener(check1);

        class SolveListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                check.setEnabled(false);

                //  ChangeNumber changeNum = new ChangeNumber();
                int i = 0;
                for (int a = 0; a < 9; a++) {
                    for (int b = 0; b < 9; b++) {
                        if (!jButtons.get(i).getForeground().equals(new Color(204, 0, 0))) {
                            jButtons.get(i).setForeground(new Color(112, 0, 186));
                            jButtons.get(i).setText(soln[a][b] + "");
                            jButtons.get(i).removeActionListener(jButtons.get(i).getActionListeners()[0]);
                            jButtons.get(i).addActionListener(new cannotChangeListener());
                        } else {
                        }

                        i++;
                    }
                }
            }
        }
        ActionListener solve1 = new SolveListener();
        solve.addActionListener(solve1);

        class NewListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                Difficulty difficult = new Difficulty();
                difficult.Difficult();
                difficult.setVisible();
                mainFrame.dispose();
            }
        }
        ActionListener new1 = new NewListener();
        newGame.addActionListener(new1);

        bottomPanel.add(check);
        bottomPanel.add(solve);
        bottomPanel.add(newGame);

        bottomPanel.setBackground(Color.GREEN);

        mainFrame.setResizable(false);
        mainPanel.add(panelNumbers, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainFrame.add(mainPanel);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 460);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public void setNonVisible() {
        mainPanel.setVisible(false);
    }
}
