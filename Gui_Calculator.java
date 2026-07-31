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

    private final JTextField displayInput, displayOutput;
    private String currentInput = "", operator = "";
    private double firstOperand = 0;
    private boolean startNewInput = false;

    public Gui_Calculator() {
        JFrame frame = new JFrame("Java_Gui_Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        displayInput = new JTextField("0");
        displayOutput = new JTextField("0");

        displayInput.setEditable(false);
        displayOutput.setEditable(false);

        displayInput.setHorizontalAlignment(JTextField.RIGHT);
        displayOutput.setHorizontalAlignment(JTextField.CENTER);
        frame.add(displayInput, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 5)); // 5 rows, 4 columns
        JButton clear = ButtonMaker.makeButton("↺", this);
        JButton dot = ButtonMaker.makeButton(".", this);
        JButton multiply = ButtonMaker.makeButton("*", this);
        JButton divide = ButtonMaker.makeButton("÷", this);
        JButton add = ButtonMaker.makeButton("+", this);
        JButton subtract = ButtonMaker.makeButton("-", this);
        JButton equals = ButtonMaker.makeButton("↵", this);
        JButton squared = ButtonMaker.makeButton("^", this);
        JButton squareRoot = ButtonMaker.makeButton("√", this);

        buttonPanel.add(clear);
        for (int i = 9; i >= 0 ; i--) {
            if (i==6) {
                buttonPanel.add(displayOutput, BorderLayout.NORTH);
                buttonPanel.add(multiply);
            }
            if (i==3) {
                buttonPanel.add(squared);
                buttonPanel.add(divide);
            }
            if (i==0) {
                buttonPanel.add(squareRoot);
                buttonPanel.add(add);
                buttonPanel.add(subtract);
            }
            buttonPanel.add(ButtonMaker.makeButton(String.valueOf(i), this));
        }
        buttonPanel.add(dot);
        buttonPanel.add(equals);

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
                displayInput.setText("0");
            }
            case "+", "-", "*", "÷","√", "^" -> {
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
                displayInput.setText(currentInput);
            }
        }
    }

    private void performCalculation() {
        double secondOperand = Double.parseDouble(currentInput);
        boolean undif = true;
        if (secondOperand == 0) undif = false;
        String a = String.valueOf(firstOperand);
        switch (operator) {
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
                    displayInput.setText("Undefined");
                    currentInput = "";
                    firstOperand = 0;
                    operator = "";
                    startNewInput = false;
                }
            }
        }
        if ((undif && operator.equals("÷")) || !operator.isEmpty()) {
            if (!operator.equals("^")) operator = " " + operator + " ";
            currentInput = String.valueOf(firstOperand);
            displayInput.setText(a + operator + secondOperand);
            displayOutput.setText(currentInput);
        }
    }
}
