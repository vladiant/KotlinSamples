#include "Calculator.h"
#include <iostream>

int main() {
    Calculator calc;
    
    int a = 10;
    int b = 5;
    
    std::cout << "Testing Calculator:" << std::endl;
    std::cout << a << " + " << b << " = " << calc.add(a, b) << std::endl;
    std::cout << a << " - " << b << " = " << calc.subtract(a, b) << std::endl;
    std::cout << a << " * " << b << " = " << calc.multiply(a, b) << std::endl;
    
    return 0;
}
