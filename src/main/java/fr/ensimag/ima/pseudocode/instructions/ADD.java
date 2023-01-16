package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegisterIMA;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class ADD extends BinaryInstructionDValToReg {
    public ADD(DVal op1, GPRegisterIMA op2) {
        super(op1, op2);
    }
}
