package oldproject;

import java.util.ArrayList;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class GraphGui extends Application implements EventHandler<ActionEvent> {
//	Pre-definitions of GUI elements
	RadioButton addEdge = new RadioButton();
	RadioButton addVertex = new RadioButton();
	RadioButton removeVertex = new RadioButton();
	RadioButton removeEdge = new RadioButton();
	RadioButton moveVertex = new RadioButton();
	Button clearButton = new Button();
	Button help = new Button(); //Helper 
	static Canvas canvas;
	VBox toolBox;
	ToggleGroup toolbar;
	
	
//	GUI panes
	BorderPane board;
	StackPane rightpane,leftpane,finished;
	Pane coordinatePlane;
	
//	Underlying data structures and variables
	static GraphicsContext gContent;
	static ArrayList<Vertex> vertexArray = new ArrayList<Vertex>();
	static ArrayList<VertexEdge> edgeArray = new ArrayList<VertexEdge>();
	static double checkXCoordinate;
	static double checkYCoordinate;
	static double changeX,changeY;
	static Vertex tempStore[] = new Vertex[2];
	static boolean toolActive = false;
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Graph GUI");
		
		
		
		//Create a CANVAS and add it to a coordinate plan
				coordinatePlane = new Pane();
				canvas = new Canvas(900,720);
				rightpane = new StackPane();
				coordinatePlane.getChildren().add(canvas);
				rightpane.getChildren().add(coordinatePlane);
				rightpane.setOnMousePressed(new EventHandler<MouseEvent>() {
					
					//Internal EventHandler
			        @Override
			        public void handle(MouseEvent event) {
			        	
//			        	AddVertex Event
			        	if(addVertex.isSelected()) {
						Vertex v = new Vertex(event.getSceneX() - 185, event.getSceneY() - 5, vertexArray.size()+1);
						vertexArray.add(v);
						Draw();
			        	}
			        	
//			        	removeVertex Event
			        	if(removeVertex.isSelected()) {
			        		double X = event.getSceneX() - 185;
			        		double Y = event.getSceneY() - 5;

	    					for(int i = vertexArray.size()-1; i > - 1 ; i--) {
			    			checkXCoordinate = vertexArray.get(i).getXCoord();
			    			checkYCoordinate = vertexArray.get(i).getYCoord();
			    			if( (checkXCoordinate <( X + 9)  &&  checkXCoordinate > (X - 9))  && (checkYCoordinate < (Y + 9) && checkYCoordinate > (Y - 9)) ) {
			    				for(int j = edgeArray.size()-1; j>-1; j--) {
			    					if(edgeArray.get(j).isPoint(vertexArray.get(i))) {
			    						edgeArray.remove(j);
			    					}
			    				}
			    				vertexArray.remove(i);
			    					Draw();
			    				} 
	    					}			
			        	}
			        	
//			        	moveVertex Event
			            if(moveVertex.isSelected()) {
			        	    double X = event.getSceneX() - 185; 
			        	    double Y = event.getSceneY() - 5;
			        	    if(toolActive == false) {
			   				 for(int i = vertexArray.size()-1; i > -1 ; i--) {
			   				 checkXCoordinate = vertexArray.get(i).getXCoord();
			   				 checkYCoordinate = vertexArray.get(i).getYCoord();
			   				 if( (checkXCoordinate <(X + 9)  &&  checkXCoordinate > (X - 9))  && (checkYCoordinate < (Y + 9) && checkYCoordinate > (Y - 9)) ) {
			   					 gContent.setFill(Color.GREEN);
			   					 gContent.fillOval(checkXCoordinate, checkYCoordinate, 10, 10);
			   					 toolActive = true;
			   					 tempStore[0] = vertexArray.get(i);
			   				   }
			   				 }
			        	   }
			        	   else if(toolActive == true) {
			   					tempStore[0].setX(X);
			   					tempStore[0].setY(Y);
			   					Draw();
								toolActive = false;
			   				} 
			           }
			            
//			           addEdge Event
			            if(addEdge.isSelected()) {
			        	    double X = event.getSceneX() - 185;
			        	    double Y = event.getSceneY() - 5;
			        	    if(toolActive == false) {
			   			 	 for(int i = vertexArray.size()-1; i > -1 ; i--) {
			   				 checkXCoordinate = vertexArray.get(i).getXCoord();
			   				 checkYCoordinate = vertexArray.get(i).getYCoord();
			   				 if( (checkXCoordinate <(X + 9)  &&  checkXCoordinate > (X - 9))  && (checkYCoordinate < (Y + 9) && checkYCoordinate > (Y - 9)) ) {
			   					 gContent.setFill(Color.BLUE);
			   					 gContent.fillOval(checkXCoordinate, checkYCoordinate, 10, 10);
			   					 toolActive = true;
			   					 tempStore[0] = vertexArray.get(i);
			   					 }
			   			 	   }
			   			 	}
			        	    else if(toolActive == true) {
			        		    for(int i = vertexArray.size()-1; i > -1 ; i--) {
					   				 checkXCoordinate = vertexArray.get(i).getXCoord();
					   				 checkYCoordinate = vertexArray.get(i).getYCoord();
					   				 if( (checkXCoordinate <(X + 9)  &&  checkXCoordinate > (X - 9))  && (checkYCoordinate < (Y + 9) && checkYCoordinate > (Y - 9)) ) {
					   					 VertexEdge e = new VertexEdge(tempStore[0],vertexArray.get(i));
					   					 edgeArray.add(e);
					   					 Draw();
					   					 toolActive = false;
					   				 }
					   				 }
			   				 }  
			            }
			            
			            
//			            removeEdge Event
			            if(removeEdge.isSelected()) {
			            	 double X = event.getSceneX() - 185;
				        	 double Y = event.getSceneY() - 5;
				        	 for(int i = edgeArray.size()-1; i > -1; i--) {
				        		 if(edgeArray.get(i).isOnLine(X, Y)) {
				        			 edgeArray.remove(i);
				        		 }
				        	 }
				        	 Draw();
			            }

			         }
			     });
				
				gContent = canvas.getGraphicsContext2D();
				gContent.setFill(Color.WHITE);
				gContent.fillRect(0,0,900,720);

//		Setting radio button text
		addVertex.setText("Add Vertex");
		addVertex.setSelected(true);
		addEdge.setText("Add Edges");
		removeVertex.setText("Remove Vertex");
		removeEdge.setText("Remove Edges");
		moveVertex.setText("Move Vertex");
		
//		Creating toggleGroup to hold radio buttons, the allocating buttons
		toolbar = new ToggleGroup();
		addVertex.setToggleGroup(toolbar);
		addEdge.setToggleGroup(toolbar);
		removeVertex.setToggleGroup(toolbar);
		removeEdge.setToggleGroup(toolbar);
		moveVertex.setToggleGroup(toolbar);
		
//		Adding listener to toolbar, safety against unnatural behaviors on tool switching
		toolbar.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> o, Toggle a, Toggle b) {
				toolActive = false;
				Draw();
			}
		});
		
//		Creating additional buttons
		clearButton.setText("Clear Board");
		clearButton.setOnAction(e -> clear());
		
		help.setText("Help");
		Help h = new Help();
		help.setOnAction(e -> h.display());
		
		
		
		toolBox = new VBox(10);
		toolBox.getChildren().addAll(addVertex,addEdge,removeVertex,removeEdge,moveVertex,clearButton,help);
		
		leftpane = new StackPane();
		leftpane.getChildren().addAll(toolBox);
	
		board = new BorderPane();
		finished = new StackPane();
		board.setLeft(leftpane);
		board.setRight(rightpane);
		finished.getChildren().add(board);
		
	
		Scene scene = new Scene(finished, 1080 ,720);
		stage.setScene(scene); 
		stage.show();		
			
	}

	public static void main(String[] args) {
		launch(args);
		}
	
	@Override
	public void handle(ActionEvent arg0) {
	}
	

//	Functions for board manipulation 
private static void Draw() {
//	Clearing canvas
	gContent.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
	gContent.setFill(Color.WHITE);
	gContent.fillRect(0,0,900,720);
	
//	Drawing all the vertex
	for (int i = 0; i < vertexArray.size(); i++) {
		gContent.setFill(Color.BLACK);
		gContent.fillOval(vertexArray.get(i).getXCoord(), vertexArray.get(i).getYCoord(), 10, 10);
	}
	
// Drawing all edges
	for (int i = 0; i < edgeArray.size(); i++) {
		gContent.strokeLine(edgeArray.get(i).getPointA().getXCoord() + 5, edgeArray.get(i).getPointA().getYCoord() + 5, edgeArray.get(i).getPointB().getXCoord() + 5, edgeArray.get(i).getPointB().getYCoord() + 5);
	}	
}


private static void clear() {
//	Clearing canvas
	gContent.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
	gContent.setFill(Color.WHITE);
	gContent.fillRect(0,0,900,720);
	
//	Clearing vertex array
	vertexArray.clear();
	vertexArray.trimToSize();
	
// Clearing edge array
	edgeArray.clear();
	edgeArray.trimToSize();
}

//Internal vertex class, holds its locational data
class Vertex {
	
//	Constructor(s)
	public Vertex() {
	
	}

	double xCoordinate,yCoordinate;
	int vertexNumber;

	
	public Vertex(double x, double y) {
		this.xCoordinate = x;
		this.yCoordinate = y;
	}
	
	public Vertex(double x, double y, int z) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.vertexNumber = z;
	}
	
	
	
	//Setters
	public void setX(double x) {
	 this.xCoordinate = x;
	}
	
	public void setY(double y) {
		 this.yCoordinate = y;
	}
	
	public void setNum(int z) {
		 this.vertexNumber = z;
	}
	
	//Getters
	public double getXCoord () {
		return this.xCoordinate;
	}
	
	public double getYCoord () {
		return this.yCoordinate;
	}
	
	public int getNum() {
		 return this.vertexNumber;
	}
}

//Internal edge class
class VertexEdge {
	private Vertex pointA,pointB;

	
//	Constructor(s)
	public VertexEdge(Vertex a, Vertex b) {
		this.pointA = a;
		this.pointB = b;
	}
	
//	Setters
	public void setPointA(Vertex a) {
		this.pointA = a;
	}
	
	public void setPointB(Vertex b) {
		this.pointB = b;
	}
	
//	Getters
	public Vertex getPointA() {
		return this.pointA;
	}
	
	public Vertex getPointB() {
		return this.pointB;
	}
	
//	Methods
	public boolean isPoint(Vertex x) {
		if(this.pointA == x || this.pointB == x) {
			return true;
		}
		return false;
	}
	
	public boolean isOnLine(double targetX, double targetY) {
//		Setting "splash range" for points, so that users do not need to click exactly on the line, just near it.
		double splash = 30;
//		Calculating y = mx + b
//		First find m or slope
		double slope = ((this.pointB.getYCoord() - this.pointA.getYCoord()) / (this.pointB.getXCoord() - this.pointA.getXCoord()));
		
//		Find y-intercept(Using point A)
		double y_intercept;
		double work = this.pointA.getXCoord() * slope;
		if(work < 0) {
			y_intercept = this.pointA.getYCoord() + work;
		} else {
			y_intercept = this.pointA.getYCoord() - work;
		}
		
//		Now check if point is on the line
		if((slope*targetX) + y_intercept > targetY - splash && (slope*targetX) + y_intercept < targetY + splash) {
			return true;
		}
		return false;
	}
	
}

//Help class
class Help {
		public void display () {
			Stage window = new Stage();
			Label addVertexHelp,addEdgeHelp,removeEdgeHelp,removeVertexHelp,moveVertexHelp,clearButtonHelp;
			
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle("			Help Screen!			");
			window.setMinWidth(500);
			window.setMinHeight(500);
			
		    //Description of each button
			addVertexHelp = new Label();
			addVertexHelp.setText("Add Vertex:			When selected click on the provided space to add a vertex");
			
			addEdgeHelp = new Label();
	        addEdgeHelp.setText("Add Edge:			When selected click a vertex, that vertex will become blue indicating its a start point for a vertex, then click on the desired end point");
	        
	        removeEdgeHelp = new Label();
	        removeEdgeHelp.setText("Remove Edge:			When selected click on a edge to remove it");
	        
	        removeVertexHelp= new Label();
	        removeVertexHelp.setText("Remove Vertex:		When selected click on a vertex to remove it along with all joined edges");
	        
	        moveVertexHelp = new Label();
	        moveVertexHelp.setText("Move Vertex:			When selected click on a vertex and it will turn green. Then click on a new location to move the vertex there. ");
	        
	        clearButtonHelp = new Label();
	        clearButtonHelp.setText("Clear Button:			When clicked, will clear the screen");
	        
			Button closeButton = new Button("Confirm");
			closeButton.setOnAction(e -> window.close());
			
			VBox layout = new VBox(10);
			layout.getChildren().addAll(addVertexHelp,addEdgeHelp,removeEdgeHelp,removeVertexHelp,moveVertexHelp,clearButtonHelp,closeButton);
			
			Scene scene = new Scene(layout);
			window.setScene(scene);
			window.showAndWait();
			
		}

	}
}