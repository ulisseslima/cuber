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

import static com.dvlcube.cuber.Cuber.$date;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.File;

import com.dvlcube.cuber.factory.MP3TaggingFactory;

/**
 * 
 * @since 09/07/2013
 * @author wonka
 */
public class AudioUtils {
	/**
	 * @param file
	 *            the WAV file.
	 * @return the resulting mp3 file.
	 * @throws InputFormatException
	 * @throws EncoderException
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static File lameEncode(String file) {
		return lameEncode(file, false);
	}

	/**
	 * @param file
	 *            the WAV file.
	 * @param removeOriginal
	 *            whether the original WAV file should be removed.
	 * @return the resulting mp3 file.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static File lameEncode(String file, boolean removeOriginal) {
		String wavFile = file.endsWith(".wav") ? file : file + ".wav";
		File source = new File(wavFile);
		String mp3File = FileUtils.removeExtension(file) + ".mp3";
		File target = new File(mp3File);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(source, target, attrs);
			if (removeOriginal) {
				new File(wavFile).delete();
			}
			return target;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param artist
	 *            artist name.
	 * @param albumName
	 *            album name.
	 * @param songName
	 *            song name.
	 * @param genre
	 *            genre.
	 * @param i
	 *            track number.
	 * @param mp3
	 *            the mp3 file.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static void tag(String artist, String albumName, String songName, String genre, int i, File mp3) {
		MP3TaggingFactory.main(new String[] { mp3.getPath(), //
				"--album=" + albumName, //
				"--artist=" + artist,//
				"--genre=" + genre, //
				"--number=" + i, //
				"--song=" + songName,//
				"--year=" + $date().year() });
	}

	/**
	 * @param artist
	 * @param albumName
	 * @param songName
	 * @param genre
	 *            TODO
	 * @param i
	 * @param mp3
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static void tag(String artist, String albumName, String songName, String genre, int i, String mp3) {
		tag(artist, albumName, songName, genre, i, new File(mp3));
	}
}
