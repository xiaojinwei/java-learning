package com.cj.learn.tree.bst;

import java.util.*;

/**
 * 功能相似于TreeMap
 * 该实现原理使用的是二叉排序树
 * @param <K>
 * @param <V>
 */
public class BSTMap<K, V> implements BSTTreeMap<K,V>, Iterable<BSTMap.BSTEntry<K,V>>{
    private int size;
    private BSTEntry<K,V> root;
    private Comparator<? super K> comparator;

    private transient EntrySet entrySet;

    public int compare(Object a, Object b) {
        if (comparator != null) {
            return comparator.compare((K)a,(K)b);//JDK中也是强转的
        }else{
            Comparable<K> t = (Comparable<K>) a;
            return t.compareTo((K)b);
        }
    }

    public BSTMap() {
    }

    public BSTMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public V put(K key,V value) {
        if (root == null) {
            root = new BSTEntry<K,V>(key,value);
            size ++;
        }else{
            BSTEntry<K,V> p = root;
            while (p != null) {
                int cmp = compare(key,p.key);
                if (cmp < 0) {
                    if (p.left == null) {
                        p.left = new BSTEntry<K,V>(key,value);
                        size ++;
                        break;
                    }else{
                        p = p.left;//再次循环比较
                    }
                } else if (cmp > 0) {
                    if (p.right == null) {
                        p.right = new BSTEntry<K,V>(key,value);
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
        //不管是插入的是新值还是重复值，都返回插入的值，这个和JDK TreeMap不一样
        return value;
    }

    @Override
    public Iterator<BSTEntry<K, V>> iterator() {
        return new BSTIterator<K,V>(root);
    }

    /**
     *  1,若根结点的关键字值等于key，成功
     *  2,若key小于根结点的关键字，递归查找左子树
     *  3,若key大于根结点的关键字，递归查找右子树
     *  4,若子树为空，查找不会成功
     * @param key
     * @return
     */
    private BSTEntry<K,V> getEntry(Object key) {
        BSTEntry<K,V> p = root;//初始化指针p,指向根节点
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

    @Override
    public V get(Object key) {
        BSTEntry<K, V> entry = getEntry(key);
        return entry != null ? entry.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        BSTEntry<K, V> entry = getEntry(key);
        return entry != null;
    }

    /**
     * 遍历所有结点查看比较value
     * 这里直接使用的迭代器，一个一个比较(中序遍历)
     * 时间复杂度O(N)
     * @param value
     * @return
     */
    @Override
    public boolean containsValue(Object value) {
        Iterator<BSTEntry<K, V>> iterator = this.iterator();
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
    public BSTEntry<K, V> getFirstEntry() {
        return getFirstEntry(root);
    }

    /**
     * 找到以p为根节点的最小结点(中序遍历的第一个结点)
     * @param p
     * @return
     */
    private BSTEntry<K, V> getFirstEntry(BSTEntry<K, V> p) {
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
    public BSTEntry<K,V> getLastEntry() {
        return getLastEntry(root);
    }

    /**
     * 找到以p为根节点的最大结点
     * @param p
     * @return
     */
    private BSTEntry<K, V> getLastEntry(BSTEntry<K, V> p) {
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
    private BSTEntry<K,V> deleteEntry(BSTEntry<K,V> p, Object key){
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
                    BSTEntry<K, V> rightMin = getFirstEntry(p.right);
                    p.key = rightMin.key;
                    p.value = rightMin.value;
                    BSTEntry<K, V> newRight = deleteEntry(p.right, p.key);
                    p.right = newRight;
                }else {//找左子树中最大的来替代
                    BSTEntry<K, V> leftMax = getLastEntry(p.left);
                    p.key = leftMax.key;
                    p.value = leftMax.value;
                    BSTEntry<K, V> newLeft = deleteEntry(p.left, p.key);
                    p.left = newLeft;
                }
            }
        } else if (cmp < 0) {//要删除的结点在左边
            BSTEntry<K, V> newLeft = deleteEntry(p.left, key);//以左子树为根结点，递归重新找要删除的结点
            p.left = newLeft;//实现真正的删除功能(如果p.left是要删除的结点，并且p.left结点没有子节点了，那么newLeft必定返回null，也就实现了删除功能)
        } else /*cmp > 0*/{//要删除的结点在右边
            BSTEntry<K, V> newRight = deleteEntry(p.right, key);//以右子树为根结点，递归重新找要删除的结点
            p.right = newRight;//实现真正的删除功能(如果p.rigth是要删除的结点，并且p.right结点没有子节点了，那么newRight必定返回null，也就实现了删除功能)
        }
        return p;
    }

    /**
     * 删除
     * @param key
     * @return
     */
    @Override
    public V remove(Object key) {
        BSTEntry<K, V> entry = getEntry(key);//查找是否有该结点
        if (entry == null) {
            return null;
        }
        V oldValue = entry.getValue();//获取value
        BSTEntry<K, V> newRoot = deleteEntry(root, key);//删除，并返回新的根节点(如果删除的是根节点，root就会变化)
        root = newRoot;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet<K>(this);
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet(this));
    }

    /**
     * 层序遍历
     */
    public void levelOrder() {
        Queue<BSTEntry<K,V>> queue = new LinkedList<>();
        queue.offer(root);
        int preCount = 1;
        int pCount = 0;
        while (!queue.isEmpty()) {
            preCount --;
            BSTEntry<K,V> p = queue.poll();
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

    //Entry
    //***************************************************************

    /**
     * 存储key,value
     * @param <K>
     * @param <V>
     */
    public static class BSTEntry<K,V> implements Map.Entry<K,V> {
        private K key;
        private V value;
        private BSTEntry<K,V> left;
        private BSTEntry<K,V> right;

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

        public BSTEntry(K key) {
            this.key = key;
        }

        public BSTEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public BSTEntry(K key, V value, BSTEntry<K, V> left, BSTEntry<K, V> right) {
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
    public class BSTIterator<K,V> implements Iterator<BSTEntry<K,V>> {
        private Stack<BSTEntry<K,V>> stack;
        public BSTIterator(BSTEntry<K, V> root) {
            stack = new Stack<>();
            addLeftPath(root);
        }

        private void addLeftPath(BSTEntry<K, V> p) {
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
        public BSTEntry<K, V> next() {
            BSTEntry<K, V> entry = stack.pop();
            addLeftPath(entry.right);
            return entry;
        }
    }



    //EntrySet
    //***************************************************************

    private class EntrySet<K,V> extends AbstractSet<BSTEntry<K,V>>{

        BSTMap<K,V> map;

        public EntrySet(BSTMap<K, V> map) {
            this.map = map;
        }

        @Override
        public Iterator<BSTEntry<K, V>> iterator() {
            return map.iterator();
        }

        @Override
        public int size() {
            return map.size();
        }
    }

    //KeySet
    //*******************************************************************

    Iterator<K> keyIterator() {
        return new KeyEntryIterator(iterator());
    }

    private class KeySet<K> extends AbstractSet<K>{

        BSTMap<K,V> map;

        public KeySet(BSTMap<K, V> map) {
            this.map = map;
        }

        @Override
        public Iterator<K> iterator() {
            return map.keyIterator();
        }

        @Override
        public int size() {
            return map.size();
        }
    }


    class KeyEntryIterator<K> implements Iterator<K> {

        Iterator<BSTEntry<K, V>> itr;

        public KeyEntryIterator(Iterator<BSTEntry<K, V>> itr) {
            this.itr = itr;
        }

        @Override
        public boolean hasNext() {
            return itr.hasNext();
        }

        @Override
        public K next() {
            return itr.next().key;
        }
    }

    //ValueSet
    //*******************************************************************

}
