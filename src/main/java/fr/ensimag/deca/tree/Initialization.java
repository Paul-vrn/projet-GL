package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * @author gl21
 * @date 01/01/2023
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, AbstractIdentifier varName) {
        expression.codeGenExpr(compiler, 2);
        compiler.addInstruction(new STORE(Register.getR(2), varName.getExpDefinition().getOperand()));
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type2 = this.getExpression().verifyExpr(compiler, localEnv, currentClass);

        if(type2 == null){
            throw new ContextualError( compiler.displaySourceFile() + ":"
                    + this.expression.getLocation().errorOutPut() + ": Initialization impossible with undefined value", this.expression.getLocation());
        }

        if(!(t.sameType(type2) || (t.isFloat() && type2.isInt()))){
            throw new ContextualError( compiler.displaySourceFile() + ":"
                    + this.expression.getLocation().errorOutPut() + ": Initialization type error, " + type2 + " into " + t + " forbidden", this.expression.getLocation());
        }

        if(t.isFloat() && type2.isInt()){
            ConvFloat c = new ConvFloat(this.expression);
            c.verifyExpr(compiler, localEnv, currentClass);
            this.expression = c;
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        this.getExpression().decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
