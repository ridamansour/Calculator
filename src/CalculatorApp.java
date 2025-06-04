import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorApp extends JFrame implements ActionListener {
    private JTextField display;
    private String currentInput = "";
    private double firstOperand = 0;
    private String currentOperator = "";
    private boolean startNewInput = true;
    private final Color BLUE = new Color(95, 131, 196);
    private final Color LIGHT_BLUE = new Color(70, 100, 150);
    private final Color ORANGE = new Color(255, 140, 0);
    private final Color LIGHT_GRAY = new Color(230, 230, 230);

    public CalculatorApp() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BLUE);

        initComponents();
    }

    private void initComponents() {
        // Create display
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 36));
        display.setBackground(BLUE);
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        display.setMargin(new Insets(10, 10, 10, 10));

        // Create buttons
        String[][] buttonLabels = {
                {"C", "⌫", "%", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"0", ".", "="}
        };

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        buttonPanel.setBackground(BLUE);

        for (int i = 0; i < buttonLabels.length; i++) {
            for (int j = 0; j < buttonLabels[i].length; j++) {
                JButton button = createStyledButton(buttonLabels[i][j]);
                buttonPanel.add(button);

                // Special case for "0" button to span 2 columns
                if (i == 4 && j == 0) {
                    buttonPanel.add(new JLabel("")); // Empty space
                }
            }
        }

        // Add components to frame
        setLayout(new BorderLayout(5, 5));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        button.addActionListener(this);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set different colors for different button types
        if (label.matches("[0-9.]")) {
            button.setBackground(LIGHT_GRAY);
            button.setForeground(Color.BLACK);
        } else if (label.matches("[+\\-*/%]")) {
            button.setBackground(LIGHT_BLUE);
            button.setForeground(Color.WHITE);
        } else if (label.equals("=")) {
            button.setBackground(ORANGE);
            button.setForeground(Color.WHITE);
        } else { // C, ⌫
            button.setBackground(new Color(100, 100, 100));
            button.setForeground(Color.WHITE);
        }

        // Make buttons rounder
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BLUE, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            handleDigitInput(command);
        } else if (command.equals(".")) {
            handleDecimalPoint();
        } else if (command.matches("[+\\-*/%]")) {
            handleOperator(command);
        } else if (command.equals("=")) {
            handleEquals();
        } else if (command.equals("C")) {
            handleClear();
        } else if (command.equals("⌫")) {
            handleBackspace();
        } else if (command.equals("%")) {
            handlePercentage();
        }
    }

    private void handleDigitInput(String digit) {
        if (startNewInput) {
            currentInput = digit;
            startNewInput = false;
        } else {
            currentInput += digit;
        }
        display.setText(currentInput);
    }

    private void handleDecimalPoint() {
        if (startNewInput) {
            currentInput = "0.";
            startNewInput = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        display.setText(currentInput);
    }

    private void handleOperator(String operator) {
        if (!currentInput.isEmpty()) {
            if (!currentOperator.isEmpty()) {
                calculateResult();
            }
            firstOperand = Double.parseDouble(currentInput);
            currentOperator = operator;
            startNewInput = true;
        } else if (!currentOperator.isEmpty()) {
            currentOperator = operator;
        }
    }

    private void handleEquals() {
        if (!currentInput.isEmpty() && !currentOperator.isEmpty()) {
            calculateResult();
            currentOperator = "";
        }
    }

    private void calculateResult() {
        double secondOperand = Double.parseDouble(currentInput);
        double result = 0;

        switch (currentOperator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "*":
                result = firstOperand * secondOperand;
                break;
            case "/":
                if (secondOperand != 0) {
                    result = firstOperand / secondOperand;
                } else {
                    display.setText("Error");
                    currentInput = "";
                    currentOperator = "";
                    startNewInput = true;
                    return;
                }
                break;
            case "%":
                result = firstOperand % secondOperand;
                break;
        }

        // Display result
        if (result == (long) result) {
            display.setText(String.format("%d", (long) result));
            currentInput = String.format("%d", (long) result);
        } else {
            display.setText(String.format("%s", result));
            currentInput = String.format("%s", result);
        }
        startNewInput = true;
    }

    private void handleClear() {
        currentInput = "";
        firstOperand = 0;
        currentOperator = "";
        startNewInput = true;
        display.setText("");
    }

    private void handleBackspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(currentInput);
            if (currentInput.isEmpty()) {
                startNewInput = true;
            }
        }
    }

    private void handlePercentage() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            currentInput = String.valueOf(value / 100);
            display.setText(currentInput);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            CalculatorApp calculator = new CalculatorApp();
            calculator.setVisible(true);
        });
    }
}