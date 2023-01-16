package fr.ensimag.ima.pseudocode.arm.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.RegisterIMA;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class STR extends BinaryInstruction {
    public STR(RegisterIMA op1, DAddr op2) {
        super(op1, op2);
    }
}