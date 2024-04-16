//package ce326.hw3;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   
import java.io.*;
import java.util.Scanner;
import org.json.JSONArray;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.*;

public class MenuDemo extends JFrame implements  ActionListener, MouseListener, KeyListener, ItemListener{
    int rows=6;
    int cols=7;
    int ii=0;
    int col, playersRow=-1;
    int depth=1;
    int timerFlag=0, timerFlag1=0, timerFlag2=0;
    String level;
    JFrame frame;
    ImageIcon redIcon = new ImageIcon("icons/red.png"); 
    ImageIcon yellowIcon = new ImageIcon("icons/yellow.png"); 
    ImageIcon ByellowIcon = new ImageIcon("icons/byellow.png");
    ImageIcon BredIcon = new ImageIcon("icons/bred.png");
    ImageIcon en = new ImageIcon("icons/en.png");
    Image enIm = en.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    ImageIcon gr = new ImageIcon("icons/gr.png");
    Image grIm = gr.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    JLabel lab[][];
    JPanel panel;
    int playEnabled=0, historyEnabled=0;;
    JRadioButton AIButton, YOUButton;
    Board board = new Board();
    char[][] table = board.createBoard(rows, cols);
    DefaultListModel<String> listModel = new DefaultListModel<>();
    MinMaxPruning minmax = new MinMaxPruning();
    LocalDateTime dateTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm:ss"); 
    MovesDone move = new MovesDone();
    File dir;
    File gameFile;
    JScrollPane scrollPane;
    JList<String> list;
    Timer timer, timer1, timer2;
    int[] res1={100, 100, 100};
    int res[]={0}; 
    int row=-1;

    public MenuDemo() {
        //create window
        frame = new JFrame("ConnectFour");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(600, 500));
        
        ImageIcon iconConnect4 = new ImageIcon("icons/c4.png");
        Image imageConnect4 = iconConnect4.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        frame.setIconImage(imageConnect4);
        
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.pack();
        
        lab = new JLabel[6][7];
        
        //set menu
        JMenu newGameMenu = new JMenu("New Game");
        newGameMenu.setBackground(Color.LIGHT_GRAY);

        JMenuItem trivial = new JMenuItem("Trivial");
        trivial.setBackground(Color.LIGHT_GRAY);
        trivial.addActionListener(this);    
        newGameMenu.add(trivial); 

        JMenuItem medium = new JMenuItem("Medium");
        medium.setBackground(Color.LIGHT_GRAY);
        medium.addActionListener(this);    
        newGameMenu.add(medium); 

        JMenuItem hard = new JMenuItem("Hard");
        hard.setBackground(Color.LIGHT_GRAY);
        hard.addActionListener(this);    
        newGameMenu.add(hard); 

        JMenu playerMenu = new JMenu("1st Player");
        playerMenu.setBackground(Color.LIGHT_GRAY);

        AIButton = new JRadioButton("AI");
        AIButton.setActionCommand("AI");
        AIButton.setSelected(true);
        AIButton.addItemListener(this);
        playerMenu.add(AIButton); 

      
        YOUButton = new JRadioButton("YOU");
        YOUButton.setActionCommand("YOU");
        YOUButton.addItemListener(this);
        playerMenu.add(YOUButton); 

        ButtonGroup group = new ButtonGroup();
        group.add(AIButton);
        group.add(YOUButton);

        JMenu historyMenu = new JMenu("History");
        historyMenu.setBackground(Color.LIGHT_GRAY);
        
        //create list for history
        listModel = new DefaultListModel<>();
        list = new JList<String>(listModel);
        scrollPane = new JScrollPane(list);
        String path = System.getProperty("user.home");
        
        dir = new File(path,"connect4");
        if(!dir.isDirectory()){
            dir.mkdir();
        }
        
        File[] gameFiles = dir.listFiles();

        if (gameFiles != null) {
            Arrays.sort(gameFiles, Comparator.comparingLong(File::lastModified).reversed());
            for (File file : gameFiles) {
                if (file.isFile()) {
                    String str = file.getName().replace("-", ":");
                    str = str.replaceAll(".json", "");
                    listModel.addElement(str);
                }
            }
        }
        historyMenu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3){
                    return;
                }
                frame.remove(panel);
                frame.add(scrollPane);
                frame.revalidate();
                frame.repaint();
                historyEnabled=1;
            }
        });
        list.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount() == 2){
                    if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3){
                        return;
                    }
                    if (historyEnabled==1){
                        String filename = list.getSelectedValue();
                        filename = filename.replaceAll(":", "-");
                        StringBuffer strbf = new StringBuffer(filename);
                        strbf = strbf.append(".json");
                        filename = strbf.toString();
                        String filepath = dir + "\\" + filename;
                        if (filepath != null) {
                            createGUITable();
                            playGameFromHistory(filepath);
                        }
                        historyEnabled=0;
                    }
                }
            }
        });

        //create help text
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setBackground(Color.LIGHT_GRAY);
        helpMenu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3){
                    return;
                }
                String engStr = "Players take turns selecting a column to put their colored disc" 
                + " into by clicking or using keyboard inputs. The game checks for a winning condition" 
                +" by detecting if any four discs are connected vertically, horizontally, or diagonally." 
                +" AI plays with the yellow discs and player with the red ones.";
                String grStr = "Oi paiktes epilegoun mia stili enallaks gia na topothetisoun to xrwmatisto"
                +" tous pouli kanontas klik h xrhsimopoiwntas to pliktrologio. To paixnidi elegxei an iparxei"
                +" niki entopizontas ean tessera poulia einai sindedemena katheta, orizontia h diagwnia. "
                +"To AI paizei me ta kitrina poulia kai o paikths me ta kokkina.";
                JTextArea txt = new JTextArea(engStr);
                txt.setColumns(20);
                txt.setRows(10); 
                txt.setLineWrap(true); //dont seperate letters from words 
                txt.setWrapStyleWord(true);
                en = new ImageIcon(enIm);
                int button = JOptionPane.showOptionDialog(frame, txt, "Connect4", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, en, new Object[]{"Greek", "OK"}, "OK");
                if(button == JOptionPane.YES_OPTION){
                    txt = new JTextArea(grStr);
                    txt.setColumns(20);
                    txt.setRows(10); 
                    txt.setLineWrap(true); 
                    txt.setWrapStyleWord(true);
                    gr = new ImageIcon(grIm);
                    JOptionPane.showOptionDialog(frame, txt, "Connect4", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, gr,  new Object[]{"OK"}, "OK");
                
                }
            }
        });
        
        JMenuBar bar = new JMenuBar();
        bar.add(newGameMenu);
        bar.add(playerMenu);
        bar.add(historyMenu);
        bar.add(helpMenu);

        frame.setJMenuBar(bar);  
        
        //create gui
        createGUITable();
        frame.setVisible(true);
    }

    //adds to history the games that finished
    public void addToHistory(String winner, String level){
        StringBuffer str = new StringBuffer();
        str.append(dateTime.format(formatter));
        str.append(level);
        str.append(winner);
        String historyName = str.toString();

        String fn = historyName.replaceAll(":", "-");
        StringBuffer strbf = new StringBuffer(fn);
        strbf = strbf.append(".json");
        fn = strbf.toString();
        gameFile = new File(dir, fn);
        try{
            createFile(gameFile);
        }catch(IOException ex){
        }
        listModel.add(0, historyName);
        list.setModel(listModel);
    }
    
    public void createFile(File file) throws IOException {
        if(file.exists()) {
            //file already exists
            throw new IOException();
        }
        else if(file != null){
            FileWriter wr = new FileWriter(file);
            String str = move.JSONtoString();
            wr.write(str);
            wr.close();
        }        
    }

    //plays the game that is selected from the history list using a .json file
    public void playGameFromHistory(String filepath){
        ii = 0;
        StringBuilder strBuilder = new StringBuilder();
        File file = new File(filepath);
        try(Scanner scan = new Scanner(file)){
            while(scan.hasNextLine()){
                String str = scan.nextLine();
                strBuilder.append(str);
                strBuilder.append("\n");
            }
        }catch(FileNotFoundException ex){
            if(!file.exists()) {
                //Unable to find file
                return;
            }
        }

        if(timer!=null && timer.isRunning()==true){
            timer.stop();
        }
        if(timer1!=null && timer1.isRunning()==true){
            timer1.stop();
        }
        if(timer2!=null && timer2.isRunning()==true){
            timer2.stop();
        }
        String json = strBuilder.toString();
        JSONArray JSONarr = new JSONArray(json);
        timer2 = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ii < JSONarr.length()){
                    String player = JSONarr.getJSONObject(ii).getString("player");
                    int row = JSONarr.getJSONObject(ii).getInt("row");
                    int column = JSONarr.getJSONObject(ii).getInt("column");
                    if(player.equals("YOU")){
                        changeLabelColor(redIcon, row, column);
                    }
                    else{
                        changeLabelColor(yellowIcon, row, column);
                    }
                    ii++;
                    timer2.start();
                }
                else{
                    timer2.stop();
                }
            }
        });
        
        timer2.start();  
    }

    //creates the gui table of the game
    public void createGUITable(){
        //dimiourgia panel
        panel = new JPanel( new GridLayout(6,7));
        if(historyEnabled==1){
            frame.remove(scrollPane);
            frame.getContentPane().add(panel);
            historyEnabled=0;
        }
        ImageIcon icon = new ImageIcon("icons/white.png");
        Image image1 = icon.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT);
        icon = new ImageIcon(image1);
        //dimiourgia tablo paixnidiou
        for(int i=0; i<6; i++){  
            for(int j=0; j<7; j++){  
                lab[i][j] = new JLabel(icon);
                panel.add(lab[i][j]);
            }
        }

        for(int i=0; i<6; i++){  
            for(int j=0; j<7; j++){  
                lab[i][j].addMouseListener(this);
                lab[i][j].setFocusable(true);
                lab[i][j].addKeyListener(this);
                lab[i][j].requestFocusInWindow();
            }
        }
        panel.revalidate();
        panel.repaint();
        frame.getContentPane().add(panel);
    }

    //clears the table from discs
    public void clearTable(){
        //clear GUI table
        ImageIcon whiteIcon = new ImageIcon("icons/white.png");
        Image whiteImage = whiteIcon.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT);
        whiteIcon = new ImageIcon(whiteImage);
        for(int i=0; i<6; i++){  
            for(int j=0; j<7; j++){  
                lab[i][j].setIcon(whiteIcon);
            }
        }

        //clear board table
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                table[i][j] = ' ';
            }
        }
    }

    //action listener for the new game menu
    public void actionPerformed(ActionEvent e){
        String buttonString = e.getActionCommand();
        if(timer != null && timer.isRunning()){
            timer.stop();
        }
        if(timer1 != null && timer1.isRunning()){
            timer1.stop();
        }
        if(timer2 != null && timer2.isRunning()){
            timer2.stop();
            playEnabled=1;
        }
        clearTable();
        if(historyEnabled==1){   
            createGUITable();
            historyEnabled=0;
        }
        if(buttonString.equals("Trivial")){
            level = "  L: Trivial ";
            depth=1;
        }
        else if(buttonString.equals("Medium")){
            level = "  L: Medium ";
            depth=3;
        }
        else if(buttonString.equals("Hard")){
            level = "  L: Hard ";
            depth=5;
        }
        move = new MovesDone();
        if(AIButton.isSelected()){
            //AI plays first default if the players doesn't change the choice
            playEnabled=0;
            int row = board.setPlayerPosition(rows, cols, 3, 'O',table);
            timer1 = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeLabelColor(yellowIcon, row, 3);
                    playEnabled=1;
                    timer1.stop();
                }
            });
            timer1.start();
            changeLabelColor(ByellowIcon, row, 3);
            move.setPlayer("AI");
            move.setMove(row, 3);
            move.addMoves(move);
        }
        playEnabled=1;
        dateTime = LocalDateTime.now(); 
    }
    
    //key listener to play with num from 0 to 6
    public void keyReleased(KeyEvent k){
        if((playEnabled == 1 && timer1 == null) || (playEnabled == 1 && timer1!=null && timer1.isRunning() == false)){
            if(k.getKeyCode() == KeyEvent.VK_0){
                play(0);
            }
            else if(k.getKeyCode() == KeyEvent.VK_1){
                play(1);
            }
            else if(k.getKeyCode() == KeyEvent.VK_2){
                play(2);
            }
            else if(k.getKeyCode() == KeyEvent.VK_3){
                play(3);
            }
            else if(k.getKeyCode() == KeyEvent.VK_4){
                play(4);
            }
            else if(k.getKeyCode() == KeyEvent.VK_5){
                play(5);
            }
            else if(k.getKeyCode() == KeyEvent.VK_6){
                play(6);
            }
            else{
                return;
            }
        }
    }

    //mouse listener to play the game
    public void mouseClicked(MouseEvent e){
        Object src = e.getSource();
        int flag = -1;

        //ignores clicks that are made with the wheel or right button of the mouse 
        if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3){
            return;
        }
        if(e.getClickCount() == 2 && playEnabled==1){
            if(timer1 != null && timer1.isRunning() == true){
                //do nothing
            }
            else{
                for (int i = 0; i < 6; i++) {
                    for (col = 0; col < 7; col++) {
                        if (src == lab[i][col]) {
                            flag=1;
                            play(col);
                            break;
                        }
                    }
                    if(flag == 1){
                        break;
                    }      
                }
            }
        }
        //do nothing
    }

    //finction that is called from key or mouse listener to set the moves for YOU and AI calling MinMax
    public void play(int pos){
        //sets YOU's playing position
        int playersRow = board.setPlayerPosition(rows, cols, pos, 'X',table);
        
        playEnabled=0;
        if(playersRow == -1){
            //full column
            playEnabled=1;
            return;
        }
        else{
            timer = new Timer(1000, new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(timerFlag==1){
                        changeLabelColor(redIcon, playersRow, pos);
                        res = minmax.MinMaxPruning_help(res1, 0, depth, "AI", -Integer.MAX_VALUE, Integer.MAX_VALUE, table, rows, cols, board);    
                        row = board.setPlayerPosition(rows, cols, res[2], 'O', table);
                        timerFlag=0;
                        timerFlag1=1;

                        move.setPlayer("YOU");
                        move.setMove(playersRow, pos);
                        move.addMoves(move);
                        if(board.calcPoints("YOU", rows, cols, table, board) >= 10000){
                            JOptionPane.showMessageDialog(frame, "You won!", "GameOver", JOptionPane.WARNING_MESSAGE);
                            playEnabled = 0;
                            addToHistory(" W: YOU", level);
                            timer.stop();
                            return;
                        }
                        if(row == -1 && board.fullBoard(table) == 1){
                            JOptionPane.showMessageDialog(frame, "Draw!", "GameOver", JOptionPane.WARNING_MESSAGE);
                            playEnabled = 0;
                            addToHistory(" W: Draw", level);
                            timer.stop();
                            return;
                        }
                        timer.stop();
                        timer.start();
                    }
                    else if(timerFlag1 == 1){
                        changeLabelColor(ByellowIcon, row, res[2]);
                        timerFlag1=0;
                        timerFlag2=1;
                        timer.stop();
                        timer.start();
                    }
                    else if(timerFlag2 == 1){
                        changeLabelColor(yellowIcon, row, res[2]);
                        timerFlag2=0;
                        int score = board.calcPoints("AI", rows, cols, table, board);
                        //save the move to add it to history file later
                        move.setPlayer("AI");
                        move.setMove(row, res[2]);
                        move.addMoves(move);
                        timerFlag2=0;
                        timer.stop();
                        if(score>=10000){
                            JOptionPane.showMessageDialog(frame, "You lost!", "GameOver", JOptionPane.WARNING_MESSAGE);
                            playEnabled = 0;
                            addToHistory(" W: AI", level);
                            return;
                        }
        
                        if(board.fullBoard(table) == 1){
                            JOptionPane.showMessageDialog(frame, "Draw!", "GameOver", JOptionPane.WARNING_MESSAGE);
                            playEnabled = 0;
                            addToHistory(" W: Draw", level);
                            return;
                        }
                        playEnabled=1;
                    }
                }
            });
            //start the Timer
            timerFlag=1;
            timer.start();
            if(timerFlag == 1){
                changeLabelColor(BredIcon, playersRow, pos);
            }
        }
        return;
    }

    //function that changes the label's (disc) color
    public void changeLabelColor(ImageIcon icon, int i, int j){
        Image redImage = icon.getImage().getScaledInstance(85, 85, Image.SCALE_DEFAULT);
        icon = new ImageIcon(redImage);
        lab[i][j].setIcon(icon);
    }

    //these functions are not used
    public void itemStateChanged(ItemEvent e){}
    public void mousePressed(MouseEvent ev){}
    public void mouseReleased(MouseEvent ev){}
    public void mouseEntered(MouseEvent ev){}
    public void mouseExited(MouseEvent ev){}
    public void keyTyped(KeyEvent k){}
    public void keyPressed(KeyEvent k){}    
}
