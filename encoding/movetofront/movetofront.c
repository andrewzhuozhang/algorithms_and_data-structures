#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

enum Dir {Transform, Reverse};

long numbytes;

char *readfile(char input[]) // read file into char*
{
  // read file into one string
  char *originaltxt;
  FILE *infile = fopen(input, "r");
  if (infile != NULL)
  {
    fseek(infile, 0L, SEEK_END);
    numbytes = ftell(infile); //get num of bytes
    fseek(infile, 0L, SEEK_SET);  
    originaltxt = (char*)calloc(numbytes, sizeof(char));
    if (originaltxt == NULL) perror(input);
    // read into buffer
    int num = fread(originaltxt, sizeof(char), numbytes, infile); 
    if (num == 0) 
    {
        printf("nothing read");
    }
    fclose(infile);
    return originaltxt;
  } 
  else 
  {
    perror(input);
    return NULL;
  }
}

void writefile(char *outputtext, char output[], int inputlen)
{
  FILE *outfile = fopen(output, "w");
  if (outfile != NULL)
  {
    fwrite(outputtext, sizeof(char), inputlen, outfile);
    fclose(outfile);
  } 
  else 
  {
    perror(output);
  }
}


// return index of char from list
int search(char inputchar, char* charlist)
{
  int charindex = 0;
  for (int i = 0; i < 128; i++) 
  {
    if (charlist[i] == inputchar) 
    {
        charindex = i;
        break;
    }
  }
  return charindex;
}

// puts char in front of the char array
void movechar(int currindex, char* charlist)
{
  int curr = charlist[currindex];
  for (int i = currindex; i > 0; i--)
  {
    charlist[i] = charlist[i-1];
  }
  charlist[0] = curr;
}
// encoding
// input and output are file names
void moveToFront(char input[], char output[], char* charlist)
{
  // read input
  char *inputtext = readfile(input);
  long inputlen = strlen(inputtext);
  char* outputtext = (char*)malloc(inputlen * sizeof(char));
  for (int i = 0; i < inputlen; i++) 
  {
        // store char index into array 
        outputtext[i] = (char)search(inputtext[i], charlist);
        // move char to front 
        movechar(outputtext[i], charlist);
  }

  writefile(outputtext, output, inputlen);
}


void reverse(char input[], char output[], char *charlist)
{
	// decoding
  // read input
  char *inputtext = readfile(input);
  long inputlen = numbytes;
  char* outputtext = (char*)malloc(inputlen * sizeof(char));
  for (int i = 0; i < inputlen; i++) 
  {
        // store char index into array 
        int inputindex = (int)inputtext[i];
        outputtext[i] = charlist[inputindex];
        // move char to front 
        movechar(inputtext[i], charlist);
  }
 
  writefile(outputtext, output, inputlen);
}

int main(int argc, char *argv[]){
  int c;
  enum Dir direction;
  char *input, *output;
  // list of legal symbols
  char* charlist = (char*)malloc(sizeof(char)*128);
  for (int i = 0; i < 128; i++)
  {
    charlist[i] = i;
  }

  direction = Transform;
  input = output = NULL;
  while ((c = getopt(argc, argv, "i:o:tTrR")) != -1){
    switch (c){
    case 'i':
      input = (char*)malloc(sizeof(char) * (strlen(optarg) + 1));
      strcpy(input, optarg);
      break;
    case 'o':
      output = (char*)malloc(sizeof(char) * (strlen(optarg) + 1));
      strcpy(output, optarg);
      break;
    case 't':
    case 'T':
      direction = Transform;
      break;
    case 'r':
    case 'R':
      direction = Reverse;
      break;
    default: break;
    }
  }
  if (input == NULL || output == NULL){
    fprintf(stderr, "usage: -i input -o output\n");
    fprintf(stderr, "\t-t for move-to-front (default)\n");
    fprintf(stderr, "\t-r for reverse transformation\n");
    exit(1);
  }

  if (direction == Transform)
    moveToFront(input, output, charlist);
  else
    reverse(input, output, charlist);

  return 0;
}
