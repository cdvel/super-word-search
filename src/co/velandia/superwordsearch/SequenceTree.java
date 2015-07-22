/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.velandia.superwordsearch;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cesar
 */
public class SequenceTree<T> {
    private Node<T> root;

    public SequenceTree(T rootData) {
        root = new Node<>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
    }
    
    public void add (Node node){
        root.children.add(node);
    }
    

}
