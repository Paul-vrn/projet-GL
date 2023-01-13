package fr.ensimag.ima.pseudocode.arm.instructions;

import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

/**
 * @author Ensimag
 * @date 01/01/2023
 */
public class ASCII extends UnaryInstruction {

    public ASCII(Operand operand) {
        super(operand);
    }

    @Override
    protected String getName() {
        return ".ascii";
    }
}
