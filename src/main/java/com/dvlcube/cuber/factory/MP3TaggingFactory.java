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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagException;
import org.farng.mp3.TagOptionSingleton;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v2_3;

import com.dvlcube.cuber.Cuber;
import com.dvlcube.cuber.factory.MP3TaggingFactory.Command.Option;

/**
 * @author wonka
 * @since 31/12/2012
 */
public class MP3TaggingFactory {
	/**
	 * Command line processor.
	 * 
	 * @author wonka
	 * @since 01/01/2013
	 */
	public static class Command {
		public enum Option {
			album {
				@Override
				public void process(final AbstractID3v2 tag, final String value) {
					tag.setAlbumTitle(value);
					super.process(tag, value);
				}

				@Override
				public void process(final ID3v2_3 tag, final String value) {
					tag.setAlbumTitle(value);
					super.process(tag, value);
				}
			},
			artist {
				@Override
				public void process(final ID3v2_3 tag, final String value) {
					tag.setAuthorComposer(value);
					tag.setLeadArtist(value);
					super.process(tag, value);
				}
			},
			genre {
				@Override
				public void process(final ID3v2_3 tag, final String value) {
					tag.setSongGenre(value);
					super.process(tag, value);
				}
			},
			number {
				@Override
				public void process(final ID3v2_3 tag, final String value) {
					tag.setTrackNumberOnAlbum(value);
					super.process(tag, value);
				}
			},
			pattern {
				@Override
				public void process(final ID3v2_3 tag, final String value) {
					final String filenamePattern = value;
					final String regex = createRegex(filenamePattern);
					final Pattern pattern = Pattern.compile(regex);

					final Matcher optionMatcher = pattern.matcher(filenamePattern);
					final Matcher valueMatcher = pattern.matcher(FILENAME);

					if (optionMatcher.matches() && valueMatcher.matches()) {
						for (int i = 0; i < valueMatcher.groupCount(); i++) {
							final String optionMatch = optionMatcher.group(i + 1);
							final String valueMatch = valueMatcher.group(i + 1);
							Option.doProcessing(tag, optionMatch, valueMatch);
						}
					}
				}
			},
			song {
				@Override
				public void process(final ID3v2_3 tag, final String value) {
					tag.setSongTitle(value);
					super.process(tag, value);
				}
			},
			year {
				@Override
				public void process(final ID3v2_3 tag, final String value) {
					tag.setYearReleased(value);
					super.process(tag, value);
				}
			};
			public static String PREFIX = "--";

			public static boolean contains(final String optionName) {
				for (final Option option : Option.values()) {
					if (option.name().equals(optionName.replace("--", ""))) {
						return true;
					}
				}
				return false;
			}

			/**
			 * Replaces every known option with a regex group.
			 * 
			 * @param filenamePattern
			 * @return the regex.
			 * @author wonka
			 * @since 01/01/2013
			 */
			public static String createRegex(final String filenamePattern) {
				String regex = filenamePattern.replace(".", "\\.");
				for (final Option option : Option.values()) {
					regex = regex.replace(option.name(), "(.+)");
				}
				debug("Using regex pattern '" + regex + "'");
				return regex;
			}

			/**
			 * @param tag
			 * @param option
			 * @param value
			 * @since 09/07/2013
			 * @author wonka
			 */
			protected static void doProcessing(AbstractID3v2 tag, String optionName, String value) {
				final Option option = Option.find(optionName);
				option.process(tag, value);
			}

			/**
			 * @param tag
			 * @param string
			 * @param valueMatch
			 * @author wonka
			 * @since 01/01/2013
			 */
			protected static void doProcessing(final ID3v2_3 tag, final String optionName, final String value) {
				final Option option = Option.find(optionName);
				option.process(tag, value);
			}

			/**
			 * @param optionName
			 * @return
			 * @author wonka
			 * @since 01/01/2013
			 */
			private static Option find(final String optionName) {
				return valueOf(optionName.replace(Option.PREFIX, ""));
			}

			public void process(final AbstractID3v2 tag, final String value) {
				log("\tSetting " + name() + " to " + value);
			}

			public void process(final ID3v2_3 tag, final String value) {
				log("\tSetting " + name() + " to " + value);
			}
		}

		private static String FILENAME;

		/**
		 * @param tag
		 * @param option
		 * @param value
		 * @since 09/07/2013
		 * @author wonka
		 */
		public static void process(AbstractID3v2 tag, String option, String value) {
			Option.doProcessing(tag, option, value);
		}

		/**
		 * @param tag
		 * @param option
		 * @param value
		 * @author wonka
		 * @since 01/01/2013
		 */
		public static void process(final ID3v2_3 tag, final String option, final String value) {
			Option.doProcessing(tag, option, value);
		}

		/**
		 * @param fileName
		 * @author wonka
		 * @since 01/01/2013
		 */
		public static void setFileName(final String fileName) {
			FILENAME = fileName.replace(".mp3", "").replace(".MP3", "");
		}

	}

	private static boolean DEBUG = false;
	public static String PATTERN = "number - artist - year - album - song";
	private static boolean VERBOSE = false;

	private static void debug(final String string) {
		if (DEBUG) {
			System.out.println(string);
		}
	}

	/**
	 * @param string
	 * @author wonka
	 * @since 01/01/2013
	 */
	private static void die(final String string) {
		System.out.println(string);
		System.out.println("Type --help for a list of valid options");
		System.exit(1);
	}

	/**
	 * @param file
	 * @return
	 * @author wonka
	 * @since 31/12/2012
	 */
	private static boolean isMp3(final File file) {
		if (file.getName().toLowerCase().endsWith(".mp3")) {
			return true;
		}
		return false;
	}

	/**
	 * Verifies if the option is a switch. And if it is, enables it.
	 * 
	 * @param option
	 * @return
	 * @author wonka
	 * @since 31/12/2012
	 */
	private static boolean isSwitch(final String option) {
		switch (option) {
		case "-v":
		case "--verbose":
			VERBOSE = true;
			return true;
		case "-d":
		case "--debug":
			DEBUG = true;
			return true;
		}
		return false;
	}

	/**
	 * @param string
	 * @author wonka
	 * @since 31/12/2012
	 */
	private static void log(final String string) {
		if (VERBOSE) {
			System.out.println(string);
		}
	}

	public static void main(final String[] args) {
		System.out.println("# DvlCube MP3 Tagger #");

		final HashMap<String, String> options = read(args);

		final File fileArg = new File(args[0]);
		TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_WRITE);

		if (fileArg.isDirectory()) {
			final File[] files = fileArg.listFiles();
			for (final File file : files) {
				tag(file, options);
			}
		} else if (fileArg.isFile()) {
			tag(fileArg, options);
		} else {
			die("Not a file: " + fileArg);
		}
	}

	/**
	 * Creates a map with parameter/value.
	 * 
	 * @param args
	 * @return the map.
	 * @author wonka
	 * @since 31/12/2012
	 */
	private static HashMap<String, String> read(final String[] args) {
		if (args.length < 1) {
			die("No folder specified.");
		} else if (args.length < 2) {
			die("No values specified.");
		}

		final HashMap<String, String> map = new HashMap<>();
		for (final String string : args) {
			if (!isSwitch(string)) {
				final String[] option = string.split("=");
				if (option.length > 1) {
					final String optionName = option[0];
					if (Option.contains(optionName)) {
						map.put(optionName, option[1]);
					} else {
						die("Invalid option: " + optionName);
					}
				}
			}
		}
		return map;
	}

	/**
	 * @param mp3
	 * @param options
	 * @author wonka
	 * @throws TagException
	 * @throws IOException
	 * @since 31/12/2012
	 */
	private static void save(final MP3File mp3, final HashMap<String, String> options) throws IOException, TagException {
		final String fileName = mp3.getMp3file().getName();
		log("new MP3File(" + fileName + ") {");

		// final ID3v2_3 tag = new ID3v2_3(new RandomAccessFile(mp3.getMp3file(), "r"));
		final ID3v2_3 tag = new ID3v2_3();
		// AbstractID3v2 tag = mp3.getID3v2Tag();

		for (final String option : options.keySet()) {
			final String value = options.get(option);
			Command.setFileName(fileName);
			Command.process(tag, option, value);
		}

		mp3.setID3v2Tag(tag);
		File f = mp3.getMp3file();
		int attempts = 0;
		while (!f.canWrite() && attempts < 10) {
			Cuber.$debug().sleeps(10);
			attempts++;
			Cuber.$out("can't write " + mp3.getMp3file().getName() + ", attempts: " + attempts);
		}
		mp3.save();
		log("}");
	}

	/**
	 * @param file
	 * @param options
	 * @author wonka
	 * @since 31/12/2012
	 */
	private static void tag(final File file, final HashMap<String, String> options) {
		if (isMp3(file)) {
			try {
				final MP3File mp3 = new MP3File(file);
				save(mp3, options);
			} catch (IOException | TagException e) {
				e.printStackTrace();
			}
		} else {
			log(file + " is not an mp3 file.");
		}
	}
}