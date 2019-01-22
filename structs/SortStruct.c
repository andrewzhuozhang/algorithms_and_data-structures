#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define MAX 10000
#define DEBUG 0
int num_strings = 0;
/* 
if(DEBUG == 1)
    printf("1"); 
*/

//define String Structure
struct String {
    char *s;
    struct String *next;
};

struct String *list;
void printstruct(struct String *list);

void input(char **str)
{
    char input[MAX] = {0}; // important to initlize or you will get wierd ? characters
    char c = 0;
    int i = 0;
    START: while ((c = getchar()) != EOF)
    {
        input[i] = c;
        if (c == ';')
        {
            if ((strcmp(input, ";")) == 0)
            {
                str[num_strings] = (char *)malloc(MAX * sizeof(char));
                strcpy(str[num_strings], "");
                for (int f = 0; f < i+2; f++) memset(&input[0], 0, sizeof(input)); //clear input
                num_strings++;
                i = 0;
                goto START; // if a double ; is found return to getchar
            }
            input[i] = '\0'; //set ; to NULL
            str[num_strings] = (char *)malloc(MAX * sizeof(char));//allocate a memory to a node
            strcpy(str[num_strings], input);
            num_strings++;
            for (int f = 0; f < i; f++) memset(&input[0], 0, sizeof(input));//clear input
            i = 0;
            goto START; // don't continue if you capture a word before a ; return to getchar
        }
        else
            i++; //if no ; is found then go to next character
    }
    // if EOF is hit then everything left in input must be what's left to parse. and no ; remain
    str[num_strings] = (char *)malloc(MAX * sizeof(char));
    strcpy(str[num_strings], input);
    num_strings++;
    for (int f = 0; f < i; f++)
        memset(&input[0], 0, sizeof(input));
}

void get_strings(char **str)
    {
        struct String *temp = (struct String *)malloc(sizeof(struct String)); //allocate memory to new temp linked list
        temp = NULL;
        list = NULL;
        int i;
        for (i = 0; i < num_strings; i++)
        {
            struct String *node = (struct String *)malloc(sizeof(struct String)); //allocate memory to new node linked list
            node->s = (char *)malloc(strlen(str[i])); //allocate memory for the text of new node
            strcpy(node->s, str[i]);
            node->next = temp; //set the node into the stack of linked list
            temp = node; //set the node to start at this new node (LIFO)
        }
        //reverse node so that FIFO occurs. Optional
        for (i = 0; i < num_strings; i++)
        {
            struct String *node = (struct String *)malloc(sizeof(struct String));
            node->s = (char *)malloc(strlen(str[i]));
            strcpy(node->s, temp->s);
            node->next = list;
            list = node;
            temp = temp->next;
        }
}

void sort_strings(struct String *list)
{
    struct String *node = (struct String *)malloc(sizeof(struct String));
    node->s = (char *)malloc(sizeof(char));
    strcpy(node->s, "");
    node->next = list;
    list = node;
    int i, j;
    char *a = (char *)malloc(sizeof(char));

    struct String *temp1;
    struct String *temp2;
    struct String *pos;
    struct String *min;
    int length = num_strings;
    // bubble sort array
    temp1 = list;
    temp2 = temp1->next;
    i = 1;
    while (i)
    {
        i = 0;
        while (1)
        {
            if (temp2 == NULL) {
                break;
            }
            if ((strlen(temp1->s)) > (strlen(temp2->s)))
            {
                a = temp1->s;
                temp1->s = temp2->s;
                temp2->s = a;
                i = 1;
            }
            temp1 = temp1->next;
            temp2 = temp1->next;
        }
          temp1 = list;
          temp2 = temp1;
    }
}

void printstruct(struct String *list)
{
    struct String *temp;
    struct String *last;
    temp = list;
    while (1) //get last element of linked list
    {
        if (temp->next == NULL) break;
        temp = temp->next;
    }
    last = temp;
    temp = list;
    while (1)
    {
        next:
            if (temp == NULL)
                break;
            if ((strcmp(temp->s, "")) == 0) // if a " " is found that means there was a ;;. print a \n
            {
                printf("\n");
                temp = temp->next;
                goto next;
            }
            else if (temp != last) { // check if we're not on the last node
            printf("%s\n", temp->s);
            }
            else printf("%s", last->s);
            temp = temp->next; // increment linked list
    }
}

void freeStruct(struct String *list) {
    struct String *runner = list;
    struct String *next_pointer;
    while (1)
    {
        if (runner == NULL)
            break;
        next_pointer = runner->next;
        free(runner->s);
        free(runner);
        runner = next_pointer;
    }
}

int main() {
    char **str;
    str = (char**)malloc(MAX * sizeof(char*));
    input(str);
    get_strings(str);
    sort_strings(list);
    printstruct(list);
    return 0;
}