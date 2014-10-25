MidiToPhaseShift
================

Midi To EOF Converter For PhaseShift

Tool to create custom songs for PhaseShift:
- 1. convert midi files (make them compatible to EOF)
- 2. use EOF and import the new midi and your .ogg song
- 3. bpm fine tuning and finished

Todos:
- read note mapping from files
- guitar and bass support (DRUMS should work)
- bpm support

PhaseShift:
- http://www.dwsk.co.uk/index_phase_shift.html

EOF (Song Editor for PhaseShift):
- http://dwsk.proboards.com/thread/672

Midi Files:
- http://webtabplayer.com/Tab/44990/equilibrium-tabs/met-tab
- http://www.songsterr.com/a/wa/search?pattern=ensiferum

Ogg Converter:
- http://audio.online-convert.com/convert-to-ogg

HowToUse:
- 1. download "MidiToPhaseShift.jar" and "midi" folder
- 2. create or download a midi
- 3. copy the downloaded midi file in the "midi" folder
- 4. rename it in the format:
-- "Artist - Title - 1=GUITAR,2=BASS,3=DRUMS.mid"
-- "Artist - Title - 3=DRUMS.mid"
-- 3=DRUMS means that the third track should be converted to the drums
-- in webtabplayer for example you can see the tracks and count the number
- 5. double click on MidiToPhaseShift.jar
- 6. now the midi folder contains sub-folders with converted midi.mid files
- 7. convert your .mp3 to .ogg file
- 8. open EOF and import the midi.mid file and the .ogg file
- 9. select all notes and move them to the beginning (sync)
- 10. now you have to change the bpm
- 11. thats it - save and copy the folder to PhaseShift