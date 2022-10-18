memoization = None

def fibonacci(n):
    if n == 0 or n == 1:
        return 1
    
    return fibonacci(n-1) + fibonacci(n-2)


def memoized_fibonacci(n):
    global memoization
    if memoization is None: 
        memoization = [0 for i in range(n+1)]

    if n == 0 or n == 1: 
        return 1

    return helper(n-1) + helper(n-2)

def helper(n): 
    global memoization

    if memoization[n] == 0: 
        memoization[n] = memoized_fibonacci(n)
        return memoization[n]
    else:
        return memoization[n]


print (fibonacci(40))