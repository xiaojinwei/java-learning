package com.cj.learn.tree.rbt;

import java.util.*;

/**
 * 功能相似于TreeMap
 * 该实现原理使用的是红黑树
 * @param <K>
 * @param <V>
 */
public class RBTMap<K,V> implements Iterable<RBTMap.RBNode<K,V>>{
    private int size = 0;
    private RBNode<K,V> root;//根节点

    private Comparator<? super K> comparator;

    public RBTMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public RBTMap() {
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((K)a,(K)b);//JDK中也是强转的
        }else{
            Comparable<K> t = (Comparable<K>) a;
            return t.compareTo((K)b);
        }
    }

    private RBNode<K,V> parentOf(RBNode<K,V> p) {
        return p!=null ? p.parent : null;
    }

    private RBNode<K,V> leftOf(RBNode<K,V> p){
        return p != null ? p.left : null;
    }

    private RBNode<K,V> rightOf(RBNode<K,V> p){
        return p != null ? p.right : null;
    }

    private boolean isRed(RBNode<K,V> p) {
        return ((p!=null)&&(p.color==RED)) ? true : false;
    }

    private boolean isBlack(RBNode<K,V> p) {
        return !isRed(p);
    }

    private boolean colorOf(RBNode<K,V> p) {
        return (p == null ? BLACK : p.color);
    }

    private void setBlack(RBNode<K,V> p) {
        if (p!=null)
            p.color = BLACK;
    }

    private void setRed(RBNode<K,V> p) {
        if (p!=null)
            p.color = RED;
    }

    private void setColor(RBNode<K,V> p,boolean color){
        if (p != null) {
            p.color = color;
        }
    }

    /*************对红黑树节点p进行左旋操作 ******************/
    /*
     * 左旋示意图：对节点p进行左旋
     *
     *    p.p                     p.p
     *    /                       /
     *   p                       r
     *  / \                     / \
     * l   r      ----->       p  r.r
     *    / \                 / \
     *   r.l r.r             l  r.l
     *
     * 左旋做了三件事：
     * 1. 将r的左子节点赋给p的右子节点,并将p赋给r左子节点的父节点(r左子节点非空时)
     * 2. 将p的父节点p.p(非空时)赋给r的父节点，同时更新p.p的子节点为r(左或右)
     * 3. 将r的左子节点设为p，将p的父节点设为r
     */
    private void rotateLeft(RBNode<K,V> p) {
        if (p == null) return;
        //1. 将r的左子节点赋给p的右子节点,并将p赋给r左子节点的父节点(r左子节点非空时)
        RBNode<K,V> r = p.right;
        p.right = r.left;
        if (r.left != null) {
            r.left.parent = p;
        }
        //2. 将p的父节点p.p(非空时)赋给r的父节点，同时更新p.p的子节点为r(左或右)
        r.parent = p.parent;
        if (p.parent == null) {
            this.root = r;//如果p的父节点为空(即p为根节点)，则将r设为根节点
        }else {
            if (p == p.parent.left) {//如果p是左子节点
                p.parent.left = r;//则也将r设为左子节点
            }else{
                p.parent.right = r;//否则将r设为右子节点
            }
        }
        // 3. 将r的左子节点设为p，将p的父节点设为r
        r.left = p;
        p.parent = r;
    }

    /*************对红黑树节点p进行右旋操作 ******************/
    /*
     * 右旋示意图：对节点p进行右旋
     *
     *       p.p                 p.p
     *       /                   /
     *      p                   l
     *     / \                 / \
     *    l   r   ----->     l.l  p
     *   / \                     / \
     * l.l l.r                  l.r r
     *
     * 右旋做了三件事：
     * 1. 将l的右子节点赋给p的左子节点,并将p赋给l右子节点的父节点(l右子节点非空时)
     * 2. 将p的父节点p.p(非空时)赋给l的父节点，同时更新p.p的子节点为l(左或右)
     * 3. 将l的右子节点设为p，将p的父节点设为l
     */
    private void rotateRight(RBNode<K,V> p) {
        if(p == null) return;
        //1. 将l的右子节点赋给p的左子节点,并将p赋给l右子节点的父节点(l右子节点非空时)
        RBNode<K,V> l = p.left;
        p.left = l.right;
        if (l.right != null) {
            l.right.parent = p;
        }
        //2. 将p的父节点p.p(非空时)赋给l的父节点，同时更新p.p的子节点为l(左或右)
        l.parent = p.parent;
        if (p.parent == null) {
            this.root = l;//如果p的父节点为空(即p为根节点)，则旋转后将l设为根节点
        }else{
            if (p == p.parent.right) {//如果p是右子节点
                p.parent.right = l;//否则将l设置为右子节点
            }else{
                p.parent.left = l;//则将l也设置为左子节点
            }
        }
        //3. 将l的右子节点设为p，将p的父节点设为l
        l.right = p;
        p.parent = l;
    }

    public V put(K key,V value) {
        RBNode<K,V> e = null;
        if (root == null) {
            root = new RBNode<K,V>(key,value,BLACK);
            e = root;
            size ++;
        }else{
            RBNode<K,V> p = root;
            while (p != null) {
                int cmp = compare(key,p.key);
                if (cmp < 0) {
                    if (p.left == null) {
                        p.left = new RBNode<K,V>(key,value,BLACK,p);
                        e = p.left;
                        size ++;
                        break;
                    }else{
                        p = p.left;//再次循环比较
                    }
                } else if (cmp > 0) {
                    if (p.right == null) {
                        p.right = new RBNode<K,V>(key,value,BLACK,p);
                        e = p.right;
                        size ++;
                        break;
                    }else{
                        p = p.right;
                    }
                }else{
                    p.setValue(value);//替换旧值
                    e = p;
                    break;
                }
            }
        }
        fixAfterInsertion(e);
        //不管是插入的是新值还是重复值，都返回插入的值，这个和JDK TreeMap不一样
        return value;
    }

    /**
     * 插入调整
     * 自底向上
     * @param x
     */
    private void fixAfterInsertion(RBNode<K,V> x) {
        setRed(x);
        RBNode<K,V> parent;//定义父节点
        //需要修正的条件：父节点存在，且父节点的颜色是红色
        while ((parent = parentOf(x))!=null && isRed(parent)){
            //若父节点是祖父节点的左子节点，下面是else相反
            if (parent == leftOf(parentOf(parent))) {
                RBNode<K,V> uncle = rightOf(parentOf(parent));//获得叔叔节点
                //case1:叔叔节点也是红色
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);//把父节点和叔叔节点涂黑
                    setBlack(uncle);
                    setRed(parentOf(parent));//把祖父节点涂红
                    x = parentOf(parent);//把位置放到祖父节点处
                    continue;
                }
                //case2:叔叔节点是黑色，且当前节点是右子节点
                if (parent.right == x) {
                    x = parent;
                    rotateLeft(x);
                }
                //case3:叔叔节点是黑色，且当前节点是左子节点
                setBlack(parent);
                setRed(parentOf(parent));
                rotateRight(parentOf(parent));
            }else {//若父节点是祖父节点的右子节点，与上面的情况完全相反，本质是一样的
                RBNode<K,V> uncle = leftOf(parentOf(parent));
                //case1:叔叔节点也是红色的
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(parentOf(parent));
                    x = parentOf(parent);
                    continue;
                }
                //case2:叔叔节点是黑色的，且当前节点是左子节点
                if (x == parent.left) {
                    x = parent;
                    rotateRight(x);
                }
                //case3:叔叔节点是黑色的，且当前节点是右子节点
                setBlack(parent);
                setRed(parentOf(parent));
                rotateLeft(parentOf(parent));
            }
        }
        setBlack(root);
    }

    private RBNode<K,V> getEntry(Object key) {
        RBNode<K,V> p = root;//初始化指针p,指向根节点
        while (p != null) {
            int cmp = compare(key, p.key);//比较key与p.key的大小
            if (cmp < 0) {
                p = p.left;//key小于p.key，递归(循环)查找左子树
            } else if (cmp > 0) {
                p = p.right;//key大于p.key，递归(循环)查找右子树
            } else {
                return p;//key等于p.key，查找成功
            }
        }
        return null;//查找失败
    }

    public V get(Object key) {
        RBNode<K, V> entry = getEntry(key);
        return entry != null ? entry.value : null;
    }

    /**
     * 找到最小结点(中序遍历的第一个结点)
     * @return
     */
    public RBNode<K, V> getFirstEntry() {
        return getFirstEntry(root);
    }

    /**
     * 找到以p为根节点的最小结点(中序遍历的第一个结点)
     * @param p
     * @return
     */
    private RBNode<K, V> getFirstEntry(RBNode<K, V> p) {
        if(p == null)
            return null;
        while (p.left != null)//一直往左找，直到p没有左子树
            p = p.left;
        return p;//p是最左边且第一个没有左子树的结点
    }

    /**
     * 找到最大结点(中序遍历的最后一个结点)
     * @return
     */
    public RBNode<K,V> getLastEntry() {
        return getLastEntry(root);
    }

    /**
     * 找到以p为根节点的最大结点
     * @param p
     * @return
     */
    private RBNode<K, V> getLastEntry(RBNode<K, V> p) {
        if (p == null)
            return null;
        while (p.right != null)//一直往右找，直到p没有右子树
            p = p.right;
        return p;//p是最右边且第一个没有右子树的结点
    }


    /**
     * 删除
     * @param key
     * @return
     */
    public V remove(K key) {
        RBNode<K, V> entry = getEntry(key);//查找是否有该结点
        if (entry == null) {
            return null;
        }
        V oldValue = entry.getValue();//获取value
        deleteEntry(entry);
        return oldValue;
    }

    /**
     * 删除结点p
     *
     * 二叉树删除节点找替代节点有3中情况
     * 情况1.若删除节点无子节点，直接删除
     * 情况2.若删除节点只有一个子节点，用子节点替换删除节点
     * 情况3.若删除节点有两个孩子，用后继节点(或前趋节点)替换删除节点
     * 删除节点被替代后，对于树来说，可以认为删除的是替代节点，3中二叉树的删除情景可以互相转换并最终都是转换为情况1、情况2
     *
     * BST删除的情况1和情况2的执行顺序略有不同
     * 情况1.先调整P，再删除P
     * 情况2.先删除P，再调整rep
     *
     * RBT的删除调整
     * 1.需要删除的节点X为红色，直接删除X 
     * 2.其他无需调整的情况为：
     *     1).当前X为根节点，无论root是什么颜色，都将root染黑，简称rootOver
     *     2).当前X为红色，将X染黑，结束，简称redOver
     * 3.删除左孩子X，分为四种情况：
     *     case1: S为红色；S染黑，P染红，左旋P
     *     case2: S为黑色，黑LN，黑RN；S染红，X回溯至P
     *     case3: S为黑色，红LN，黑RN；LN染黑，S染红，右旋S；转成case4
     *     case4: 黑S，LN随意，红RN；S变P的颜色，P和RN染黑，左旋P
     * 4.删除右孩子X，情况正好和删除左孩子X对称
     * @param p 要删除的结点
     */
    private void deleteEntry(RBNode<K,V> p){
        size --;
        //情况3 删除节点被替代后，对于树来说，可以认为删除的是替代节点，3中二叉树的删除情景可以互相转换并最终都是转换为情况1、情况2，所以先执行情况3
        if (p.left != null && p.right != null) { //情况3
            ////为了达到平衡效果，随机执行以下两种情况
            if ((size & 1) == 0) {//找右子树中最小的来替换
                RBNode<K, V> rightMin = getFirstEntry(p.right);//也可以使用获取p的后继节点方法(successor(p))
                p.key = rightMin.key;
                p.value = rightMin.value;
                p = rightMin;
            }else {//找左子树中最大的来替代
                RBNode<K, V> leftMax = getLastEntry(p.left);//也可以使用获取p的前趋节点方法(predecessor(p))
                p.key = leftMax.key;
                p.value = leftMax.value;
                p = leftMax;
            }
        }
        //以下顺序不能变(重要)
        //1.p结点只有一个孩子
        //  如果整棵树只有两个结点的时候，这是删除的是根结点，如果先执行2和3的话就会直接将root置为null
        //2.p为根结点
        //  如果整棵树只有一个结点的时候，这是删除的是跟结点，如果先执行3的话，因为这时根结点也是叶子结点，就根本不会执行2了，也就不会将root置为null了，导致根结点永远删除不了
        //3.p为叶子结点
        if (p.left == null && p.right != null) {//情况2
            RBNode<K, V> rep = p.right;
            deleteOneEntry(p,rep);
        } else if (p.left != null && p.right == null) {//情况2
            RBNode<K, V> rep = p.left;
            deleteOneEntry(p,rep);
        } else if (p.parent == null) { //如果是根结点直接将root置null
            root = null;
        } else if (p.left == null && p.right == null) {//情况1
            //情况1.先调整P，再删除P
            if (isBlack(p)) {//删除的是黑色结点才需要调整
                fixAfterDeletion(p);
            }
            //释放p
            if (p.parent != null) {
                if (p == p.parent.left) {//左孩子
                    p.parent.left = null;
                } else if (p == p.parent.right) {//右孩子
                    p.parent.right = null;
                }
                p.parent = null;//断开与父节点的连接
            }
        }
    }

    /**
     * 情况二删除
     * 若删除节点只有一个子节点，用子节点替换删除节点
     * @param p
     * @param rep
     */
    private void deleteOneEntry(RBNode<K, V> p,RBNode<K, V> rep){
        //情况2.先删除P，再调整rep
        rep.parent = p.parent;
        if (p.parent == null) { //删除的p是根结点，直接将p.right设置成root
            root = rep;
        } else if (p == p.parent.left) { //p为左孩子
            p.parent.left = rep;
        } else { //p为右孩子
            p.parent.right = rep;
        }
        //释放p
        p.left = p.right = p.parent = null;
        //调整
        if (isBlack(p)) {//删除的是黑色结点才需要调整
            fixAfterDeletion(rep);
        }
    }

    /**
     * 删除调整
     * @param x
     */
    private void fixAfterDeletion(RBNode<K,V> x){
        while (x != root && isBlack(x)){
            if (x == leftOf(parentOf(x))) { //删除左孩子X ,leftCase
                RBNode<K, V> sib = rightOf(parentOf(x));
                //case1: S为红色；S染黑，P染红，左旋P，LN成为新的S(sib)
                if (isRed(sib)) {
                    setBlack(sib);
                    setRed(parentOf(x));
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));//叔叔结点sib是相对于x结点来说的
                }
                //case2: S为黑色，黑LN，黑RN；S染红，X回溯至P
                if (isBlack(leftOf(sib)) && isBlack(rightOf(sib))) { //这里S肯定为黑色
                    setRed(sib);
                    x = parentOf(x);
                }
                //case3: S为黑色，红LN，黑RN；LN染黑，S染红，右旋S，S指向LN ；转成case4
                else { //这里S肯定为黑色
                    if (isBlack(rightOf(sib))) { //这里LN肯定为红色
                        setBlack(leftOf(sib));
                        setRed(sib);
                        rotateRight(sib);
                        //叔叔结点sib是相对于x结点来说的
                        sib = rightOf(parentOf(x));//旋转后，LN转到了S的位置，所以S需要执行LN的位置
                    }
                    //case4: 黑S，LN随意，红RN；S变P的颜色，P和RN染黑，左旋P，X指向根节点，rootOver
                    //这里的S肯定为黑色，RN肯定为红色(case3右旋S后，sib变为新的RN，case3已经将sib染成了红色)
                    setColor(sib,colorOf(parentOf(x)));
                    setBlack(parentOf(x));
                    setBlack(rightOf(sib));
                    rotateLeft(parentOf(x));
                    x = root;
                }

            }else{ ////删除右孩子X ,rightCase
                RBNode<K, V> sib = leftOf(parentOf(x));
                //case1：S为红色；S染黑，P染红，右旋P，RN成为新的S(sib)
                if (isRed(sib)) {
                    setBlack(sib);
                    setRed(parentOf(x));
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                //case2：S为黑色，黑LN，黑RN；S染红，X回溯至P
                if (isBlack(rightOf(sib)) && isBlack(leftOf(sib))) { //S肯定为黑色
                    setRed(sib);
                    x = parentOf(x);
                }
                //case3：S为黑色，红RN，黑LN；RN染黑，S染红，左旋S，S指向RN ；转成case4
                else{
                    if (isBlack(leftOf(sib))) {//如果LN为黑色，RN肯定为红色
                        setBlack(rightOf(sib));
                        setRed(sib);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    //case4-1：黑S，红LN，RN随意；S以父为名(S变P的颜色)，P和LN染黑，右旋P，X指向根节点，rootOver
                    //这里的S肯定为黑色，LN肯定为红色(case3左旋S后，sib变为新的LN，case3已经将sib染成了红色)
                    setColor(sib, colorOf(parentOf(x)));
                    setBlack(parentOf(x));
                    setBlack(leftOf(sib));
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setBlack(x);
    }

    //*********************************************************

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public static class RBNode<K,V>{
        boolean color;//颜色
        K key;//关键字
        V value;//value去掉就是TreeSet
        RBNode<K,V> left; //左孩子
        RBNode<K,V> right;//右孩子
        RBNode<K,V> parent;//父节点

        public RBNode(K key,V value, boolean color, RBNode<K,V> parent, RBNode<K,V> left, RBNode<K,V> right){
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public RBNode(K key, V value,boolean color) {
            this.color = color;
            this.key = key;
            this.value = value;
        }

        public RBNode(K key, V value,boolean color, RBNode<K, V> parent) {
            this.color = color;
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public boolean isColor() {
            return color;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "RBNode{" +
                    "color=" + color +
                    ", key=" + key +
                    ", value=" + value +
                    '}';
        }
    }


    //**********************************************************

    @Override
    public Iterator<RBNode<K, V>> iterator() {
        return new RBTIterator<>(root);
    }

    /**
     * 中序遍历实现迭代器
     * 这里实现引入Stack，而非递归实现
     * @param <K>
     * @param <V>
     */
    public static class RBTIterator<K,V> implements Iterator<RBNode<K,V>> {
        private Stack<RBNode<K,V>> stack;
        public RBTIterator(RBNode<K, V> root) {
            stack = new Stack<>();
            addLeftPath(root);
        }

        private void addLeftPath(RBNode<K, V> p) {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public RBNode<K, V> next() {
            RBNode<K, V> entry = stack.pop();
            addLeftPath(entry.right);
            return entry;
        }
    }


    //**********************************************************


    /**
     * 层序遍历
     */
    public void levelOrder() {
        if(root == null) return;
        Queue<RBNode<K,V>> queue = new LinkedList<>();
        queue.offer(root);
        int preCount = 1;
        int pCount = 0;
        while (!queue.isEmpty()) {
            preCount --;
            RBNode<K,V> p = queue.poll();
            System.out.print(p + " ");
            if (p.left != null) {
                queue.offer(p.left);
                pCount ++;
            }
            if (p.right != null) {
                queue.offer(p.right);
                pCount ++;
            }
            if (preCount == 0) {
                preCount = pCount;
                pCount = 0;
                System.out.println();
            }
        }
    }

}
