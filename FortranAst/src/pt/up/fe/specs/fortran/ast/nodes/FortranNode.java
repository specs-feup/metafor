/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.fortran.ast.nodes;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.treenode.DataNode;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.FortranNodeFactory;
import pt.up.fe.specs.fortran.ast.FortranNodes;
import pt.up.fe.specs.fortran.ast.utils.Position;
import pt.up.fe.specs.util.treenode.NodeInsertUtils;
import pt.up.fe.specs.util.utilities.PrintOnce;

import java.util.Collection;

/**
 * Represents a node of the Fortran AST.
 *
 * @author JoaoBispo
 */
public abstract class FortranNode extends DataNode<FortranNode> {

    // DATAKEYS BEGIN

    /**
     * Global object with information about the program.
     */
    public final static DataKey<FortranContext> CONTEXT = KeyFactory.object("context", FortranContext.class);

    /**
     * Id of the node.
     */
    public final static DataKey<String> ID = KeyFactory.string("id");

    /**
     * If this node was automatically generated from another node, returns the id of that node, otherwise returns empty.
     */
    public final static DataKey<String> PREVIOUS_ID = KeyFactory.string("previousId");


    // DATAKEYS END

    public FortranNode(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    protected FortranNode getThis() {
        return this;
    }

    @Override
    public String toContentString() {
        return getData().toInlinedString();
    }

    @Override
    protected Class<FortranNode> getBaseClass() {
        return FortranNode.class;
    }

    /**
     * By default, copying a node creates a new, unique id for the new copy.
     */
    @Override
    public FortranNode copy() {
        return copy(false);
    }

    /**
     * @param keepId if true, the id of the copy will be the same as the id of the original node
     * @return
     */
    public FortranNode copy(boolean keepId) {
        // Copy node, without children
        var newNode = copyPrivate();

        // Change id, if needed
        if (!keepId) {
            String previousId = newNode.get(ID);
            String newId = get(CONTEXT).get(FortranContext.ID_GENERATOR).next("node_");

            newNode.set(ID, newId);
            newNode.set(PREVIOUS_ID, previousId);
        }

        // Copy children
        for (var child : getChildren()) {
            var newChild = child.copy(keepId);
            newNode.addChild(newChild);
        }

        return newNode;
    }


    public String getCode() {
        PrintOnce.info("getCode() not implemented for nodes of type " + getClass());

        return "\n/*<.getCode() not implemented for node " + this.getClass() + ">*/";
    }

    protected String ln() {
        return FortranNodes.ln();
    }

    protected String tab() {
        return "   ";
    }

    protected String keyword(FortranKeyword keyword) {
        return get(CONTEXT).get(FortranContext.FORTRAN_KEYWORDS).get(keyword);
    }

    public FortranNodeFactory getFactory() {
        return get(FortranNode.CONTEXT).get(FortranContext.FACTORY);
    }


    public FortranNode insert(Position position, FortranNode newNode) {
        switch (position) {
            case BEFORE -> NodeInsertUtils.insertBefore(this, newNode);
            case AFTER -> NodeInsertUtils.insertAfter(this, newNode);
            case REPLACE -> NodeInsertUtils.replace(this, newNode);
        }

        return newNode;
    }

    public FortranNode insert(Position position, String code) {
        // By default, transforms code into a literal statement.
        // This might need to evolve in the future, depending on where the code is inserted
        var literalStmt = getFactory().literalExecutionStmt(code);

        return insert(position, literalStmt);
    }
}
