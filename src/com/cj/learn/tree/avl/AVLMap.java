package com.cj.learn.tree.avl;

import java.util.*;

/**
 * 功能相似于TreeMap
 * 该实现原理使用的是二叉排序树
 * @param <K>
 * @param <V>
 */
public class AVLMap<K, V> implements Iterable<AVLMap.AVLEntry<K,V>>{
    private int size;
    private AVLEntry<K,V> root;
    private Comparator<K> comparator;
    private LinkedList<AVLEntry<K,V>> stack = new LinkedList<>();//用于实现插入调整的非递归算法

    public int compare(K a, K b) {
        if (comparator != null) {
            return comparator.compare(a,b);
        }else{
            Comparable<K> t = (Comparable<K>) a;
            return t.compareTo(b);
        }
    }

    public AVLMap() {
    }

    public AVLMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public V put(K key,V value) {
        if (root == null) {
            root = new AVLEntry<K,V>(key,value);
            stack.push(root);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
            size ++;
        }else{
            AVLEntry<K,V> p = root;
            while (p != null) {
                stack.push(p);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                int cmp = compare(key,p.key);
                if (cmp < 0) {
                    if (p.left == null) {
                        p.left = new AVLEntry<K,V>(key,value);
                        stack.push(p.left);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                        size ++;
                        break;
                    }else{
                        p = p.left;//再次循环比较
                    }
                } else if (cmp > 0) {
                    if (p.right == null) {
                        p.right = new AVLEntry<K,V>(key,value);
                        stack.push(p.right);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                        size ++;
                        break;
                    }else{
                        p = p.right;
                    }
                }else{
                    p.setValue(value);//替换旧值
                    break;
                }
            }
        }
        fixAfterInsertion(key);
        //不管是插入的是新值还是重复值，都返回插入的值，这个和JDK TreeMap不一样
        return value;
    }

    @Override
    public Iterator<AVLEntry<K, V>> iterator() {
        return new AVLIterator<>(root);
    }

    /**
     *  1,若根结点的关键字值等于key，成功
     *  2,若key小于根结点的关键字，递归查找左子树
     *  3,若key大于根结点的关键字，递归查找右子树
     *  4,若子树为空，查找不会成功
     * @param key
     * @return
     */
    private AVLEntry<K,V> getEntry(K key) {
        AVLEntry<K,V> p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0) {
                p = p.left;
            } else if (cmp > 0) {
                p = p.right;
            } else {
                return p;
            }
        }
        return null;
    }

    public V get(K key) {
        AVLEntry<K, V> entry = getEntry(key);
        return entry != null ? entry.value : null;
    }

    public boolean containsKey(K key) {
        AVLEntry<K, V> entry = getEntry(key);
        return entry != null;
    }

    /**
     * 遍历所有结点查看比较value
     * 这里直接使用的迭代器，一个一个比较(中序遍历)
     * @param value
     * @return
     */
    public boolean containsValue(V value) {
        Iterator<AVLEntry<K, V>> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 找到最小结点(中序遍历的第一个结点)
     * @return
     */
    public AVLEntry<K, V> getFirstEntry() {
        return getFirstEntry(root);
    }

    /**
     * 找到以p为根节点的最小结点(中序遍历的第一个结点)
     * @param p
     * @return
     */
    public AVLEntry<K, V> getFirstEntry(AVLEntry<K, V> p) {
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
    public AVLEntry<K,V> getLastEntry() {
        return getLastEntry(root);
    }

    /**
     * 找到以p为根节点的最大结点
     * @param p
     * @return
     */
    public AVLEntry<K, V> getLastEntry(AVLEntry<K, V> p) {
        if (p == null)
            return null;
        while (p.right != null)//一直往右找，直到p没有右子树
            p = p.right;
        return p;//p是最右边且第一个没有右子树的结点
    }

    /**
     * 以p为根结点删除key
     * 递归查找删除
     * @param p
     * @param key
     * @return
     */
    private AVLEntry<K,V> deleteEntry(AVLEntry<K,V> p, K key){
        if(p == null)
            return null;
        int cmp = compare(key, p.key);
        if (cmp == 0) {//找到删除的结点
            if (p.left == null && p.right == null) {//情况1
                p = null;
            } else if (p.left == null && p.right != null) {//情况2
                p = p.right;
            } else if (p.left != null && p.right == null) {//情况2
                p = p.left;
            } else {//情况3
                //为了达到平衡效果，随机执行以下两种情况
                if ((size & 1) == 0) {//找右子树中最小的来替换
                    AVLEntry<K, V> rightMin = getFirstEntry(p.right);
                    p.key = rightMin.key;
                    p.value = rightMin.value;
                    AVLEntry<K, V> newRight = deleteEntry(p.right, p.key);
                    p.right = newRight;
                }else {//找左子树中最大的来替代
                    AVLEntry<K, V> leftMax = getLastEntry(p.left);
                    p.key = leftMax.key;
                    p.value = leftMax.value;
                    AVLEntry<K, V> newLeft = deleteEntry(p.left, p.key);
                    p.left = newLeft;
                }
            }
        } else if (cmp < 0) {//要删除的结点在左边
            AVLEntry<K, V> newLeft = deleteEntry(p.left, key);//以左子树为根结点，递归重新找要删除的结点
            p.left = newLeft;//实现真正的删除功能(如果p.left是要删除的结点，并且p.left结点没有子节点了，那么newLeft必定返回null，也就实现了删除功能)
        } else /*cmp > 0*/{//要删除的结点在右边
            AVLEntry<K, V> newRight = deleteEntry(p.right, key);//以右子树为根结点，递归重新找要删除的结点
            p.right = newRight;//实现真正的删除功能(如果p.rigth是要删除的结点，并且p.right结点没有子节点了，那么newRight必定返回null，也就实现了删除功能)
        }
        return p;
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public V remove(K key) {
        AVLEntry<K, V> entry = getEntry(key);//查找是否有该结点
        if (entry == null) {
            return null;
        }
        V oldValue = entry.getValue();//获取value
        AVLEntry<K, V> newRoot = deleteEntry(root, key);//删除，并返回新的根节点(如果删除的是根节点，root就会变化)
        root = newRoot;
        return oldValue;
    }

    /**
     * 层序遍历
     */
    public void levelOrder() {
        Queue<AVLEntry<K,V>> queue = new LinkedList<>();
        queue.offer(root);
        int preCount = 1;
        int pCount = 0;
        while (!queue.isEmpty()) {
            preCount --;
            AVLEntry<K,V> p = queue.poll();
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

    /**
     * 返回一个结点的高度
     * @param p
     * @return
     */
    public int getHeight(AVLEntry<K,V> p){
        return p == null ? 0 : p.height;
    }

    /**
     * 右旋(单旋转)
     * 该方法需要右返回值，因为AVLEntry中没有parent指针(JDK中是有parent指针的，所以不需要有返回值)，旋转之后它的新的根节点需要返回
     * @param p
     * @return
     */
    private AVLEntry<K,V> rotateRight(AVLEntry<K,V> p) {
        AVLEntry<K,V> left = p.left;
        p.right = left.right;
        left.right = p;
        p.height = Math.max(getHeight(p.left),getHeight(p.right)) + 1;
        left.height = Math.max(getHeight(left.left),p.height) + 1;
        return left;//新的根节点
    }

    /**
     * 左旋(单旋转)
     * @param p
     * @return
     */
    private AVLEntry<K, V> rotateLeft(AVLEntry<K, V> p) {
        AVLEntry<K,V> right = p.right;
        p.right = right.left;
        right.left = p;
        p.height = Math.max(getHeight(p.left),getHeight(p.right)) + 1;
        right.height = Math.max(p.height,getHeight(right.right)) + 1;
        return right;//新的根节点
    }

    /**
     * 先左旋再右旋
     * 先将p.left进行左旋，再将p进行右旋
     * @param p
     * @return
     */
    private AVLEntry<K,V> firstLeftThenRight(AVLEntry<K,V> p) {
        p.left = rotateLeft(p.left);
        p = rotateRight(p);
        return p;
    }

    /**
     * 先右旋再左旋
     * 先将p.right进行右旋，再将p进行左旋
     * @param p
     * @return
     */
    private AVLEntry<K, V> firstRightThenLeft(AVLEntry<K, V> p) {
        p.right = rotateRight(p.right);
        p = rotateLeft(p);
        return p;
    }

    /**
     * 插入调整，使其二叉搜索树达到平衡
     * @param key
     */
    private void fixAfterInsertion(K key){
        AVLEntry<K,V> p = root;
        while (!stack.isEmpty()) {
            p = stack.pop();
            //优化
            //**************************************************************
            int newHeight = Math.max(getHeight(p.left),getHeight(p.right)) + 1;
            if (p.height > 1 /*保证p不是叶子节点*/ && newHeight == p.height) {
                stack.clear();
                return;
            }
            //**************************************************************
            p.height = newHeight;//Math.max(getHeight(p.left),getHeight(p.right)) + 1;
            int d = getHeight(p.left) - getHeight(p.right);
            if (Math.abs(d) <= 1) {
                continue;
            }else{
                if (d == 2) {
                    if (compare(key, p.left.key) < 0) { //插入到了左子树的左子树
                        p = rotateRight(p);//单旋转：右旋rotateRight
                    }else{//插入到了左子树的右子树
                        p = firstLeftThenRight(p); //双旋转：先左后右firstLeftThenRight
                    }
                }else{ //d == -2
                    if (compare(key, p.right.key) > 0) { //插入到了右子树的右子树
                        p = rotateLeft(p);//单旋转：左旋rotateLeft
                    }else{//插入到了右子树的左子树
                        p = firstRightThenLeft(p);//双旋转：先右后左firstRightThenLeft
                    }
                }
                //旋转过后，需要判断走的是左子树还是右子树，也就是检测爷爷结点，也就是p.parent要设置左子树还是右子树
                if (!stack.isEmpty()) {
                    if (compare(key, stack.peek().key) < 0) { //表明插入到了左子树
                        stack.peek().left = p;
                    }else{
                        stack.peek().right = p;
                    }
                }
            }
        }
        root = p;//重新设置根节点
     }

    /**
     * 删除调整
     * 1,类似插入，假设删除了p右子树的某个结点，引起了p的平衡因子d[p]=2，分析p的左子树left，三种情况如下：
     *     情况1：left的平衡因子d[left]=1，将p右旋
     *     情况2：left的平衡因子d[left]=0，将p右旋
     *     情况3：left的平衡因子d[left]=-1，先左旋left，再右旋p
     * 2,删除左子树，即d[p]=-2的情况，与d[p]=2对称
     * @param p
     * @return
     */
    private AVLEntry<K, V> fixAfterDeletion(AVLEntry<K, V> p) {
        if (p == null) return null;
        else{
            p.height = Math.max(getHeight(p.left),getHeight(p.right)) + 1;
            int d = getHeight(p.left) - getHeight(p.right);
            if (d == 2) { //说明p.left一定不为null
                if (getHeight(p.left.left) - getHeight(p.left.right) >= 0) {
                    p = rotateRight(p);
                }else{
                    p = firstLeftThenRight(p);
                }
            } else if (d == -2) {//说明p.right一定不为null
                if (getHeight(p.right.right) - getHeight(p.right.left) >= 0) {
                    p = rotateLeft(p);
                }else{
                    p = firstRightThenLeft(p);
                }
            }
            return p;
        }
    }


    //Entry
    //***************************************************************

    /**
     * 存储key,value
     * @param <K>
     * @param <V>
     */
    public static class AVLEntry<K,V> implements Map.Entry<K,V> {
        private K key;
        private V value;
        private AVLEntry<K,V> left;
        private AVLEntry<K,V> right;
        private int height = 1;//单个结点的高度是1

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        public AVLEntry(K key) {
            this.key = key;
        }

        public AVLEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public AVLEntry(K key, V value, AVLEntry<K, V> left, AVLEntry<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "AVLEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    ", height=" + height +
                    '}';
        }
    }

    //Iterator
    //*********************************************************************************

    /**
     * 中序遍历实现迭代器
     * 这里实现引入Stack，而非递归实现
     * @param <K>
     * @param <V>
     */
    public class AVLIterator<K,V> implements Iterator<AVLEntry<K,V>> {
        private Stack<AVLEntry<K,V>> stack;
        public AVLIterator(AVLEntry<K, V> root) {
            stack = new Stack<>();
            addLeftPath(root);
        }

        private void addLeftPath(AVLEntry<K, V> p) {
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
        public AVLEntry<K, V> next() {
            AVLEntry<K, V> entry = stack.pop();
            addLeftPath(entry.right);
            return entry;
        }
    }

}
