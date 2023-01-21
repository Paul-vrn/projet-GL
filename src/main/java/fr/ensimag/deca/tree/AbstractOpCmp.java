package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl21
 * @date 01/01/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //TODO : class objects.

        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        this.setType(compiler.environmentType.BOOLEAN);
        if(this.getOperatorName().equals("==") || this.getOperatorName().equals("!=")) {
            if (!(((type1 == null || type1.isClass()) && (type2 == null || type2.isClass()))
                || ((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat()))
                || (type1.isBoolean() && type2.isBoolean()))) {
                throw new ContextualError(compiler.displaySourceFile() + ":"
                        + this.getLocation().errorOutPut() + ": Compare operation type mismatch", this.getLocation());
            }
        }else {
            if (!((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat()))) {
                throw new ContextualError(compiler.displaySourceFile() + ":"
                        + this.getLocation().errorOutPut() + ": Compare operation type mismatch", this.getLocation());
            }
        }

        return this.getType();
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        getLeftOperand().codeGenExpr(compiler, n);
        if (n < Register.RMAX) {
            getRightOperand().codeGenExpr(compiler, n + 1);
            compiler.addInstruction(new CMP(Register.getR(n+1), Register.getR(n)));

        } else {
            compiler.addInstruction(new PUSH(Register.getR(n)));
            compiler.getMemory().increaseTSTO();
            getRightOperand().codeGenExpr(compiler, n);
            compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
            compiler.addInstruction(new POP(Register.getR(n)));
            compiler.getMemory().decreaseTSTO();
            compiler.addInstruction(new CMP(Register.getR(n), Register.R0));
        }
    }
}
