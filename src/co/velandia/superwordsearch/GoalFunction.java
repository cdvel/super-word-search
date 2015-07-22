/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.velandia.superwordsearch;

/**
 *
 * @author cesar
 */
interface GoalFunction<T> {

    boolean evaluate(WordTreeNode<GridCoordinates> o);
}
