package sc.bflow.app.presentation

import com.jidesoft.tree. {FilterableTreeModel, QuickTreeFilterField}
import com.jidesoft.list.ListModelWrapper
import javax.swing. {JTree, JPanel}
import com.xduke.xswing.DataTipManager
import com.jidesoft.swing.SearchableUtils
import java.awt. {BorderLayout, FlowLayout}
import org.apache.commons.vfs.FileObject
import boxwood.binding.swing.tree.GenericTreeNode
import javax.swing.tree. {TreeNode, TreePath, DefaultMutableTreeNode, TreeModel}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/17/10
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */

class SearchableImageFileExplorer(roots: FileObject*) extends ImageFileExplorer(roots: _*) {

  val quickSearchPanel: JPanel = new JPanel(new FlowLayout(FlowLayout.LEADING))




  val field: QuickTreeFilterField =

    new QuickTreeFilterField(super.treeModel) {
      protected override def createDisplayTreeModel(treeModel: TreeModel): FilterableTreeModel = {
        new FilterableTreeModel((treeModel)) {
          protected override def configureListModelWrapper(wrapper: ListModelWrapper, node: AnyRef): Unit = {
             println("filtering node " + node)
            if (node.isInstanceOf[GenericTreeNode[_]]) {

              val inode = node.asInstanceOf[GenericTreeNode[_]]
              val ipath = inode.getPath
              println("path length: " + ipath.length)


              val expanded: Boolean = treeComponent.isExpanded(new TreePath(ipath))
              println("expanded: " + expanded)
              println("leaf: " + inode.isLeaf)
              if (inode.isLeaf || expanded) {
                super.configureListModelWrapper(wrapper, node)
              }
            } else {
              println("node is not an instance of GenericTreeNode")
            }
          }
        }

      }
    }

  def getPathForNode(current: TreeNode) = {
    var depth: Int = 0
    var node: TreeNode = current
    while (node != null) {
      node = node.getParent
      println("parent node " + node)
      println("depth " + depth)
      depth = depth+1
    }

    val path: Array[TreeNode] = new Array[TreeNode](depth)
    node = current
    while (node != null) {
      node = node.getParent
      path(depth-1) = node
      depth = depth-1

    }

    path

  }



    field.setSearchingDelay(200)
    field.setTree(treeComponent)

    quickSearchPanel.add(field)
    println(field.getTree)
    SearchableUtils.installSearchable(field.getTree)



    field.getTree.setModel(field.getDisplayTreeModel)
    DataTipManager.get.register(field.getTree)
    add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE)


}