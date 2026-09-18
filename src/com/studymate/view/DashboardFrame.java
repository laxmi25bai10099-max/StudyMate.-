package com.studymate.view;

import com.studymate.model.*;
import com.studymate.repository.DataStore;
import com.studymate.service.StudyAnalytics;
//import com.studymate.service.ValidationUtil;
import com.studymate.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/** Fully functional StudyMate desktop dashboard. All CRUD actions persist to a local .ser file. */
public class DashboardFrame extends JFrame {
    private final DataStore dataStore;
    private final com.studymate.service.SubjectService subjectService;
    private final com.studymate.service.TaskService taskService;
    private final com.studymate.service.ProgressService progressService;
    private JPanel contentPanel;
    private JLabel dateLabel;
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    private final Set<String> notifiedReminders = new HashSet<>();

    public DashboardFrame() {
        dataStore = DataStore.loadData();
        subjectService = new com.studymate.service.SubjectService(dataStore);
        taskService = new com.studymate.service.TaskService(dataStore);
        progressService = new com.studymate.service.ProgressService(dataStore);
        setTitle("StudyMate Pro - Plan · Learn · Achieve");
        setSize(1380, 880);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildShell();
        showDashboard();
    }

    private void buildShell() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.MAIN_BG);
        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createSidebar(), BorderLayout.WEST);
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.MAIN_BG);
        root.add(contentPanel, BorderLayout.CENTER);
        add(root);
        Timer timer = new Timer(1000, e -> updateDate());
        timer.start();
        Timer reminderTimer = new Timer(15000, e -> checkDueReminders());
        reminderTimer.setInitialDelay(1500);
        reminderTimer.start();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.SIDEBAR_BG);
        header.setPreferredSize(new Dimension(0, 54));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel title = new JLabel("StudyMate Pro   |   Plan · Learn · Achieve");
        title.setFont(UITheme.FONT_HEADER); title.setForeground(Color.WHITE);
        dateLabel = new JLabel(); dateLabel.setFont(UITheme.FONT_BODY); dateLabel.setForeground(Color.WHITE);
        updateDate();
        header.add(title, BorderLayout.WEST); header.add(dateLabel, BorderLayout.EAST);
        return header;
    }
    private void updateDate() { if (dateLabel != null) dateLabel.setText("" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy | hh:mm:ss a"))); }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(215, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(14, 10, 14, 10));
        String[][] nav = {
            {"", "Dashboard"}, {"", "Subjects"}, {"", "Tasks"}, {"", "Notes"},
            {"", "Schedule"}, {"", "Reminders"}, {"", "Exams & Tests"}, {"", "Goals"},
            {"", "Habit Tracker"}, {"", "Progress & Reports"}, {"", "Achievements"}, {"", "Settings"}
        };
        for (String[] n : nav) {
            JButton b = new JButton(n[0] + "  " + n[1]);
            b.setFont(UITheme.FONT_BODY); b.setForeground(Color.WHITE); b.setBackground(UITheme.SIDEBAR_BG);
            b.setFocusPainted(false); b.setBorderPainted(false); b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setMaximumSize(new Dimension(195, 38)); b.setPreferredSize(new Dimension(195, 38));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addActionListener(e -> navigate(n[1]));
            sidebar.add(b); sidebar.add(Box.createRigidArea(new Dimension(0, 3)));
        }
        sidebar.add(Box.createVerticalGlue());
        JLabel q = new JLabel("<html><center><i>Small steps every day<br>lead to big results!</i></center></html>");
        q.setFont(UITheme.FONT_SMALL); q.setForeground(UITheme.TEXT_MUTED); sidebar.add(q);
        return sidebar;
    }

    private void navigate(String page) {
        switch(page) {
            case "Dashboard": showDashboard(); break; case "Subjects": showSubjects(); break; case "Tasks": showTasks(); break;
            case "Notes": showNotes(); break; case "Schedule": showSchedule(); break; case "Reminders": showReminders(); break;
            case "Exams & Tests": showExams(); break; case "Goals": showGoals(); break; case "Habit Tracker": showHabits(); break;
            case "Progress & Reports": showProgress(); break; case "Achievements": showAchievements(); break; case "Settings": showSettings(); break;
        }
    }
    private void setContent(JComponent c) { contentPanel.removeAll(); contentPanel.add(c, BorderLayout.CENTER); contentPanel.revalidate(); contentPanel.repaint(); }
    private JPanel page(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(12,12)); p.setBackground(UITheme.MAIN_BG); p.setBorder(new EmptyBorder(18,18,18,18));
        JPanel head = new JPanel(new BorderLayout()); head.setOpaque(false);
        JLabel t = new JLabel(title); t.setFont(new Font("Segoe UI",Font.BOLD,24)); t.setForeground(UITheme.TEXT_DARK);
        JLabel s = new JLabel(subtitle); s.setFont(UITheme.FONT_BODY); s.setForeground(UITheme.TEXT_MUTED);
        head.add(t,BorderLayout.NORTH); head.add(s,BorderLayout.SOUTH); p.add(head,BorderLayout.NORTH); return p;
    }
    private JScrollPane scroll(JComponent c) { JScrollPane sp=new JScrollPane(c); sp.setBorder(null); sp.getVerticalScrollBar().setUnitIncrement(16); return sp; }
    private JButton addButton(String text, ActionListener l) { JButton b=UITheme.styledButton(text,UITheme.ACCENT_PRIMARY,Color.WHITE); b.addActionListener(l); return b; }

    private void showDashboard() {
        JPanel p=page("Dashboard","Everything you need to organize your academic life in one place.");
        JPanel grid=new JPanel(new GridLayout(0,3,12,12)); grid.setOpaque(false);
        grid.add(stat("Subjects",String.valueOf(dataStore.getSubjects().size()),"Manage your subjects",UITheme.ACCENT_LIGHT,UITheme.ACCENT_PRIMARY));
        grid.add(stat("Tasks",String.valueOf(dataStore.getTasks().size()),"Track assignments & work",UITheme.COLOR_PENDING_BG,UITheme.COLOR_PENDING_FG));
        grid.add(stat("Exams",String.valueOf(dataStore.getExams().size()),"Upcoming tests",UITheme.COLOR_HIGH_BG,UITheme.COLOR_HIGH_FG));
        grid.add(stat("Notes",String.valueOf(dataStore.getNotes().size()),"Your study notes",new Color(243,232,255),new Color(126,34,206)));
        grid.add(stat("Goals",String.valueOf(dataStore.getGoals().size()),"Targets to achieve",UITheme.COLOR_COMPLETED_BG,UITheme.COLOR_COMPLETED_FG));
        grid.add(stat("Reminders",String.valueOf(dataStore.getReminders().size()),"Never miss a deadline",UITheme.ACCENT_LIGHT,UITheme.ACCENT_PRIMARY));
        JPanel actions=UITheme.createCardPanel(); actions.setLayout(new GridLayout(2,3,10,10));
        actions.add(addButton("+ Add Subject",e->showSubjectsAndAdd())); actions.add(addButton("+ Add Task",e->showTasksAndAdd())); actions.add(addButton("+ Add Note",e->showNotesAndAdd()));
        actions.add(addButton("+ Add Exam",e->showExamsAndAdd())); actions.add(addButton("+ Add Goal",e->showGoalsAndAdd())); actions.add(addButton("+ Add Reminder",e->showRemindersAndAdd()));
        JPanel lower=new JPanel(new BorderLayout(12,12)); lower.setOpaque(false);
        lower.add(actions,BorderLayout.NORTH);
        JPanel dashboardBottom=new JPanel(new GridLayout(1,2,12,12)); dashboardBottom.setOpaque(false);
        dashboardBottom.add(createRecentTasks());
        dashboardBottom.add(createPriorityPanel());
        lower.add(dashboardBottom,BorderLayout.CENTER);
        p.add(scroll(wrap(grid,lower)),BorderLayout.CENTER); setContent(p);
    }
    private JPanel wrap(JComponent a,JComponent b){ JPanel x=new JPanel(new BorderLayout(12,12));x.setOpaque(false);x.add(a,BorderLayout.NORTH);x.add(b,BorderLayout.CENTER);return x; }
    private JPanel stat(String a,String b,String c,Color bg,Color fg){ JPanel p=UITheme.createCardPanel();p.setLayout(new BorderLayout(5,5));JLabel x=new JLabel(a);x.setFont(UITheme.FONT_TITLE);JLabel n=new JLabel(b);n.setFont(new Font("Segoe UI",Font.BOLD,30));n.setForeground(fg);JLabel d=new JLabel(c);d.setForeground(UITheme.TEXT_MUTED);p.add(x,BorderLayout.NORTH);p.add(n,BorderLayout.CENTER);p.add(d,BorderLayout.SOUTH);p.setBackground(bg);return p; }
    private JPanel createRecentTasks(){ JPanel p=UITheme.createCardPanel();p.setLayout(new BorderLayout(8,8));JLabel t=new JLabel("Recent Tasks");t.setFont(UITheme.FONT_TITLE);p.add(t,BorderLayout.NORTH);DefaultTableModel m=model("Title","Subject","Due","Priority","Status");for(Task x:dataStore.getTasks())m.addRow(new Object[]{x.getTitle(),x.getSubject(),x.getDueDate(),x.getPriority(),x.getStatus()});JTable table=new JTable(m);table.setRowHeight(27);p.add(scroll(table),BorderLayout.CENTER);return p; }

    private JPanel createPriorityPanel(){
        JPanel p=UITheme.createCardPanel(); p.setLayout(new BorderLayout(8,8));
        JLabel title=new JLabel("Recommended Study Tasks"); title.setFont(UITheme.FONT_TITLE);
        p.add(title,BorderLayout.NORTH);
        DefaultTableModel m=model("Task","Due","Score","Level");
        List<StudyAnalytics.TaskScore> scores=StudyAnalytics.prioritizedTasks(dataStore);
        int limit=Math.min(5,scores.size());
        for(int i=0;i<limit;i++){ StudyAnalytics.TaskScore ts=scores.get(i); Task t=ts.getTask(); m.addRow(new Object[]{t.getTitle(),t.getDueDate(),ts.getScore(),ts.getLevel()}); }
        JTable table=new JTable(m); table.setRowHeight(26);
        if(scores.isEmpty()) m.addRow(new Object[]{"No pending tasks","-","-","Great job"});
        p.add(scroll(table),BorderLayout.CENTER);
        JLabel hint=new JLabel("Rule-based score: deadline + priority + nearby subject exam"); hint.setFont(UITheme.FONT_SMALL); hint.setForeground(UITheme.TEXT_MUTED); p.add(hint,BorderLayout.SOUTH);
        return p;
    }

    private DefaultTableModel model(String... cols){return new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};}
    private JPanel tablePage(String title,String subtitle,JTable table,JButton... buttons){
        JPanel p=page(title,subtitle);
        JPanel bar=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        bar.setOpaque(false);
        for(JButton b:buttons) bar.add(b);
        JPanel center=new JPanel(new BorderLayout(10,10));
        center.setOpaque(false);
        center.add(bar,BorderLayout.NORTH);
        center.add(scroll(table),BorderLayout.CENTER);
        p.add(center,BorderLayout.CENTER);
        return p;
    }

    private void showSubjects(){
        DefaultTableModel m=model("Code","Subject","Instructor","Room");for(Subject s:dataStore.getSubjects())m.addRow(new Object[]{s.getCode(),s.getName(),s.getInstructor(),s.getRoom()});JTable t=new JTable(m);t.setRowHeight(30);
        JButton add=addButton("+ Add Subject",e->showSubjectsAndAdd()); JButton edit=addButton("Edit",e->{int r=t.getSelectedRow();if(r>=0)editSubject(r);else warn("Select a subject first.");}); JButton del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected subject?")){dataStore.getSubjects().remove(r);save();showSubjects();}else if(r<0)warn("Select a subject first.");});
        setContent(tablePage("Subjects","Add, edit and delete subjects. Changes are saved automatically.",t,add,edit,del));
    }
    private void showSubjectsAndAdd(){showSubjects();SwingUtilities.invokeLater(this::addSubject);}
    private void addSubject(){JTextField code=new JTextField(),name=new JTextField(),ins=new JTextField(),room=new JTextField();if(form("Add Subject",new String[]{"Code","Subject name","Instructor","Room"},new JComponent[]{code,name,ins,room})){String error=subjectService.validateNew(code.getText(),name.getText(),ins.getText(),room.getText());if(error!=null){warn(error);return;}dataStore.getSubjects().add(new Subject(code.getText().trim(),name.getText().trim(),ins.getText().trim(),room.getText().trim()));save();showSubjects();}}
    private void editSubject(int r){Subject s=dataStore.getSubjects().get(r);JTextField code=new JTextField(s.getCode()),name=new JTextField(s.getName()),ins=new JTextField(s.getInstructor()),room=new JTextField(s.getRoom());if(form("Edit Subject",new String[]{"Code","Subject name","Instructor","Room"},new JComponent[]{code,name,ins,room})){s.setCode(code.getText().trim());s.setName(name.getText().trim());s.setInstructor(ins.getText().trim());s.setRoom(room.getText().trim());save();showSubjects();}}

    private void showTasks(){
        DefaultTableModel m=model("Title","Subject","Type","Due Date","Priority","Status");for(Task x:dataStore.getTasks())m.addRow(new Object[]{x.getTitle(),x.getSubject(),x.getType(),x.getDueDate(),x.getPriority(),x.getStatus()});JTable t=new JTable(m);t.setRowHeight(30);
        JButton add=addButton("+ Add Task",e->showTasksAndAdd()); JButton status=addButton("Mark Completed",e->{int r=t.getSelectedRow();if(r>=0){dataStore.getTasks().get(r).setStatus("Completed");save();showTasks();}else warn("Select a task first.");}); JButton edit=addButton("Edit",e->{int r=t.getSelectedRow();if(r>=0)editTask(r);else warn("Select a task first.");}); JButton del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected task?")){dataStore.getTasks().remove(r);save();showTasks();}else if(r<0)warn("Select a task first.");});
        setContent(tablePage("Tasks","Create assignments, set due dates, priorities and completion status.",t,add,status,edit,del));
    }
    private void showTasksAndAdd(){showTasks();SwingUtilities.invokeLater(this::addTask);}
    private void addTask(){JTextField title=new JTextField(),sub=new JTextField(),type=new JTextField("Assignment"),date=new JTextField(LocalDate.now().toString());JComboBox<String> pri=new JComboBox<>(new String[]{"Low","Medium","High"});JComboBox<String> stat=new JComboBox<>(new String[]{"Pending","In Progress","Completed"});if(form("Add Task",new String[]{"Title","Subject","Type","Due date (YYYY-MM-DD)","Priority","Status"},new JComponent[]{title,sub,type,date,pri,stat})){String error=taskService.validate(title.getText(),sub.getText(),type.getText(),date.getText());if(error!=null){warn(error);return;}LocalDate d=parseDate(date.getText());if(d!=null){dataStore.getTasks().add(new Task(title.getText().trim(),sub.getText().trim(),type.getText().trim(),d,(String)pri.getSelectedItem(),(String)stat.getSelectedItem()));save();showTasks();}}}
    private void editTask(int r){Task x=dataStore.getTasks().get(r);JTextField title=new JTextField(x.getTitle()),sub=new JTextField(x.getSubject()),type=new JTextField(x.getType()),date=new JTextField(x.getDueDate().toString());JComboBox<String> pri=new JComboBox<>(new String[]{"Low","Medium","High"});pri.setSelectedItem(x.getPriority());JComboBox<String> stat=new JComboBox<>(new String[]{"Pending","In Progress","Completed"});stat.setSelectedItem(x.getStatus());if(form("Edit Task",new String[]{"Title","Subject","Type","Due date (YYYY-MM-DD)","Priority","Status"},new JComponent[]{title,sub,type,date,pri,stat})){LocalDate d=parseDate(date.getText());if(d!=null){x.setTitle(title.getText().trim());x.setSubject(sub.getText().trim());x.setType(type.getText().trim());x.setDueDate(d);x.setPriority((String)pri.getSelectedItem());x.setStatus((String)stat.getSelectedItem());save();showTasks();}}}

    private void showNotes(){DefaultTableModel m=model("Title","Subject","Tags","Date","Content");for(Note n:dataStore.getNotes())m.addRow(new Object[]{n.getTitle(),n.getSubject(),n.getTags(),n.getDate(),n.getContent()});JTable t=new JTable(m);t.setRowHeight(30);t.getColumnModel().getColumn(4).setPreferredWidth(360);JButton add=addButton("+ Add Note",e->showNotesAndAdd()),edit=addButton("Edit",e->{int r=t.getSelectedRow();if(r>=0)editNote(r);else warn("Select a note first.");}),del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected note?")){dataStore.getNotes().remove(r);save();showNotes();}else if(r<0)warn("Select a note first.");});setContent(tablePage("Notes","Store revision notes, tags and study material.",t,add,edit,del));}
    private void showNotesAndAdd(){showNotes();SwingUtilities.invokeLater(this::addNote);} private void addNote(){JTextField title=new JTextField(),sub=new JTextField(),tags=new JTextField();JTextArea content=new JTextArea(5,25);content.setLineWrap(true);if(form("Add Note",new String[]{"Title","Subject","Tags","Content"},new JComponent[]{title,sub,tags,new JScrollPane(content)})){dataStore.getNotes().add(new Note(title.getText().trim(),sub.getText().trim(),content.getText().trim(),tags.getText().trim(),LocalDate.now()));save();showNotes();}}
    private void editNote(int r){Note n=dataStore.getNotes().get(r);JTextField title=new JTextField(n.getTitle()),sub=new JTextField(n.getSubject()),tags=new JTextField(n.getTags());JTextArea content=new JTextArea(n.getContent(),5,25);content.setLineWrap(true);if(form("Edit Note",new String[]{"Title","Subject","Tags","Content"},new JComponent[]{title,sub,tags,new JScrollPane(content)})){n.setTitle(title.getText().trim());n.setSubject(sub.getText().trim());n.setTags(tags.getText().trim());n.setContent(content.getText().trim());n.setDate(LocalDate.now());save();showNotes();}}

    private void showSchedule(){DefaultTableModel m=model("Day","Time","Subject","Room");for(ScheduleItem x:dataStore.getScheduleItems())m.addRow(new Object[]{x.getDay(),x.getTimeSlot(),x.getSubject(),x.getRoom()});JTable t=new JTable(m);t.setRowHeight(30);JButton add=addButton("+ Add Class",e->addSchedule()),del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected class?")){dataStore.getScheduleItems().remove(r);save();showSchedule();}else if(r<0)warn("Select a class first.");});setContent(tablePage("Schedule","Manage your weekly class timetable.",t,add,del));}
    private void addSchedule(){JTextField day=new JTextField("Mon"),time=new JTextField("08:00 - 10:00"),sub=new JTextField(),room=new JTextField();if(form("Add Class",new String[]{"Day","Time","Subject","Room"},new JComponent[]{day,time,sub,room})){dataStore.getScheduleItems().add(new ScheduleItem(day.getText().trim(),time.getText().trim(),sub.getText().trim(),room.getText().trim()));save();showSchedule();}}

    private void showReminders(){
        DefaultTableModel m=model("Title","Message","Date & Time","Status");
        LocalDateTime now = LocalDateTime.now();
        for(Reminder x:dataStore.getReminders()){
            String status = x.isDone() ? "Completed" : (x.getDateTime().isBefore(now) ? "Overdue" : "Pending");
            m.addRow(new Object[]{x.getTitle(),x.getMessage(),x.getDateTime().format(DATE_TIME),status});
        }
        JTable t=new JTable(m); t.setRowHeight(30);
        JButton add=addButton("+ Add Reminder",e->showRemindersAndAdd()),
                done=addButton("Mark Done",e->{int r=t.getSelectedRow();if(r>=0){dataStore.getReminders().get(r).setDone(true);save();showReminders();}else warn("Select a reminder first.");}),
                del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected reminder?")){dataStore.getReminders().remove(r);save();showReminders();}else if(r<0)warn("Select a reminder first.");});
        setContent(tablePage("Reminders","Create deadline reminders and get an in-app notification when they become due.",t,add,done,del));
    }
    private void showRemindersAndAdd(){showReminders();SwingUtilities.invokeLater(this::addReminder);}
    private void addReminder(){
        JTextField title=new JTextField(),msg=new JTextField(),dt=new JTextField(LocalDateTime.now().plusHours(2).format(DATE_TIME));
        if(form("Add Reminder",new String[]{"Title","Message","Date & Time (dd MMM yyyy, hh:mm a)"},new JComponent[]{title,msg,dt})){
            try{
                LocalDateTime when=LocalDateTime.parse(dt.getText().trim(),DATE_TIME);
                if(when.isBefore(LocalDateTime.now())){warn("Please choose a future date/time for a new reminder.");return;}
                dataStore.getReminders().add(new Reminder(title.getText().trim(),msg.getText().trim(),when));
                save();showReminders();
            }catch(Exception e){warn("Invalid date/time. Example: 12 Sep 2026, 06:30 PM");}
        }
    }
    private void checkDueReminders(){
        LocalDateTime now=LocalDateTime.now();
        for(Reminder r:dataStore.getReminders()){
            if(!r.isDone() && !r.getDateTime().isAfter(now)){
                String key=r.getTitle()+"|"+r.getDateTime();
                if(notifiedReminders.add(key)){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this,
                        "\uD83D\uDD14 " + r.getTitle() + "\n\n" + r.getMessage() + "\n\nScheduled for: " + r.getDateTime().format(DATE_TIME),
                        "StudyMate Reminder", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void showExams(){DefaultTableModel m=model("Subject","Exam","Date","Time","Room","Countdown");for(Exam x:dataStore.getExams())m.addRow(new Object[]{x.getSubject(),x.getExamName(),x.getDate(),x.getTime(),x.getRoom(),daysLeft(x.getDate())});JTable t=new JTable(m);t.setRowHeight(30);JButton add=addButton("+ Add Exam",e->showExamsAndAdd()),edit=addButton("Edit",e->{int r=t.getSelectedRow();if(r>=0)editExam(r);else warn("Select an exam first.");}),del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected exam?")){dataStore.getExams().remove(r);save();showExams();}else if(r<0)warn("Select an exam first.");});setContent(tablePage("Exams & Tests","Keep exam dates, rooms and countdowns organized.",t,add,edit,del));}
    private String daysLeft(LocalDate d){long x=java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(),d);if(x<0)return "Past";if(x==0)return "Today";return x+" days";}
    private void showExamsAndAdd(){showExams();SwingUtilities.invokeLater(this::addExam);} private void addExam(){JTextField sub=new JTextField(),name=new JTextField(),date=new JTextField(LocalDate.now().plusDays(7).toString()),time=new JTextField("10:00 AM"),room=new JTextField();if(form("Add Exam",new String[]{"Subject","Exam name","Date (YYYY-MM-DD)","Time","Room"},new JComponent[]{sub,name,date,time,room})){LocalDate d=parseDate(date.getText());if(d!=null){dataStore.getExams().add(new Exam(sub.getText().trim(),name.getText().trim(),d,time.getText().trim(),room.getText().trim()));save();showExams();}}}
    private void editExam(int r){Exam x=dataStore.getExams().get(r);JTextField sub=new JTextField(x.getSubject()),name=new JTextField(x.getExamName()),date=new JTextField(x.getDate().toString()),time=new JTextField(x.getTime()),room=new JTextField(x.getRoom());if(form("Edit Exam",new String[]{"Subject","Exam name","Date (YYYY-MM-DD)","Time","Room"},new JComponent[]{sub,name,date,time,room})){LocalDate d=parseDate(date.getText());if(d!=null){x.setSubject(sub.getText().trim());x.setExamName(name.getText().trim());x.setDate(d);x.setTime(time.getText().trim());x.setRoom(room.getText().trim());save();showExams();}}}

    private void showGoals(){JPanel list=new JPanel();list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));list.setOpaque(false);for(int i=0;i<dataStore.getGoals().size();i++){Goal g=dataStore.getGoals().get(i);JPanel row=UITheme.createCardPanel();row.setLayout(new BorderLayout(8,8));JLabel l=new JLabel(g.getTitle()+"  ["+g.getCategory()+"]");l.setFont(UITheme.FONT_TITLE);JProgressBar pb=new JProgressBar(0,g.getMaxProgress());pb.setValue(g.getCurrentProgress());pb.setStringPainted(true);pb.setString(g.getCurrentProgress()+" / "+g.getMaxProgress());JButton plus=addButton("+1",e->{g.setCurrentProgress(g.getCurrentProgress()+1);save();showGoals();});JButton del=addButton("Delete",e->{dataStore.getGoals().remove(g);save();showGoals();});row.add(l,BorderLayout.NORTH);row.add(pb,BorderLayout.CENTER);JPanel a=new JPanel();a.setOpaque(false);a.add(plus);a.add(del);row.add(a,BorderLayout.EAST);list.add(row);list.add(Box.createRigidArea(new Dimension(0,8)));}JButton add=addButton("+ Add Goal",e->showGoalsAndAdd());JPanel p=page("Goals","Set measurable targets and update progress with one click.");p.add(add,BorderLayout.NORTH);p.add(scroll(list),BorderLayout.CENTER);setContent(p);}
    private void showGoalsAndAdd(){showGoals();SwingUtilities.invokeLater(this::addGoal);}private void addGoal(){JTextField title=new JTextField(),cat=new JTextField("Academic"),cur=new JTextField("0"),max=new JTextField("10");if(form("Add Goal",new String[]{"Goal title","Category","Current progress","Target"},new JComponent[]{title,cat,cur,max})){try{dataStore.getGoals().add(new Goal(title.getText().trim(),cat.getText().trim(),Integer.parseInt(cur.getText()),Integer.parseInt(max.getText())));save();showGoals();}catch(Exception e){warn("Progress and target must be whole numbers.");}}}

    private void showHabits(){
        JPanel p=page("Habit Tracker","Track daily habits with a yearly activity heatmap, streaks and quick daily logging.");

        JPanel top=UITheme.createCardPanel();
        top.setLayout(new BorderLayout(10,10));
        JLabel heading=new JLabel("Yearly Activity"); heading.setFont(UITheme.FONT_TITLE);
        JLabel hint=new JLabel("Each square represents one day. Darker squares mean more habits completed."); hint.setForeground(UITheme.TEXT_MUTED);
        JPanel head=new JPanel(new BorderLayout()); head.setOpaque(false); head.add(heading,BorderLayout.WEST); head.add(hint,BorderLayout.CENTER);
        top.add(head,BorderLayout.NORTH);
        top.add(new HabitHeatmapPanel(dataStore),BorderLayout.CENTER);

        DefaultTableModel m=model("Habit","Today","Current Streak","Best Streak");
        for(Habit h:dataStore.getHabits()) m.addRow(new Object[]{h.getTitle(),h.isCompletedOn(LocalDate.now())?"Done":"Not done",h.getCurrentStreak()+" days",h.getBestStreak()+" days"});
        JTable t=new JTable(m); t.setRowHeight(30);
        JButton add=addButton("+ Add Habit",e->addHabit());
        JButton toggle=addButton("Toggle Today",e->{int r=t.getSelectedRow();if(r>=0){toggleHabitToday(dataStore.getHabits().get(r));}else warn("Select a habit first.");});
        JButton del=addButton("Delete",e->{int r=t.getSelectedRow();if(r>=0&&confirm("Delete selected habit?")){dataStore.getHabits().remove(r);save();showHabits();}else if(r<0)warn("Select a habit first.");});
        JPanel tableCard=UITheme.createCardPanel(); tableCard.setLayout(new BorderLayout(8,8));
        JLabel th=new JLabel("My Habits"); th.setFont(UITheme.FONT_TITLE); tableCard.add(th,BorderLayout.NORTH); tableCard.add(scroll(t),BorderLayout.CENTER);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT,8,4)); buttons.setOpaque(false); buttons.add(add);buttons.add(toggle);buttons.add(del); tableCard.add(buttons,BorderLayout.SOUTH);
        JPanel center=new JPanel(new BorderLayout(12,12)); center.setOpaque(false); center.add(top,BorderLayout.NORTH); center.add(tableCard,BorderLayout.CENTER);
        p.add(scroll(center),BorderLayout.CENTER); setContent(p);
    }
    private void toggleHabitToday(Habit h){
        LocalDate today=LocalDate.now();
        boolean newValue=!h.isCompletedOn(today);
        h.setCompletedOn(today,newValue);
        int day=today.getDayOfWeek().getValue()-1;
        boolean[] w=h.getWeekLog(); if(w==null||w.length!=7) w=new boolean[7]; w[day]=newValue; h.setWeekLog(w);
        recalculateStreaks(h); save(); showHabits();
    }
    private void recalculateStreaks(Habit h){
        LocalDate d=LocalDate.now(); int current=0;
        while(h.isCompletedOn(d)){current++;d=d.minusDays(1);}
        int best=0,run=0;
        LocalDate start=LocalDate.now().minusDays(364);
        for(int i=0;i<365;i++){LocalDate x=start.plusDays(i);if(h.isCompletedOn(x)){run++;best=Math.max(best,run);}else run=0;}
        h.setCurrentStreak(current); h.setBestStreak(Math.max(best,h.getBestStreak()));
    }

    private static class HabitHeatmapPanel extends JPanel {
        private final DataStore ds;
        private final int cell=12, gap=3, left=42, top=30;
        HabitHeatmapPanel(DataStore ds){this.ds=ds;setOpaque(false);setPreferredSize(new Dimension(1050,210));setToolTipText("");}
        private int completedCount(LocalDate date){int n=0;for(Habit h:ds.getHabits())if(h.isCompletedOn(date))n++;return n;}
        private Color heatColor(int count){
            if(count<=0)return new Color(241,245,249);
            if(count==1)return new Color(187,247,208);
            if(count==2)return new Color(74,222,128);
            if(count==3)return new Color(22,163,74);
            return new Color(21,128,61);
        }
        protected void paintComponent(Graphics g){
            super.paintComponent(g); Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            LocalDate end=LocalDate.now(); LocalDate start=end.minusDays(364);
            LocalDate first=start.minusDays(start.getDayOfWeek().getValue()%7);
            int weeks=(int)((end.toEpochDay()-first.toEpochDay())/7)+1;
            String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            g2.setFont(UITheme.FONT_SMALL);g2.setColor(UITheme.TEXT_MUTED);
            int lastMonth=-1;
            for(int w=0;w<weeks;w++){LocalDate colStart=first.plusDays(w*7);int month=colStart.getMonthValue()-1;if(month!=lastMonth){g2.drawString(months[month],left+w*(cell+gap),18);lastMonth=month;}}
            String[] days={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
            for(int r=0;r<7;r++)g2.drawString(days[r],2,top+r*(cell+gap)+10);
            for(int w=0;w<weeks;w++)for(int r=0;r<7;r++){LocalDate date=first.plusDays(w*7L+r);if(date.isBefore(start)||date.isAfter(end))continue;int n=completedCount(date);int x=left+w*(cell+gap),y=top+r*(cell+gap);g2.setColor(heatColor(n));g2.fillRoundRect(x,y,cell,cell,3,3);g2.setColor(new Color(226,232,240));g2.drawRoundRect(x,y,cell,cell,3,3);}
            int legendY=top+7*(cell+gap)+10;g2.setColor(UITheme.TEXT_MUTED);g2.drawString("Less",left,legendY+10);int lx=left+35;for(int i=0;i<=4;i++){g2.setColor(heatColor(i));g2.fillRoundRect(lx+i*18,legendY,cell,cell,3,3);}g2.setColor(UITheme.TEXT_MUTED);g2.drawString("More",lx+95,legendY+10);
            g2.dispose();
        }
        public String getToolTipText(java.awt.event.MouseEvent e){
            LocalDate end=LocalDate.now(),start=end.minusDays(364);LocalDate first=start.minusDays(start.getDayOfWeek().getValue()%7);
            int w=(e.getX()-left)/(cell+gap),r=(e.getY()-top)/(cell+gap); if(w<0||r<0||r>6)return null;LocalDate d=first.plusDays(w*7L+r);if(d.isBefore(start)||d.isAfter(end))return null;return d+" • "+completedCount(d)+" habit(s) completed";
        }
    }

    private void addHabit(){JTextField title=new JTextField();if(form("Add Habit",new String[]{"Habit name"},new JComponent[]{title})){dataStore.getHabits().add(new Habit(title.getText().trim(),new boolean[7],0,0));save();showHabits();}}

    private void showProgress(){
        JPanel p=page("Progress & Reports","Live analytics calculated from your saved academic data.");
        int total=dataStore.getTasks().size(),done=StudyAnalytics.completedTasks(dataStore);
        int taskPct=StudyAnalytics.taskCompletionPercent(dataStore);
        int goalPct=StudyAnalytics.goalProgressPercent(dataStore);
        int habitPct=StudyAnalytics.habitConsistencyPercent(dataStore);
        int overall=StudyAnalytics.overallStudyScore(dataStore);
        JPanel cards=new JPanel(new GridLayout(2,4,12,12)); cards.setOpaque(false);
        cards.add(stat("Tasks Completed",done+" / "+total,"Task completion",UITheme.COLOR_COMPLETED_BG,UITheme.COLOR_COMPLETED_FG));
        cards.add(stat("Task Rate",taskPct+"%","Completed tasks",UITheme.ACCENT_LIGHT,UITheme.ACCENT_PRIMARY));
        cards.add(stat("Goal Progress",goalPct+"%","Across all goals",UITheme.COLOR_COMPLETED_BG,UITheme.COLOR_COMPLETED_FG));
        cards.add(stat("Habit Consistency",habitPct+"%","Last 7-day logs",UITheme.ACCENT_LIGHT,UITheme.ACCENT_PRIMARY));
        cards.add(stat("Overall Study Score",overall+"%","Rule-based summary",UITheme.COLOR_PENDING_BG,UITheme.COLOR_PENDING_FG));
        cards.add(stat("Subjects",String.valueOf(dataStore.getSubjects().size()),"Active subjects",UITheme.COLOR_PENDING_BG,UITheme.COLOR_PENDING_FG));
        cards.add(stat("Exams",String.valueOf(dataStore.getExams().size()),"Scheduled exams",UITheme.COLOR_HIGH_BG,UITheme.COLOR_HIGH_FG));
        cards.add(stat("Notes",String.valueOf(dataStore.getNotes().size()),"Study resources",new Color(243,232,255),new Color(126,34,206)));
        JPanel detail=UITheme.createCardPanel(); detail.setLayout(new BorderLayout(8,8));
        JLabel h=new JLabel("Subject-wise Task Completion"); h.setFont(UITheme.FONT_TITLE); detail.add(h,BorderLayout.NORTH);
        DefaultTableModel m=model("Subject","Progress","Task Count");
        java.util.Map<String,Integer> progress=progressService.subjectCompletion();
        for(java.util.Map.Entry<String,Integer> e:progress.entrySet()) m.addRow(new Object[]{e.getKey(),e.getValue()+"%",taskService.countForSubject(e.getKey())});
        JTable table=new JTable(m); table.setRowHeight(28); detail.add(scroll(table),BorderLayout.CENTER);
        JTextArea explanation=new JTextArea("How the score works\n\nOverall Study Score = average of task completion, goal progress and 7-day habit consistency.\n\nRecommended tasks use a transparent rule-based score using deadline urgency, task priority and exams within 7 days for the same subject. This is not machine learning; it is deterministic business logic that can be tested and explained.");
        explanation.setFont(UITheme.FONT_BODY); explanation.setEditable(false); explanation.setLineWrap(true); explanation.setWrapStyleWord(true); explanation.setBackground(UITheme.CARD_BG); explanation.setBorder(new EmptyBorder(12,12,12,12));
        JPanel center=new JPanel(new BorderLayout(12,12)); center.setOpaque(false); center.add(detail,BorderLayout.CENTER); center.add(scroll(explanation),BorderLayout.SOUTH);
        p.add(scroll(wrap(cards,center)),BorderLayout.CENTER); setContent(p);
    }
    private void showAchievements(){JPanel p=page("Achievements","Milestones are calculated from your StudyMate activity.");JPanel a=new JPanel(new GridLayout(0,2,12,12));a.setOpaque(false);addAchievement(a,"First Step","Add your first subject",dataStore.getSubjects().size()>=1);addAchievement(a,"Task Master","Complete 5 tasks",countCompleted()>=5);addAchievement(a,"Organized","Create 5 notes",dataStore.getNotes().size()>=5);addAchievement(a,"Goal Setter","Create 3 goals",dataStore.getGoals().size()>=3);addAchievement(a,"Consistent","Track 3 habits",dataStore.getHabits().size()>=3);addAchievement(a,"Exam Ready","Add 3 exams",dataStore.getExams().size()>=3);p.add(scroll(a),BorderLayout.CENTER);setContent(p);}
    private void addAchievement(JPanel p,String title,String desc,boolean ok){JPanel c=UITheme.createCardPanel();c.setLayout(new BorderLayout());JLabel t=new JLabel((ok?"":"")+title);t.setFont(UITheme.FONT_TITLE);JLabel d=new JLabel("<html>"+desc+"<br>"+(ok?"Unlocked":"Keep going to unlock")+"</html>");d.setForeground(UITheme.TEXT_MUTED);c.add(t,BorderLayout.NORTH);c.add(d,BorderLayout.CENTER);p.add(c);}
    private int countCompleted(){int n=0;for(Task t:dataStore.getTasks())if("Completed".equalsIgnoreCase(t.getStatus()))n++;return n;}
    private void showSettings(){JPanel p=page("Settings","Application preferences and data controls.");JPanel c=UITheme.createCardPanel();c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));JLabel info=new JLabel("StudyMate stores your data locally in studymate_dashboard_data.ser");info.setFont(UITheme.FONT_BODY);c.add(info);c.add(Box.createRigidArea(new Dimension(0,15)));JButton save=addButton("Save Data Now",e->{save();JOptionPane.showMessageDialog(this,"All data saved successfully.");});JButton reset=addButton("Reset Sample Data",e->{if(confirm("This will replace current data with sample data. Continue?")){dataStore.resetToSampleData(); save(); showDashboard(); }});c.add(save);c.add(Box.createRigidArea(new Dimension(0,8)));c.add(reset);p.add(c,BorderLayout.NORTH);setContent(p);}
    private boolean form(String title,String[] labels,JComponent[] fields){JPanel panel=new JPanel(new GridBagLayout());panel.setBorder(new EmptyBorder(5,5,5,5));GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(5,5,5,5);g.fill=GridBagConstraints.HORIZONTAL;g.weightx=1;for(int i=0;i<labels.length;i++){g.gridx=0;g.gridy=i;g.weightx=0;panel.add(new JLabel(labels[i]+":"),g);g.gridx=1;g.weightx=1;panel.add(fields[i],g);}int r=JOptionPane.showConfirmDialog(this,panel,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);if(r!=JOptionPane.OK_OPTION)return false;for(JComponent f:fields){if(f instanceof JTextField && ((JTextField)f).getText().trim().isEmpty()){warn("Please fill all fields.");return false;}if(f instanceof JScrollPane){Component v=((JScrollPane)f).getViewport().getView();if(v instanceof JTextArea && ((JTextArea)v).getText().trim().isEmpty()){warn("Please fill all fields.");return false;}}}return true;}
    private LocalDate parseDate(String s){try{return LocalDate.parse(s.trim());}catch(Exception e){warn("Invalid date. Use YYYY-MM-DD, for example 2026-09-20.");return null;}}
    private boolean confirm(String q){return JOptionPane.showConfirmDialog(this,q,"StudyMate",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
    private void warn(String q){JOptionPane.showMessageDialog(this,q,"StudyMate",JOptionPane.WARNING_MESSAGE);}
    private void save(){dataStore.saveData();}
}
