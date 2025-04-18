import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class UI {
    // Data storage
    private static final Map<String, String> teacherCredentials = new HashMap<>();
    private static final Map<String, String> studentCredentials = new HashMap<>();
    private static final Map<String, List<String>> studentCourses = new HashMap<>();
    private static final Map<String, Map<String, Integer>> studentGrades = new HashMap<>();
    private static final Map<String, String[]> studentInfo = new HashMap<>();
    private static final List<String> availableCourses = new ArrayList<>();
    
    // Track current user
    private static String currentUser;
    private static String currentRole;

    static {
        // Initialize with sample data
        teacherCredentials.put("teacher1", "teacherpass");
        studentCredentials.put("student1", "studentpass");
        studentInfo.put("student1", new String[]{"John Wick", "S1001"});
        studentCourses.put("student1", new ArrayList<>());
        studentGrades.put("student1", new HashMap<>());
        
        // Available courses
        availableCourses.add("Mathematics");
        availableCourses.add("History");
        availableCourses.add("Physics");
        availableCourses.add("Chemistry");
        availableCourses.add("Biology");
        availableCourses.add("English");
        availableCourses.add("Filipino");
    }

    public void createLoginGUI() {
        JFrame frame = new JFrame("Student Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        // Main panel with card layout for different views
        JPanel mainPanel = new JPanel(new CardLayout());
        
        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 245, 250));
        
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 100, 150));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);
        
        // Login Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 215, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(400, 300));
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Username:"), gbc);
        
        JTextField userText = new JTextField(15);
        userText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(userText, gbc);
        
        // Password
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordText = new JPasswordField(15);
        passwordText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(passwordText, gbc);
        
        // Role
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Role:"), gbc);
        
        String[] roles = {"Teacher", "Student"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        roleDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(roleDropdown, gbc);
        
        // Login button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;   
        gbc.insets = new Insets(15, 5, 5, 5);
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(70, 130, 180));
        loginButton.setPreferredSize(new Dimension(120, 35));
        formPanel.add(loginButton, gbc);
        
        // Add form panel to login panel
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.gridx = 0;
        loginPanel.add(formPanel, gbc);
        
        // Register button
        gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton registerButton = new JButton("Register as Student");
        styleButton(registerButton, new Color(100, 150, 200));
        registerButton.addActionListener(e -> showRegistrationDialog(frame));
        loginPanel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();
            String role = (String) roleDropdown.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                showMessageDialog(frame, "Please enter both username and password", "Error", true);
                return;
            }
            
            if (authenticate(username, password, role)) {
                currentUser = username;
                currentRole = role;
                openDashboard(role, username);
                frame.dispose();
            } else {
                showMessageDialog(frame, "Invalid credentials", "Error", true);
            }
        });
        
        mainPanel.add(loginPanel, "login");
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void showMessageDialog(Component parent, String message, String title, boolean isError) {
        JOptionPane.showMessageDialog(parent, message, title, 
            isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRegistrationDialog(JFrame parent) {
        JDialog registerDialog = new JDialog(parent, "Student Registration", true);
        registerDialog.setSize(400, 350);
        registerDialog.setLayout(new BorderLayout());
        registerDialog.getContentPane().setBackground(new Color(240, 245, 250));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;
        
        // Full Name
        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        
        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(nameField, gbc);
        
        // Student ID
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Student ID:"), gbc);
        
        JTextField idField = new JTextField(15);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(idField, gbc);
        
        // Username
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Username:"), gbc);
        
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(userField, gbc);
        
        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(passField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(70, 130, 180));
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            
            if (name.isEmpty() || id.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                showMessageDialog(registerDialog, "Please fill all fields", "Error", true);
                return;
            }
            
            if (studentCredentials.containsKey(user)) {
                showMessageDialog(registerDialog, "Username already exists", "Error", true);
                return;
            }
            
            studentCredentials.put(user, pass);
            studentInfo.put(user, new String[]{name, id});
            studentCourses.put(user, new ArrayList<>());
            studentGrades.put(user, new HashMap<>());
            
            showMessageDialog(registerDialog, "Registration successful!", "Success", false);
            registerDialog.dispose();
        });
        
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(150, 150, 150));
        cancelBtn.addActionListener(e -> registerDialog.dispose());
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        
        registerDialog.add(formPanel, BorderLayout.CENTER);
        registerDialog.add(buttonPanel, BorderLayout.SOUTH);
        registerDialog.setLocationRelativeTo(parent);
        registerDialog.setVisible(true);
    }

    private boolean authenticate(String username, String password, String role) {
        switch (role) {
            case "Teacher":
                return teacherCredentials.containsKey(username) && teacherCredentials.get(username).equals(password);
            case "Student":
                return studentCredentials.containsKey(username) && studentCredentials.get(username).equals(password);
            default:
                return false;
        }
    }

    private void openDashboard(String role, String username) {
        JFrame dashboard = new JFrame(role + " Dashboard - " + username);
        dashboard.setSize(900, 600);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 100, 150));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel(role + " Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel userLabel = new JLabel("Welcome, " + username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Dashboard Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        if (role.equals("Teacher")) {
            addDashboardCard(cardsPanel, "View Students", "View all registered students", 
                new Color(70, 130, 180), e -> viewStudents(dashboard));
            addDashboardCard(cardsPanel, "Enter Grades", "Enter student grades", 
                new Color(60, 179, 113), e -> enterGrades(dashboard));
            addDashboardCard(cardsPanel, "Update Student", "Modify student information", 
                new Color(186, 85, 211), e -> updateStudent(dashboard));
            addDashboardCard(cardsPanel, "Delete Student", "Remove a student from system", 
                new Color(220, 20, 60), e -> deleteStudent(dashboard));
        } else if (role.equals("Student")) {
            String[] studentInfo = this.studentInfo.get(username);
            if (studentInfo != null) {
                JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 215, 230)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                infoPanel.setBackground(Color.WHITE);
                
                infoPanel.add(new JLabel("Name: " + studentInfo[0]));
                infoPanel.add(new JLabel("Student ID: " + studentInfo[1]));
                
                cardsPanel.add(infoPanel);
            }
            
            addDashboardCard(cardsPanel, "View My Grades", "Check your course grades", 
                new Color(70, 130, 180), e -> viewGrades(username, dashboard));
            addDashboardCard(cardsPanel, "View My Courses", "See your enrolled courses", 
                new Color(60, 179, 113), e -> viewCourses(username, dashboard));
            addDashboardCard(cardsPanel, "Enroll in Course", "Register for new courses", 
                new Color(186, 85, 211), e -> enrollCourse(username, dashboard));
        }
        
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        
        // Footer Panel with Logout Button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(new Color(240, 245, 250));
        
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(220, 20, 60));
        logoutButton.addActionListener(e -> {
            dashboard.dispose();
            createLoginGUI();
        });
        footerPanel.add(logoutButton);
        
        // Add components to dashboard
        dashboard.add(headerPanel, BorderLayout.NORTH);
        dashboard.add(mainPanel, BorderLayout.CENTER);
        dashboard.add(footerPanel, BorderLayout.SOUTH);
        
        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }
    
    private void addDashboardCard(JPanel panel, String title, String description, Color color, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 215, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setForeground(Color.DARK_GRAY);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        card.add(descArea, BorderLayout.CENTER);
        
        // Button
        JButton actionButton = new JButton("Open");
        styleButton(actionButton, color);
        actionButton.addActionListener(action);
        card.add(actionButton, BorderLayout.SOUTH);
        
        panel.add(card);
    }
    
    private void enrollCourse(String username, JFrame parentFrame) {
        JDialog enrollDialog = new JDialog(parentFrame, "Enroll in Course", true);
        enrollDialog.setSize(400, 300);
        enrollDialog.setLayout(new BorderLayout());
        enrollDialog.getContentPane().setBackground(new Color(240, 245, 250));
        
        // Get courses not already enrolled in
        List<String> enrolledCourses = studentCourses.getOrDefault(username, new ArrayList<>());
        List<String> enrollableCourses = new ArrayList<>(availableCourses);
        enrollableCourses.removeAll(enrolledCourses);
        
        if (enrollableCourses.isEmpty()) {
            showMessageDialog(enrollDialog, "You're already enrolled in all available courses!", "Info", false);
            enrollDialog.dispose();
            return;
        }
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        panel.add(new JLabel("Select a course to enroll in:"), gbc);
        
        gbc.insets = new Insets(5, 5, 5, 5);
        JComboBox<String> courseDropdown = new JComboBox<>(enrollableCourses.toArray(new String[0]));
        courseDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(courseDropdown, gbc);
        
        JButton enrollButton = new JButton("Enroll");
        styleButton(enrollButton, new Color(70, 130, 180));
        enrollButton.addActionListener(e -> {
            String selectedCourse = (String) courseDropdown.getSelectedItem();
            studentCourses.computeIfAbsent(username, k -> new ArrayList<>()).add(selectedCourse);
            showMessageDialog(enrollDialog, "Successfully enrolled in " + selectedCourse, "Success", false);
            enrollDialog.dispose();
            parentFrame.dispose();
            openDashboard(currentRole, currentUser);
        });
        
        panel.add(enrollButton, gbc);
        enrollDialog.add(panel, BorderLayout.CENTER);
        enrollDialog.setLocationRelativeTo(parentFrame);
        enrollDialog.setVisible(true);
    }

    private void viewStudents(JFrame parentFrame) {
        JDialog studentDialog = new JDialog(parentFrame, "Student List", true);
        studentDialog.setSize(600, 400);
        studentDialog.setLayout(new BorderLayout());
        
        // Convert student data to a display-friendly format
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Map.Entry<String, String[]> entry : studentInfo.entrySet()) {
            String username = entry.getKey();
            String[] info = entry.getValue();
            List<String> courses = studentCourses.getOrDefault(username, new ArrayList<>());
            Map<String, Integer> grades = studentGrades.getOrDefault(username, new HashMap<>());
            
            String studentEntry = String.format(
                "%-15s | %-20s | %-10s | Courses: %-30s | Grades: %s",
                username, 
                info[0],  // Name
                info[1],  // ID
                courses,
                grades
            );
            listModel.addElement(studentEntry);
        }
        
        JList<String> studentList = new JList<>(listModel);
        studentList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Add scrolling
        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        studentDialog.add(scrollPane, BorderLayout.CENTER);
        studentDialog.setLocationRelativeTo(parentFrame);
        studentDialog.setVisible(true);
    }

    private void enterGrades(JFrame parentFrame) {
        JDialog gradeDialog = new JDialog(parentFrame, "Enter Grades", true);
        gradeDialog.setSize(600, 450);
        gradeDialog.setLayout(new BorderLayout());
        gradeDialog.getContentPane().setBackground(new Color(240, 245, 250));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        JPanel inputPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header row
        JLabel studentLabel = new JLabel("Student");
        studentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(studentLabel);
        
        JLabel courseLabel = new JLabel("Course");
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(courseLabel);
        
        JLabel gradeLabel = new JLabel("Grade");
        gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputPanel.add(gradeLabel);
        
        Map<String, Map<JComboBox<String>, JTextField>> studentGradeEntries = new HashMap<>();
        
        for (String student : studentInfo.keySet()) {
            JLabel studentName = new JLabel(student);
            studentName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            inputPanel.add(studentName);
            
            JComboBox<String> courseDropdown = new JComboBox<>();
            courseDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            for (String course : studentCourses.getOrDefault(student, new ArrayList<>())) {
                courseDropdown.addItem(course);
            }
            inputPanel.add(courseDropdown);
            
            JTextField gradeField = new JTextField();
            gradeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            inputPanel.add(gradeField);
            
            Map<JComboBox<String>, JTextField> gradeEntry = new HashMap<>();
            gradeEntry.put(courseDropdown, gradeField);
            studentGradeEntries.put(student, gradeEntry);
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton submitButton = new JButton("Submit Grades");
        styleButton(submitButton, new Color(70, 130, 180));
        submitButton.addActionListener(e -> {
            for (Map.Entry<String, Map<JComboBox<String>, JTextField>> studentEntry : studentGradeEntries.entrySet()) {
                String student = studentEntry.getKey();
                for (Map.Entry<JComboBox<String>, JTextField> entry : studentEntry.getValue().entrySet()) {
                    String course = (String) entry.getKey().getSelectedItem();
                    String gradeText = entry.getValue().getText();
                    
                    if (!gradeText.isEmpty()) {
                        try {
                            int grade = Integer.parseInt(gradeText);
                            studentGrades.get(student).put(course, grade);
                        } catch (NumberFormatException ex) {
                            showMessageDialog(gradeDialog, 
                                "Invalid grade format for " + student + " in " + course, "Error", true);
                            return;
                        }
                    }
                }
            }
            showMessageDialog(gradeDialog, "Grades submitted successfully", "Success", false);
            gradeDialog.dispose();
        });
        
        buttonPanel.add(submitButton);
        
        panel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        gradeDialog.add(panel, BorderLayout.CENTER);
        gradeDialog.setLocationRelativeTo(parentFrame);
        gradeDialog.setVisible(true);
    }

    private void viewGrades(String username, JFrame parentFrame) {
        JDialog gradesDialog = new JDialog(parentFrame, "Your Grades", true);
        gradesDialog.setSize(400, 300);
        gradesDialog.setLayout(new BorderLayout());
        gradesDialog.getContentPane().setBackground(new Color(240, 245, 250));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        Map<String, Integer> grades = studentGrades.getOrDefault(username, new HashMap<>());
        JTextArea gradesText = new JTextArea();
        gradesText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradesText.setEditable(false);
        
        StringBuilder sb = new StringBuilder("Your Grades:\n\n");
        for (Map.Entry<String, Integer> entry : grades.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        if (grades.isEmpty()) {
            sb.append("No grades recorded yet");
        }
        
        gradesText.setText(sb.toString());
        panel.add(new JScrollPane(gradesText), BorderLayout.CENTER);
        
        gradesDialog.add(panel, BorderLayout.CENTER);
        gradesDialog.setLocationRelativeTo(parentFrame);
        gradesDialog.setVisible(true);
    }
    
    private void viewCourses(String username, JFrame parentFrame) {
        JDialog coursesDialog = new JDialog(parentFrame, "My Courses", true);
        coursesDialog.setSize(400, 300);
        coursesDialog.setLayout(new BorderLayout());
        coursesDialog.getContentPane().setBackground(new Color(240, 245, 250));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        List<String> courses = studentCourses.getOrDefault(username, new ArrayList<>());
        JTextArea coursesText = new JTextArea();
        coursesText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        coursesText.setEditable(false);
        
        StringBuilder sb = new StringBuilder("Your Courses:\n\n");
        for (String course : courses) {
            sb.append("- ").append(course).append("\n");
        }
        
        if (courses.isEmpty()) {
            sb.append("No courses registered yet");
        }
        
        coursesText.setText(sb.toString());
        panel.add(new JScrollPane(coursesText), BorderLayout.CENTER);
        
        coursesDialog.add(panel, BorderLayout.CENTER);
        coursesDialog.setLocationRelativeTo(parentFrame);
        coursesDialog.setVisible(true);
    }

    private void deleteStudent(JFrame parentFrame) {
        if (studentInfo.isEmpty()) {
            showMessageDialog(parentFrame, "No students to delete", "Info", false);
            return;
        }

        JDialog deleteDialog = new JDialog(parentFrame, "Delete Student", true);
        deleteDialog.setSize(400, 200);
        deleteDialog.setLayout(new BorderLayout());
        deleteDialog.getContentPane().setBackground(new Color(240, 245, 250));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(new JLabel("Select student to delete:"), gbc);

        gbc.insets = new Insets(5, 5, 5, 5);
        JComboBox<String> studentDropdown = new JComboBox<>(studentInfo.keySet().toArray(new String[0]));
        studentDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(studentDropdown, gbc);

        JButton deleteButton = new JButton("Delete");
        styleButton(deleteButton, new Color(220, 20, 60));
        deleteButton.addActionListener(e -> {
            String selectedStudent = (String) studentDropdown.getSelectedItem();
            int confirm = JOptionPane.showConfirmDialog(deleteDialog, 
                "Are you sure you want to delete " + selectedStudent + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                studentCredentials.remove(selectedStudent);
                studentInfo.remove(selectedStudent);
                studentCourses.remove(selectedStudent);
                studentGrades.remove(selectedStudent);
                showMessageDialog(deleteDialog, "Student deleted successfully", "Success", false);
                deleteDialog.dispose();
                parentFrame.dispose();
                openDashboard(currentRole, currentUser);
            }
        });

        panel.add(deleteButton, gbc);
        deleteDialog.add(panel, BorderLayout.CENTER);
        deleteDialog.setLocationRelativeTo(parentFrame);
        deleteDialog.setVisible(true);
    }

    private void updateStudent(JFrame parentFrame) {
        if (studentInfo.isEmpty()) {
            showMessageDialog(parentFrame, "No students to update", "Info", false);
            return;
        }

        JDialog updateDialog = new JDialog(parentFrame, "Update Student", true);
        updateDialog.setSize(500, 350);
        updateDialog.setLayout(new BorderLayout());
        updateDialog.getContentPane().setBackground(new Color(240, 245, 250));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;

        // Student selection
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Select student:"), gbc);

        JComboBox<String> studentDropdown = new JComboBox<>(studentInfo.keySet().toArray(new String[0]));
        studentDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(studentDropdown, gbc);

        // Name field
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Full Name:"), gbc);

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(nameField, gbc);

        // ID field
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Student ID:"), gbc);

        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(idField, gbc);

        // Password field
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("New Password:"), gbc);

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passField, gbc);

        // Load button
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loadButton = new JButton("Load Student Data");
        styleButton(loadButton, new Color(100, 150, 200));
        loadButton.addActionListener(e -> {
            String selectedStudent = (String) studentDropdown.getSelectedItem();
            String[] info = studentInfo.get(selectedStudent);
            nameField.setText(info[0]);
            idField.setText(info[1]);
        });
        panel.add(loadButton, gbc);

        // Update button
        gbc.gridy = 5;
        JButton updateButton = new JButton("Update Student");
        styleButton(updateButton, new Color(70, 130, 180));
        updateButton.addActionListener(e -> {
            String selectedStudent = (String) studentDropdown.getSelectedItem();
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (name.isEmpty() || id.isEmpty()) {
                showMessageDialog(updateDialog, "Name and ID cannot be empty", "Error", true);
                return;
            }

            studentInfo.put(selectedStudent, new String[]{name, id});
            
            if (!password.isEmpty()) {
                studentCredentials.put(selectedStudent, password);
            }

            showMessageDialog(updateDialog, "Student updated successfully", "Success", false);
            updateDialog.dispose();
            parentFrame.dispose();
            openDashboard(currentRole, currentUser);
        });
        panel.add(updateButton, gbc);

        updateDialog.add(panel, BorderLayout.CENTER);
        updateDialog.setLocationRelativeTo(parentFrame);
        updateDialog.setVisible(true);
    }

}