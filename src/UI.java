import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class UI {
    // Data storage
    private static final Map<String, String> adminCredentials = new HashMap<>();
    private static final Map<String, String> teacherCredentials = new HashMap<>();
    private static final Map<String, String> studentCredentials = new HashMap<>();
    private static final Map<String, List<String>> studentCourses = new HashMap<>();
    private static final Map<String, Map<String, Integer>> studentGrades = new HashMap<>();
    private static final Map<String, String[]> studentInfo = new HashMap<>();
    private static final List<String> availableCourses = new ArrayList<>();

    static {
        // Initialize with sample data
        adminCredentials.put("admin", "adminpass");
        teacherCredentials.put("teacher1", "teacherpass");
        studentCredentials.put("student1", "studentpass");
        studentInfo.put("student1", new String[]{"John Doe", "S1001"});
        studentCourses.put("student1", new ArrayList<>());
        studentGrades.put("student1", new HashMap<>());
        
        // Available courses
        availableCourses.add("Mathematics");
        availableCourses.add("Computer Science");
        availableCourses.add("Physics");
        availableCourses.add("Chemistry");
        availableCourses.add("Biology");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.createGUI();
        });
    }

    public void createGUI() {
        JFrame frame = new JFrame("Student Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Username
        gbc.gridy = 1;
        gbc.gridx = 0;
        frame.add(new JLabel("Username:"), gbc);
        
        JTextField userText = new JTextField(15);
        gbc.gridx = 1;
        frame.add(userText, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        frame.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordText = new JPasswordField(15);
        gbc.gridx = 1;
        frame.add(passwordText, gbc);
        
        // Role
        gbc.gridy = 3;
        gbc.gridx = 0;
        frame.add(new JLabel("Role:"), gbc);
        
        String[] roles = {"Admin", "Teacher", "Student"};
        JComboBox<String> roleDropdown = new JComboBox<>(roles);
        gbc.gridx = 1;
        frame.add(roleDropdown, gbc);
        
        // Login button
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        frame.add(loginButton, gbc);
        
        // Register button
        gbc.gridy = 5;
        JButton registerButton = new JButton("Register as Student");
        registerButton.addActionListener(e -> showRegistrationDialog(frame));
        frame.add(registerButton, gbc);
        
        loginButton.addActionListener(e -> {
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();
            String role = (String) roleDropdown.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (authenticate(username, password, role)) {
                openDashboard(role, username);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void showRegistrationDialog(JFrame parent) {
        JDialog registerDialog = new JDialog(parent, "Student Registration", true);
        registerDialog.setSize(350, 300);
        registerDialog.setLayout(new GridLayout(0, 2, 5, 5));
        
        registerDialog.add(new JLabel("Full Name:"));
        JTextField nameField = new JTextField();
        registerDialog.add(nameField);
        
        registerDialog.add(new JLabel("Student ID:"));
        JTextField idField = new JTextField();
        registerDialog.add(idField);
        
        registerDialog.add(new JLabel("Username:"));
        JTextField userField = new JTextField();
        registerDialog.add(userField);
        
        registerDialog.add(new JLabel("Password:"));
        JPasswordField passField = new JPasswordField();
        registerDialog.add(passField);
        
        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            
            if (name.isEmpty() || id.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "Please fill all fields");
                return;
            }
            
            if (studentCredentials.containsKey(user)) {
                JOptionPane.showMessageDialog(registerDialog, "Username already exists");
                return;
            }
            
            studentCredentials.put(user, pass);
            studentInfo.put(user, new String[]{name, id});
            studentCourses.put(user, new ArrayList<>());
            studentGrades.put(user, new HashMap<>());
            
            JOptionPane.showMessageDialog(registerDialog, "Registration successful!");
            registerDialog.dispose();
        });
        
        registerDialog.add(new JLabel());
        registerDialog.add(registerBtn);
        registerDialog.setLocationRelativeTo(parent);
        registerDialog.setVisible(true);
    }

    private boolean authenticate(String username, String password, String role) {
        switch (role) {
            case "Admin":
                return adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password);
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
        dashboard.setSize(600, 400);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboard.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        if (role.equals("Teacher")) {
            JButton viewStudentsBtn = new JButton("View Students");
            viewStudentsBtn.setPreferredSize(new Dimension(200, 40));
            viewStudentsBtn.addActionListener(e -> viewStudents());
            
            JButton enterGradesBtn = new JButton("Enter Grades");
            enterGradesBtn.setPreferredSize(new Dimension(200, 40));
            enterGradesBtn.addActionListener(e -> enterGrades());
            
            gbc.gridy = 0;
            panel.add(viewStudentsBtn, gbc);
            gbc.gridy = 1;
            panel.add(enterGradesBtn, gbc);
            
        } else if (role.equals("Student")) {
            JButton viewGradesBtn = new JButton("View My Grades");
            viewGradesBtn.setPreferredSize(new Dimension(200, 40));
            viewGradesBtn.addActionListener(e -> viewGrades(username));
            
            JButton viewCoursesBtn = new JButton("View My Courses");
            viewCoursesBtn.setPreferredSize(new Dimension(200, 40));
            viewCoursesBtn.addActionListener(e -> viewCourses(username));
            
            JButton enrollCourseBtn = new JButton("Enroll in Course");
            enrollCourseBtn.setPreferredSize(new Dimension(200, 40));
            enrollCourseBtn.addActionListener(e -> enrollCourse(username));
            
            gbc.gridy = 0;
            panel.add(viewGradesBtn, gbc);
            gbc.gridy = 1;
            panel.add(viewCoursesBtn, gbc);
            gbc.gridy = 2;
            panel.add(enrollCourseBtn, gbc);
        }
        
        dashboard.add(panel, BorderLayout.CENTER);
        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }
    
    private void enrollCourse(String username) {
        JFrame enrollFrame = new JFrame("Enroll in Course");
        enrollFrame.setSize(400, 300);
        enrollFrame.setLayout(new BorderLayout());
        
        // Get courses not already enrolled in
        List<String> enrolledCourses = studentCourses.getOrDefault(username, new ArrayList<>());
        List<String> enrollableCourses = new ArrayList<>(availableCourses);
        enrollableCourses.removeAll(enrolledCourses);
        
        if (enrollableCourses.isEmpty()) {
            JOptionPane.showMessageDialog(enrollFrame, "You're already enrolled in all available courses!");
            enrollFrame.dispose();
            return;
        }
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Select a course to enroll in:"));
        
        JComboBox<String> courseDropdown = new JComboBox<>(enrollableCourses.toArray(new String[0]));
        panel.add(courseDropdown);
        
        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(e -> {
            String selectedCourse = (String) courseDropdown.getSelectedItem();
            studentCourses.computeIfAbsent(username, k -> new ArrayList<>()).add(selectedCourse);
            JOptionPane.showMessageDialog(enrollFrame, "Successfully enrolled in " + selectedCourse);
            enrollFrame.dispose();
        });
        
        panel.add(enrollButton);
        enrollFrame.add(panel, BorderLayout.CENTER);
        enrollFrame.setLocationRelativeTo(null);
        enrollFrame.setVisible(true);
    }

    private void viewStudents() {
        JFrame studentFrame = new JFrame("Student List");
        studentFrame.setSize(400, 300);
        studentFrame.setLayout(new BorderLayout());

        JTextArea studentTextArea = new JTextArea();
        studentTextArea.setEditable(false);

        StringBuilder studentList = new StringBuilder("Registered Students:\n\n");
        studentInfo.forEach((username, info) -> 
            studentList.append("Username: ").append(username)
                .append("\nName: ").append(info[0])
                .append("\nID: ").append(info[1])
                .append("\nCourses: ").append(studentCourses.get(username))
                .append("\n\n"));

        studentTextArea.setText(studentList.toString());
        studentFrame.add(new JScrollPane(studentTextArea), BorderLayout.CENTER);
        studentFrame.setLocationRelativeTo(null);
        studentFrame.setVisible(true);
    }

    private void enterGrades() {
        JFrame gradeFrame = new JFrame("Enter Grades");
        gradeFrame.setSize(500, 400);
        gradeFrame.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        inputPanel.add(new JLabel("Student"));
        inputPanel.add(new JLabel("Course"));
        inputPanel.add(new JLabel("Grade"));
        
        Map<JComboBox<String>, JTextField> gradeEntries = new HashMap<>();
        
        for (String student : studentInfo.keySet()) {
            JComboBox<String> courseDropdown = new JComboBox<>();
            for (String course : studentCourses.getOrDefault(student, new ArrayList<>())) {
                courseDropdown.addItem(course);
            }
            
            JTextField gradeField = new JTextField();
            
            inputPanel.add(new JLabel(student));
            inputPanel.add(courseDropdown);
            inputPanel.add(gradeField);
            
            gradeEntries.put(courseDropdown, gradeField);
        }
        
        JButton submitButton = new JButton("Submit Grades");
        submitButton.addActionListener(e -> {
            for (Map.Entry<JComboBox<String>, JTextField> entry : gradeEntries.entrySet()) {
                String course = (String) entry.getKey().getSelectedItem();
                String gradeText = entry.getValue().getText();
                
                if (!gradeText.isEmpty()) {
                    try {
                        int grade = Integer.parseInt(gradeText);
                        // In a real application, you would save this grade to the student's record
                        JOptionPane.showMessageDialog(gradeFrame, 
                            "Grades submitted (simulation - not actually saved)");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(gradeFrame, 
                            "Invalid grade format for one or more entries", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            gradeFrame.dispose();
        });
        
        gradeFrame.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
        gradeFrame.add(submitButton, BorderLayout.SOUTH);
        gradeFrame.setLocationRelativeTo(null);
        gradeFrame.setVisible(true);
    }

    private void viewGrades(String username) {
        JFrame gradesFrame = new JFrame("Your Grades");
        gradesFrame.setSize(300, 200);
        
        Map<String, Integer> grades = studentGrades.getOrDefault(username, new HashMap<>());
        JTextArea gradesText = new JTextArea();
        gradesText.setEditable(false);
        
        StringBuilder sb = new StringBuilder("Your Grades:\n\n");
        for (Map.Entry<String, Integer> entry : grades.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        if (grades.isEmpty()) {
            sb.append("No grades recorded yet");
        }
        
        gradesText.setText(sb.toString());
        gradesFrame.add(new JScrollPane(gradesText));
        gradesFrame.setLocationRelativeTo(null);
        gradesFrame.setVisible(true);
    }
    
    private void viewCourses(String username) {
        JFrame coursesFrame = new JFrame("My Courses");
        coursesFrame.setSize(300, 200);
        
        List<String> courses = studentCourses.getOrDefault(username, new ArrayList<>());
        JTextArea coursesText = new JTextArea();
        coursesText.setEditable(false);
        
        StringBuilder sb = new StringBuilder("Your Courses:\n\n");
        for (String course : courses) {
            sb.append("- ").append(course).append("\n");
        }
        
        if (courses.isEmpty()) {
            sb.append("No courses registered yet");
        }
        
        coursesText.setText(sb.toString());
        coursesFrame.add(new JScrollPane(coursesText));
        coursesFrame.setLocationRelativeTo(null);
        coursesFrame.setVisible(true);
    }
}