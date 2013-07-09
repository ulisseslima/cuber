package com.dvlcube.cuber.factory;

import static com.dvlcube.cuber.Cuber.$;
import static com.dvlcube.cuber.Cuber.$f;
import static com.dvlcube.cuber.Cuber.$img;
import static com.dvlcube.cuber.Cuber.$midi;

import java.util.Random;

import com.dvlcube.cuber.CubeFile;
import com.dvlcube.cuber.utils.AudioUtils;

/**
 * 
 * @since 08/07/2013
 * @author wonka
 */
public class AlbumFactory {
	private static final String ARTIST = "DvlCube";

	/**
	 * @param args
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("album folder not specified.");
		} else {
			new AlbumFactory().createAlbum(args[0]);
		}
	}

	private final Random random = new Random();

	/**
	 * Creates a random music album.
	 * 
	 * @param dir
	 *            destination.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public void createAlbum(String dir) {
		String albumName = $("${name}").o + " " + $("${name}").o;
		CubeFile f = $f(dir + "/" + albumName + "/");
		String albumDir = f.o.getPath();
		String extrasDir = albumDir + "/" + "extras";
		$img(640, 480).label(ARTIST + "_" + albumName).write(f.newFile(albumName + ".png"));

		int i = 1;
		int tempoGoal = 1500;
		int tempoCount = 0;
		while (tempoCount < tempoGoal) {
			String songName = createSongName();
			String songPath = albumDir + "/" + i + " - " + songName;
			tempoCount += $midi().publish(songPath).tempo;

			AudioUtils.tag(ARTIST, albumName, songName, i, songPath + ".mp3");
			$f(songPath + ".original.mp3").rm();
			$f(songPath + ".csq").mv(extrasDir);
			$f(songPath + ".midi").mv(extrasDir);

			i++;
		}
	}

	private String createSongName() {
		int names = random.nextInt(3) + 1;
		String songName = "";
		for (int j = 0; j < names; j++) {
			songName += $(" ${name}").o;
		}
		songName = songName.replaceFirst(" ", "");
		return songName;
	}
}
