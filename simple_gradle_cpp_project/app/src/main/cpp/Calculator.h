#ifndef CALCULATOR_H
#define CALCULATOR_H

// Interface (pure abstract class) for Calculator operations
class ICalculator {
public:
    virtual ~ICalculator() = default;
    
    virtual int add(int a, int b) = 0;
    virtual int subtract(int a, int b) = 0;
    virtual int multiply(int a, int b) = 0;
};

// Concrete implementation of ICalculator
class Calculator : public ICalculator {
public:
    int add(int a, int b) override;
    int subtract(int a, int b) override;
    int multiply(int a, int b) override;
};

#endif // CALCULATOR_H
