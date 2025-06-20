When Running the program, first you must enter a .csv or .xlsx(Excel) file from memory. 
The file should be formatted accordingly:
(Row x) Object Name (ex. Parrot)
(Row x+1)(Column 0) Feature Name (ex. Color) (Row x+1)(Column 1) Value (ex. 1)
(Row x+2)(Column(0) Feature Name (ex. WingSpan) (Row x+2)(Column 1) Value (ex. 2.43)
...
(Row x+n) New Object Name (ex. Desk)
...


Additional Info:
if the first character of a feature is a * - the feature is considered important.
if the first character of a feature is a ! - the feature is considered less important.
If a row has more than two entries, the file will become invalid.
If a row is empty, it is ignored.
Every file must start with optional empty rows and a declaration of the first object immedietly(Entry with 1 row).
A file with a non number Feature value will become invalid.
A test.csv file is added in the document as an example with multiple Objects, each having multiple Features, that is legal in the system.

After the csv file is successfully entered, you are given a list of your items and are asked to provide the indexes of 2 items to compare.
Indexes are 1 -> number of items, the list of items is given numbered so that it is clear which index matches which object.
You will then recieve a score of 0-7 of how similiar the items are
After 2.5 seconds, the terminal will clear and you will be able to make a new comparison.
Entering -1 will terminate the program.
