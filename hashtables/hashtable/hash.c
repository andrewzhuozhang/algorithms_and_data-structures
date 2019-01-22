#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "hash.h" // have to include the correct header; use "" as it's user defined

// #define NUM_BUCKETS 47

// // structure of a Node
// typedef struct Node {
// 	char* value;
// 	struct Node* next;
// } NODE; // use typedef to assign alias to struct Node
// // NODE m;

// implement a lookup func to look for a given value in HT
NODE* lookup(NODE** htable, char* value) {
	int hash_value = hash(value);
	NODE* list = htable[hash_value];
	while(list != NULL) {
		if (strcmp(list->value, value) == 0) { break; }
		else { list = list->next; }
	}
	return list; // points at a NODE if matches, otherwise points to NULL
}

int hash(char* value) {
	int i = 0;
	for (; *value != '\0'; value++) {
		i = i + (int) (*value);
	}
	return (i * 10001) % NUM_BUCKETS;
}

NODE** insert(NODE** htable, char* value) {
	// insert value into htable
	NODE* node = lookup(htable, value); // see if value is in 
	if (node != NULL) {
		return htable;
	}
	int hash_value = hash(value);
	NODE* list = htable[hash_value]; // go to the corresponding linked list
	NODE* new_node = (NODE *) malloc(sizeof(NODE));
	new_node->value = (char *) malloc(strlen(value) + 1);
	sprintf(new_node->value, "%s", value); // sprintf value to new_node 
	new_node->next = list;
	list = new_node; // update beginning of list
	htable[hash_value] = list;
	return htable;
}

void print(NODE** htable) {
	for (int i = 0; i < NUM_BUCKETS; i++) {
		// print each linked list
		NODE* list = htable[i];
		if (list == NULL) { continue; }
		while(list != NULL) {
			printf("%s; ", list->value);
			list = list->next;
		}
		printf("\n");
	}
}

// int main() {
// 	// hash table as a pointer to NODE*
// 	NODE** hash_table = (NODE**) malloc(NUM_BUCKETS * sizeof(NODE*)) ;
// 	for (int i = 0; i < NUM_BUCKETS; i++) {
// 		hash_table[i] = NULL;
// 	}
// 	hash_table = insert(hash_table, "first string");
// 	hash_table = insert(hash_table, "second string");

// 	print(hash_table);

// }