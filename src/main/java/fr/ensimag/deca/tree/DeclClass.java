package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
//import jdk.javadoc.internal.doclint.Env;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl21
 * @date 01/01/2023
 */
public class DeclClass extends AbstractDeclClass {

    private final AbstractIdentifier name;
    private AbstractIdentifier superClass;
    private final ListDeclMethod methods;
    private final ListDeclField fieldSets;

    AbstractIdentifier getSuperClass(){
        return superClass;
    }

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass,
                     ListDeclField lf, ListDeclMethod lm) {
        Validate.notNull(name);
        this.name = name;
        this.superClass = superClass;
        this.fieldSets = lf;
        this.methods = lm;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        if (superClass != null) {
            s.print(" extends ");
            superClass.decompile(s);
        }
        s.println(" {");
        s.indent();
        fieldSets.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {

        //TODO: Comprendre où est envExpr ici et comment y accéder + faire le cas où c'est Object

        if(this.superClass == null){
            this.superClass = new Identifier(compiler.createSymbol("Object"));
            this.superClass.setLocation(Location.BUILTIN);
            this.superClass.setDefinition(compiler.environmentType.getEnvTypes().get(compiler.createSymbol("Object")));
        }

        ClassType classtype = new ClassType(name.getName(),getLocation(), superClass.getClassDefinition());
        this.name.setType(classtype);
        this.name.setDefinition(new ClassDefinition(classtype, getLocation(), superClass.getClassDefinition()));

        try{
            compiler.environmentType.declare(name.getName(), (TypeDefinition) name.getDefinition());
        }catch(EnvironmentType.DoubleDefException e){
            throw new ContextualError( compiler.displaySourceFile() + ":"
                    + this.getLocation().errorOutPut() + ": Class name already used", this.getLocation());
        }

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        EnvironmentExp envExpF = this.fieldSets.verifyListDeclFieldPass2(compiler, superClass, name);
        EnvironmentExp envExpM = this.methods.verifyListDeclMethodPass2(compiler,superClass,name);

        if(superClass.getClassDefinition().getMembers().get(name.getName()) != null){
            EnvironmentType envTypeR = new EnvironmentType(compiler);
            ClassDefinition newDef = new ClassDefinition(this.name.getClassDefinition().getType(), this.getLocation(), this.superClass.getClassDefinition());
            newDef.disjointUnion(compiler, envExpF, envExpM);


            compiler.environmentType.getEnvTypes().remove(name.getName());
            ClassType classtype = new ClassType(name.getName(),getLocation(), superClass.getClassDefinition());
            this.name.setType(classtype);
            this.name.setDefinition(new ClassDefinition(classtype, getLocation(), superClass.getClassDefinition()));

            //compiler.environmentType.declare(name.getName(), (TypeDefinition) name.getDefinition());

        }

    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.name.prettyPrint(s, prefix, false);
        if (this.superClass != null) {
            this.superClass.prettyPrint(s, prefix, true);
        }
        this.fieldSets.prettyPrint(s, prefix, true);
        this.methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.name.iter(f);
        if (this.superClass != null) {
            this.superClass.iter(f);
        }
        this.fieldSets.iter(f);
        this.methods.iter(f);
    }

}
