#include <stdio.h>
#include <stdlib.h>


int main() {
    
}

int fibonacci(int n) {
    if (n == 0 || n == 1)
        return 1;
    else
        return fibonacci(n-1) + fibonacci(n-2);
}

int memoizedFibonacci(int n) {
    
}