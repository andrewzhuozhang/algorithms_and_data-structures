#include <stdio.h>
#include <math.h>
#define MAXC 50 // max length of coordinates
#define DIM 1000 // max number of points

double COST[DIM] = {0}; // cost after adding ith point
int PREV[DIM] = {0}; // previous point at ith point
int TOUR[DIM] = {0}; // tour to keep track of the route
int CNNT[DIM] = {0};
double PTS[DIM][2];

// get points
void read_points() 
{
	static const char filename[] = "1000.txt";	
	FILE *f = fopen(filename, "r");
	if (f != NULL)
	{
		char point[MAXC];
		int i = 0;
		while (fgets(point, sizeof(point), f) != NULL)
		{
			double x, y;
			sscanf(point, "%lf\t%lf", &x, &y);
			// stores x, y to matrix PTS 
			PTS[i][0] = x; 
			PTS[i][1] = y;
			// printf("%f | %f \n", x, y);
			i++;
		}	
	} 
	else 
	{
		perror(filename);
	}
}

// get distance between two points a and b
double distance(int a, int b) 
{
	// get 4 coordinates
	double a1 = PTS[a][0];
	double a2 = PTS[a][1];
	double b1 = PTS[b][0];
	double b2 = PTS[b][1];
	double dist = (a1 - b1) * (a1 - b1) + (a2 - b2) * (a2 - b2);
	dist =  sqrt(dist);
	return dist;
}

int main()
{
	read_points(); // call method to populate PTS
	double total = 0.0;
	// for first 3 points, just add
	COST[1] = COST[0] + distance(0, 1);
	PREV[1] = 0;
	COST[2] = COST[1] + distance(1, 2);
	PREV[2] = 0;
	// for 4th on, get COST and PREV
	for (int k = 3; k < DIM; k++) // kth point
	{
		// calculate cost connecting to from k-2 to 0 then get min
		double tmp_cost; // temp cost at point
		COST[k] = 9999;
		for (int i = k-1; i > 0; i--) // calculate all posibilities for kth pt
		{
			tmp_cost = COST[i] + distance(i-1, k); 
			for (int j = i; j < k-1; j++) // add additional dist
			{
				tmp_cost += distance(j, j+1);
			}
			if (tmp_cost < COST[k]) // update cost and prev if shorter
			{
				COST[k] = tmp_cost;
				PREV[k] = i-1;
			}	
		}
	}
	// calculate distance
	for (int i = DIM-1; i > 0; ) // i is prev, j is prev index in TOUR	
	{
		CNNT[i] = 1;
		i = PREV[i];
	}
	int k = 0;
	int h = DIM-1;
	for (int i = 0; i < DIM; i++)
	{
		// CNNT marks if point belongs to path 0 or 1
		if (CNNT[i] == 0)
		{
			TOUR[k] = i;
			k++;
		}
		if (CNNT[i] == 1)
		{
			TOUR[h] = i;
			h--;
		}
	}
	for (int i = 0; i < DIM-1 ; i++)
	{
		total += distance(TOUR[i], TOUR[i+1]);
	}
	total += distance(TOUR[0], TOUR[DIM-1]);
	printf("%lf", total);
	return 0;
}

