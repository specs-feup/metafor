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
import pt.up.fe.specs.fortran.ast.nodes.program.Application;
import pt.up.fe.specs.fortran.ast.nodes.program.FortranFile;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;
import pt.up.fe.specs.fortran.weaver.abstracts.AFortranWeaverJoinPoint;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.fortran.weaver.joinpoints.FFile;
import pt.up.fe.specs.fortran.weaver.joinpoints.FProgram;
import pt.up.fe.specs.fortran.weaver.joinpoints.FStatement;
import pt.up.fe.specs.fortran.weaver.joinpoints.GenericFortranJoinpoint;
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
        JOINPOINT_FACTORY.put(FortranNode.class, FortranJoinpoints::defaultFactory);
    }


    private static AFortranWeaverJoinPoint defaultFactory(FortranNode node) {
        SpecsLogs.warn("Factory not defined for nodes of class '" + node.getClass().getSimpleName() + "'");
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
