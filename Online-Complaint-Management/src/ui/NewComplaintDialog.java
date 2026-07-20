package ui;

import model.ComplaintCategory;
import model.ComplaintPriority;
import model.ComplaintService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewComplaintDialog extends JDialog {
    private static NewComplaintDialog instance;
    private final MainWindow parentFrame;
    private final ComplaintService complaintService;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<ComplaintCategory> categoryCombo;
    private JTextField dateField; 
    private JRadioButton lowRadio, medRadio, highRadio, critRadio; 

    private NewComplaintDialog(MainWindow parent) {
        super(parent, "Create New Card", false); 
        this.parentFrame = parent;
        this.complaintService = ComplaintService.getInstance();
        initComponents();
        centerOnParent(); 
    }

    public static NewComplaintDialog getInstance(MainWindow parent) {
        if (instance == null) {
            instance = new NewComplaintDialog(parent);
        }
        return instance;
    }

    public void showNewCardForm() {
        titleField.setText("");
        descriptionArea.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateField.setText(sdf.format(new Date()));
        medRadio.setSelected(true); 
        this.centerOnParent();
        this.setVisible(true);
    }

    private void initComponents() {
        setUndecorated(true); 
        setSize(450, 600); 
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE); 

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 249, 250)); 
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(229, 231, 235))); 

        JLabel titleLabel = new JLabel("New complaint");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(52, 73, 94));
        header.add(titleLabel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel formBody = new JPanel();
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));
        formBody.setOpaque(false);
        formBody.setBorder(new EmptyBorder(25, 25, 25, 25));

        titleField = createModernTextField("Complaint Title...");
        formBody.add(titleField);
        formBody.add(Box.createVerticalStrut(15));

        descriptionArea = new JTextArea(6, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(null); 
        formBody.add(descScroll);
        formBody.add(Box.createVerticalStrut(20));

        formBody.add(createModernLabel("Category"));
        categoryCombo = new JComboBox<>(ComplaintCategory.values()); 
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        formBody.add(categoryCombo);
        formBody.add(Box.createVerticalStrut(15));

        formBody.add(createModernLabel("Created Date"));
        dateField = createModernTextField("Auto-populated");
        dateField.setEditable(false); 
        dateField.setBackground(new Color(248, 249, 250)); 
        formBody.add(dateField);
        formBody.add(Box.createVerticalStrut(20));

        formBody.add(createModernLabel("Priority"));
        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        priorityPanel.setOpaque(false);
        lowRadio = createModernRadio("Low");
        medRadio = createModernRadio("Medium"); 
        highRadio = createModernRadio("High");
        critRadio = createModernRadio("Critical");
        ButtonGroup pGroup = new ButtonGroup();
        pGroup.add(lowRadio); pGroup.add(medRadio); pGroup.add(highRadio); pGroup.add(critRadio);
        priorityPanel.add(lowRadio); priorityPanel.add(medRadio); priorityPanel.add(highRadio); priorityPanel.add(critRadio);
        formBody.add(priorityPanel);

        add(formBody, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(229, 231, 235))); 

        JButton createBtn = new JButton("Create new card");
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBackground(new Color(41, 128, 185)); 
        createBtn.setBorder(new EmptyBorder(10, 25, 10, 25)); 
        createBtn.setFocusPainted(false);
        createBtn.addActionListener(e -> handleSubmit()); 
        footer.add(createBtn, BorderLayout.CENTER);

        JLabel cancelLabel = new JLabel("Cancel");
        cancelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelLabel.setForeground(new Color(127, 140, 141));
        cancelLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        cancelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setVisible(false); 
            }
        });
        footer.add(cancelLabel, BorderLayout.WEST);

        return footer;
    }

    private void handleSubmit() {
        String title = titleField.getText().trim();
        String desc = descriptionArea.getText().trim();
        if (title.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Description are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mockUserID = "simulated_user_101"; 
        ComplaintCategory category = (ComplaintCategory) categoryCombo.getSelectedItem();
        ComplaintPriority priority = medRadio.isSelected() ? ComplaintPriority.MEDIUM : ComplaintPriority.HIGH; 

        try {
            complaintService.registerNewComplaint(title, desc, category, priority, mockUserID);
            setVisible(false); 
            parentFrame.refreshKanbanBoard(); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void centerOnParent() {
        if (parentFrame != null) {
            Dimension pDim = parentFrame.getSize();
            Point pLoc = parentFrame.getLocation();
            setSize(450, 600); 
            setLocation(pLoc.x + (pDim.width - getWidth()) / 2, pLoc.y + (pDim.height - getHeight()) / 2 + 50); 
        }
    }

    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        label.setForeground(new Color(127, 140, 141)); 
        label.setBorder(new EmptyBorder(5, 0, 2, 0));
        return label;
    }

    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new LineBorder(new Color(229, 231, 235), 1));
        field.setForeground(new Color(189, 195, 199)); 
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(new Color(52, 73, 94)); 
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(189, 195, 199)); 
                }
            }
        });
        return field;
    }

    private JRadioButton createModernRadio(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setOpaque(false);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        radio.setForeground(new Color(52, 73, 94));
        radio.setFocusPainted(false);
        return radio;
    }
}