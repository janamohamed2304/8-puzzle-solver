import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PuzzleGUI extends JFrame {
    private JButton[][] tiles = new JButton[3][3];
    private JButton startButton, resetButton, solveButton;
    private JComboBox<String> algorithmSelector;
    private JTextField initialStateField;
    private JLabel statusLabel, statsLabel;
    private JPanel boardPanel, controlPanel;
    private Timer animationTimer;
    private List<?> solutionPath;
    private int currentStep = 0;
    private String currentState = "123405678";
    private boolean isAnimating = false;

    public PuzzleGUI() {
        setTitle("8-Puzzle Solver");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        updateBoard(currentState);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        // Board Panel
        boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(new Color(52, 73, 94));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j] = new JButton();
                tiles[i][j].setFont(new Font("Arial", Font.BOLD, 48));
                tiles[i][j].setFocusPainted(false);
                tiles[i][j].setBackground(new Color(52, 152, 219));
                tiles[i][j].setForeground(Color.WHITE);
                tiles[i][j].setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
                boardPanel.add(tiles[i][j]);
            }
        }

        // Control Panel
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Initial State Input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Initial State (9 digits):"));
        initialStateField = new JTextField(currentState, 15);
        inputPanel.add(initialStateField);
        controlPanel.add(inputPanel);

        // Algorithm Selector
        JPanel algoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algoPanel.add(new JLabel("Algorithm:"));
        String[] algorithms = {"BFS", "A* (AStar)", "IDDFS", "DFS"};
        algorithmSelector = new JComboBox<>(algorithms);
        algoPanel.add(algorithmSelector);
        controlPanel.add(algoPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        solveButton = new JButton("Solve");
        solveButton.setBackground(new Color(46, 125, 204));
        solveButton.setForeground(Color.WHITE);
        solveButton.setFont(new Font("Arial", Font.BOLD, 14));
        solveButton.setFocusPainted(false);
        solveButton.addActionListener(e -> solvePuzzle());

        startButton = new JButton("Start Animation");
        startButton.setBackground(new Color(52, 152, 219));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setFocusPainted(false);
        startButton.setEnabled(false);
        startButton.addActionListener(e -> startAnimation());

        resetButton = new JButton("Reset");
        resetButton.setBackground(new Color(12, 21, 58));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> resetPuzzle());

        buttonPanel.add(solveButton);
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        controlPanel.add(buttonPanel);

        // Status Label
        statusLabel = new JLabel("Enter initial state and select algorithm");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(statusLabel);

        // Stats Label
        statsLabel = new JLabel(" ");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(Box.createVerticalStrut(5));
        controlPanel.add(statsLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void updateBoard(String state) {
        for (int i = 0; i < 9; i++) {
            char c = state.charAt(i);
            int row = i / 3;
            int col = i % 3;

            if (c == '0') {
                tiles[row][col].setText("");
                tiles[row][col].setBackground(new Color(142, 118, 146));
            } else {
                tiles[row][col].setText(String.valueOf(c));
                tiles[row][col].setBackground(new Color(87, 46, 84, 255));
            }
        }
    }

    private void solvePuzzle() {
        String initialState = initialStateField.getText().trim();

        // Validate input
        if (!isValidState(initialState)) {
            JOptionPane.showMessageDialog(this,
                    "Invalid state! Enter exactly 9 digits (0-8) with each digit appearing once.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentState = initialState;
        updateBoard(currentState);
        statusLabel.setText("Solving...");
        statsLabel.setText(" ");
        solveButton.setEnabled(false);
        startButton.setEnabled(false);

        // Run solver in background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    String selectedAlgo = (String) algorithmSelector.getSelectedItem();
                    long startTime = System.currentTimeMillis();

                    switch (selectedAlgo) {
                        case "BFS":
                            solveBFS(initialState);
                            break;
                        case "A* (AStar)":
                            solveAStar(initialState);
                            break;
                        case "IDDFS":
                            solveIDDFS(initialState);
                            break;
                        case "DFS":
                            solveDFS(initialState);
                            break;
                    }

                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    SwingUtilities.invokeLater(() -> {
                        if (solutionPath != null && !solutionPath.isEmpty()) {
                            statusLabel.setText("Solution found! Press 'Start Animation' to view.");
                            statsLabel.setText("Path length: " + solutionPath.size() +
                                    " | Time: " + duration + " ms");
                            startButton.setEnabled(true);
                        } else {
                            statusLabel.setText("No solution found!");
                            statsLabel.setText(" ");
                        }
                        solveButton.setEnabled(true);
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Error: " + ex.getMessage());
                        solveButton.setEnabled(true);
                    });
                }
                return null;
            }
        };
        worker.execute();
    }

    private void solveBFS(String initialState) {
        Node start = new Node(initialState, null, null, 0, 0);
        BFS solver = new BFS();
        List<Node> path = solver.bfs(start);
        solutionPath = path;
    }

    private void solveAStar(String initialState) {
        AStar solver = new AStar();
        List<String> moves = solver.aStar(initialState, "012345678");
        if (moves != null) {
            // Convert moves to states
            java.util.List<Pair> statePath = new java.util.ArrayList<>();
            String state = initialState;
            statePath.add(new Pair(null, state));

            for (String move : moves) {
                for (Pair succ : Successors.getSuccessors(state)) {
                    if (succ.getDirection().equals(move)) {
                        state = succ.getState();
                        statePath.add(succ);
                        break;
                    }
                }
            }
            solutionPath = statePath;
        } else {
            solutionPath = null;
        }
    }

    private void solveIDDFS(String initialState) {
        IDFS solver = new IDFS();
        List<Pair> path = solver.iddfs(initialState);
        solutionPath = path;
    }

    private void solveDFS(String initialState) {
        // DFS doesn't return path, so we'll create a simple wrapper
        statusLabel.setText("DFS completed (check console for output)");
        DFS solver = new DFS();
        solver.DFS(initialState);
        solutionPath = null; // DFS doesn't provide visualization path
    }

    private void startAnimation() {
        if (solutionPath == null || solutionPath.isEmpty()) return;

        isAnimating = true;
        currentStep = 0;
        startButton.setEnabled(false);
        solveButton.setEnabled(false);
        resetButton.setEnabled(true);

        animationTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep < solutionPath.size()) {
                    Object step = solutionPath.get(currentStep);
                    String state = null;
                    String move = null;

                    if (step instanceof Node) {
                        Node node = (Node) step;
                        state = node.state;
                        move = node.action;
                    } else if (step instanceof Pair) {
                        Pair pair = (Pair) step;
                        state = pair.getState();
                        move = pair.getDirection();
                    }

                    if (state != null) {
                        updateBoard(state);
                        statusLabel.setText("Step " + currentStep +
                                (move != null ? " (Move: " + move + ")" : ""));
                    }

                    currentStep++;
                } else {
                    animationTimer.stop();
                    isAnimating = false;
                    statusLabel.setText("Animation complete!");
                    solveButton.setEnabled(true);
                }
            }
        });

        animationTimer.start();
    }

    private void resetPuzzle() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        currentState = initialStateField.getText().trim();
        if (!isValidState(currentState)) {
            currentState = "123405678";
            initialStateField.setText(currentState);
        }

        updateBoard(currentState);
        solutionPath = null;
        currentStep = 0;
        isAnimating = false;

        statusLabel.setText("Ready to solve");
        statsLabel.setText(" ");
        solveButton.setEnabled(true);
        startButton.setEnabled(false);
    }

    private boolean isValidState(String state) {
        if (state.length() != 9) return false;

        boolean[] seen = new boolean[9];
        for (char c : state.toCharArray()) {
            if (c < '0' || c > '8') return false;
            int digit = c - '0';
            if (seen[digit]) return false;
            seen[digit] = true;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PuzzleGUI());
    }
}