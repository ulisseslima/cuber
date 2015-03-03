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
package com.dvlcube.cuber;

import static com.dvlcube.cuber.Cuber.$;
import static com.dvlcube.cuber.Cuber.$debug;
import static com.dvlcube.cuber.Cuber.$f;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import com.dvlcube.cuber.utils.AudioUtils;
import com.dvlcube.cuber.utils.MidiUtils;
import com.dvlcube.cuber.utils.MidiUtils.Channel;

/**
 * Notes:<br>
 * http://en.wikipedia.org/wiki/Pulses_Per_Quarter<br>
 * http://www.onjava.com/pub/a/onjava/excerpt/jenut3_ch17/index1.html
 * <p>
 * Cool instruments: <br>
 * eerie: 97<br>
 * blow: 77<br>
 * horn: 58,56<br>
 * acoustic: 13<br>
 * choir: 94<br>
 * glass: 98<br>
 * percussive: 47
 * 
 * @since 06/07/2013
 * @author wonka
 */
public class CubeSequence {
	public static final String ATTRIBUTE_SEPARATOR = "§";
	public static final String FILE_EXTENSION = ".csq";
	public static final String SEQUENCE_ATTRIBUTE_SEPARATOR = ":";
	public static final String SEQUENCE_SEPARATOR = "µ";

	public static CubeSequence valueOf(CubeFile f) {
		return valueOf(f.firstLine());
	}

	/**
	 * @param string
	 *            string representing a saved cube sequence.
	 * @return a CubeSequence.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static CubeSequence valueOf(String string) {
		String[] sequenceArray = string.split(ATTRIBUTE_SEPARATOR);
		if (sequenceArray.length != 3) {
			throw new IllegalArgumentException("this string does not represent a valid cube sequence");
		}

		try {
			int instrument = Integer.parseInt(sequenceArray[0]);
			int tempo = Integer.parseInt(sequenceArray[1]);
			String sequences = sequenceArray[2];

			return new CubeSequence(instrument, tempo, sequences.split(SEQUENCE_SEPARATOR));
		} catch (Exception e) {
			throw new IllegalArgumentException("this string does not represent a cube sequence");
		}
	}

	public int count;
	public boolean ended = true;
	public int instrument = 0;
	public long lastModified = System.currentTimeMillis();
	private boolean loadedFromMidi;
	private final Object lock = new Object();
	public Sequence o;
	private final Random random = new Random();
	private boolean sequenceChanged = false;

	public Sequencer sequencer;
	private final ArrayList<String> sequences = new ArrayList<>();
	public int tempo = 120;
	public int timeout = 30;

	{
		try {
			// 16 ticks per quarter note.
			o = new Sequence(Sequence.PPQ, 16);
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());

			// Let us know when it is done playing
			sequencer.addMetaEventListener(new MetaEventListener() {
				@Override
				public void meta(MetaMessage m) {
					// A message of this type is automatically sent
					// when we reach the end of the track
					if (m.getType() == MidiUtils.END_OF_TRACK) {
						synchronized (lock) {
							count++;
							ended = true;
							lastModified = System.currentTimeMillis();
							lock.notify();
							sequencer.setTickPosition(0);
						}
					}
				}
			});

			new Thread(new Runnable() {
				@Override
				public void run() {
					while (sequencer.isOpen()) {
						$debug().sleeps(timeout);
						long now = System.currentTimeMillis();
						if (ended && now > lastModified) {
							close();
						}
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CubeSequence(CubeFile input) {
		loadedFromMidi = true;
		o = MidiUtils.read(input.o);
		setSequence(o);
	}

	/**
	 * Useful only if you want a random song.
	 * 
	 * @param length
	 *            length of the random song.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeSequence(int length) {
		randomizeTempoAndInstrument();

		add(1, MidiUtils.randomNotes(length));
		addTracks();
	}

	/**
	 * @param instrument
	 *            0 to 127
	 * @param tempo
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeSequence(int instrument, int tempo) {
		this.instrument = instrument;
		this.tempo = tempo;
	}

	public CubeSequence(int instrument, int tempo, String... sequences) {
		this.instrument = instrument;
		this.tempo = tempo;
		add(1, sequences);
		addTracks();
	}

	/**
	 * Example: <br>
	 * $seq("A B C D E F G +A s/32 D E C D E C /1-->>CEG").play();
	 * 
	 * @param sequences
	 * @since 06/07/2013
	 * @author wonka
	 */
	public CubeSequence(String... sequences) {
		randomizeTempoAndInstrument();

		add(1, sequences);
		addTracks();
	}

	public final CubeSequence add(int times, String... sequenceStrings) {
		sequenceChanged = true;
		if (sequenceStrings == null) {
			return this;
		} else {
			for (int i = 0; i < times; i++) {
				for (String sequence : sequenceStrings) {
					sequences.add(sequence);
				}
			}
		}
		return this;
	}

	public CubeSequence add(String... sequences) {
		return add(1, sequences);
	}

	public void addDrumsTrack(int tempo) {
		addDrumsTrack(tempo, MidiUtils.randomDrumsNotes());
	}

	public void addDrumsTrack(int tempo, String notes) {
		MidiUtils.addDrumsTrack(o, tempo, notes.toCharArray());
		setSequence(o);
	}

	public void addTrack(int instrument, int tempo) {
		addTrack(instrument, tempo, MidiUtils.randomNotes());
	}

	public void addTrack(int instrument, int tempo, String notes) {
		MidiUtils.addTrack(o, instrument, tempo, notes.toCharArray(), Channel.DEFAULT);
		setSequence(o);
	}

	private void addTracks() {
		checkIfNeedsRandomization();
		if (sequenceChanged) {
			for (String sequenceString : sequences) {
				String[] channelSequence = sequenceString.split(SEQUENCE_ATTRIBUTE_SEPARATOR);
				Channel channel = Channel.parse(channelSequence[1]);
				int instrument = $(channelSequence[2]).i();
				MidiUtils.addTrack(o, instrument, tempo, channelSequence[0].toCharArray(), channel);
			}
			setSequence(o);
		}
	}

	public CubeSequence changeTempo(float multiplier) {
		float tempoFactor = sequencer.getTempoFactor();
		sequencer.setTempoFactor(tempoFactor * multiplier);
		return this;
	}

	private void checkIfNeedsRandomization() {
		if (sequences.isEmpty() && !loadedFromMidi) {
			randomize();
		}
	}

	public CubeSequence close() {
		if (sequencer.isOpen()) {
			sequencer.stop();
		}
		sequencer.close();
		return this;
	}

	public CubeSequence downTempo() {
		float tempoFactor = sequencer.getTempoFactor();
		sequencer.setTempoFactor((float) (tempoFactor * .97));
		return this;
	}

	public CubeSequence loop() {
		sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		play();

		return this;
	}

	public CubeSequence loop(String... sequences) {
		resetSequence();
		add(1, sequences);

		return loop();
	}

	/**
	 * Renders to WAV, encodes to MP3 and removes the original WAV.
	 * 
	 * @param file
	 *            destination.
	 * @return this.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public CubeSequence mp3(CubeFile file) {
		checkIfNeedsRandomization();
		MidiUtils.renderWav(o, file.o);
		AudioUtils.lameEncode(file.o.getPath(), true);
		return this;
	}

	public CubeSequence mp3(String file) {
		return mp3($f($(file)));
	}

	public CubeSequence play() {
		synchronized (lock) {
			while (!ended) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			ended = false;
			if (!loadedFromMidi) {
				sequencer.setTempoInBPM(tempo);
			}
			sequencer.start();
			return this;
		}
	}

	public CubeSequence play(String... sequences) {
		resetSequence();
		add(1, sequences);

		return play();
	}

	/**
	 * Creates the MIDI, .csq, and WAV files.
	 * 
	 * @param file
	 *            destination.
	 * @return this.
	 * @since 08/07/2013
	 * @author wonka
	 */
	public CubeSequence publish(String file) {
		String fileName = $(file).o;
		save(fileName);
		write(fileName + ".midi");
		mp3(fileName + ".wav");
		return this;
	}

	private void randomize() {
		int tracks = random.nextInt(4) + 1;
		int avgNotes = 0;
		for (int i = 0; i < tracks; i++) {
			int randomNumNotes = random.nextInt(800) + 200;
			avgNotes += randomNumNotes;
			String randomNotes = MidiUtils.randomNotes(randomNumNotes);
			String channel = "0";
			String instrument = String.valueOf(randomizeInstrument());
			add($(randomNotes, channel, instrument).join(SEQUENCE_ATTRIBUTE_SEPARATOR));
		}

		int drumsLenght = (int) (avgNotes / tracks / 1.5);
		String randomNotes = MidiUtils.randomDrumsNotes(drumsLenght);
		String channel = MidiUtils.Channel.DRUMS.string;
		String instrument = "0";
		add($(randomNotes, channel, instrument).join(SEQUENCE_ATTRIBUTE_SEPARATOR));
		sequenceChanged = true;
	}

	/**
	 * @return the randomized instrument.
	 * @since 09/07/2013
	 * @author wonka
	 */
	private int randomizeInstrument() {
		return instrument = random.nextInt(128);
	}

	private void randomizeTempoAndInstrument() {
		instrument = random.nextInt(128);
		tempo = random.nextInt(300) + 1;
	}

	private void resetSequence() {
		try {
			o = new Sequence(Sequence.PPQ, 16);
			sequences.clear();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes a text file that can be used to reconstruct this.
	 * 
	 * @param path
	 *            destination.
	 * @return this.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeSequence save(CubeString path) {
		checkIfNeedsRandomization();
		$f(path + FILE_EXTENSION).write(toString());
		return this;
	}

	/**
	 * Writes a text file that can be used to reconstruct this.
	 * <p>
	 * The file will have the .csq extension.
	 * 
	 * @param path
	 *            destination.
	 * @return this.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeSequence save(String path) {
		return save($(path));
	}

	private void setSequence(Sequence sequence) {
		try {
			sequencer.setSequence(sequence);
			sequenceChanged = false;
			// TODO find proper way to kill delay before first note.
			$debug().sleep(30);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public CubeSequence setTempoFactor(float factor) {
		sequencer.setTempoFactor(factor);
		return this;
	}

	public CubeSequence stop() {
		sequencer.stop();
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(instrument).append(ATTRIBUTE_SEPARATOR);
		builder.append(tempo).append(ATTRIBUTE_SEPARATOR);
		for (String sequence : sequences) {
			builder.append(SEQUENCE_SEPARATOR).append(sequence);
		}
		return builder.toString().replaceFirst(SEQUENCE_SEPARATOR, "");
	}

	public CubeSequence upTempo() {
		float tempoFactor = sequencer.getTempoFactor();
		sequencer.setTempoFactor((float) (tempoFactor * 1.03));
		return this;
	}

	/**
	 * Renders in the WAV format.
	 * 
	 * @param file
	 *            destination.
	 * @return this.
	 * @since 08/07/2013
	 * @author wonka
	 */
	public CubeSequence wav(CubeFile file) {
		checkIfNeedsRandomization();
		MidiUtils.renderWav(o, file.o);
		return this;
	}

	/**
	 * Renders in the WAV format.
	 * 
	 * @param file
	 *            destination.
	 * @return this.
	 * @since 08/07/2013
	 * @author wonka
	 */
	public CubeSequence wav(String file) {
		return wav($f($(file)));
	}

	/**
	 * Writes a MIDI file.
	 * 
	 * @param path
	 *            destination.
	 * @return this.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeSequence write(CubeString path) {
		checkIfNeedsRandomization();
		MidiUtils.write(o, path.o);
		return this;
	}

	/**
	 * Writes a MIDI file.
	 * 
	 * @param path
	 *            file destination.
	 * @return this.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeSequence write(String path) {
		return write($(path));
	}
}
