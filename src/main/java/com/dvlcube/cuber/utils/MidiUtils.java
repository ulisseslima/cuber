/*
  This file is part of Cuber.

    Cuber is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Cuber is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Cuber.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dvlcube.cuber.utils;

import static com.dvlcube.cuber.Cuber.$;
import static com.dvlcube.cuber.Range.$range;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.jfugue.examples.Midi2WavRenderer;

import com.dvlcube.cuber.Range;
import com.dvlcube.cuber.factory.DrumsFactory;
import com.dvlcube.cuber.utils.MidiUtils.Note.Modifier;

/**
 * http://www.onjava.com/pub/a/onjava/excerpt/jenut3_ch17/index1.html
 * 
 * @since 06/07/2013
 * @author wonka
 */
public class MidiUtils {
	/**
	 * @author wonka
	 * @since 20/07/2013
	 */
	public enum Channel {
		DEFAULT(0), DRUMS(9);
		public static Channel parse(String n) {
			for (Channel channel : values()) {
				if (channel.string.equals(n)) {
					return channel;
				}
			}
			return null;
		}

		public int number;

		public String string;

		/**
		 * @param number
		 * @author wonka
		 * @since 20/07/2013
		 */
		Channel(int number) {
			this.number = number;
			string = String.valueOf(number);
		}
	}

	public static class Key {
		public static Key $(int note, int velocity) {
			return new Key(note, velocity);
		}

		public int note;
		public int velocity;

		/**
		 * @param note
		 * @param velocity
		 * @author wonka
		 * @since 13/07/2013
		 */
		public Key(int note, int velocity) {
			this.note = note;
			this.velocity = velocity;
		}
	}

	public enum Note {
		A, B, C, D, E, F, G;
		/**
		 * The musical notation is the following:
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
		 * 
		 * @author wonka
		 * @since 13/07/2013
		 */
		public enum Modifier {
			EIGHTH("/8"), //
			FLAT("b"), //
			HALF("/2"), //
			LOUDER(">"), //
			OCTAVE_DECREASE("-"), //
			OCTAVE_INCREASE("+"), //
			PLAY(" "), //
			QUARTER("/4"), //
			REST("."), //
			SHARP("#"), //
			SIXTEENTH("/16"), //
			SIXTY_FOURTH("/64"), //
			SOFTER("<"), //
			SUSTAIN("s"), //
			THIRTY_SECOND("/32"), //
			WHOLE("/1");
			public static Modifier random() {
				return values()[random.nextInt(values().length)];
			}

			private final String token;

			Modifier(String token) {
				this.token = token;
			}

			public char c() {
				return token.toCharArray()[0];
			}

			/**
			 * @return the token
			 */
			public String getToken() {
				return token;
			}

			public boolean isFraction() {
				return $(this).in(WHOLE, HALF, QUARTER, EIGHTH, SIXTEENTH, THIRTY_SECOND, SIXTY_FOURTH);
			}

			public boolean isSlowFraction() {
				return $(this).in(WHOLE, HALF, QUARTER);
			}

			/**
			 * @return the next modifier.
			 * @author wonka
			 * @since 14/07/2013
			 */
			public Modifier next() {
				if (this == QUARTER) {
					return SIXTY_FOURTH;
				}

				if (this == SIXTY_FOURTH) {
					return QUARTER;
				}

				int next = ordinal() + 1;
				if (next > values().length) {
					next = values().length - 1;
				}
				return values()[next];
			}

			/**
			 * @return 1 of the octave was increased, -1 if the octave was decreased and 0 if there was no
			 *         change in the octave.
			 * @author wonka
			 * @since 13/07/2013
			 */
			public int octaveCheck() {
				if (this == OCTAVE_DECREASE) {
					return -1;
				}
				if (this == OCTAVE_INCREASE) {
					return 1;
				}
				return 0;
			}

			/**
			 * @return previous modifier.
			 * @since 07/07/2013
			 * @author wonka
			 */
			public Modifier previous() {
				if (this == WHOLE) {
					return SIXTY_FOURTH;
				}

				if (this == Modifier.SIXTY_FOURTH) {
					return WHOLE;
				}

				int previous = ordinal() - 1;
				if (previous < 0) {
					previous = 0;
				}
				return values()[previous];
			}
		}

		private static Random random = new Random();

		/**
		 * @return
		 * @since 07/07/2013
		 * @author wonka
		 */
		public static Note random() {
			return values()[random.nextInt(values().length)];
		}
	}

	public static class Tick {
		public static Tick $(int start, int length) {
			return new Tick(start, length);
		}

		public int length;
		public int start;

		/**
		 * @param start
		 * @param length
		 * @author wonka
		 * @since 13/07/2013
		 */
		public Tick(int start, int length) {
			this.start = start;
			this.length = length;
		}
	}

	public static final int DAMPER_OFF = 0;
	public static final int DAMPER_ON = 127;
	public static final int DAMPER_PEDAL = 64;
	public static final int END_OF_TRACK = 47;
	/**
	 * add these amounts to the base value <br>
	 * A B C D E F G
	 */
	static final int[] offsets = { -4, -2, 0, 1, 3, 5, 7 };
	private static Random random = new Random();

	/**
	 * @param sequence
	 * @param instrument
	 * @param notes
	 * @param channel
	 * @author wonka
	 * @since 20/07/2013
	 */
	private static void addDefaultTrack(Sequence sequence, int instrument, char[] notes, Channel channel) {
		try {
			Track track = sequence.createTrack(); // Begin with a new track

			// Set the instrument on the channel
			ShortMessage message = changeInstrument(instrument, channel);
			track.add(new MidiEvent(message, 0));

			int n = 0; // current character in notes[] array
			int ticks = 0; // time in ticks for the composition

			// These values persist and apply to all notes 'till changed
			int notelength = 16; // default to quarter notes
			int velocity = 64; // default to middle volume
			int basekey = 60; // 60 is middle C. Adjusted up and down by octave
			boolean sustain = false; // is the sustain pedal depressed?
			int numNotes = 0; // How many notes in current chord?

			while (n < notes.length) {
				char c = notes[n++];

				if (c == Modifier.OCTAVE_INCREASE.c()) {
					basekey += 12; // increase octave
				} else if (c == Modifier.OCTAVE_DECREASE.c()) {
					basekey -= 12; // decrease octave
				} else if (c == Modifier.LOUDER.c()) {
					velocity += 16; // increase volume;
				} else if (c == Modifier.SOFTER.c()) {
					velocity -= 16; // decrease volume;
				} else if (c == '/') {
					char d = notes[n++];
					if (d == '2') {
						notelength = 32; // half note
					} else if (d == '4') {
						notelength = 16; // quarter note
					} else if (d == '8') {
						notelength = 8; // eighth note
					} else if (d == '3' && notes[n++] == '2') {
						notelength = 2;
					} else if (d == '6' && notes[n++] == '4') {
						notelength = 1;
					} else if (d == '1') {
						if (n < notes.length && notes[n] == '6') {
							notelength = 4; // 1/16th note
						} else {
							notelength = 64; // whole note
						}
					}
				} else if (c == Modifier.OCTAVE_INCREASE.c()) {
					sustain = !sustain;
					// Change the sustain setting for channel 0
					ShortMessage m = new ShortMessage();
					m.setMessage(ShortMessage.CONTROL_CHANGE, 0, MidiUtils.DAMPER_PEDAL, sustain ? MidiUtils.DAMPER_ON
							: MidiUtils.DAMPER_OFF);
					track.add(new MidiEvent(m, ticks));
				} else if (c >= 'A' && c <= 'G') {
					int key = basekey + offsets[c - 'A'];
					if (n < notes.length) {
						if (notes[n] == Modifier.FLAT.c()) { // flat
							key--;
							n++;
						} else if (notes[n] == Modifier.SHARP.c()) { // sharp
							key++;
							n++;
						}
					}

					addNote(track, 0, Tick.$(ticks, notelength), Key.$(key, velocity));
					numNotes++;
				} else if (c == Modifier.PLAY.c()) {
					// Spaces separate groups of notes played at the same time.
					// But we ignore them unless they follow a note or notes.
					if (numNotes > 0) {
						ticks += notelength;
						numNotes = 0;
					}
				} else if (c == Modifier.REST.c()) {
					// Rests are like spaces in that they force any previous
					// note to be output (since they are never part of chords)
					if (numNotes > 0) {
						ticks += notelength;
						numNotes = 0;
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
	 * @param sequence
	 * @param tempo
	 * @param notes
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static void addDrumsTrack(Sequence sequence, int tempo, char[] notes) {
		try {
			Track track = sequence.createTrack(); // Begin with a new track

			int n = 0; // current character in notes[] array
			int ticks = 0; // time in ticks for the composition

			// These values persist and apply to all notes 'till changed
			int notelength = 16; // default to quarter notes
			int velocity = 64; // default to middle volume
			int numNotes = 0; // How many notes in current chord?
			boolean sustain = false; // is the sustain pedal depressed?

			while (n < notes.length) {
				char token = notes[n++];

				if (token == Modifier.LOUDER.c()) {
					velocity += 16; // increase volume;
				} else if (token == Modifier.SOFTER.c()) {
					velocity -= 16; // decrease volume;
				} else if (token == '/') {
					char d = notes[n++];
					if (d == '2') {
						notelength = 32; // half note
					} else if (d == '4') {
						notelength = 16; // quarter note
					} else if (d == '8') {
						notelength = 8; // eighth note
					} else if (d == '3' && notes[n++] == '2') {
						notelength = 2;
					} else if (d == '6' && notes[n++] == '4') {
						notelength = 1;
					} else if (d == '1') {
						if (n < notes.length && notes[n] == '6') {
							notelength = 4; // 1/16th note
						} else {
							notelength = 64; // whole note
						}
					}
				} else if (token == Modifier.OCTAVE_INCREASE.c()) {
					sustain = !sustain;
					// Change the sustain setting for drums channel
					ShortMessage m = new ShortMessage();
					m.setMessage(ShortMessage.CONTROL_CHANGE, DrumsFactory.CHANNEL, MidiUtils.DAMPER_PEDAL,
							sustain ? MidiUtils.DAMPER_ON : MidiUtils.DAMPER_OFF);
					track.add(new MidiEvent(m, ticks));
				} else if (token == 'i') {
					int key = $(notes, n, ++n).i();
					addNote(track, DrumsFactory.CHANNEL, Tick.$(ticks, notelength), Key.$(key, velocity));
					numNotes++;
				} else if (token == Modifier.PLAY.c()) {
					// Spaces separate groups of notes played at the same time.
					// But we ignore them unless they follow a note or notes.
					if (numNotes > 0) {
						ticks += notelength;
						numNotes = 0;
					}
				} else if (token == Modifier.REST.c()) {
					// Rests are like spaces in that they force any previous
					// note to be output (since they are never part of chords)
					if (numNotes > 0) {
						ticks += notelength;
						numNotes = 0;
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
	 * Add a note to the track on a channel.
	 * 
	 * @param track
	 *            track.
	 * @param channel
	 *            channel.
	 * @param tick
	 *            tick.
	 * @param key
	 *            key.
	 * @throws InvalidMidiDataException
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static void addNote(Track track, int channel, Tick tick, Key key) throws InvalidMidiDataException {
		ShortMessage on = new ShortMessage();
		key.note = $(key.note).limit(0, 127).i();
		key.velocity = $(key.velocity).limit(0, 127).i();

		on.setMessage(ShortMessage.NOTE_ON, channel, key.note, key.velocity);
		ShortMessage off = new ShortMessage();
		off.setMessage(ShortMessage.NOTE_OFF, channel, key.note, key.velocity);
		track.add(new MidiEvent(on, tick.start));
		track.add(new MidiEvent(off, tick.start + tick.length));
	}

	/**
	 * This method parses the specified char[] of notes into a Track in channel 0.
	 */
	public static void addTrack(Sequence sequence, int instrument, int tempo, char[] notes) {
		addTrack(sequence, instrument, tempo, notes, Channel.DEFAULT);
	}

	/**
	 * This method parses the specified char[] of notes into a Track.
	 * 
	 * @param channel
	 *            channel
	 */
	public static void addTrack(Sequence sequence, int instrument, int tempo, char[] notes, Channel channel) {
		switch (channel) {
		case DRUMS:
			addDrumsTrack(sequence, tempo, notes);
			break;
		default:
			addDefaultTrack(sequence, instrument, notes, channel);
		}
	}

	/**
	 * @param instrument
	 * @param channel
	 * @return a message that can be used to change the instrument.
	 * @throws InvalidMidiDataException
	 * @author wonka
	 * @since 13/07/2013
	 */
	private static ShortMessage changeInstrument(int instrument, Channel channel) throws InvalidMidiDataException {
		ShortMessage message = new ShortMessage();
		message.setMessage(ShortMessage.PROGRAM_CHANGE, channel.number, instrument, 0);
		return message;
	}

	/**
	 * @param octave
	 * @param range
	 * @return a fix for the octave, if necessary.
	 * @author wonka
	 * @since 13/07/2013
	 */
	private static String octaveFix(int octave, Range<Integer> range) {
		int factor = 3;
		if (octave > range.getEnd()) {
			return $(Note.Modifier.OCTAVE_DECREASE.token, factor).o;
		}

		if (octave < range.getStart()) {
			return $(Note.Modifier.OCTAVE_INCREASE.token, factor).o;
		}

		return "";
	}

	/**
	 * @return random drum notes.
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static String randomDrumsNotes() {
		return randomDrumsNotes(random.nextInt(1000) + 200);
	}

	/**
	 * @param numNotes
	 *            desired number of notes.
	 * @return random drum notes.
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static String randomDrumsNotes(int numNotes) {
		StringBuilder builder = new StringBuilder();
		int restLength = numNotes * 5 / 100;
		int restStart = random.nextInt(numNotes / 2);
		int minOctave = -5;
		int maxOctave = 5;
		int octave = 0;
		boolean resting = false;

		String lastModifier = "";
		String modifier = "";
		String fix = "";
		MidiUtils.Note.Modifier lastFraction = MidiUtils.Note.Modifier.WHOLE;
		MidiUtils.Note.Modifier fraction = MidiUtils.Note.Modifier.WHOLE;
		String playMod = MidiUtils.Note.Modifier.PLAY.getToken();
		for (int i = 0; i < numNotes; i++) {
			if (i >= restStart && i < restLength + restStart) {
				if (!resting) {
					builder.append(Note.Modifier.QUARTER);
					resting = true;
				}
				builder.append(Note.Modifier.REST);
			} else {
				int r = random.nextInt(7);
				if (r < 2) {
					Modifier randomMod = MidiUtils.Note.Modifier.random();
					octave += randomMod.octaveCheck();
					fix = octaveFix(octave, $range(minOctave, maxOctave));

					// ensures that there will be no equal consecutive fraction modifiers
					if (randomMod.isFraction()) {
						if (randomMod.isSlowFraction()) {
							randomMod = Modifier.EIGHTH;
						}
						if (randomMod == lastFraction) {
							randomMod = randomMod.next();
						}
						lastFraction = fraction;
						fraction = randomMod;
					}
					modifier = randomMod.getToken();
				} else if (r > 3 && !lastModifier.equals(playMod)) {
					modifier = playMod;
				} else {
					String str = "i" + DrumsFactory.randomLead();
					builder.append(str);
				}

				builder.append(modifier).append(fix);
				lastModifier = modifier;
			}
		}
		return builder.toString();
	}

	public static String randomNotes() {
		return randomNotes(random.nextInt(1000) + 200);
	}

	/**
	 * @return random notes.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static String randomNotes(int numNotes) {
		StringBuilder builder = new StringBuilder();

		int minOctave = -5;
		int maxOctave = 5;
		int octave = 0;

		String lastModifier = "";
		String modifier = "";
		String fix = "";
		MidiUtils.Note.Modifier lastFraction = MidiUtils.Note.Modifier.WHOLE;
		MidiUtils.Note.Modifier fraction = MidiUtils.Note.Modifier.WHOLE;
		String playMod = MidiUtils.Note.Modifier.PLAY.getToken();
		for (int i = 0; i < numNotes; i++) {
			int r = random.nextInt(7);
			if (r < 1) {
				Modifier randomMod = MidiUtils.Note.Modifier.random();
				octave += randomMod.octaveCheck();
				fix = octaveFix(octave, $range(minOctave, maxOctave));

				// ensures that there will be no equal consecutive fraction modifiers
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

			builder.append(modifier).append(fix);
			lastModifier = modifier;
		}
		return builder.toString();
	}

	/**
	 * @param file
	 *            file name;
	 * @return the sequence represented by the MIDI file.
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

	/**
	 * @param sequence
	 *            sequence to write.
	 * @param file
	 *            destination.
	 * @return total bytes written, -1 in case of an exception.
	 * @since 08/07/2013
	 * @author wonka
	 */
	public static double renderWav(Sequence sequence, File file) {
		try {
			return new Midi2WavRenderer().createWavFile(sequence, file);
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
		return -1;
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
		if (sequence == null) {
			throw new IllegalArgumentException("sequence is null");
		}

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
}
