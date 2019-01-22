#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define MAX 10000

// structs
typedef struct Position {
    int line; // line number
    int offset; // pos on the line
    struct Position *next;
} POS;

typedef struct TreeNode {
    char *word; // word in node
    POS *positions; // position of word
    struct TreeNode *parent;
    struct TreeNode *left;
    struct TreeNode *right;
} NODE;


// insert node, return tree
NODE *insertNode(NODE *tree, char *word, POS *pos) {
    NODE *root = tree; // tree points to root node
    NODE *last = NULL; // node to keep track of parent nodes

    if (root == NULL) { // if not found then make new node
    	NODE *new = (NODE *) malloc(sizeof(NODE));
    	// append new node to left or right
    	new->parent = last;
    	new->left = NULL;
    	new->right = NULL;
    	// put in word
    	new->word = (char *) malloc(sizeof(char) * 100);
    	strcpy(new->word, word);
    	// put in position
    	new->positions = (POS *) malloc(sizeof(POS)); // new pos pointer
        new->positions->line = pos->line;
        new->positions->offset = pos->offset;
        new->positions->next = root->positions;
    	// root = new;
    	return root;
    } else if (strcmp(word, root->word) == 0) {
        // if word already exist
        POS *newpos = (POS *) malloc(sizeof(POS)); // new pos pointer
        // add pos into beginning of positions of root
        newpos->line = pos->line;
        newpos->offset = pos->offset;
        newpos->next = root->positions;
        root->positions = newpos;
        // root->positions = (POS *) malloc(sizeof(POS)); // new pos pointer
        root->positions = pos;
        return root;
    } else if (strcmp(word, root->word) < 0) {
    	// if word not found and smaller
    	last = root; // keep track of parent
        return insertNode(root->left, word, pos);
    } else {
    	// if word not found and larger
    	last = root; // keep track of parent
        return insertNode(root->right, word, pos);
    }
}

// remove pos(one instance of the word only)
NODE *removePos(NODE *tree, POS pos) {
	// find position
	NODE *root = tree;
	POS *tmppos = root->positions
	// if positions found. base case
	if (tmppos->line == pos.line 
			&& tmppos->offset == pos.offset) {
		while (tmppos->next != NULL) {
			// remove current and update rest
			tmppos = NULL;
			tmppos = tmppos->next;
		}
	}
	removePos(root->left, pos);
	removePos(root->right, pos);
	return root;
}

// remove a word(the node)
NODE *removeWord(NODE *tree, char[] word) {
	NODE *root = tree;
	if (strcmp(word, root->word) == 0) {
		// remove the word and reconstruct the tree
		// ...
		return root;
	} else if (strcmp(word, root->word) < 0) {
		return removeWord(root->left, word);
	} else{
		return removeWord(root->right, word);
	}
}

// remove all POSs with the same line number
NODE *removeLine(NODE *tree, int line) {
	NODE *root = tree;
	// traverse through the whole tree find pos
	if (root->positions->line == line) {
		root->positions = NULL;
	}
	root = removeLine(root->left, line);
	root = removeLine(root->right, line);
}

// helper method for getNode
// checks if the position exists and validates it for printing
int contain(POS *list, int line, int pos) {
	if (list == NULL) { return 0; }
	if (list->line == line && list->offset == pos) { return 1; }
	return contain(list->next, line, pos);
}

// helper method for saveTree 
NODE *getNode(NODE *t, int line, int pos) {
	if (t == NULL) { return NULL; }
	if (contain(t->positions, line, pos)) {
  		// line pos is included in a node, hence node exists in tree
  		return t;
  	}
  	NODE *node = getNode(t->left, line, pos); // search left tree
  	if (node = NULL) {
    	// search right tree if left is null
    	return getNode(t->right, line, pos);
  	} else {
    	return node; // if found returns the node found, if not return NULL
  	}
}

// print nodes in tree to a text file
void saveTree(NODE *t, char *output) {
	FILE *f;
	f = fopen(output, "a"); // file for output
  	int line = 1; // keep track of line count for formating
  	while (1) {
   	 int pos = 1; // keep track of offset
    	while (1) {
	      	// search for node that contains the word at position (line, pos)
	      	NODE *node = getNode(t, line, pos);
	        	// print the word (node->word) to the output file
	      	if (node != NULL) {
	        	if (line > 1 && pos == 1) { printf("\n");}
	        	fprintf(f, "%s ", node->word);
	        	pos++;
	      	} else {
	        	// node is NULL and pos not found
	        	if (pos == 1) {
	          		return; // if node not found at the beginning of a line
	        	} // break pos loop
	      	}
    	} // pos is not one and word is not found
    line++; 
  	}
  	fclose(f);
}

int main() {
	// read input from stdin:
	char file_in[100];
	char file_out[100];
	// first line input/output
	scanf("%s %s\n", file_in, file_out);
	FILE *f;
	f = fopen(file_in, "r");
	char str[MAX]; // string holder	    
	while (fgets(str, MAX, f) != NULL) { // get a line till new line
		// construct nodes and tree from words read from input
		int line_count = 0; 
		NODE *tree = NULL; // node pointer to point to the root of tree
		while (1) {
		    	line_count++;
		    	int offset = 0;
		    	// printf("%s", str);
		    	while (1) {
		        	// read words from line
			        offset++;
			        char word[100];
			        sscanf(str, "%s", word); // read word from line str
			        POS *tmp = (POS *) malloc(sizeof(POS)); // construct POS for word/node
			        tmp->offset = (int) malloc(sizeof(int));
			        tmp->line = (int) malloc(sizeof(int));
			        tmp->line = line_count;
			        tmp->offset = offset;
			        tmp->next = NULL; // ??? 
			        // and remember word position
			        tree = insertNode(tree, word, tmp);
			        // insert into the tree
		    	}
		} else { break; }
	}
	    // then commands for remove/insert

	    // manipulate tree based on commands

	  	// save to a text file using saveTree method
	saveTree(tree, file_out);
	return 0;
}
