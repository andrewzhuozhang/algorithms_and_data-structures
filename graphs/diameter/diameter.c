#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include <string.h>
#include <sys/stat.h>

#define MAX 1023

unsigned verbose = 0;
unsigned n, m;             //num vertices, num edges
unsigned *row, *rowOffset; //compressed sparse row

//string to unsigned
static unsigned s2u(const char s[]){
  unsigned a = 0;

  while (*s != '\n')
    a = a*10 + (*(s++) - '0');
  return a;
}

void readAdjGraph(char *filename){
  uint64_t i, *off;
  unsigned numItem;
  struct stat buf;
  char *buffer;
  int status;
  FILE *fp;

  status = stat(filename, &buf);
  if (status){
    fprintf(stderr, "no such file %s\n", filename);
    exit(0);
  }
  if (verbose)
    printf("file %s has %lu bytes\n", filename, buf.st_size);

  buffer =     (char*)malloc(buf.st_size);
  off    = (uint64_t*)malloc(buf.st_size * sizeof(uint64_t));

  fp = fopen(filename, "rb");
  if (!fp){
    fprintf(stderr, "can't open file: %s\n", filename);
    exit(0);
  }
  i = fread((void*)buffer, 1, buf.st_size, fp);
  if (i != buf.st_size){
    fprintf(stderr, "read %lu bytes, but file size is %lu\n",
	    i, buf.st_size);
    exit(0);
  }
  fclose(fp);

  for (i=0, numItem=0; i<buf.st_size; i++)
    if (buffer[i] == '\n')
      off[numItem++] = i+1;

  buffer[off[0]-1] = 0;
  if (strcmp(buffer, "AdjacencyGraph") != 0){
    fprintf(stderr, "file %s is not AdjacencyGraph\n", filename);
    exit(0);
  }

  n = s2u(buffer + off[0]);
  rowOffset = (unsigned*)malloc(sizeof(unsigned) * (n+1));
  m = s2u(buffer + off[1]);
  row = (unsigned*)malloc(sizeof(unsigned) * m);

  if (verbose)
    printf("n %u, m %u\n", n, m);

  for (i=0; i<n; i++)
    rowOffset[i] = s2u(buffer + off[i+2]);
  rowOffset[n] = m;
  for (i=0; i<m; i++)
    row[i] = s2u(buffer + off[i+n+2]);

  free(off);
  free(buffer);
}

int main(int argc, char* argv[]){
  char *filename;
  unsigned i;
  int c;
  int depth[MAX]; // depth to keep track of which level it is
  int longest = 0;
  int path[MAX] = {0};


  filename = NULL;
  while ((c = getopt(argc, argv, "f:v")) != -1){
    switch (c){
    case 'f':
      filename = (char*)malloc(sizeof(char) * (strlen(optarg) + 1));
      strcpy(filename, optarg);
      break;
    case 'v':
      verbose = 1;
      break;
    default:
      break;
    }
  }
  for (i=optind; i<argc; i++){
    filename = (char*)malloc(sizeof(char) * (strlen(argv[i]) + 1));
    strcpy(filename, argv[i]);
  }
  if (!filename){
    fprintf(stderr, "filename?\n");
    return 0;
  }
  readAdjGraph(filename);
  // rowOffset and row for data
  // initialize depth list
  for (int a = 0; a < MAX; a++)
  {
    for (int i = 0; i < MAX; i++)
    {
      depth[i] = -1;
    }
    int source = a;
    depth[source] = 0; // mark source
    int level = 0;
    int notDone = 1;
    // keep visiting neighbors till all visited
    while (notDone)
    {
      for (int i = 0; i < MAX; i++) 
      {
        if (depth[i] == level) // get all nodes at source level
        {
          source = i;
          // printf("\n source is %d \n", i);
          // mark all neighbors of source
          for (int j=rowOffset[source]; j<rowOffset[source+1]; j++)
          {
            if (depth[row[j]] == -1)
              {
                depth[row[j]] = level+1; // mark its neighbors
              }
          }
        } 
      }
      level++; // if not done, incresease level
      
      // check if all visited
      notDone = 0;
      for (int j = 0; j < MAX; j++)
      {
        if (depth[j] == -1) // if there's any -1 values
        {
          notDone = 1;
          break;
        }
      }
      for (int i = 0; i < MAX; i++)
      {
        if (depth[i] > path[a]) {
          path[a] = depth[i];
        }
      }
    }
    
  }
  for (int i = 0; i < MAX; i++)
  {
    if (path[i] > longest)
    {
      longest = path[i];
    }
  }
  printf("Diameter of graph is %d\n.", longest);
  return 0;
}
