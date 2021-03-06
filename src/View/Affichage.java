/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Library.Game;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author Dorian
 */
public class Affichage implements Observer
{

    private Game _game;
    private GridPane _pane;

    public Affichage(Game game, GridPane pane)
    {
        this._game = game;
        this._pane = pane;
    }

   
    /**
     * Updates the view
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if(this.checkLose() || this.checkWin())
        {
            // we step out from the update function to cause no conflicts between the UI reloads if the game is either won or lost
            return;
        }
        this._game.moveGhosts();
        this._game.movePacman();
        this.updateImageViews();
    }
    
     /**
     * Checks if the game is lost or not according to the CONTROLLER state
     * @return true if the game is lost
     */
    private boolean checkLose()
    {
        // IF THE GAME IS OVER
        if (this._game.getState().getLost())
        {
            // we stop the independant thread game
            this._game.stop();
            Platform.runLater(() -> this._pane.getChildren().clear());
            Text label = new Text("Game over !");
            label.setStyle("-fx-font: 90 arial;");
            Platform.runLater(() -> this._pane.add(label, 0, 0));

            return true;
        }
        return false;
    }

    /**
     * Checks if the game is won or not yet according to the CONTROLLER state
     * @return true if the game is won
     */
    private boolean checkWin()
    {
        if (this._game.getState().noMoreGums())
        {
            // END OF THE GAME : we stop the independant thread game
            this._game.stop();
            Platform.runLater(() -> this._pane.getChildren().clear());
            Text label = new Text("You won !");
            label.setStyle("-fx-font: 90 arial;");
            Platform.runLater(() -> this._pane.add(label, 0, 0));

            return true;
        }
        return false;
    }

    /**
     * Updates the imageView according to the controller (library grid)
     */
    private void updateImageViews()
    {
        for (int i = 0; i < this._game.getState().getCells().length; i++)
        {
            for (int j = 0; j < this._game.getState().getCells()[i].length; j++)
            {
                ImageView img = new ImageView(this._game.getState().getCells()[i][j].getImgPath());
                Node imgAsNode = this.getNodeByRowColumnIndex(i, j, _pane);
                ImageView oldImage = (ImageView) imgAsNode;
                Image newImage = new Image(this._game.getState().getCells()[i][j].getImgPath());
                oldImage.setImage(newImage);
            }
        }
    }

    /**
     *
     * @param row
     * @param column
     * @param gridPane
     * @see
     * https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
     * (Sedrick°
     * @return
     */
    private Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane)
    {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens)
        {
            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column)
            {
                result = node;
                break;
            }
        }
        return result;
    }
}
