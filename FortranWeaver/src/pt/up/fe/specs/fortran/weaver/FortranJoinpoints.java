/**
 * Copyright 2016 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.fortran.weaver;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.*;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpOrderedClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.program.Application;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.program.*;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.program.Application;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.*;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt.*;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;
import pt.up.fe.specs.fortran.weaver.abstracts.AFortranWeaverJoinPoint;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.fortran.weaver.joinpoints.*;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

import java.util.List;

public class FortranJoinpoints {

    private static final FunctionClassMap<FortranNode, AFortranWeaverJoinPoint> JOINPOINT_FACTORY;

    static {
        JOINPOINT_FACTORY = new FunctionClassMap<>();

        JOINPOINT_FACTORY.put(Application.class, FProgram::new);
        JOINPOINT_FACTORY.put(FortranFile.class, FFile::new);
        JOINPOINT_FACTORY.put(Stmt.class, FStatement::new);
        JOINPOINT_FACTORY.put(ActionStmt.class, FActionStatement::new);
        JOINPOINT_FACTORY.put(ArraySubscriptExpr.class, FArraySubscriptExpr::new);
        JOINPOINT_FACTORY.put(AssignmentStmt.class, FAssignmentStatement::new);
        JOINPOINT_FACTORY.put(BinaryOperator.class, FBinaryOperator::new);
        JOINPOINT_FACTORY.put(CompilerDirective.class, FCompilerDirective::new);
        JOINPOINT_FACTORY.put(DataRef.class, FDataRef::new);
        JOINPOINT_FACTORY.put(Designator.class, FDesignator::new);
        JOINPOINT_FACTORY.put(DoStmt.class, FDoStatement::new);
        JOINPOINT_FACTORY.put(ExecutableStmt.class, FExecutableStatement::new);
        JOINPOINT_FACTORY.put(Execution.class, FExecution::new);
        JOINPOINT_FACTORY.put(Expr.class, FExpr::new);
        JOINPOINT_FACTORY.put(IntLiteral.class, FIntLiteral::new);
        JOINPOINT_FACTORY.put(Literal.class, FLiteral::new);
        JOINPOINT_FACTORY.put(LoopControl.class, FLoopControl::new);
        JOINPOINT_FACTORY.put(NameValue.class, FNameValue::new);
        JOINPOINT_FACTORY.put(RangeLoopControl.class, FRangeLoopControl::new);
        JOINPOINT_FACTORY.put(RealLiteral.class, FRealLiteral::new);
        JOINPOINT_FACTORY.put(StmtBlock.class, FStatementBlock::new);
        JOINPOINT_FACTORY.put(StringLiteral.class, FStringLiteral::new);
        JOINPOINT_FACTORY.put(Specification.class, FSpecification::new);
        JOINPOINT_FACTORY.put(OmpConstruct.class, FOmpConstruct::new);
        JOINPOINT_FACTORY.put(OmpClause.class, FOmpClause::new);
        JOINPOINT_FACTORY.put(OmpLoopConstruct.class, FOmpLoopConstruct::new);
        JOINPOINT_FACTORY.put(OmpBlockConstruct.class, FOmpBlockConstruct::new);
        JOINPOINT_FACTORY.put(OmpDataSharingClause.class, FOmpDataSharingClause::new);
        JOINPOINT_FACTORY.put(UseStmt.class, FUseStatement::new);
        JOINPOINT_FACTORY.put(ProgramUnit.class, FProgramUnit::new);
        JOINPOINT_FACTORY.put(OmpReductionClause.class, FOmpReductionClause::new);
        JOINPOINT_FACTORY.put(OmpOrderedClause.class, FOmpOrderedClause::new);
        JOINPOINT_FACTORY.put(IfStmt.class, FIfStatement::new);
        JOINPOINT_FACTORY.put(IfConstruct.class, FIfConstruct::new);
        JOINPOINT_FACTORY.put(IfThenBlock.class, FIfThenBlock::new);
        JOINPOINT_FACTORY.put(IfThenStmt.class, FIfThenStatement::new);
        JOINPOINT_FACTORY.put(ElseIfBlock.class, FElseIfBlock::new);
        JOINPOINT_FACTORY.put(ElseIfStmt.class, FElseIfStatement::new);
        JOINPOINT_FACTORY.put(ElseBlock.class, FElseBlock::new);
        JOINPOINT_FACTORY.put(FortranNode.class, FortranJoinpoints::defaultFactory);
    }


    private static AFortranWeaverJoinPoint defaultFactory(FortranNode node) {
        SpecsLogs.debug(() -> "Factory not defined for nodes of class '" + node.getClass().getSimpleName() + "'");
        return new GenericFortranJoinpoint(node);
    }

    public static AFortranWeaverJoinPoint create(FortranNode node) {
        if (node == null) {
            SpecsLogs.debug("CxxJoinpoints: tried to create join point from null node, returning undefined");
            return null;
        }

        return JOINPOINT_FACTORY.apply(node);
    }

    public static <T extends AJoinPoint> T create(FortranNode node, Class<T> targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("Check if you meant to call 'create' with a single argument");
        }

        return targetClass.cast(create(node));
    }

    public static <T extends AJoinPoint> T[] create(List<? extends FortranNode> nodes, Class<T> targetClass) {
        return nodes.stream()
                .map(node -> create(node, targetClass))
                .toArray(size -> SpecsCollections.newArray(targetClass, size));
    }
}
