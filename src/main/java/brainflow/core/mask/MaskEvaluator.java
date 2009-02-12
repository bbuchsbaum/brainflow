package brainflow.core.mask;

import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 11, 2008
 * Time: 8:24:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskEvaluator extends AnalysisAdapter {


    @Override
    @Testable
    public void inComparison(ComparisonNode node) {

        if (node.left().isLeaf() && node.right().isLeaf()) {
            LeafNode left = (LeafNode) node.left();
            LeafNode right = (LeafNode) node.right();

            LeafNode mnode = left.accept(right, node.getOp());
            node.replaceBy(mnode);

        }

    }



    public void outComparison(ComparisonNode node) {

    }

    @Override
    public void inUnary(UnaryNode node) {
        if (node.getChild().isLeaf()) {
            LeafNode lnode = (LeafNode) node.getChild();
            LeafNode ret = lnode.visitUnary(node.getOp());
            node.replaceBy(ret);
        }
    }

    @Override
    public RootNode outRootNode(RootNode rootNode) {

        if (rootNode.getChild() instanceof ComparisonNode) {
            ComparisonNode node = (ComparisonNode) rootNode.getChild();

            LeafNode left = (LeafNode) node.left();
            LeafNode right = (LeafNode) node.right();

            LeafNode mnode = left.accept(right, node.getOp());

            return new RootNode(mnode);
        } else {
            return rootNode;
        }



    }

    public INode outStart(INode rootNode) {

        if (rootNode instanceof ComparisonNode) {
            ComparisonNode node = (ComparisonNode) rootNode;

            if (node.left() instanceof LeafNode && node.right() instanceof LeafNode) {
                LeafNode left = (LeafNode) node.left();
                LeafNode right = (LeafNode) node.right();

                LeafNode mnode = left.accept(right, node.getOp());

                rootNode = mnode;
            }

        }

        return rootNode;

    }
}
