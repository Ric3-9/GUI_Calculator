import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
class Gui_Calculator implements ActionListener {
    public static class ButtonMaker {
        public static JButton makeButton(String text, ActionListener listener) {
            JButton b = new JButton(text);
            b.addActionListener(listener);
            return b;
        }
    }

    private final JTextField displayField;
    private String currentInput = "", operator = "";
    private double firstOperand = 0;
    private boolean startNewInput = false;

    public Gui_Calculator() {
        // 1. Create the frame (window)
        JFrame frame = new JFrame("Java_Gui_Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        displayField = new JTextField("0");
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        frame.add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4)); // 5 rows, 4 columns

        JButton clear = ButtonMaker.makeButton("↺", this);
        JButton dot = ButtonMaker.makeButton(".", this);
        JButton multiply = ButtonMaker.makeButton("*", this);
        JButton divide = ButtonMaker.makeButton("÷", this);
        JButton add = ButtonMaker.makeButton("+", this);
        JButton subtract = ButtonMaker.makeButton("-", this);
        JButton equals = ButtonMaker.makeButton("↵", this);
        JButton squared = ButtonMaker.makeButton("^", this);
        JButton cubed = ButtonMaker.makeButton("-/+", this);
        JButton squareRoot = ButtonMaker.makeButton("√", this);

        buttonPanel.add(clear);
        for (int i = 9; i >= 0 ; i--) {
            if (i==6)buttonPanel.add(squared);
            if (i==3) buttonPanel.add(cubed);
            if (i==0) {
                buttonPanel.add(squareRoot);
                buttonPanel.add(dot);
            }
            buttonPanel.add(ButtonMaker.makeButton(String.valueOf(i), this));
        }
        buttonPanel.add(equals);
        buttonPanel.add(multiply);
        buttonPanel.add(divide);
        buttonPanel.add(add);
        buttonPanel.add(subtract);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.pack(); // Sizes the frame so that all its contents are at or above their preferred sizes
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui_Calculator::new);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "C", "↺" -> {
                currentInput = "";
                firstOperand = 0;
                operator = "";
                startNewInput = false;
                displayField.setText("0");
            }
            case "+", "-", "*", "÷","√", "^", "-/+"  -> {
                if (currentInput.isEmpty()&&operator.equals("√")) {
                    performCalculation();
                } else {
                    if (operator.isEmpty()) {
                        firstOperand = Double.parseDouble(currentInput);
                    } else {
                        performCalculation();
                    }
                    operator = command;
                    startNewInput = true;
                }
            }
            case "↵" -> {
                performCalculation();
                operator = "";
                startNewInput = true;
            }
            case null, default -> {
                if (startNewInput) {
                    currentInput = command;
                    startNewInput = false;
                } else {
                    currentInput += command;
                }
                displayField.setText(currentInput);
            }
        }
    }

    private void performCalculation() {
        double secondOperand = Double.parseDouble(currentInput);
        boolean undif = true;
        if (secondOperand == 0) undif = false;
        String a = String.valueOf(firstOperand);
        switch (operator) {
            case "-/+" -> {
                firstOperand = Math.negateExact((long) firstOperand);
                a =" ";
            }
            case "^" -> firstOperand = Math.pow(firstOperand, secondOperand);
            case "√" -> {
                firstOperand = Math.sqrt(firstOperand);
                a =" ";
            }
            case "+" -> firstOperand += secondOperand;
            case "-" -> firstOperand -= secondOperand;
            case "*" -> firstOperand *= secondOperand;
            case "÷" -> {
                if (undif) {
                    firstOperand /= secondOperand;
                } else {
                    displayField.setText("Undefined");
                    currentInput = "";
                    firstOperand = 0;
                    operator = "";
                    startNewInput = false;
                }
            }
        }
        if ((undif && operator.equals("÷")) || !operator.isEmpty()) {
            if (!operator.equals("^")&&!operator.equals("-/+")) operator = " " + operator + " ";
            currentInput = String.valueOf(firstOperand);
            if (!operator.equals("-/+")) {
                displayField.setText(a + operator + secondOperand + " = " + currentInput);
            } else displayField.setText("(-)"+secondOperand + " = " + currentInput);

        }
    }
}
