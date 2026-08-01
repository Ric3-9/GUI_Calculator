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
    private boolean isManualInput = false;
    private boolean detectManualInput = false;
    private JButton manualInput = ButtonMaker.makeButton("MAN", this);

    public Gui_Calculator() {
        JFrame frame = new JFrame("Java_Gui_Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);

        displayInput = new JTextField("");
        displayOutput = new JTextField("=");

        displayInput.setEditable(false);
        displayOutput.setEditable(false);

        displayInput.setHorizontalAlignment(JTextField.RIGHT);
        displayOutput.setHorizontalAlignment(JTextField.CENTER);
        frame.add(displayInput, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5)); // 5 rows, 4 columns
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
        buttonPanel.add(manualInput);

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
            case "C", "↺", "MAN", "BTN" -> {
                currentInput = "";
                firstOperand = 0;
                operator = "";
                startNewInput = false;
                displayOutput.setText("=");
                if (command.equals("MAN") || command.equals("BTN")) {
                    isManualInput = !isManualInput;
                    detectManualInput = isManualInput;
                    displayInput.setEditable(isManualInput);
                    if (isManualInput) {
                        manualInput.setText("BTN");
                    } else manualInput.setText("MAN");
                } else {
                    displayInput.setText("");
                    detectManualInput = false;
                    isManualInput = false;
                    displayInput.setEditable(false);
                }
            }
            case "+", "-", "*", "÷", "√", "^" -> {
                if (command.equals("√")) {
                    if (!currentInput.isEmpty()) firstOperand = Double.parseDouble(currentInput);
                    operator = "√";
                    performCalculation();
                    startNewInput = true;
                    return;
                }
                
                if (!currentInput.isEmpty()) {
                    if (operator.isEmpty()) {
                        firstOperand = Double.parseDouble(currentInput);
                    } else performCalculation();
                }
                operator = command;
                startNewInput = true;
                displayInput.setText(firstOperand + " " + operator);
            }
            case "↵" -> {
                if (!currentInput.isEmpty() && !operator.isEmpty()) {
                    performCalculation();
                    operator = "";
                    startNewInput = true;
                } else if (detectManualInput) {
                    String inputText = displayInput.getText();
                    StringBuilder firstStr = new StringBuilder();
                    StringBuilder secondStr = new StringBuilder();
                    boolean operatorFound = false;

                    for (char c : inputText.toCharArray()) {
                        if ("+-*÷^".indexOf(c) != -1) {
                            operator = String.valueOf(c);
                            operatorFound = true;
                            continue;
                        }
                        if (Character.isDigit(c) || c == '.') {
                            if (!operatorFound) {
                                firstStr.append(c);
                            } else secondStr.append(c);
                        }
                    }
                    if (!firstStr.isEmpty()) firstOperand = Double.parseDouble(firstStr.toString());
                    if (!secondStr.isEmpty()) currentInput = secondStr.toString();

                    if (!operator.isEmpty() && !currentInput.isEmpty()) {
                        performCalculation();
                        operator = "";
                        startNewInput = true;
                    }
                }
            }
            case null, default -> {
                if (startNewInput) {
                    currentInput = command;
                    startNewInput = false;
                } else currentInput += command;
                if (operator.isEmpty()) {
                    displayInput.setText(currentInput);
                } else displayInput.setText(firstOperand + " " + operator + " " + currentInput);
            }
        }
    }

    private void performCalculation() {
        double secondOperand = Double.parseDouble(currentInput);
        boolean undif = secondOperand != 0;
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
