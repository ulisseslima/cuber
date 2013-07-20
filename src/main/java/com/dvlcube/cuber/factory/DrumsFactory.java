package com.dvlcube.cuber.factory;

import static com.dvlcube.cuber.Cuber.$;
import static com.dvlcube.cuber.Cuber.$midi;
import static com.dvlcube.cuber.Range.$range;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * http://www.onjava.com/pub/a/onjava/excerpt/jenut3_ch17/index1.html?page=2
 * <p>
 * This program the MIDI percussion channel with a Swing window. It monitors keystrokes and mouse motion in
 * the window and uses them to create music. Keycodes between 35 and 81, inclusive, generate different
 * percussive sounds. See the VK_ constants in java.awt.event.KeyEvent, or just experiment. Mouse position
 * controls volume: move the mouse to the right of the window to increase the volume.
 */
public class DrumsFactory extends JFrame {
	public static class Note {
		public static class Crash {
			public static final Integer[] key = { 46, 49, 51, 52, 55, 57, 59 };
			public static final String token = "-";
		}

		public static class Hat {
			public static final Integer[] key = { 42, 44, 53, 54, 67, 68, 69, 70, 80, 81, 82 };
			public static final String token = "h";
		}

		public static class High {
			public static final Integer[] key = { 37, 48, 50, 56, 60, 61, 62, 63, 64, 65, 66, 76, 77 };
			public static final String token = "^";
		}

		public static class Low {
			public static final Integer[] key = { 35, 36, 41, 43, 45, 47 };
			public static final String token = "o";
		}

		public static class Misc {
			public static final Integer[] key = { 39, 58, 71, 72, 73, 74, 75, 78, 79 };
			public static final String token = "m";
		}

		public static class Snare {
			public static final Integer[] key = { 38, 40 };
			public static final String token = "-";
		}
	}

	public static final String AMEN_BREAK = "/8i35 i35 i38 /16i35 i38 i35 i38 /8i35 i38.";
	public static final String BEAT = "i35 i38 /8i35 i35 /4i38";

	public static final int CHANNEL = 9;
	public static int NOTE_END = 82;
	public static int NOTE_START = 35;
	private static Random random = new Random();

	private static final long serialVersionUID = 2863911930801987115L;

	public static void addNote(Track track, int startTick, int tickLength, int key, int velocity)
			throws InvalidMidiDataException {
		int drumPiece = random.nextInt(NOTE_END - NOTE_START) + NOTE_START;
		ShortMessage onD = new ShortMessage();

		onD.setMessage(ShortMessage.NOTE_ON, CHANNEL, drumPiece, 127);
		ShortMessage offD = new ShortMessage();
		offD.setMessage(ShortMessage.NOTE_OFF, CHANNEL, drumPiece, 127);
		track.add(new MidiEvent(onD, startTick));
		track.add(new MidiEvent(offD, startTick + tickLength));
	}

	/**
	 * We don't need a Sequencer in this example, since we send MIDI events directly to the Synthesizer
	 * instead.
	 * 
	 * @throws MidiUnavailableException
	 * @author wonka
	 * @since 13/07/2013
	 */
	private static void createDrums() throws MidiUnavailableException {
		Synthesizer synthesizer = openSynthesizer();
		JFrame frame = new DrumsFactory(synthesizer);
		swingStuff(frame);
	}

	/**
	 * Creates a frame that captures keystrokes and transforms them into drums notes. The mouse x position on
	 * the frame controls the velocity (which is basically volume).
	 * 
	 * @param args
	 * @throws MidiUnavailableException
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			createDrums();
		} else {
			randomizeDrums();
		}
	}

	/**
	 * @return
	 * @throws MidiUnavailableException
	 * @author wonka
	 * @since 13/07/2013
	 */
	private static Synthesizer openSynthesizer() throws MidiUnavailableException {
		Synthesizer synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		return synthesizer;
	}

	public static int randomCrash() {
		Integer[] crashes = $(Note.Crash.key).o;
		int i = random.nextInt(crashes.length);
		return crashes[i];
	}

	/**
	 * 
	 * @author wonka
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 * @since 13/07/2013
	 */
	private static void randomizeDrums() throws MidiUnavailableException, InvalidMidiDataException {
		$midi().play();
	}

	/**
	 * @return random note from lead parts of the drums. (i.e. bass drums, snares...)
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static int randomLead() {
		Integer[] leads = $(Note.Snare.key, Note.High.key, Note.Low.key).o;
		int i = random.nextInt(leads.length);
		return leads[i];
	}

	public static int randomNote() {
		return random.nextInt(NOTE_END - NOTE_START) + NOTE_START;
	}

	/**
	 * @return random note from rhythm parts of the drums. (i.e. hats, misc...)
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static int randomRhythm() {
		Integer[] rhythms = $(Note.Hat.key, Note.Misc.key).o;
		int i = random.nextInt(rhythms.length);
		return rhythms[i];
	}

	/**
	 * @param frame
	 * @author wonka
	 * @since 13/07/2013
	 */
	private static void swingStuff(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.pack();
		frame.setVisible(true);
	}

	MidiChannel channel; // The channel we play on: 10 is for percussion
	int velocity = 64; // Default volume is 50%

	public DrumsFactory(Synthesizer synth) {
		super("Drums");

		// Channel 10 is the GeneralMidi percussion channel. In Java code, we
		// number channels from 0 and use channel 9 instead.
		channel = synth.getChannels()[9];

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key >= 35 && key <= 81) {
					channel.noteOn(key, velocity);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key >= 35 && key <= 81) {
					channel.noteOff(key);
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// We use window width as volume control
				velocity = $(e.getX(), $range(0d, 127d)).i();
			}
		});

		for (int i = NOTE_START; i <= NOTE_END; i++) {
			final JButton button = new JButton(String.valueOf(i));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Integer note = Integer.valueOf(button.getText());
					channel.noteOn(note, velocity);
					channel.noteOff(note);
				}
			});
			this.add(button);
		}
	}
}