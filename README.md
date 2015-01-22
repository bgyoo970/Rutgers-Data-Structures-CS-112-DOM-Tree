# Rutgers-Data-Structures-CS-112-DOM-Tree
## Fall 2013 with Andrew Tjang

The objective is to build a document object tree (DOM) and be able to perform operations on the HTML text given. The given methods to fill out are build(), boldRow(), removeTag(), and addTag(). The collective possible operation to perform (based on the user's input) are:

- print HTML
- replace tag
- boldface row
- delete tag
- add tag
- quit operation

and each operation corresponds to the first character of the word *(e.g. input "p" to print HTML, input "r" to replace tag, input "b" to boldface row, etc.)*

A DOM tree is used as the data structure to store the data from the HTML file and to print out the resulting HTML text correctly. Recursion is used heavily as inorder traversal is used to traverse the tree. Helper methods are used.

###### A note.
Some parts of the code seem to be a bit buggy. Boldface does not seem to work correctly as well
