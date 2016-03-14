# okaeri-translation-viewer

This is a GUI used to assist the dialog translation of [this](https://github.com/cjuub/okaeri-chibi-translation) project.

## Features

* Live view of how the original and translated text would look in the game.
* Easy switching between the different files.
* Display progress of how many lines are translated for each file.
* Color indication for which lines have been translated and not.
* Human readable escape sequences (e.g. `<1a0680010003>` becomes `<BIG>`).
* Search function spanning all the files.
* Multi-platform.

## Build

##### Getting the necessary files
The original font is required in order to simulate the in-game views. This project uses font files extracted from [Tinke](https://github.com/pleonex/tinke). Download the latest release from the *release* page.

Open the original ROM in Tinke and for each of the files:

* Font_large.NFTR
* Font_normal.NFTR
* Font_small.NFTR

Do the following:

1. Select *View*.
2. Click *Inverse palette*.
3. Click *Export font info* and save them as *large.xml*, *normal.xml* and *small.xml* in the repository root.
3. Click *Export to image* and save them as *large_white.png*, *normal_white.png* and *small_white.png* in the repository root.

##### Building
In the project root run:

`javac -cp src src/*/*.java`

##### Running
Then run the GUI in the project root with:

`java -cp .:src gui.TranslationViewerGUI arg`

Where *arg* is replaced by the scale of the live screen views. Default is *3*, which requires a 1440p resolution to fit. Choose *2* or *1* to run it on smaller resolutions.

Select the repository root of [this](https://github.com/cjuub/okaeri-chibi-translation) project at startup.

## Screenshots
![alt tag](http://cjuub.se/u/1511041532518.png)