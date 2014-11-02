MidiToPhaseShift
================

Midi To EOF Converter For PhaseShift

Tool to create custom songs for PhaseShift:
- 1. convert midi files (make them compatible to EOF)
- 2. use EOF and import the new midi and your .ogg song
- 3. move beginning, fine tuning bpm, save
- 4. and the next one

Todos:
- bpm values from midi are not right (problem in eof?)
- .txt file can have some format problems
- remove crazy useless stuff from midi-files (problems in eof) 

PhaseShift:
- http://www.google.de/webhp?q=dwsk+phase+shift

EOF (Song Editor for PhaseShift):
- http://www.google.de/webhp?q=dwsk+phase+shift+eof+song+editor

Midi Files:
- http://www.google.de/webhp?q=webtabplayer.com+equilibrium+met
- http://www.google.de/webhp?q=songsterr.com+equilibrium+met

Ogg Converter:
- http://www.google.de/webhp?q=online-convert+ogg

Features:
- supported instruments (GUITAR_REAL_6, BASS_REAL_4, BASS_REAL_5, BASS_REAL_6, DRUMS)
- convert midi files to use in eof
- supports additional .txt tab-files for real guitar/bass (to find note positions)
- config-files to change mapping from midi notes to lines (useful for drums)
- config-files to change tuning (useful for guitar/bass)

HowToUse:
1. download "MidiToPhaseShift.jar" and "midi" folder
2. create or download a midi-file (see links)
3. create or download a .txt tab-file (if you want to create guitar/bass part)(see links)
4. copy the downloaded files in the "midi" folder
5. rename it in the format (examples see "midi" folder):
    - "Artist - Title - 1=GUITAR_REAL_6,2=BASS_REAL_4,3=DRUMS.mid"
    - "Artist - Title - 3=DRUMS.mid" 
    - "2=BASS_REAL_4" means that the second track should be converted to the real bass with 4 strings
    - in webtabplayer for example you can see the tracks and count the number
6. start (double click) MidiToPhaseShift.jar
7. now the midi folder contains sub-folders with converted midi.mid files
8. (optional) you can put config files in the sub-folder and change the mapping for example and repeat 5.
9. convert your .mp3 to .ogg file (see links)
10. open EOF and import the midi.mid file and the .ogg file
11. move the first fret to the beginning of the song (sync)
12. now you have to change the bpm
    - click on the bpm number, "Beat", "BPM Change" and use "Adjust Notes"
    - play the song on slower speed to check it
12. thats it - save and copy the folder to PhaseShift

Problems:
- see "logs" folder
- send me not working midis, to find bugs and fix them