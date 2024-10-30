/**
 * Feature: Grid Model
 *    First Choice ->2d array 
 *         ->(no other choice, more complex graph represantion node based).
 *
 * MxN Grid with m=n
 *    First Choice(9x9) -> odd number ?
 *        Second Choice -> 8x8 with a max of 10 pieces per player? 10x10 for 12.
 *
 * Implementation Details:
 * - The grid model  provides
 *   methods to set, remove, and check the occupancy of cells.
 * - The Tile class represents an individual cell on the game board and stores
 *   information about the current piece , maybe about the stats too.
 * 
 * Possible Methods requiredd:
 *     -Set Piece(row, column,->(obj) piece( charachter object with name ,stats))
 *     -remove Piece( maybe just move) same....
 *     -boolean occupied (cell obejct (col , row)) is charachter or not
 *      
 *
 * Structure:
 *     -class Cell
 *        -row,column, Charachter(Object)
 *        -construct: -> Empty Cell, charachter obj= null
 *        -methods : -> toString
 *                   -> set piece
 *                   -> get piece
 *                   -> remove piece
 *                   -> bool occupied
 *
 *     -Class Board:
 *        -private Cell [][];
 *        -Construct: -> 9x9(8x8)
 *                    ->initialize Board
 *        -methods: -> initializeBoard : give the value 0 til 9 for the Cell
 *                  (IF NOTATION A-I, 1-9)-> getNotation: chess notaion from 0-8,0-8 to A-I,1-9
 *                                        -> getCell : invers Chess notation -'A',9-charat(1)
 *                  -> getCell: given 0-8,-0-8
 *
 *





 @version 1.0
 * @since 2024-10-29
 */
public class GridModel{
}
