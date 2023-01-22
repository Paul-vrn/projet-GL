package fr.ensimag.pseudocode.arm.instructions;

import fr.ensimag.pseudocode.Operand;
import fr.ensimag.pseudocode.UnaryInstruction;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class FLOAT extends UnaryInstruction {

    public FLOAT(Operand operand) {
        super(operand);
    }

    @Override
    protected String getName() {
        return ".float";
    }
}
