package life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class View {
	public static final int DEFAULT_SPEED = 2000;
	public static final Color DEFAULT_COLOR = Color.GRAY;
	public static final Color RED = Color.RED;
	public static final Color GREEN = Color.green;

	private Model model;
	private JFrame frame;
	private JButton clear;

	private JButton step;
	private JButton run;
	private JButton stop;
	private JSlider slider;
	private JLabel timerLabel;
	private JButton [] [] gridView;
	private JPanel gridPanel ;
	private JPanel buttons;

	private ButtonListener bListener;
	private GridListener gridListener;
	private TimerListener timerListener;
	private ChangeValue sliderListener;

	private int speed;
	private int rows;

	private Timer timer;



	private void initialise(){

		//gridView = new GridButton[rows][rows];

		gridView = new JButton[rows][rows];
		frame = new JFrame();
		clear = new JButton("Clear");
		step = new JButton("Step");
		run = new JButton("Run");
		stop = new JButton("Stop");
		timerLabel = new JLabel("0", SwingConstants.CENTER);
		frame.setTitle("Game of Life");


		slider = new JSlider(JSlider.VERTICAL,1,10,1);
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));



		speed = DEFAULT_SPEED;
		gridPanel = new JPanel(new GridLayout(rows, rows));
		buttons = new JPanel(new GridLayout(1, 5));

		buttons.add(clear);
		buttons.add(step);
		buttons.add(run);
		buttons.add(stop);


		gridListener = new GridListener();
		timerListener = new TimerListener();

		bListener = new ButtonListener();
		step.addActionListener(bListener);
		run.addActionListener(bListener);
		clear.addActionListener(bListener);
		stop.addActionListener(bListener);

		timer = new Timer(speed, timerListener);
		sliderListener = new ChangeValue();
		slider.addChangeListener(sliderListener);

		initialiseBoard();

		for (int i = 0; i <rows; i++){
			for (int j = 0; j < rows; j++){
				gridPanel.add(gridView[i][j]);
			}
		}

		frame.setSize(600, 600);
		frame.add(timerLabel, BorderLayout.NORTH);
		frame.add(buttons, BorderLayout.SOUTH);
		frame.add(gridPanel, BorderLayout.CENTER);
		frame.add(slider, BorderLayout.EAST);
		frame.setVisible(true);

	}

	public void updateModel(){
		for (int i = 0; i< rows; i++){
			for (int j = 0; j< rows; j++){
				model.setColor(i, j, gridView[i][j].getBackground());
			}
		}
	}

	private void updateTime(){
		timerLabel.setText(Integer.toString(model.getTime()));
	}

	public View(Model _model) {
		//Setting up the view initially
		rows  = _model.getRows();
		model = _model;
		initialise();
	}



	public void changeSpeed(int _speed){
		speed = _speed;
	}

	private int convertSpeed(int _value){
		return 2000/_value;
	}

	public void initialiseBoard(){
		for (int i = 0; i< rows; i++){
			for (int j = 0; j < rows; j++){
				gridView[i][j] = new JButton();
				//new GridButton(i, j);
				gridView[i][j].setBackground(model.getColor(i, j));
				gridView[i][j].addMouseListener(gridListener);
			}
		}
	}


	public void clearBoard(){
		model.clearGrid();
		updateView();
	}

	public void step(){
		model.step();
		updateView();

	}


	//Changes the view according to how the model has changed
	public void updateView(){
		for (int i = 0; i< rows; i++){
			for (int j = 0; j <rows; j++){
				gridView[i][j].setBackground(model.getColor(i, j));
			}
		}
		updateTime();
	}

	public void changeColor(int x, int y, Color c){
		model.setColor(x, y, c);

	}

	class TimerListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			step();

		}

	}

	class ChangeValue implements ChangeListener{

		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			if (!source.getValueIsAdjusting()) {
				speed = convertSpeed((int)source.getValue());

				if (run.getText().equals("Pause")){
					timer.setDelay(speed);
				}
			}
		}

	}

	private void enableButtons(){
		stop.setEnabled(true);
		step.setEnabled(true);
		clear.setEnabled(true);
	}

	private void disableButtons(){
		stop.setEnabled(false);
		step.setEnabled(false);
		clear.setEnabled(false);
	}

	private void exitView(){
		System.exit(0);
	}

	class GridListener extends MouseAdapter{

		public void mouseClicked(MouseEvent e) {
			if (!isRunning()){
				JButton button = (JButton) e.getSource();
				if (SwingUtilities.isLeftMouseButton(e)){
					button.setBackground(RED);
				}
				else if (SwingUtilities.isRightMouseButton(e)){
					button.setBackground(GREEN);
				}
				updateModel();
			}
		}

	}

	public boolean isRunning(){
		return timer.isRunning();
	}


	class RunView extends TimerTask{

		public void run() {
			step();		
		}

	}

	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			JButton sent = (JButton) e.getSource();
			String label = sent.getText();

			if (label.equals("Step")){
				step();
			}
			else if (label.equals("Pause")){
				sent.setText("Run");
				timer.stop();
				enableButtons();
				return;

			}
			else if(label.equals("Run")){
				timer.start();
				sent.setText("Pause");
				disableButtons();
				return;
			}

			else if (label.equals("Stop")){
				exitView();
			}
			else if (label.equals("Clear")){
				model.clearGrid();
				updateView();
				timer.stop();
			}


		}
	}
}
