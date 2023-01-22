package fr.ensimag.pseudocode.ima.instructions;

import fr.ensimag.pseudocode.GPRegister;
import fr.ensimag.pseudocode.UnaryInstructionToReg;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class DEL extends UnaryInstructionToReg {

    public DEL(GPRegister op) {
        super(op);
    }

}