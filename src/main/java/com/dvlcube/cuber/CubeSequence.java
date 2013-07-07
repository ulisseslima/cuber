package com.dvlcube.cuber;

import static com.dvlcube.cuber.Cuber.$debug;
import static com.dvlcube.cuber.Cuber.$seq;

import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/**
 * Notes:<br>
 * http://en.wikipedia.org/wiki/Pulses_Per_Quarter<br>
 * http://www.onjava.com/pub/a/onjava/excerpt/jenut3_ch17/index1.html
 * <p>
 * Cool instruments: <br>
 * eerie: 97<br>
 * blow: 77<br>
 * acoustic: 13<br>
 * choir: 94
 * 
 * @since 06/07/2013
 * @author wonka
 */
public class CubeSequence {
	private final ArrayList<String> sequences = new ArrayList<>();
	public int instrument = 0;
	public int tempo = 120;
	public boolean ended = true;
	public int count;
	private boolean sequenceChanged = false;
	private final Object lock = new Object();
	public long lastModified = System.currentTimeMillis();
	public int timeout = 30;

	public Sequence sequence;
	public Sequencer sequencer;
	{
		try {
			// 16 ticks per quarter note.
			sequence = new Sequence(Sequence.PPQ, 16);
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
		prepareSequence();
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
		prepareSequence();
	}

	private void randomizeTempoAndInstrument() {
		Random random = new Random();
		this.instrument = random.nextInt(128);
		this.tempo = random.nextInt(300) + 1;
	}

	public CubeSequence add(String... sequences) {
		return add(1, sequences);
	}

	public final CubeSequence add(int times, String... sequenceStrings) {
		sequenceChanged = true;
		if (sequenceStrings == null) {
			sequences.add(MidiUtils.randomNotes());
		} else {
			for (int i = 0; i < times; i++) {
				for (String sequence : sequenceStrings) {
					sequences.add(sequence);
				}
			}
		}
		return this;
	}

	public CubeSequence play() {
		if (sequences.isEmpty())
			add(1, MidiUtils.randomNotes());

		synchronized (lock) {
			while (!ended) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			ended = false;
			prepareSequence();
			sequencer.setTempoInBPM(tempo);
			sequencer.start();
			return this;
		}
	}

	public CubeSequence play(String... sequences) {
		resetSequence();
		add(1, sequences);

		return play();
	}

	private void resetSequence() {
		try {
			sequence = new Sequence(Sequence.PPQ, 16);
			sequences.clear();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public CubeSequence loop(String... sequences) {
		resetSequence();
		add(1, sequences);

		return loop();
	}

	private void prepareSequence() {
		if (sequenceChanged) {
			for (String sequenceString : sequences) {
				MidiUtils.addTrack(sequence, instrument, tempo, sequenceString.toCharArray());
			}

			try {
				sequencer.setSequence(sequence);
				sequenceChanged = false;
				// TODO find proper way to kill delay before first note.
				$debug().sleep(30);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
	}

	public CubeSequence stop() {
		sequencer.stop();
		return this;
	}

	public CubeSequence close() {
		if (sequencer.isOpen())
			sequencer.stop();
		sequencer.close();
		return this;
	}

	public CubeSequence downTempo() {
		float tempoFactor = sequencer.getTempoFactor();
		sequencer.setTempoFactor((float) (tempoFactor * .97));
		return this;
	}

	public CubeSequence upTempo() {
		float tempoFactor = sequencer.getTempoFactor();
		sequencer.setTempoFactor((float) (tempoFactor * 1.03));
		return this;
	}

	public CubeSequence changeTempo(float multiplier) {
		float tempoFactor = sequencer.getTempoFactor();
		sequencer.setTempoFactor(tempoFactor * multiplier);
		return this;
	}

	public CubeSequence setTempoFactor(float factor) {
		sequencer.setTempoFactor(factor);
		return this;
	}

	public CubeSequence loop() {
		if (sequences.isEmpty())
			add(1, MidiUtils.randomNotes());

		sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		play();

		return this;
	}

	@Override
	public String toString() {
		return "CubeSequence [sequences=" + sequences + ", instrument=" + instrument + ", tempo=" + tempo + "]";
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println($seq().play());
	}
}
