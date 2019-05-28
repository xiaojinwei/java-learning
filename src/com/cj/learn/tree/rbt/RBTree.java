package com.cj.learn.tree.rbt;

/**
 * 红-黑树的特征
 * 有如下两个特征：
 *
 * 　　①、节点都有颜色；
 *
 * 　　②、在插入和删除的过程中，要遵循保持这些颜色的不同排列规则。
 *
 * 第一个很好理解，在红-黑树中，每个节点的颜色或者是黑色或者是红色的。当然也可以是任意别的两种颜色，这里的颜色用于标记，我们可以在节点类Node中增加一个boolean型变量isRed，以此来表示颜色的信息。
 *
 * 　　第二点，在插入或者删除一个节点时，必须要遵守的规则称为红-黑规则：
 *
 * 　　1.每个节点不是红色就是黑色的；
 *
 * 　　2.根节点总是黑色的；
 *
 * 　　3.如果节点是红色的，则它的子节点必须是黑色的（反之不一定）,(也就是从每个叶子到根的所有路径上不能有两个连续的红色节点)；
 *
 * 　　4.从根节点到叶节点或空子节点的每条路径，必须包含相同数目的黑色节点（即相同的黑色高度）。
 * @param <T>
 */
public class RBTree<T extends Comparable<T>> {

    private RBNode<T> root;//根节点

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public class RBNode<T extends Comparable<T>>{
        boolean color;//颜色
        T key;//关键字(健值)
        RBNode<T> left; //左孩子
        RBNode<T> right;//右孩子
        RBNode<T> parent;//父节点

        public RBNode(T key, boolean color, RBNode<T> parent, RBNode<T> left, RBNode<T> right){
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }

    private RBNode<T> parentOf(RBNode<T> node) {
        return node!=null ? node.parent : null;
    }

    private boolean isRed(RBNode<T> node) {
       return ((node!=null)&&(node.color==RED)) ? true : false;
    }
    private boolean isBlack(RBNode<T> node) {
       return !isRed(node);
    }
    private void setBlack(RBNode<T> node) {
        if (node!=null)
          node.color = BLACK;
    }
    private void setRed(RBNode<T> node) {
        if (node!=null)
            node.color = RED;
    }

    /*************对红黑树节点x进行左旋操作 ******************/
    /*
     * 左旋示意图：对节点x进行左旋
     *     p                       p
     *    /                       /
     *   x                       y
     *  / \                     / \
     * lx  y      ----->       x  ry
     *    / \                 / \
     *   ly ry               lx ly
     * 左旋做了三件事：
     * 1. 将y的左子节点赋给x的右子节点,并将x赋给y左子节点的父节点(y左子节点非空时)
     * 2. 将x的父节点p(非空时)赋给y的父节点，同时更新p的子节点为y(左或右)
     * 3. 将y的左子节点设为x，将x的父节点设为y
     */
    private void leftRotate(RBNode<T> x) {
        //1. 将y的左子节点赋给x的右子节点，并将x赋给y左子节点的父节点(y左子节点非空时)
        RBNode<T> y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        //2. 将x的父节点p(非空时)赋给y的父节点，同时更新p的子节点为y(左或右)
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;//如果x的父节点为空(即x为根节点)，则将y设为根节点
        }else {
            if (x == x.parent.left) {//如果x是左子节点
                x.parent.left = y;//则也将y设为左子节点
            }else{
                x.parent.right = y;//否则将y设为右子节点
            }
        }
        //3. 将y的左子节点设为x，将x的父节点设为y
        y.left = x;
        x.parent = y;
    }

    /*************对红黑树节点y进行右旋操作 ******************/
    /*
     * 左旋示意图：对节点y进行右旋
     *        p                   p
     *       /                   /
     *      y                   x
     *     / \                 / \
     *    x  ry   ----->      lx  y
     *   / \                     / \
     * lx  rx                   rx ry
     * 右旋做了三件事：
     * 1. 将x的右子节点赋给y的左子节点,并将y赋给x右子节点的父节点(x右子节点非空时)
     * 2. 将y的父节点p(非空时)赋给x的父节点，同时更新p的子节点为x(左或右)
     * 3. 将x的右子节点设为y，将y的父节点设为x
     */
    private void rightRotate(RBNode<T> y) {
        //1. 将y的左子节点赋给x的右子节点，并将x赋给y左子节点的父节点(y左子节点非空时)
        RBNode<T> x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        //2. 将y的父节点p(非空时)赋给x的父节点，同时更新p的子节点为x(左或右)
        x.parent = y.parent;
        if (y.parent == null) {
            this.root = x;//如果y的父节点为空(即y为根节点)，则旋转后将x设为根节点
        }else{
            if (y == y.parent.left) {//如果y是左子节点
                y.parent.left = x;//则将x也设置为左子节点
            }else{
                y.parent.right = x;//否则将x设置为右子节点
            }
        }
        //3. 将x的右子节点设为y，将y的父节点设为x
        y.parent = x;
        x.right = y;
    }

    public void insert(T key) {
        RBNode<T> node = new RBNode<T>(key, RED, null, null, null);
        if (node != null) {
            insert(node);
        }
    }

    public void insert(RBNode<T> node) {
        RBNode<T> current = null;////表示最后node的父节点
        RBNode<T> x = this.root;//用于向下搜索

        //1.找到插入位置
        while (x != null) {
            current = x;
            int cmp = node.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            }else {
                x = x.right;
            }
        }
        node.parent = current;//找到了插入的位置，将当前current作为node的父节点

        //2.接下来判断node是左子节点还是右子节点
        if (current != null) {
            int cmp = node.key.compareTo(current.key);
            if (cmp < 0) {
                current.left = node;
            }else{
                current.right = node;
            }
        }else{
            this.root = node;
        }

        //3.利用旋转操作将其修正为一颗红黑树
        insertFixUp(node);
    }

    private void insertFixUp(RBNode<T> node) {
        RBNode<T> parent, gparent;//定义父节点和祖父节点
        //需要修正的条件：父节点存在，且父节点的颜色是红色
        while ((parent = parentOf(node))!=null && isRed(parent)){
            gparent = parentOf(parent);//获得祖父节点

            //若父节点是祖父节点的左子节点，下面是else相反
            if (parent == gparent.left) {
                RBNode<T> uncle = gparent.right;//获得叔叔节点
                //case1:叔叔节点也是红色
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);//把父节点和叔叔节点涂黑
                    setBlack(uncle);
                    setRed(gparent);//把祖父节点涂红
                    node = gparent;//把位置放到祖父节点处
                    continue;
                }
                //case2:叔叔节点是黑色，且当前节点是右子节点
                if (parent.right == node) {
                    leftRotate(parent);//从父节点出左旋
                    RBNode<T> tmp = parent;//然后将父节点和自己调换一下，为下面右旋做准备
                    parent = node;
                    node = tmp;
                }
                //case3:叔叔节点是黑色，且当前节点是左子节点
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            }else {//若父节点是祖父节点的右子节点，与上面的情况完全相反，本质是一样的
                RBNode<T> uncle = gparent.left;
                //case1:叔叔节点也是红色的
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node = gparent;
                }
                //case2:叔叔节点是黑色的，且当前节点是左子节点
                if (node == parent.left) {
                    rightRotate(parent);
                    RBNode<T> tmp = parent;
                    parent = node;
                    node = tmp;
                }
                //case3:叔叔节点是黑色的，且当前节点是右子节点
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }
        setBlack(root);
    }
}
