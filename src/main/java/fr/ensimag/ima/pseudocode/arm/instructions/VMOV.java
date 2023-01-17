package fr.ensimag.ima.pseudocode.arm.instructions;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.TernaryInstruction;

public class VMOV extends TernaryInstruction {

        public VMOV(Operand op1, Operand op2, Operand op3) {
            super(op1, op2, op3);
        }

        public VMOV(DVal op1, GPRegister op2) {
        super(op1, op2);
    }


}
