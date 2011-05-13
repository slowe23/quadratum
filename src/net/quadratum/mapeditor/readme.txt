Map editor (net.quadratum.mapeditor.MapEditor)

Command line arguments:
To create a new map, specify the width, height, and save file name as command line arguments (in that order).
To edit an existing map, specify the filename.  You can optionally specify a new filename for the save file; otherwise, the old file will be written over when saving.


Using the program:
The toolbar on the left displays the possible kinds of terrain to apply.
Click something to select it.  Then click and/or drag on the map to add the selected thing to the map.

The first option is the eraser tool.  This erases both player start areas and terrain features.

The next set of options are terrain features.  Hold shift while pressing/dragging to selectively erase the currently selected feature, leaving other map features unchanged.  Multiple terrain features can exist on a given square.

The last set of options are the placement areas for the players.  Hold shift while pressing/dragging to selectively erase the currently selected player's area.  Multiple placement areas cannot exist on a given square, so applying this to a square will replace any placement area previously there.

To save and/or quit, close the window.  A dialog will pop up allowing you to cancel (keep editing), save and quit, or quit without saving.


Notes:
The program runs pretty slowly so dragging the mouse from one square to another may skip squares in between.

There is also no thread safety so the program may behave unpredictably if stressed.