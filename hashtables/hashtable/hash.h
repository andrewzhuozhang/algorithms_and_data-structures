#define NUM_BUCKETS 47

// structure of a Node
typedef struct Node {
	char* value;
	struct Node* next;
} NODE; // use typedef to assign alias to struct Node
// NODE m;

NODE* lookup(NODE**, char*);

int hash(char*);

NODE** insert(NODE**, char*);

void print(NODE**);
