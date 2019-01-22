#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#define init_size 1031

// struct for key value pair
typedef struct {
	char *key;
	unsigned int value;
} Item; 

// struct for hash table
typedef struct {
    int size;
    int count;
    Item** table;
} HT;

// helper method to get hash value of string
unsigned int hashstr(char *key) {
	unsigned int val = 0;
	while (*key) {
		val += 31 * val + *key;
		key++;
	}
	return val;
}

// hash function implementing double hashing
// int i is number of attempts starting at 0
unsigned int hash(char *key, int i, unsigned int ht_size){
	unsigned int hs = hashstr(key);
	int h1 = hs  % ht_size;
	int h2 = 1 + (hs % (ht_size - 1));
	return (h1 + i * h2) % ht_size; // h2 is 0 at the first attempt
}

// create new Item pointer to put in hastable
Item *create_item(char *key) {
	Item *new_item = malloc(sizeof(Item));
	new_item->key = strdup(key);
	new_item->value = 1;
	return new_item;
}

// helper method search if a key is in the table, return index or -1(not found)
int search(HT *HT, char *key) {
	unsigned int index = hash(key, 0, HT->size);
	Item *current = HT->table[index];
	int i = 0;
	// current NULL means hitting the end of the sequence, stop searching
	while (current != NULL) {
		if (strcmp(current->key, key) == 0) {
			// return key index if found
			return index;
		}
		i++;
		index = hash(key, i, HT->size);
		current = HT->table[index];
	}
	return -1;
}

// helper method to increment value if Item already in table
void increment(HT *HT, unsigned int index) {
	if (index >= 0) {
		Item *item = HT->table[index];
		item->value += 1;
	}
}

// insert key or increment value
void insert(HT *HT, char *key) {
	unsigned index = search(HT, key);
	// if key not in table
	if (index != -1) {
		increment(HT, index);
	} else {
		Item *item = create_item(key);
		unsigned int index = hash(item->key, 0, HT->size);
		Item *current = HT->table[index];
		int i = 1; // if secondary hashing needed
		while (current != NULL) {
			index = hash(item->key, i, HT->size);
			current = HT->table[index];
			i++;
		}
		HT->table[index] = item;
		HT->count++; // only increment count if new key added
	}
}

// resize the hash table when at capacity
// returns a new populated table
HT* resize(HT *old) {
	// get new size based on the old size
	unsigned int new_size = 0;
	switch (old->size) {
		case 1031:
			new_size = 2063;
			break;
		case 2063:
			new_size = 4127;
			break;
		case 4127:
			new_size = 8263;
			break;
		case 8263:
			new_size = 16529;
			break;
		case 16529:
			new_size = 33071;
			break;
		case 33071:
			new_size = 66161; // added one extra size
			break;
	}
	// create new table
	HT *new = malloc(sizeof(HT)); 
	new->size = new_size;
	new->count = 0;
	new->table = calloc((size_t)new->size, sizeof(Item *));
	// go though old table and rehash
	for (int i = 0; i < old->size; i++) {
		Item *item = old->table[i];
		if (item != NULL) {
			// insert() also takes cares of counts and incrementing
			insert(new, item->key); 
		}
	}
	return new;
}

// clean up text for prerocessing
char* cleanup(char *input) {
	char *text = strdup(input); 
	char *a = text;
	while (*a != '\0') {
	    *a = tolower(*a); // convert to all lower case
		if (!isalpha(*a) && *a != ' ') {
			*a = ' '; // get rid of none alphabetical chars
		}
		a++;
	}
	return text;
}

// main function
int main() {
	// itiatialize new HT with initial size
	HT *ht = malloc(sizeof(HT)); 
	ht->size = 1031;
	ht->count = 0;
	ht->table = calloc((size_t)ht->size, sizeof(Item *));

	// comparator for sorting
	// sorting by key
	int cmpkey(const void *a,const void *b) {
		Item **x = ((Item **)a);
    	Item **y = ((Item **)b); 
		// printf("%s | %s\n", (*x)->key, (*y)->key);
		return  strcmp((*x)->key, (*y)->key);
	}
	// sorting by value, descending order
	int cmpval(const void *a,const void *b) {
		Item **x = ((Item **)a);
    	Item **y = ((Item **)b); 
		// printf("%u | %u\n", (*x)->value, (*y)->value);
		return  (*y)->value - (*x)->value;
	}
	// reading from file line by line
  	FILE *fp;
   	char line[100];
   	fp = fopen("CompleteShakespeare.txt", "r");
   	// clean up each line and tokenize, then insert tokens into table
	while (1) {
	    if (fgets(line,100, fp) == NULL) break;
	    char * text = cleanup(line);
	    char * pch;
		pch = strtok (text," ");
		while (pch != NULL)
		{
		    insert(ht, pch);
		    if (ht->count >= ht->size/2) {
		    	// resize creates new table and rehashes in old keys
		    	ht = resize(ht); 
		    }
	 		pch = strtok (NULL, " ");
		}
	}
   	fclose(fp);

  	// array to store all items for sorting
	Item* all_words[ht->count];
	unsigned int j = 0;
  	for (int i = 0; i < ht->size; i++) {
  		if (ht->table[i] != NULL) {
  			all_words[j] = ht->table[i];
  			j++;
	  	}
  	}

  	// compare by value then by key with stable qsort()
  	qsort (all_words, j , sizeof(Item *), cmpkey);
  	qsort (all_words, j , sizeof(Item *), cmpval);

  	// print out key value pairs sorted
  	for (int i = 0; i < ht->count; i++) {
  		printf("%s\t%u \n", all_words[i]->key, all_words[i]->value);
  	}

	return 0;
}