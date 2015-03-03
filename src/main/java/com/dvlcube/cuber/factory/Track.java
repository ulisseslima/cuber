package com.dvlcube.cuber.factory;

import com.dvlcube.cuber.CubeSequence;

/**
 * @author Ulisses Lima
 * @since 02/03/2015
 */
public class Track {
	private String name;
	private int length;
	private CubeSequence sequence;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the sequence
	 */
	public CubeSequence getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(CubeSequence sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return sequence.
	 * @author Ulisses Lima
	 * @since 02/03/2015
	 */
	public void play() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				sequence.play();
			}
		}).start();
		;
	}
}
