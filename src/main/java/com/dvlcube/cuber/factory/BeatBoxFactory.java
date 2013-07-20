package com.dvlcube.cuber.factory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * An example from Head First Java.
 * 
 * @author Wonka
 */
public class BeatBoxFactory {

	private class MyClearListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			for (JCheckBox checkbox : checkboxList) {
				checkbox.setSelected(false);
			}
			status("Sequence reset.");
		}
	}

	public class MyDownTempoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * .97));
			status("Tempo factor: " + sequencer.getTempoFactor());
		}
	}

	/**
	 * This is pretty much the save in reverse... read the boolean array and use it to restore the state of
	 * the GUI checkboxes. It all happens when the user hits the "restore" button.
	 */
	public class MyReadListener implements ActionListener {

		/**
		 * Read the single object in the file (the boolean array) and cast it back to a boolean array
		 * (remember, readObject() returns a reference of type Object.
		 * 
		 * @param a
		 */
		@Override
		public void actionPerformed(ActionEvent a) {
			status("Loading Sequence...");

			explorer.showOpenDialog(frame);
			File selectedFile = explorer.getSelectedFile();

			if (selectedFile != null) {
				boolean[] checkboxState = null;
				try {
					FileInputStream fileIn = new FileInputStream(selectedFile);
					ObjectInputStream is = new ObjectInputStream(fileIn);
					checkboxState = (boolean[]) is.readObject();
					is.close();
				} catch (IOException | ClassNotFoundException e) {
					status(e.getMessage());
				}

				/**
				 * Now restore the state of each of the checkboxes in the array list of actual JCheckBox
				 * objects (checkboxList).
				 */
				for (int i = 0; i < 256; i++) {
					JCheckBox check = checkboxList.get(i);
					if (checkboxState[i]) {
						check.setSelected(true);
					} else {
						check.setSelected(false);
					}
				}

				/**
				 * Now stop whatever is currently playing, and rebuild the sequence using the new state of the
				 * checkboxes in the arraylist.
				 */
				sequencer.stop();
				buildTrackAndStart();
				status("Sequence loaded.");
			} else {
				status("No file was selected.");
			}
		}
	}

	/**
	 * Saves a sequence.
	 */
	public class MySendListener implements ActionListener {

		/**
		 * It all happens when the user clicks the button and the actionEvent fires.
		 * 
		 * @param a
		 *            Event sent by the save button.
		 */
		@Override
		public void actionPerformed(ActionEvent a) {
			status("Serializing...");

			explorer.showSaveDialog(frame);
			File selectedFile = explorer.getSelectedFile();

			if (selectedFile != null) {
				/**
				 * Make a boolean array to hold the state of each checkbox.
				 */
				boolean[] checkboxState = new boolean[256];

				/**
				 * Walk through the checkboxList (ArrayList of checkboxes), and get the state of each one, and
				 * add it to the boolean array.
				 */
				for (int i = 0; i < 256; i++) {
					JCheckBox check = checkboxList.get(i);
					if (check.isSelected()) {
						checkboxState[i] = true;
					}
				}

				/**
				 * This part's just a piece of cake. Just write/serialize the one boolean array!
				 */
				try {
					FileOutputStream fileStream = new FileOutputStream(selectedFile);
					ObjectOutputStream os = new ObjectOutputStream(fileStream);
					os.writeObject(checkboxState);
					os.close();
					status("Sequence serialized.");
				} catch (Exception e) {
					status("Error serializing: " + e.getMessage());
				}
			} else {
				status("Pattern was not saved.");
			}
		}
	}

	public class MyStartListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent a) {
			buildTrackAndStart();
			status("Started");
		}
	}

	public class MyStopListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent a) {
			sequencer.stop();
			status("Stopped");
		}
	}

	public class MyUpTempoListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent a) {
			/**
			 * The Tempo Factor scales the sequencer's tempo by the factor provided. The default is 1.0, so
			 * we're adjusting +/-3% per click.
			 */
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 1.03));
			status("Tempo factor: " + sequencer.getTempoFactor());
		}
	}

	public static void main(String[] args) {
		new BeatBoxFactory().buildGUI();
	}

	ArrayList<JCheckBox> checkboxList;
	JFileChooser explorer = new JFileChooser();
	JFrame frame;

	/**
	 * These are the names of the instruments, as a String array, for building the GUI labels (on each row)
	 */
	String[] instrumentNames = { "Bass Drum", //
			"Closed Hi-Hat", //
			"Open Hi-Hat",//
			"Acoustic Snare", //
			"Crash Cymbal",//
			"Hand Clap", //
			"High Tom", //
			"Hi Bongo", //
			"Maracas", //
			"Whistle", //
			"Low Conga", //
			"Cowbell", //
			"Vibraslap", //
			"Low-mid Tom", //
			"High Agogo", //
			"Open Hi Conga" };

	/**
	 * These represent the actual drum "keys". the drum channel is like a piano except each "key" on the piano
	 * is a different drum. So the number 35 is the key for the Bass drum, 42 is closed hi-hat, etc.
	 */
	int[] instruments = { 35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63 };
	JPanel mainPanel;
	Sequence sequence;
	Sequencer sequencer;
	JLabel statusLabel = new JLabel("Started.");
	Track track;

	public void buildGUI() {
		frame = new JFrame("Cyber BeatBox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			status(e.getMessage());
		}

		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		/**
		 * An "empty border" gives us a margin between the edges of the panel and where the components are
		 * placed. Purely asthetic.
		 */
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		checkboxList = new ArrayList<>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);

		JButton start = new JButton("Start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);

		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);

		JButton upTempo = new JButton("Tempo Up");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);

		JButton downTempo = new JButton("Tempo Down");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);

		JButton serialize = new JButton("Serialize");
		serialize.addActionListener(new MySendListener());
		buttonBox.add(serialize);

		JButton restore = new JButton("Restore");
		restore.addActionListener(new MyReadListener());
		buttonBox.add(restore);

		JButton clear = new JButton("Clear");
		clear.addActionListener(new MyClearListener());
		buttonBox.add(clear);

		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for (int i = 0; i < 16; i++) {
			nameBox.add(new Label(instrumentNames[i]));
		}

		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);

		frame.getContentPane().add(background);

		GridLayout grid = new GridLayout(16, 16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);

		/**
		 * Make the checkboxes, set them to "false" (so they aren't checked) and add them to the ArrayList AND
		 * to the GUI panel.
		 */
		for (int i = 0; i < 256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
		}
		background.add(BorderLayout.SOUTH, statusLabel);

		setUpMidi();

		frame.setBounds(50, 50, 300, 300);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * This is where it all happens! Where we turn checkbox state into MIDI events, and add them to the Track.
	 */
	public void buildTrackAndStart() {
		/**
		 * we'll make a 16-element array to hold the values for one instrument, across all 16 beats. If the
		 * instrument is supposed to play on that beat, the value at that element will be the key. If that
		 * instrument is NOT supposed to play on that beat, put in a zero
		 */
		int[] trackList = null;

		sequence.deleteTrack(track); // get rid of the old track,
		track = sequence.createTrack(); // make a fresh one

		/**
		 * Do this for each of the 16 rows (i.e. Bass, Congo, etc.)
		 */
		for (int i = 0; i < 16; i++) {
			trackList = new int[16];

			/**
			 * Set the "key" that represents which instrument this is (Bass, Hi-Hat, etc. The instruments
			 * array holds the actual MIDI numbers for each instrument.)
			 */
			int key = instruments[i];

			/**
			 * Do this for each of the BEATS for this row
			 */
			for (int j = 0; j < 16; j++) {
				JCheckBox jc = checkboxList.get(j + 16 * i);

				/**
				 * is the checkbox at this beat selected? If yes, put the key value in this slot in the array
				 * (the slot that represents this beat). Otherwise, the instrument is NOT supposed to play at
				 * this beat, so set it to zero.
				 */
				if (jc.isSelected()) {
					trackList[j] = key;
				} else {
					trackList[j] = 0;
				}
			}

			/**
			 * for this instrument, and for all 16 beats, make events and add them to the track
			 */
			makeTracks(trackList);
			int waitBeforeLooping = 16;
			track.add(makeEvent(ShortMessage.CONTROL_CHANGE, 1, 127, 0, waitBeforeLooping));
		}

		/**
		 * We always want to make sure that there IS an event at beat 16 (it goes 0 to 15). Otherwise, the
		 * BeatBox might not go the full 16 beats before it starts over.
		 */
		track.add(makeEvent(ShortMessage.PROGRAM_CHANGE, 9, 1, 0, 15));
		try {
			sequencer.setSequence(sequence);
			/**
			 * Let's you specify the number of loop iterations, or in this case, continuous looping.
			 */
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start(); // now play the thing!
			sequencer.setTempoInBPM(120);
			status("Done building track and starting.");
		} catch (Exception e) {
			status(e.getMessage());
		}
	}

	/**
	 * This is the utility method from last chapter's Code Kitchen. Nothing new.
	 * 
	 * @param comd
	 * @param chan
	 * @param one
	 * @param two
	 * @param tick
	 * @return
	 */
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage msg = new ShortMessage();
			msg.setMessage(comd, chan, one, two);
			event = new MidiEvent(msg, tick);
		} catch (Exception e) {
			status(e.getMessage());
		}
		return event;
	}

	/**
	 * This makes events for one instrument at a time, for all 16 beats. So it might get an int[] for the Bass
	 * drum, and each index in the array will hold either the key of that instrument, or a zero. If it's a
	 * zero, the instrument isn't supposed to play at that beat. Otherwise, make an event and add it to the
	 * track.
	 * 
	 * @param list
	 */
	public void makeTracks(int[] list) {
		for (int tick = 0; tick < 16; tick++) {
			int key = list[tick];

			if (key != 0) {
				/**
				 * Make the NOTE ON and NOTE OFF events, and add them to the Track.
				 */
				track.add(makeEvent(144, 9, key, 100, tick));
				track.add(makeEvent(128, 9, key, 100, tick + 1));
			}
		}
	}

	/**
	 * The usual MIDI setup stuff for getting the sequencer, the Sequence, and the Track. Again. nothing
	 * special.
	 */
	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
			status("Done setting up MIDI");
		} catch (MidiUnavailableException | InvalidMidiDataException e) {
			status(e.getMessage());
		}
	}

	/**
	 * Updates the status label.
	 * 
	 * @param status
	 *            What's going on.
	 */
	public void status(String status) {
		statusLabel.setText(status);
	}
}
