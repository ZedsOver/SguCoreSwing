SGU Core Swing:

Is an Lightweight Java Swing UI code implementation from #GameUtility.

This app is a general purpose, for read/write binary files.

This is only a core app module, and principal features is:

-Indexing all contained in file
-Explore indexed data
-you can modifiy index file
-import|export file
-import|export raw file

Formats icluded:
-AFS Assets File System      READ|ERITE
-ISO only supports ISO-9660  READ|WRITE
-Directory indexer

Swing UI features implemented:
-open file, ISO|AFS
-save file, as ISO|AFS
-tabbed files: 
    -Path bar implemented
    -aditional option added
        -show index as HEX|DEC
        -show VDIR this active shor VDIR files
        -show REAL this activate use real index number instead of order index
    -Dnd operation INSER|REPLACE
    -Create new VDIR
    -inserted or replaced files is diferent identified
    -sortable  columns: Types, Names, Offsets and sizes