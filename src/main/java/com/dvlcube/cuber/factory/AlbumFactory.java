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
package com.dvlcube.cuber.factory;

import static com.dvlcube.cuber.Cuber.$;
import static com.dvlcube.cuber.Cuber.$f;
import static com.dvlcube.cuber.Cuber.$img;
import static com.dvlcube.cuber.Cuber.$midi;

import java.io.File;
import java.util.Random;

import com.dvlcube.cuber.CubeFile;
import com.dvlcube.cuber.utils.AudioUtils;

/**
 * Run with no args to just play a random song, run with a directory as first
 * argument to save a whole album.
 * 
 * @since 08/07/2013
 * @author wonka
 */
public class AlbumFactory {
	/**
	 * @param args
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println($midi().play());
		} else {
			if (new File(args[0]).isDirectory()) {
				new AlbumFactory().createRandomAlbum(args[0]);
			} else {
				$midi(args).play();
			}
		}
	}

	public static final Random random = new Random();

	/**
	 * Creates a random music album at the specified location.
	 * 
	 * @param dir
	 *            destination.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public void createRandomAlbum(String dir) {
		String albumName = $("${name}").o + " " + $("${name}").o;
		CubeFile f = $f(dir + "/" + albumName + "/");
		String albumDir = f.o.getPath();
		String extrasDir = albumDir + "/" + "extras";
		$img(640, 480).label(Album.ARTIST + "_" + albumName).write(f.newFile(albumName + ".png"));

		int i = 1;
		int tempoGoal = 1500;
		int tempoCount = 0;
		while (tempoCount < tempoGoal) {
			String songName = createSongName();
			String songPath = albumDir + "/" + i + " - " + songName;
			tempoCount += $midi().publish(songPath).tempo;

			AudioUtils.tag(Album.ARTIST, albumName, songName, Album.GENRE, i, songPath + ".mp3");
			$f(songPath + ".original.mp3").rm();
			$f(songPath + ".csq").mv(extrasDir);
			$f(songPath + ".midi").mv(extrasDir);

			i++;
		}
	}

	/**
	 * Creates a random music album with MIDI tracks.
	 * 
	 * @author Ulisses Lima
	 * @since 02/03/2015
	 */
	public RandomMidiAlbum createRandomMidiAlbum() {
		RandomMidiAlbum album = new RandomMidiAlbum();
		return album;
	}

	public static String createSongName() {
		int names = random.nextInt(3) + 1;
		String songName = "";
		for (int j = 0; j < names; j++) {
			songName += $(" ${name}").o;
		}
		songName = songName.replaceFirst(" ", "");
		return songName;
	}
}
