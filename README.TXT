By Tomas Barragan and Akramov Akbar

Put people.txt in the same folder where you run the program (I placed it in src). Compile and run

This project loads a dataset of people (each line in people.txt is one record with 13 fields separated by semicolons). 
The program:
-Loads all records into a Binary Search Tree (BST)
-Lets you search by given name + family name (case-insensitive)
-Sorts the entire dataset by given name (then family name) using a custom heap (heap sort)
-Finds the most frequent words from selected text fields using a custom hash map (quadratic probing)

Sample output:
Loaded records: 500
BST info: size=500, height=...
Search test prints at least one record
Sort test prints 10 records in sorted order
Most frequent words prints top words + counts
Exception test confirms the custom exception works