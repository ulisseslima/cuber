package com.dvlcube.cuber.factory;

import static com.dvlcube.cuber.Cuber.$;
import static com.dvlcube.cuber.Cuber.$img;
import static com.dvlcube.cuber.Cuber.$midi;

import java.util.ArrayList;

import com.dvlcube.cuber.CubeSequence;

/**
 * @author Ulisses Lima
 * @since 02/03/2015
 */
public class RandomMidiAlbum extends Album {
	public RandomMidiAlbum() {
		super.name = $("${name}").o + " " + $("${name}").o;
		super.art = $img(640, 480).label(ARTIST + "_" + super.name);
		super.trackList = new ArrayList<>();

		int tempoGoal = 1500;
		int tempoCount = 0;
		while (tempoCount < tempoGoal) {
			Track track = new Track();

			track.setName(AlbumFactory.createSongName());
			CubeSequence $midi = $midi();
			track.setSequence($midi);
			track.setLength($midi.tempo);
			tempoCount += $midi.tempo;

			trackList.add(track);
		}
	}
}
