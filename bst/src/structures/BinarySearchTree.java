package structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> implements BSTInterface<T> {
  protected BSTNode<T> root;

  public boolean isEmpty() {
    return root == null;
  }

  public int size() {
    return subtreeSize(root);
  }

  protected int subtreeSize(BSTNode<T> node) {
    if (node == null) {
      return 0;
    } else {
      return 1 + subtreeSize(node.getLeft()) + subtreeSize(node.getRight());
    }
  }

  public boolean contains(T t) {
    if (t == null)
      throw new NullPointerException();
    else
      return this.get(t) != null;
  }

  public boolean remove(T t) {
    if (t == null) {
      throw new NullPointerException();
    }
    boolean result = contains(t);
    if (result) {
      root = removeFromSubtree(root, t);
    }
    return result;
  }

  private BSTNode<T> removeFromSubtree(BSTNode<T> node, T t) {
    // node must not be null
    int result = t.compareTo(node.getData());
    if (result < 0) {
      node.setLeft(removeFromSubtree(node.getLeft(), t));
      return node;
    } else if (result > 0) {
      node.setRight(removeFromSubtree(node.getRight(), t));
      return node;
    } else { // result == 0
      if (node.getLeft() == null) {
        return node.getRight();
      } else if (node.getRight() == null) {
        return node.getLeft();
      } else { // neither child is null
        T predecessorValue = getHighestValue(node.getLeft());
        node.setLeft(removeRightmost(node.getLeft()));
        node.setData(predecessorValue);
        return node;
      }
    }
  }

  private T getHighestValue(BSTNode<T> node) {
    // node must not be null
    if (node.getRight() == null) {
      return node.getData();
    } else {
      return getHighestValue(node.getRight());
    }
  }

  private BSTNode<T> removeRightmost(BSTNode<T> node) {
    // node must not be null
    if (node.getRight() == null) {
      return node.getLeft();
    } else {
      node.setRight(removeRightmost(node.getRight()));
      return node;
    }
  }

  public T get(T t) {
    if (t == null)
      throw new NullPointerException();
    else {
      BSTNode<T> curr = root;
      while (curr != null) {
        if (t.compareTo(curr.getData()) == 0)
          return curr.getData();
        else if (t.compareTo(curr.getData()) < 0)
          curr = curr.getLeft();
        else if (t.compareTo(curr.getData()) > 0)
          curr = curr.getRight();
      }
    }
    return null;
  }

  public void add(T t) {
    if (t == null) {
      throw new NullPointerException();
    }
    root = addToSubtree(root, new BSTNode<T>(t, null, null));
  }

  protected BSTNode<T> addToSubtree(BSTNode<T> node, BSTNode<T> toAdd) {
    if (node == null) {
      return toAdd;
    }
    int result = toAdd.getData().compareTo(node.getData());
    if (result <= 0) {
      node.setLeft(addToSubtree(node.getLeft(), toAdd));
    } else {
      node.setRight(addToSubtree(node.getRight(), toAdd));
    }
    return node;
  }

  @Override
  public T getMinimum() {
    if (this.isEmpty())
      return null;
    else {
      BSTNode<T> curr = root;
      if (curr.getLeft() == null)
        return curr.getData();
      else {
        while (curr.getLeft() != null)
          curr = curr.getLeft();
      }
      return curr.getData();
    }
  }

  @Override
  public T getMaximum() {// Similar to getMin, but getRight() instead
    if (this.isEmpty())
      return null;
    else {
      BSTNode<T> curr = root;
      if (curr.getRight() == null)
        return curr.getData();
      else {
        while (curr.getRight() != null)
          curr = curr.getRight();
      }
      return curr.getData();
    }
  }

  @Override
  public int height() {
    return heightFinder(root);
  }

  private int heightFinder(BSTNode<T> root) {
    if (root == null)// tree is empty
      return -1;
    else {
      return 1 + Math.max(heightFinder(root.getLeft()), heightFinder(root.getRight()));
    }
  }

  public Iterator<T> preorderIterator() {
    Queue<T> reg = new LinkedList<T>();
    preorderIteratorHelper(reg, root);
    return reg.iterator();

  }

  private void preorderIteratorHelper(Queue<T> reg, BSTNode<T> node) {
    if (node != null) {
      reg.add(node.getData());
      preorderIteratorHelper(reg, node.getLeft());
      preorderIteratorHelper(reg, node.getRight());
    }
  }

  public Iterator<T> inorderIterator() {
    Queue<T> queue = new LinkedList<T>();
    inorderTraverse(queue, root);
    return queue.iterator();
  }

  private void inorderTraverse(Queue<T> queue, BSTNode<T> node) {
    if (node != null) {
      inorderTraverse(queue, node.getLeft());
      queue.add(node.getData());
      inorderTraverse(queue, node.getRight());
    }
  }

  public Iterator<T> postorderIterator() {
    Queue<T> reg = new LinkedList<T>();
    postorderIteratorHelper(reg, root);
    return reg.iterator();
  }

  private void postorderIteratorHelper(Queue<T> reg, BSTNode<T> node) {
    if (node != null) {
      postorderIteratorHelper(reg, node.getLeft());
      postorderIteratorHelper(reg, node.getRight());
      reg.add(node.getData());
    }
  }

  @Override
  public boolean equals(BSTInterface<T> other) {
    boolean bool = true;
    if (other == null)
      throw new NullPointerException();
    else {
      Queue<BSTNode<T>> fqueue = new LinkedList<BSTNode<T>>();
      Queue<BSTNode<T>> squeue = new LinkedList<BSTNode<T>>();
      equalsHelper(fqueue, root);
      equalsHelper(squeue, other.getRoot());
      if (fqueue.size() != squeue.size())
        return !bool;
      else {
        while (!fqueue.isEmpty()) {
          if (!fqueue.remove().getData().equals(squeue.remove().getData()))
            bool = false;
        }
        return bool;
      }
    }
  }

  private void equalsHelper(Queue<BSTNode<T>> queue, BSTNode<T> node) {
    Queue<BSTNode<T>> bstq = new LinkedList<BSTNode<T>>();
    if (node != null) {
      bstq.add(node);
      while (!bstq.isEmpty()) {
        BSTNode<T> holder = bstq.remove();
        queue.add(holder);
        if (holder.getLeft() != null)
          bstq.add(holder.getLeft());
        if (holder.getRight() != null)
          bstq.add(holder.getRight());
      }
    }
  }

  @Override
  public boolean sameValues(BSTInterface<T> other) {
    if (other == null)
      throw new NullPointerException();
    else {
      Iterator<T> first = this.inorderIterator();
      Iterator<T> second = other.inorderIterator();
      while (first.hasNext() && second.hasNext()) {
        if (!first.next().equals(second.next()))
          return false;
      }
    }
    return true;
  }

  @Override
  public boolean isBalanced() {
    if (this.isEmpty())
      return true;
    if (Math.pow(2, height()) <= size() && size() < Math.pow(2, height() + 1))
      return true;
    return false;
  }

  @Override
  @SuppressWarnings("unchecked")

  public void balance() {
    if (this.isBalanced())// Don't have to do anything
      return;
    T[] reg = (T[]) new Comparable[size()];
    Iterator<T> iteratorInorder = this.inorderIterator();
    int i = 0;
    while (iteratorInorder.hasNext()) {
      reg[i] = iteratorInorder.next();
      i++;
    }
    root = balanceHelper(reg, 0, size() - 1);
  }

  private BSTNode<T> balanceHelper(T[] reg, int lBound, int uBound) {
    if (lBound > uBound)
      return null;
    int middle = (lBound + uBound) / 2;
    BSTNode<T> newNode = new BSTNode<T>(reg[middle], balanceHelper(reg, lBound, middle - 1),
        balanceHelper(reg, middle + 1, uBound));
    return newNode;
  }

  @Override
  public BSTNode<T> getRoot() {
    // DO NOT MODIFY
    return root;
  }

  /**
   * toDotFormat.
   * 
   * @param root root of tree.
   * @return type T.
   */
  public static <T extends Comparable<T>> String toDotFormat(BSTNode<T> root) {
    // header
    int count = 0;
    String dot = "digraph G { \n";
    dot += "graph [ordering=\"out\"]; \n";
    // iterative traversal
    Queue<BSTNode<T>> queue = new LinkedList<BSTNode<T>>();
    queue.add(root);
    BSTNode<T> cursor;
    while (!queue.isEmpty()) {
      cursor = queue.remove();
      if (cursor.getLeft() != null) {
        // add edge from cursor to left child
        dot += cursor.getData().toString() + " -> " + cursor.getLeft().getData().toString() + ";\n";
        queue.add(cursor.getLeft());
      } else {
        // add dummy node
        dot += "node" + count + " [shape=point];\n";
        dot += cursor.getData().toString() + " -> " + "node" + count + ";\n";
        count++;
      }
      if (cursor.getRight() != null) {
        // add edge from cursor to right child
        dot += cursor.getData().toString() + " -> " + cursor.getRight().getData().toString() + ";\n";
        queue.add(cursor.getRight());
      } else {
        // add dummy node
        dot += "node" + count + " [shape=point];\n";
        dot += cursor.getData().toString() + " -> " + "node" + count + ";\n";
        count++;
      }

    }
    dot += "};";
    return dot;
  }

  public static void main(String[] args) {
    for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
      BSTInterface<String> tree = new BinarySearchTree<String>();
      for (String s : new String[] { "d", "b", "a", "c", "f", "e", "g" }) {
        tree.add(s);
      }
      Iterator<String> iterator = tree.inorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();
      iterator = tree.preorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();
      iterator = tree.postorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();

      System.out.println(tree.remove(r));

      iterator = tree.inorderIterator();
      while (iterator.hasNext()) {
        System.out.print(iterator.next());
      }
      System.out.println();
    }

    BSTInterface<String> tree = new BinarySearchTree<String>();
    for (String r : new String[] { "a", "b", "c", "d", "e", "f", "g" }) {
      tree.add(r);
    }
    System.out.println(tree.size());
    System.out.println(tree.height());
    System.out.println(tree.isBalanced());
    tree.balance();
    System.out.println(tree.size());
    System.out.println(tree.height());
    System.out.println(tree.isBalanced());
  }
}