CS310 HW4 implement hashtable with double hashing

What I have done:

1. Implementing Hashtable:

    After an intensive session of diagram drawing I decided to make the hashtable itself into a struct so I can easily resize it without having to maintain multiple global variables for keeping word count for different tables and such, it's just easier to keep all the data encapsuled inside the struct. I then declared structs for the Items in the hashtable. 
    
    I then went ahead and implemented the hash functions by using a helper method hashstr to get the hashed values of the key strings, which is the base of the primary hash function. I implemented another method to combine hash1 and hash2, using a variable i to keep track of attempts. i starts at 0 so for the first attempt hash2 doesn't come into play. 
    
    To add Items into the table I implemented a few mathods to create the Item structs from the passed in keys; then for inserting I implemented helper methods search to see if the key is already in the table and increment to simply increment its value if it is. Otherwise I just insert the newly created Item into the table and update the count.
    
    For resizing I had to go one size up since the final word count if way beyond the half point of the biggest prime given in the write-up. For my resize method I pass in the old table to get its count and all Items, then rehash all the keys int the Items into their new positions. I could've just rehashed the keys and kept their original counts but instead I re-updted the values as well just to be able to reuse the same insert function to be more modular. 
    
2. Text Preprocessing
    
    To clean up the text I implemented a cleanup function to simply get rid of the non-alphabetical characters and convert all chars into lower case. The function returns a new char array to be broken down into tokens.
    
    I used the c function strtok to tokenize the strings read from the file, line by line. I at first used fread to get all the text into one giant string but it wasn't working as well so I switched to reading line by line with plain and simple fgets. 
    
3. Sorting and Printing

    For sorting and printing the word and frequences I implemented two comparators for qsort. Since qsort is a stable sort, I sorted the array by the keys first then by value in descending order to keep the relative alphabetical order among Items with the same value. One thing that took me a while to figure out was that I had to pass in double pointers to the Items to get them to compare properly as otherwise only the memory locations are compared.
    
    For printing I went throught the sorted Item array and printed out all the key and values, then I used the > operand to write the output into the output.txt file.
    
Special Features:

    There's nothing special about the features I implemented but I did use a few functions from c that I thought was really cool. First I really like how efficient qsort is even thought it gave me a headache with its weird passing in void pointers requirement. But I guess it is the c way of passing in generics since it needs to be able to pass in pointers to any kind of data types or structs. 
    
    There's also the strdup function that I was able to use more confidently and understand better its functionality. I always had trouble with char pointers in c but after this project I feel a little more confident. 
    
    The last thing worth mentioning is the strtok function which was really useful. I at first tried to implement my own function for tokenizing but having this function is really making my code more efficient and elegent. 
    
    Overall I really enjoyed this project. I used to be stressed out every time I had to use c since I felt like I didn't learn enough in cs240 but just reading through the documentation and working on projects helped me learn more and build confidence in this language. 
