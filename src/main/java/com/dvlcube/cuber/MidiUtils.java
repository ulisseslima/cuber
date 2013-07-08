package com.dvlcube.cuber;

import static com.dvlcube.cuber.Cuber.$;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.dvlcube.cuber.MidiUtils.Note.Modifier;

/**
 * 
 * @since 06/07/2013
 * @author wonka
 */
public class MidiUtils {
	public static final int DAMPER_PEDAL = 64;
	public static final int DAMPER_ON = 127;
	public static final int DAMPER_OFF = 0;
	public static final int END_OF_TRACK = 47;
	private static Random random = new Random();
	/**
	 * add these amounts to the base value <br>
	 * A B C D E F G
	 */
	static final int[] offsets = { -4, -2, 0, 1, 3, 5, 7 };

	public enum Note {
		A, B, C, D, E, F, G;
		private static Random random = new Random();

		public enum Modifier {
			OCTAVE_INCREASE("+"),
			OCTAVE_DECREASE("-"),
			FLAT("b"),
			SHARP("#"),
			WHOLE("/1"),
			HALF("/2"),
			QUARTER("/4"),
			EIGHTH("/8"),
			SIXTEENTH("/16"),
			THIRTY_SECOND("/32"),
			SIXTY_FOURTH("/64"),
			SUSTAIN("S"),
			LOUDER(">"),
			SOFTER("<"),
			REST("."),
			PLAY(" ");
			private final String token;

			Modifier(String token) {
				this.token = token;
			}

			/**
			 * @return the token
			 */
			public String getToken() {
				return token;
			}

			public static Modifier random() {
				return values()[random.nextInt(values().length)];
			}

			public boolean isFraction() {
				return $(this).in(WHOLE, HALF, QUARTER, EIGHTH, SIXTEENTH, THIRTY_SECOND, SIXTY_FOURTH);
			}

			/**
			 * @return
			 * @since 07/07/2013
			 * @author wonka
			 */
			public Modifier previous() {
				if (this == WHOLE)
					return SIXTY_FOURTH;

				if (this == Modifier.SIXTY_FOURTH)
					return WHOLE;

				return values()[ordinal() - 1];
			}
		}

		/**
		 * @return
		 * @since 07/07/2013
		 * @author wonka
		 */
		public static Note random() {
			return values()[random.nextInt(values().length)];
		}
	}

	/**
	 * This method parses the specified char[] of notes into a Track. The musical notation is the following:
	 * <p>
	 * A-G: A named note; Add b for flat and # for sharp. <br>
	 * +: Move up one octave. Persists. <br>
	 * -: Move down one octave. Persists. <br>
	 * /1: Notes are whole notes. Persists 'till changed <br>
	 * /2: Half notes <br>
	 * /4: Quarter notes <br>
	 * /n: N can also be 8, 16, 32, 64. <br>
	 * s: Toggle sustain pedal on or off (initially off)<br>
	 * >: Louder. Persists <br>
	 * <: Softer. Persists <br>
	 * .: Rest. Length depends on current length setting <br>
	 * Space: Play the previous note or notes; notes not separated by spaces are played at the same time
	 * (chords)
	 */
	public static void addTrack(Sequence sequence, int instrument, int tempo, char[] notes) {
		try {
			Track track = sequence.createTrack(); // Begin with a new track

			// Set the instrument on channel 0
			ShortMessage message = new ShortMessage();
			message.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
			track.add(new MidiEvent(message, 0));

			int n = 0; // current character in notes[] array
			int ticks = 0; // time in ticks for the composition

			// These values persist and apply to all notes 'till changed
			int notelength = 16; // default to quarter notes
			int velocity = 64; // default to middle volume
			int basekey = 60; // 60 is middle C. Adjusted up and down by octave
			boolean sustain = false; // is the sustain pedal depressed?
			int numnotes = 0; // How many notes in current chord?

			while (n < notes.length) {
				char c = notes[n++];

				if (c == '+')
					basekey += 12; // increase octave
				else if (c == '-')
					basekey -= 12; // decrease octave
				else if (c == '>')
					velocity += 16; // increase volume;
				else if (c == '<')
					velocity -= 16; // decrease volume;
				else if (c == '/') {
					char d = notes[n++];
					if (d == '2')
						notelength = 32; // half note
					else if (d == '4')
						notelength = 16; // quarter note
					else if (d == '8')
						notelength = 8; // eighth note
					else if (d == '3' && notes[n++] == '2')
						notelength = 2;
					else if (d == '6' && notes[n++] == '4')
						notelength = 1;
					else if (d == '1') {
						if (n < notes.length && notes[n] == '6')
							notelength = 4; // 1/16th note
						else
							notelength = 64; // whole note
					}
				} else if (c == 's') {
					sustain = !sustain;
					// Change the sustain setting for channel 0
					ShortMessage m = new ShortMessage();
					m.setMessage(ShortMessage.CONTROL_CHANGE, 0, MidiUtils.DAMPER_PEDAL, sustain ? MidiUtils.DAMPER_ON
							: MidiUtils.DAMPER_OFF);
					track.add(new MidiEvent(m, ticks));
				} else if (c >= 'A' && c <= 'G') {
					int key = basekey + offsets[c - 'A'];
					if (n < notes.length) {
						if (notes[n] == 'b') { // flat
							key--;
							n++;
						} else if (notes[n] == '#') { // sharp
							key++;
							n++;
						}
					}

					addNote(track, ticks, notelength, key, velocity);
					numnotes++;
				} else if (c == ' ') {
					// Spaces separate groups of notes played at the same time.
					// But we ignore them unless they follow a note or notes.
					if (numnotes > 0) {
						ticks += notelength;
						numnotes = 0;
					}
				} else if (c == '.') {
					// Rests are like spaces in that they force any previous
					// note to be output (since they are never part of chords)
					if (numnotes > 0) {
						ticks += notelength;
						numnotes = 0;
					}
					// Now add additional rest time
					ticks += notelength;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a note to the track on channel 0
	 * 
	 * @param track
	 * @param startTick
	 * @param tickLength
	 * @param key
	 * @param velocity
	 * @throws InvalidMidiDataException
	 * @since 06/07/2013
	 * @author wonka
	 */
	public static void addNote(Track track, int startTick, int tickLength, int key, int velocity)
			throws InvalidMidiDataException {
		ShortMessage on = new ShortMessage();
		on.setMessage(ShortMessage.NOTE_ON, 0, key, velocity);
		ShortMessage off = new ShortMessage();
		off.setMessage(ShortMessage.NOTE_OFF, 0, key, velocity);
		track.add(new MidiEvent(on, startTick));
		track.add(new MidiEvent(off, startTick + tickLength));
	}

	/**
	 * @return random notes.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static String randomNotes(int length) {
		StringBuilder builder = new StringBuilder();
		String lastModifier = "";
		String modifier = "";
		MidiUtils.Note.Modifier lastFraction = MidiUtils.Note.Modifier.WHOLE;
		MidiUtils.Note.Modifier fraction = MidiUtils.Note.Modifier.WHOLE;
		String playMod = MidiUtils.Note.Modifier.PLAY.getToken();
		for (int i = 0; i < length; i++) {
			int r = random.nextInt(7);
			if (r < 1) {
				Modifier randomMod = MidiUtils.Note.Modifier.random();
				if (randomMod.isFraction()) {
					if (randomMod == lastFraction) {
						randomMod = randomMod.previous();
					}
					lastFraction = fraction;
					fraction = randomMod;
				}
				modifier = randomMod.getToken();
			} else if (r > 3 && !lastModifier.equals(playMod)) {
				modifier = playMod;
			} else {
				modifier = MidiUtils.Note.random().name();
			}

			builder.append(modifier);
			lastModifier = modifier;
		}
		return builder.toString();
	}

	public static String randomNotes() {
		return randomNotes(random.nextInt(200));
	}

	/**
	 * Writes a sequence to disc using the first available MIDI type.
	 * 
	 * @param sequence
	 *            the sequence to save.
	 * @return true if the sequence was written to disc.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static boolean write(Sequence sequence, String filename) {
		if (sequence == null)
			throw new IllegalArgumentException("sequence is null");

		int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
		if (allowedTypes.length == 0) {
			System.err.println("No supported MIDI file types.");
		} else {
			try {
				MidiSystem.write(sequence, allowedTypes[0], new File(filename));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @param file
	 *            file name;
	 * @return the sequence represented by the midi file.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static Sequence read(File file) {
		try {
			return MidiSystem.getSequence(file);
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("not a file: '" + file + "'");
	}
}
