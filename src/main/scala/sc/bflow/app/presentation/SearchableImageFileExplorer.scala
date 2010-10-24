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
import collection.mutable.ArrayBuffer
import java.lang.reflect.Field
import java.util.Hashtable

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

            if (node.isInstanceOf[GenericTreeNode[_]]) {
              val inode = node.asInstanceOf[GenericTreeNode[_]]
              val ipath = nodePath(inode)
              val expanded: Boolean = treeComponent.isExpanded(ipath)

              if (inode.isLeaf || expanded) {
                super.configureListModelWrapper(wrapper, node)
              }
            }
          }
        }

      }
    }

  def nodePath(_node: TreeNode) = {
    var node = _node
    val list = ArrayBuffer[AnyRef]()
    while (node != null) {
      list += node
      node = node.getParent();
    }

    new TreePath(list.reverse.toArray)


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