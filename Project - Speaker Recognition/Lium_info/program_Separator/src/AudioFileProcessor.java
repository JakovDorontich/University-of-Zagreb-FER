import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.*;

class AudioFileProcessor {

	public static void main(String[] args) {
		
		Map<String, ArrayList<String>> analyzedData = new LinkedHashMap<>();
		analyzedData = fillData(Paths.get("showName.seg"));
		
		Set<String> speakers = analyzedData.keySet();
		for (String speaker : speakers) {
			createAudio("showName.wav", "showName_speaker_"+speaker+".wav", analyzedData, speaker);
		}
		
	}
	
	private static void createAudio(String sourceFileName, String destinationFileName, Map<String, ArrayList<String>> analyzedData, String speaker) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream1 = null;
		AudioInputStream shortenedStream2 = null;
		ArrayList<String> speakingTime = analyzedData.get(speaker);
		System.out.println(speaker);
		
		try {
			File file = new File(sourceFileName);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();

			int bytesPerSecond = format.getFrameSize() * (int)format.getFrameRate();
			
			for (String segment : speakingTime) {
				String chunks[]=segment.split(" ");
				int startSecond = getStart(chunks);
				int secondsToCopy = getSecondsToCopy(chunks);

				System.out.println(startSecond+" "+secondsToCopy);
				inputStream = AudioSystem.getAudioInputStream(file);
				inputStream.skip(startSecond * bytesPerSecond);			//postavi kazaljku na ulaznu datoteku
				long framesOfAudioToCopy = secondsToCopy * (int)format.getFrameRate(); // izraèunava udaljenost
				
				if(shortenedStream1 == null) {
					shortenedStream1 = new AudioInputStream(inputStream, format, framesOfAudioToCopy); // kreiranje novog InputStream
				}
				else {
					shortenedStream2 = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
					AudioInputStream appendedFiles = new AudioInputStream(new SequenceInputStream(shortenedStream1, shortenedStream2), shortenedStream1.getFormat(), 
							shortenedStream1.getFrameLength() + shortenedStream2.getFrameLength());
					shortenedStream1 = appendedFiles;
				}
			}
			File destinationFile = new File(destinationFileName);
			AudioSystem.write(shortenedStream1, fileFormat.getType(), destinationFile);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (inputStream != null) try { inputStream.close(); } catch (Exception e) {System.out.println(e);}
			if (shortenedStream1 != null) try { shortenedStream1.close(); } catch (Exception e) {System.out.println(e);}
			if (shortenedStream2 != null) try { shortenedStream2.close(); } catch (Exception e) {System.out.println(e);}
		}
	
		
	}

	private static int getSecondsToCopy(String[] chunks) {
		double value = Double.parseDouble(chunks[1]);
		value = Math.round(value/100);
		return (int)value;
	}

	private static int getStart(String[] chunks) {
		double value = Double.parseDouble(chunks[0]);
		value = Math.round(value/100);
		return (int)value;
	}

	private static Map<String, ArrayList<String>> fillData(Path sourceFileName) {
		Map<String, ArrayList<String>> analyzedData = new LinkedHashMap<>();
		try {
			List<String> lines = Files.readAllLines(sourceFileName);
			for (String line: lines) {
				if(!line.startsWith(";;")) {
					ArrayList<String> time = new ArrayList<>();
					
					String[] chunks = line.split(" ");
					String startSecond = chunks[2];
					String durationSecond = chunks[3];
					String speakerID = chunks[7];
					
					time.add(startSecond+" "+durationSecond);
					if(!analyzedData.containsKey(speakerID)) {
						analyzedData.put(speakerID, time);
					}
					else {
						ArrayList<String> nextTime = analyzedData.get(speakerID);
						nextTime.add(startSecond+" "+durationSecond);
						analyzedData.put(speakerID, nextTime);
					}					
				}
			}
			return analyzedData;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}

}