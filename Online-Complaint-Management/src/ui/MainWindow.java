package ui;

import data.FileManager;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainWindow extends JFrame {
    private static MainWindow instance;
    private final ComplaintService complaintService;
    private final NewComplaintDialog newComplaintDialog;

    private MainWindow() {
        this.complaintService = ComplaintService.getInstance();
        this.newComplaintDialog = NewComplaintDialog.getInstance(this); 
        initComponents();
    }

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    private void initComponents() {
        setTitle("Complaint management - pipefy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setSize(1280, 800);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250)); 

        add(createHeaderBar(), BorderLayout.NORTH);
        add(createKanbanScrollPanel(), BorderLayout.CENTER);
        this.newComplaintDialog.setVisible(false); 
    }

    private JPanel createHeaderBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185)); 
        header.setPreferredSize(new Dimension(0, 60)); 
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);
        JLabel brandLabel = new JLabel("pipefy"); 
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandLabel.setForeground(Color.WHITE);
        leftPanel.add(brandLabel);

        JLabel titleLabel = new JLabel("Complaint management");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLabel.setForeground(new Color(236, 240, 241));
        leftPanel.add(titleLabel);
        header.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setOpaque(false);
        String[] iconPlaceholders = {"[Search]", "[Filter]", "[Settings]", "[Profile]"};
        for (String placeholder : iconPlaceholders) {
            JLabel iconLabel = new JLabel(placeholder);
            iconLabel.setForeground(Color.WHITE);
            iconLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
            rightPanel.add(iconLabel);
        }
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JScrollPane createKanbanScrollPanel() {
        JPanel columnsContainer = new JPanel(new GridLayout(1, 4, 15, 0)); 
        columnsContainer.setOpaque(false);
        columnsContainer.setBorder(new EmptyBorder(20, 15, 20, 15)); 

        columnsContainer.add(createColumn("Inbox", ComplaintStatus.OPEN));
        columnsContainer.add(createColumn("Investigate complaint", ComplaintStatus.ASSIGNED));
        columnsContainer.add(createColumn("Escalation", ComplaintStatus.ESCALATED));
        columnsContainer.add(createColumn("Deliver resolution", ComplaintStatus.RESOLVED));

        JScrollPane scrollPane = new JScrollPane(columnsContainer);
        scrollPane.setBorder(null); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        return scrollPane;
    }

    private JPanel createColumn(String title, ComplaintStatus statusToFilter) {
        JPanel column = new JPanel(new BorderLayout());
        column.setOpaque(false);

        JPanel columnHeader = new JPanel(new BorderLayout());
        columnHeader.setBackground(Color.WHITE);
        columnHeader.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        columnHeader.add(titleLabel, BorderLayout.WEST);

        if (statusToFilter == ComplaintStatus.OPEN) {
            JButton addCardBtn = new JButton("+");
            addCardBtn.setFont(new Font("Arial", Font.BOLD, 18));
            addCardBtn.setForeground(new Color(41, 128, 185)); 
            addCardBtn.setFocusPainted(false);
            addCardBtn.setContentAreaFilled(false); 
            addCardBtn.addActionListener(e -> newComplaintDialog.showNewCardForm()); 
            columnHeader.add(addCardBtn, BorderLayout.EAST);
        }

        columnHeader.setBorder(new MatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        column.add(columnHeader, BorderLayout.NORTH);

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);
        cardsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));

        List<Complaint> filteredComplaints = complaintService.getComplaintsByStatus(statusToFilter);

        for (Complaint complaint : filteredComplaints) {
            cardsContainer.add(new ComplaintCardPanel(complaint));
            cardsContainer.add(Box.createVerticalStrut(10)); 
        }
        column.add(cardsContainer, BorderLayout.CENTER);

        return column;
    }

    public void refreshKanbanBoard() {
        getContentPane().removeAll(); 
        initComponents(); 
        revalidate(); 
        repaint(); 
    }

    private class ComplaintCardPanel extends JPanel {
        private final Complaint complaint;

        public ComplaintCardPanel(Complaint complaint) {
            this.complaint = complaint;
            initComponents();
            setupHoverEffect(); 
            setupContextMenu(); 
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                    new EmptyBorder(10, 15, 10, 15)));
            setPreferredSize(new Dimension(300, 120)); 

            JPanel dotContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            dotContainer.setOpaque(false);
            JLabel dotLabel = new JLabel();
            dotLabel.setIcon(createColoredDotIcon(complaint)); 
            dotContainer.add(dotLabel);
            add(dotContainer, BorderLayout.WEST);

            JPanel bodyPanel = new JPanel();
            bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
            bodyPanel.setOpaque(false);
            bodyPanel.setBorder(new EmptyBorder(0, 10, 0, 0)); 

            JLabel titleLabel = new JLabel(complaint.getCategory().toString() + " - " + complaint.getTitle());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLabel.setForeground(new Color(52, 73, 94));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            bodyPanel.add(titleLabel);

            bodyPanel.add(Box.createVerticalStrut(5));

            JLabel summaryLabel = new JLabel("ID: " + complaint.getComplaintID() + " | Raised: " + complaint.getCreatedDate().substring(0, 10));
            summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            summaryLabel.setForeground(new Color(127, 140, 141));
            summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            bodyPanel.add(summaryLabel);

            add(bodyPanel, BorderLayout.CENTER);

            JPanel footerPanel = new JPanel(new BorderLayout());
            footerPanel.setOpaque(false);
            footerPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

            JLabel iconsPlaceholder = new JLabel("[Status Icons]"); 
            iconsPlaceholder.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
            iconsPlaceholder.setForeground(new Color(189, 195, 199));
            footerPanel.add(iconsPlaceholder, BorderLayout.WEST);

            JLabel profilePlaceholder = new JLabel("[Pic]"); 
            profilePlaceholder.setFont(new Font("Arial", Font.BOLD, 10));
            profilePlaceholder.setForeground(Color.DARK_GRAY);
            profilePlaceholder.setBackground(new Color(230, 230, 230));
            profilePlaceholder.setOpaque(true);
            profilePlaceholder.setBorder(new EmptyBorder(2, 5, 2, 5));
            footerPanel.add(profilePlaceholder, BorderLayout.EAST);

            add(footerPanel, BorderLayout.SOUTH);
        }

        private void setupContextMenu() {
            JPopupMenu popupMenu = new JPopupMenu();

            for (ComplaintStatus newStatus : ComplaintStatus.values()) {
                if (newStatus == complaint.getStatus()) continue;

                JMenuItem item = new JMenuItem("Move to " + newStatus.toString());
                item.addActionListener(e -> {
                    complaint.setStatus(newStatus);
                    try {
                        FileManager.saveComplaints(ComplaintService.getInstance().getAllComplaints());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Error saving state.");
                    }
                    MainWindow.this.refreshKanbanBoard();
                });
                popupMenu.add(item);
            }
            
            this.setComponentPopupMenu(popupMenu);
            for (Component c : this.getComponents()) {
                if (c instanceof JComponent) {
                    ((JComponent) c).setInheritsPopupMenu(true);
                }
            }
        }

        private void setupHoverEffect() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                            new EmptyBorder(10, 15, 10, 15)));
                    setCursor(new Cursor(Cursor.HAND_CURSOR)); 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                            new EmptyBorder(10, 15, 10, 15)));
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        JOptionPane.showMessageDialog(MainWindow.this, 
                            "Complaint: " + complaint.getTitle() + 
                            "\nDescription: " + complaint.getDescription() +
                            "\nStatus: " + complaint.getStatus() + 
                            "\nPriority: " + complaint.getPriority(), 
                            "Card Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }

        private Icon createColoredDotIcon(Complaint complaint) {
            int size = 14; 
            Color dotColor;
            
            switch (complaint.getStatus()) {
                case OPEN:
                    dotColor = new Color(231, 76, 60); 
                    break;
                case ASSIGNED:
                    dotColor = new Color(155, 89, 182); 
                    break;
                case ESCALATED:
                    dotColor = new Color(243, 156, 18); 
                    break;
                case RESOLVED:
                    dotColor = new Color(46, 204, 113); 
                    break;
                default:
                    dotColor = new Color(189, 195, 199); 
            }

            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(dotColor);
                    g2d.fillOval(x, y + 2, size, size); 
                    g2d.dispose();
                }

                @Override
                public int getIconWidth() {
                    return size;
                }

                @Override
                public int getIconHeight() {
                    return size + 4; 
                }
            };
        }
    }
}