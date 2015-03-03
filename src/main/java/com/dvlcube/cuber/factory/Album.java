package com.dvlcube.cuber.factory;

import java.util.List;

import com.dvlcube.cuber.CubeImage;

/**
 * @author Ulisses Lima
 * @since 02/03/2015
 */
public abstract class Album {
	public static final String ARTIST = "dvlcube";
	public static final String GENRE = "Procedural";

	public String name;
	public CubeImage art;
	public List<Track> trackList;

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
	 * @return the art
	 */
	public CubeImage getArt() {
		return art;
	}

	/**
	 * @param art
	 *            the art to set
	 */
	public void setArt(CubeImage art) {
		this.art = art;
	}

	/**
	 * @return the trackList
	 */
	public List<Track> getTrackList() {
		return trackList;
	}

	/**
	 * @param trackList
	 *            the trackList to set
	 */
	public void setTrackList(List<Track> trackList) {
		this.trackList = trackList;
	}
}
