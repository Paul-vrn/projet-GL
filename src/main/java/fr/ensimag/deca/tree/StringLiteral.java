package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.arm.instructions.ASCII;
import fr.ensimag.ima.pseudocode.arm.instructions.BL;
import fr.ensimag.ima.pseudocode.arm.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * String literal
 *
 * @author gl21
 * @date 01/01/2023
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.STRING);
        return this.getType();
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    protected void armCodeGenPrint(DecacCompiler compiler) {
        Label strLabel = new Label("str" + compiler.getLabelFactory().nbString());
        compiler.addData(new Line(strLabel, new ASCII(new ImmediateString(value))));
        compiler.addInstruction(new MOV(new LabelOperand(strLabel), RegisterIMA.R0));
        compiler.addInstruction(new BL(compiler.getLabelFactory().getPrintfLabel()));
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("\"");
        s.print(getValue());
        s.print("\"");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {return "StringLiteral (" + value + ")";}

}
